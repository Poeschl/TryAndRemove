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

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.SimpleAdapter;
import de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver;
import de.poeschl.apps.tryandremove.interfaces.AppContainer;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import timber.log.Timber;


public class MainActivity extends Activity {

    @InjectView(R.id.toggleButton)
    ToggleButton toggleButton;
    @InjectView(R.id.listView)
    ListView listView;

    private boolean listeningForApps = false;

    private SimpleAdapter adapter;

    private boolean init;

    private ViewGroup container;
    @Inject
    AppContainer appContainer;
    @Inject
    AppDetectionReceiver receiver;
    @Inject
    PackageList packageListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp app = TryAndRemoveApp.get(this);
        app.inject(this);

        container = appContainer.get(this);

        getLayoutInflater().inflate(R.layout.activity_main, container);

        ButterKnife.inject(this);

        adapter = new SimpleAdapter(this, new ArrayList<String>());
        listView.setAdapter(adapter);
    }

    @OnItemClick(R.id.listView)
    void onItemClick(int position) {
        String packageString = adapter.getItem(position);

        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", packageString, null));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        init = true;

        adapter.clear();
        adapter.addAll(packageListData.getPackages());

        init = false;
    }

    @OnClick(R.id.toggleButton)
    void clickToggle() {
        if (listeningForApps) {
            Toast.makeText(this, "Disable Listener", Toast.LENGTH_SHORT).show();
            unregisterReceiver();
        } else {
            Toast.makeText(this, "Enable Listener", Toast.LENGTH_SHORT).show();
            registerReceiver();
        }
        listeningForApps = !listeningForApps;
        Timber.d("Listener Status: " + listeningForApps);
    }


    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("package");
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);

        registerReceiver(receiver, filter);
    }

    public void unregisterReceiver() {
        unregisterReceiver(receiver);
    }
}
