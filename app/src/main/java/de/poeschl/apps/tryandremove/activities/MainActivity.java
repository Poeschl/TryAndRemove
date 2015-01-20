/*
 * Copyright (c) 2014 Markus Poeschl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.poeschl.apps.tryandremove.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.NavigationItemAdapter;
import de.poeschl.apps.tryandremove.annotations.IsTracking;
import de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver;
import de.poeschl.apps.tryandremove.fragments.AppListFragment;
import de.poeschl.apps.tryandremove.fragments.PrivatePolicyFragment;
import de.poeschl.apps.tryandremove.interfaces.NavigationDrawerListener;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import timber.log.Timber;


public class MainActivity extends TryAndRemoveActivity implements NavigationDrawerListener<MainActivity.Mode> {

    @InjectView(R.id.navigation_drawer_top_recyclerView)
    RecyclerView topRecyclerView;
    @InjectView(R.id.navigation_drawer_bottom_recyclerView)
    RecyclerView bottomRecyclerView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private MenuItem recordToolbarButton;

    @Inject
    AppDetectionReceiver receiver;
    @Inject
    @IsTracking
    BooleanPreference isTracking;

    @Inject
    AppListFragment appListFragment;
    @Inject
    PrivatePolicyFragment privatePolicyFragment;

    private FrameLayout navigationDrawer;
    private Mode displayMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

        setupLayout(R.layout.activity_navigation_drawer);

        //Inflate the navigation drawer
        navigationDrawer = ButterKnife.findById(this, R.id.navigation_drawer);
        getLayoutInflater().inflate(R.layout.navigation_drawer, navigationDrawer);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        setUpTopNavPart();
        setUpBottomNavPart();

        setViewMode(Mode.APP_LIST);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        Timber.v("Called onDestroy - unregister receiver");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_list_toolbar_actions, menu);

        recordToolbarButton = menu.findItem(R.id.app_list_toolbar_action_record);

        setRecordButtonState(isTracking.get());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_list_toolbar_action_record:
                toggleRecording();
                return true;
            case R.id.app_list_toolbar_action_refresh:
                appListFragment.updatePackageList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRecordButtonState(boolean active) {
        if (active) {
            Toast.makeText(this, getString(R.string.app_list_activity_enable_track_toast_message), Toast.LENGTH_SHORT).show();
            recordToolbarButton.setIcon(R.drawable.ic_menu_record_on);
        } else {
            Toast.makeText(this, getString(R.string.app_list_activity_disable_track_toast_message), Toast.LENGTH_SHORT).show();
            recordToolbarButton.setIcon(R.drawable.ic_menu_record_off);
        }
    }

    private void toggleRecording() {
        boolean newTrackState = !isTracking.get();

        Timber.d("Listener Status: " + newTrackState);
        setRecordState(newTrackState);

        if (newTrackState) {
            registerReceiver();
        } else {
            unregisterReceiver();
        }
    }

    private void setRecordState(boolean state) {
        if (isTracking.get() == state) {
            return;
        }
        setRecordButtonState(state);
    }

    private void setUpTopNavPart() {
        topRecyclerView.setHasFixedSize(true);
        topRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpBottomNavPart() {
        bottomRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<de.poeschl.apps.tryandremove.models.MenuItem> bottomItemList = new ArrayList<>();
        //TODO: Add right icon for private policy
        bottomItemList.add(new de.poeschl.apps.tryandremove.models.MenuItem<Mode>(getString(
                R.string.navigation_drawer_private_policy_title), R.drawable.ic_launcher_app, Mode.PRIVATE_POLICY));

        NavigationItemAdapter adapter = new NavigationItemAdapter<Mode>(bottomItemList);
        adapter.setNavigationListener(this);
        bottomRecyclerView.setAdapter(adapter);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("package");
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);

        receiver.setRegistered(true);
        isTracking.set(true);
        registerReceiver(receiver, filter);
    }

    private void unregisterReceiver() {
        try {
            if (receiver.isRegistered()) {
                unregisterReceiver(receiver);
            }
            isTracking.set(false);
            receiver.setRegistered(false);

        } catch (IllegalArgumentException e) {
            Timber.e(e, "App install receiver was unregistered while not registered.");
        }
    }

    private void setViewMode(Mode viewMode) {
        if (displayMode != viewMode) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            switch (viewMode) {
                case APP_LIST:
                    ft.replace(R.id.mainContent, appListFragment);
                    break;
                case PRIVATE_POLICY:
                    ft.replace(R.id.mainContent, privatePolicyFragment);
                    ft.addToBackStack(null);
                    break;
            }
            if (drawerLayout.isDrawerVisible(navigationDrawer)) {
                drawerLayout.closeDrawers();
            }
            ft.commit();

            Timber.v("Show " + viewMode.name());
            displayMode = viewMode;

        }
    }

    @Override
    public void onNavigationItemClick(Mode targetViewMode) {
        setViewMode(targetViewMode);
    }

    enum Mode {
        APP_LIST, PRIVATE_POLICY;
    }
}
