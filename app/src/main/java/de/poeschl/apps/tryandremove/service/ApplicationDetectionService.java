/*
 * Copyright 2014 Markus Poeschl
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

package de.poeschl.apps.tryandremove.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import hugo.weaving.DebugLog;


/**
 * This intent service cares about the broadcasts from the {@link de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver}
 */
public class ApplicationDetectionService extends IntentService {
    static final String ACTION_APP_INSTALLED = "APP_INSTALLED";
    static final String ACTION_APP_REMOVED = "APP_REMOVED";

    static final String EXTRA_INTENT_STRING = "INTENT_STRING";

    /**
     * Starts this service to perform a app install action.
     *
     * @see IntentService
     */
    public static void startAppInstall(Context context, String intentString) {
        Intent intent = new Intent(context, ApplicationDetectionService.class);
        intent.setAction(ACTION_APP_INSTALLED);
        intent.putExtra(EXTRA_INTENT_STRING, intentString);
        context.startService(intent);
    }

    /**
     * Starts this service to perform a app install action.
     *
     * @see IntentService
     */
    public static void startAppRemove(Context context, String intentString) {
        Intent intent = new Intent(context, ApplicationDetectionService.class);
        intent.setAction(ACTION_APP_REMOVED);
        intent.putExtra(EXTRA_INTENT_STRING, intentString);
        context.startService(intent);
    }

    @Inject
    PackageList packageList;

    public ApplicationDetectionService() {
        super(ApplicationDetectionService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TryAndRemoveApp app = TryAndRemoveApp.get(ApplicationDetectionService.this);
        app.inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String packageString = extractPackageString(intent.getStringExtra(EXTRA_INTENT_STRING));
            switch (action) {
                case ACTION_APP_INSTALLED:
                    handleActionAppInstalled(packageString);
                    break;
                case ACTION_APP_REMOVED:
                    handleActionAppRemoved(packageString);
                    break;
            }
        }
    }

    void handleActionAppInstalled(String packageString) {
        if (!packageList.contains(packageString)) {
            packageList.addPackage(packageString);
        }
    }

    void handleActionAppRemoved(String packageString) {
        if (packageList.contains(packageString)) {
            packageList.removePackage(packageString);
        }
    }

    @DebugLog
    String extractPackageString(String intentData) {
        String packagePre = "intent:";
        String packageSuf = "#Intent";
        return intentData.substring(intentData.indexOf(packagePre) + packagePre.length(), intentData.indexOf(packageSuf));
    }
}
