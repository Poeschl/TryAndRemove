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

package de.poeschl.apps.debuganddelete.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.poeschl.apps.debuganddelete.DebugAndDeleteApp;
import de.poeschl.apps.debuganddelete.R;
import de.poeschl.apps.debuganddelete.app.adapter.SimpleAdapter;
import de.poeschl.apps.debuganddelete.app.appContainer.AppContainer;
import de.poeschl.apps.debuganddelete.service.broadcastReciever.AppInstall;


public class MainActivity extends Activity {

    @InjectView(R.id.toggleButton)
    ToggleButton toggleButton;
    @InjectView(R.id.listView)
    ListView listView;

    private boolean listeningForApps = false;

    private SimpleAdapter adapter;

    public static List<String> incoming;

    private AppInstall receiver;

    private boolean init;
    private ViewGroup container;
    @Inject
    AppContainer appContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DebugAndDeleteApp app = DebugAndDeleteApp.get(this);
        app.inject(this);

        container = appContainer.get(this);

        getLayoutInflater().inflate(R.layout.activity_main, container);

        ButterKnife.inject(this);

        incoming = new ArrayList<>();

        adapter = new SimpleAdapter(this, new ArrayList<String>());
        listView.setAdapter(adapter);

        receiver = new AppInstall();
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

        if (!incoming.isEmpty()) {
            adapter.addAll(incoming);
            incoming.clear();
        }

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
        Log.d("ListenStatus", listeningForApps + "");
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
