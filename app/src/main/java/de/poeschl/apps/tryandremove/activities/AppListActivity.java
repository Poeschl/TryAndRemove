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

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.AppAdapter;
import de.poeschl.apps.tryandremove.annotations.IsTracking;
import de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver;
import de.poeschl.apps.tryandremove.dialogs.ClearWarningDialogFragment;
import de.poeschl.apps.tryandremove.dialogs.RemoveWarningDialogFragment;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import timber.log.Timber;


public class AppListActivity extends NavigationActivity implements ClearWarningDialogFragment.ButtonListener, RemoveWarningDialogFragment.ButtonListener {

    @InjectView(R.id.app_list_layout_apps_recyclerView)
    RecyclerView appListView;
    @InjectView(R.id.app_list_layout_floating_menu)
    FloatingActionsMenu floatMenu;

    @Inject
    AppDetectionReceiver receiver;
    @Inject
    @IsTracking
    BooleanPreference isTracking;
    @Inject
    PackageList packageListData;
    @Inject
    AppAdapter appAdapter;
    @Inject
    AppManager appManager;

    private MenuItem recordToolbarButton;
    private MenuItem reloadToolbarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

        setupLayout(R.layout.activity_app_list);

        ButterKnife.inject(this);

        appListView.setAdapter(appAdapter);
        appListView.setLayoutManager(new LinearLayoutManager(this));
        appListView.setHasFixedSize(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePackageList();
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
                updatePackageList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRecordButtonState(boolean active) {
        if (active) {
            Timber.d("App tracking activated");
            recordToolbarButton.setIcon(R.drawable.ic_action_record_on);
        } else {
            Timber.d("App tracking deactivated");
            recordToolbarButton.setIcon(R.drawable.ic_action_record_off);
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

    @OnClick(R.id.app_list_layout_clear_action_button)
    void clearList() {
        if (!packageListData.isEmpty()) {
            ClearWarningDialogFragment wf = new ClearWarningDialogFragment();
            wf.setButtonListener(this);
            wf.show(getFragmentManager());
        }
    }

    @Override
    public void onUserConfirmedClear() {
        packageListData.clear();
        updatePackageList();
        floatMenu.collapse();
    }

    @OnClick(R.id.app_list_layout_remove_action_button)
    void removeAllApps() {
        if (!packageListData.isEmpty()) {
            RemoveWarningDialogFragment rf = new RemoveWarningDialogFragment();
            rf.setButtonListener(this);
            rf.show(getFragmentManager());
        }
    }

    @Override
    public void onUserConfirmedRemove() {
        appManager.remove(packageListData.getPackages());
        updatePackageList();
        floatMenu.collapse();
    }

    private void updatePackageList() {
        appAdapter.updateAdapter(packageListData);
    }
}
