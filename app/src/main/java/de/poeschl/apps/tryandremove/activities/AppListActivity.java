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
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import timber.log.Timber;


public class AppListActivity extends TryAndRemoveActivity {

    @InjectView(R.id.app_list_layout_apps_listView)
    RecyclerView appListView;

    @Inject
    AppDetectionReceiver receiver;
    @Inject
    PackageList packageListData;
    @Inject
    AppAdapter appAdapter;

    private BooleanPreference isTracking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

        setupLayout(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_list_toolbar_actions, menu);
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
        boolean isTrackingBool = isTracking.get();
        if (isTrackingBool) {
            Toast.makeText(this, "Disable Listener", Toast.LENGTH_SHORT).show();
            unregisterReceiver();
        } else {
            Toast.makeText(this, "Enable Listener", Toast.LENGTH_SHORT).show();
            registerReceiver();
        }
        Timber.d("Listener Status: " + !isTrackingBool);
        isTracking.set(!isTrackingBool);
    }

    @OnClick(R.id.app_list_layout_clear_actions_button)
    void clearList() {
        //TODO: remove all items from packageList
    }

    @OnClick(R.id.app_list_layout_remove_app_button)
    void removeAllApps() {
        //TODO: Go through all apps and delete every app
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
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            Timber.e(e, "App install receiver was unregistered while not registered.");
        }
    }
}
