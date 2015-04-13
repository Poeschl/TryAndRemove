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
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.AppListAdapter;
import de.poeschl.apps.tryandremove.adapter.ListDividerDecoration;
import de.poeschl.apps.tryandremove.annotations.IsTracking;
import de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver;
import de.poeschl.apps.tryandremove.dialogs.ClearWarningDialogFragment;
import de.poeschl.apps.tryandremove.dialogs.RemoveWarningDialogFragment;
import de.poeschl.apps.tryandremove.handler.ListUpdateHandler;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import de.poeschl.apps.tryandremove.utils.AdManager;
import timber.log.Timber;

public class AppListActivity extends NavigationActivity implements ClearWarningDialogFragment.ButtonListener, RemoveWarningDialogFragment.ButtonListener, AppListAdapter.AppListAdapterListener {

    @InjectView(R.id.app_list_layout_apps_recyclerView)
    RecyclerView appListView;
    @InjectView(R.id.app_list_layout_floating_menu)
    FloatingActionsMenu floatMenu;
    @InjectView(R.id.app_list_layout_ad_banner)
    AdView bannerAd;

    @Inject
    AppDetectionReceiver receiver;
    @Inject
    @IsTracking
    BooleanPreference isTracking;
    @Inject
    PackageList packageListData;
    @Inject
    AppListAdapter appListAdapter;
    @Inject
    AppManager appManager;
    @Inject
    AdManager bannerAdManager;

    private MenuItem recordToolbarButton;
    private MenuItem reloadToolbarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

        setupLayout(R.layout.activity_app_list);

        ButterKnife.inject(this);

        appListView.setAdapter(appListAdapter);
        appListView.addItemDecoration(new ListDividerDecoration(this, null));
        appListView.setLayoutManager(new LinearLayoutManager(this));
        appListView.setHasFixedSize(true);

        packageListData.setPackageUpdateHandler(new UpdateHandler());

        appListAdapter.setListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bannerAdManager.setAdView(bannerAd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePackageList();

        bannerAdManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bannerAdManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        Timber.v("Called onDestroy - unregister receiver");

        bannerAdManager.onDestroy();
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
            recordToolbarButton.setIcon(R.drawable.ic_action_record_disable);
            recordToolbarButton.setTitle(R.string.app_list_toolbar_action_record_disable);
        } else {
            Timber.d("App tracking deactivated");
            recordToolbarButton.setIcon(R.drawable.ic_action_record_enable);
            recordToolbarButton.setTitle(R.string.app_list_toolbar_action_record_enable);
        }
    }

    private void toggleRecording() {
        boolean newTrackState = !isTracking.get();

        Timber.d("Listener Status: " + newTrackState);
        setRecordState(newTrackState);

        Toast hint;
        if (newTrackState) {
            registerReceiver();
            hint = Toast.makeText(this, getString(R.string.app_list_hint_enabled), Toast.LENGTH_SHORT);
        } else {
            unregisterReceiver();
            hint = Toast.makeText(this, getString(R.string.app_list_hint_disabled), Toast.LENGTH_SHORT);
        }
        hint.setGravity(Gravity.CENTER, 0, 0);
        hint.show();


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
        floatMenu.collapse();
    }

    @Override
    public void onItemClearClick(String packageName, int position) {
        packageListData.removePackage(packageName);
    }

    @Override
    public void onItemRemoveClick(String packageName, int position) {
        appManager.remove(packageName);

    }

    private void updatePackageList() {
        appListAdapter.updateApps(packageListData);
    }

    public class UpdateHandler extends ListUpdateHandler {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if (data.getString(ListUpdateHandler.CHANGE_KEY).equals(ListUpdateHandler.APP_LIST_CHANGE)) {
                updatePackageList();
            }
        }
    }
}
