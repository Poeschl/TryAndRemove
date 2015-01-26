/*
 * Copyright (c) 2015 Markus Poeschl
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

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

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
import de.poeschl.apps.tryandremove.interfaces.FragmentChangeListener;
import de.poeschl.apps.tryandremove.interfaces.NavigationDrawerListener;
import de.poeschl.apps.tryandremove.layoutManager.SmallLayoutManager;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import timber.log.Timber;


public class MainActivity extends TryAndRemoveActivity implements NavigationDrawerListener<MainActivity.Mode>, FragmentChangeListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.navigation_drawer_top_recyclerView)
    RecyclerView topRecyclerView;
    @InjectView(R.id.navigation_drawer_bottom_recyclerView)
    RecyclerView bottomRecyclerView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

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
    private MenuItem recordToolbarButton;
    private MenuItem reloadToolbarButton;

    private ActionBarDrawerToggle drawerToggle;

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

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name_not_found);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        setUpTopNavPart();
        setUpBottomNavPart();

        setUpToolbar(Mode.APP_LIST);
        setViewMode(Mode.APP_LIST);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        Timber.v("Called onDestroy - unregister receiver");
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_list_toolbar_actions, menu);

        recordToolbarButton = menu.findItem(R.id.app_list_toolbar_action_record);
        reloadToolbarButton = menu.findItem(R.id.app_list_toolbar_action_refresh);

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

    @Override
    public void onNavigationItemClick(Mode targetViewMode) {
        setViewMode(targetViewMode);
    }

    @Override
    public void onFragmentChange(Fragment currentFragment) {
        if (currentFragment.getClass().equals(appListFragment.getClass())) {
            setUpToolbar(Mode.APP_LIST);
        } else if (currentFragment.getClass().equals(privatePolicyFragment.getClass())) {
            setUpToolbar(Mode.PRIVATE_POLICY);
        }
    }

    private void setRecordButtonState(boolean active) {
        if (active) {
            Timber.d("App tracking activated");
            recordToolbarButton.setIcon(R.drawable.ic_menu_record_on);
        } else {
            Timber.d("App tracking deactivated");
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
        topRecyclerView.setLayoutManager(new SmallLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<de.poeschl.apps.tryandremove.models.MenuItem<Mode>> upItemList = new ArrayList<>();
        upItemList.add(new de.poeschl.apps.tryandremove.models.MenuItem<>(
                getString(R.string.navigation_drawer_app_list_title), R.drawable.ic_launcher_app, Mode.APP_LIST));

        NavigationItemAdapter<Mode> adapter = new NavigationItemAdapter<>(upItemList);
        adapter.setNavigationListener(this);
        topRecyclerView.setAdapter(adapter);
    }

    private void setUpBottomNavPart() {
        bottomRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.setLayoutManager(new SmallLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<de.poeschl.apps.tryandremove.models.MenuItem<Mode>> bottomItemList = new ArrayList<>();
        bottomItemList.add(new de.poeschl.apps.tryandremove.models.MenuItem<>(
                getString(R.string.navigation_drawer_private_policy_title), R.drawable.ic_menu_info, Mode.PRIVATE_POLICY));


        NavigationItemAdapter<Mode> adapter = new NavigationItemAdapter<>(bottomItemList);
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

    private void setUpToolbar(Mode mode) {
        switch (mode) {
            case APP_LIST:
                toolbar.setTitle(R.string.app_name);
                if (recordToolbarButton != null && reloadToolbarButton != null) {
                    recordToolbarButton.setVisible(true);
                    reloadToolbarButton.setVisible(true);
                }
                break;
            case PRIVATE_POLICY:
                toolbar.setTitle(R.string.navigation_drawer_private_policy_title);
                if (recordToolbarButton != null && reloadToolbarButton != null) {

                    recordToolbarButton.setVisible(false);
                    reloadToolbarButton.setVisible(false);
                }
                break;
        }
    }

    private void setViewMode(Mode viewMode) {

        switch (viewMode) {
            case APP_LIST:
                openFragment(appListFragment);
                break;
            case PRIVATE_POLICY:
                openFragment(privatePolicyFragment);
                break;
        }

        Timber.v("Show " + viewMode.name());

        if (drawerLayout.isDrawerVisible(navigationDrawer)) {
            drawerLayout.closeDrawers();
        }
    }

    private void openFragment(Fragment fragment) {
        boolean resumed = getFragmentManager().popBackStackImmediate(fragment.getClass().getName(), 0);
        boolean onTop = getFragmentManager().getBackStackEntryCount() > 0
                && getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName().equals(fragment.getClass().getName());

        if (!resumed && !onTop) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, fragment);
            ft.addToBackStack(fragment.getClass().getName());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }

            ft.commit();

            Timber.v("Init " + fragment.getClass().getSimpleName());
        }

        this.onFragmentChange(fragment);
    }

    enum Mode {
        APP_LIST, PRIVATE_POLICY;
    }
}
