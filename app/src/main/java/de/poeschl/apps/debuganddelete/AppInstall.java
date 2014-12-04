package de.poeschl.apps.debuganddelete;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AppInstall extends BroadcastReceiver {

    public AppInstall() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "new app installed", Toast.LENGTH_SHORT).show();
        String intentdata = intent.toUri(Intent.URI_INTENT_SCHEME);
        Log.e("BCReceiver", "new app installed " + intentdata);
        HandleInstallService.startActionInstall(context, intentdata);
    }
}
