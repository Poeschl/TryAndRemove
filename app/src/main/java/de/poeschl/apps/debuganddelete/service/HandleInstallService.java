package de.poeschl.apps.debuganddelete.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.poeschl.apps.debuganddelete.app.activities.MainActivity;

public class HandleInstallService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_INSTALL_APP = "com.dummy.mpo.dummy.action.INSTALL_APP";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.dummy.mpo.dummy.extra.PACKAGE_STRING";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionInstall(Context context, String param1) {
        Intent intent = new Intent(context, HandleInstallService.class);
        intent.setAction(ACTION_INSTALL_APP);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    public HandleInstallService() {
        super("HandleInstallService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSTALL_APP.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleActionFoo(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1) {
        String packagePre = "intent:";
        String packageSuf = "#Intent";
        String packageName = param1.substring(param1.indexOf(packagePre) + packagePre.length(), param1.indexOf(packageSuf));

        Log.e("Service", "handle:" + packageName);

        MainActivity.incoming.add(packageName);
    }
}
