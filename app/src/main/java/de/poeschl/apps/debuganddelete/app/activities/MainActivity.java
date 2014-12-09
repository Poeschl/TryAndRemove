package de.poeschl.apps.debuganddelete.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
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
    @InjectView(R.id.checkBox)
    CheckBox debugCheck;

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

        try {
            debugCheck.setChecked(getDebugOption());
        } catch (IOException e) {
            Log.e("Debug", "Exception", e);
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

    @OnCheckedChanged(R.id.checkBox)
    void debugChanged(boolean checked) {
        if (init) {
            return;
        }

        int enabled = checked ? 1 : 0;
        try {
            setDebugOption(enabled);
        } catch (IOException e) {
            Log.e("DEBUG OPTION", "Exception", e);
        }
    }

    private void setDebugOption(int enabled) throws IOException {

        Process p = Runtime.getRuntime().exec("su");
        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());

        //from here all commands are executed with su permissions
        List<String> commands = new ArrayList<>();
        commands.add("setprop persist.service.adb.enable " + enabled);
        if (enabled == 1) {
            commands.add("start adbd");
        } else {
            commands.add("stop adbd");
        }

        for (String command : commands) {
            stdin.write((command + "\n").getBytes("UTF-8"));
            stdin.flush();
        }
    }

    private boolean getDebugOption() throws IOException {

        //TODO: read the flag from the shell. The android settings are not updating correctly

        boolean debug = false;
        try {
            debug = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ADB_ENABLED) == 1;

        } catch (Settings.SettingNotFoundException e) {
            Log.e("DEBUG OPTION", "Exception", e);
        }

        return debug;
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
