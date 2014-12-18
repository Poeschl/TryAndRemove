/*
 * Copyright 2014 Markus Pöschl
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.AppAdapter;
import de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver;
import de.poeschl.apps.tryandremove.dialogs.ClearWarningDialogFragment;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import timber.log.Timber;


public class AppListActivity extends TryAndRemoveActivity implements ClearWarningDialogFragment.ButtonListener {

    @InjectView(R.id.app_list_layout_apps_listView)
    RecyclerView appListView;

    MenuItem recordToolbarButton;

    @Inject
    AppDetectionReceiver receiver;
    @Inject
    PackageList packageListData;
    @Inject
    AppAdapter appAdapter;
    @Inject
    AppManager appManager;

    private BooleanPreference isTracking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

        setupLayout(this, R.layout.app_list_layout);

        ButterKnife.inject(this);

        appListView.setAdapter(appAdapter);
        appListView.setLayoutManager(new LinearLayoutManager(this));

        isTracking = new BooleanPreference(getPreferences(MODE_PRIVATE), "IS_TRACKING", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePackageList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        Timber.v("Called onDestroy - unregister reciever");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_list_toolbar_actions, menu);

        recordToolbarButton = menu.findItem(R.id.app_list_toolbar_action_record);

        setRecordButtonState(isTracking.get());

        return super.onCreateOptionsMenu(menu);
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

    private void toggleRecording() {
        boolean newTrackState = !isTracking.get();
        if (newTrackState) {
            registerReceiver();
        } else {
            unregisterReceiver();
        }
        Timber.d("Listener Status: " + newTrackState);
        setRecordButtonState(newTrackState);
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

    @OnClick(R.id.app_list_layout_clear_action_button)
    void clearList() {
        ClearWarningDialogFragment wf = new ClearWarningDialogFragment();
        wf.setButtonListener(this);
        wf.show(getSupportFragmentManager());
    }

    @Override
    public void onUserConfirmedClear() {
        packageListData.clear();
        updatePackageList();
    }

    @OnClick(R.id.app_list_layout_remove_action_button)
    void removeAllApps() {
        //TODO: Security answer
        appManager.remove(packageListData.getPackages());
    }

    private void updatePackageList() {
        appAdapter.updateAdapter(packageListData);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("package");
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);

        registerReceiver(receiver, filter);
        isTracking.set(true);
    }

    private void unregisterReceiver() {
        try {
            isTracking.set(false);
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            Timber.e(e, "App install receiver was unregistered while not registered.");
        }
    }
}
