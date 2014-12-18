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

package de.poeschl.apps.tryandremove.broadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.poeschl.apps.tryandremove.service.ApplicationDetectionService;

public class AppDetectionReceiver extends BroadcastReceiver {

    private static final String TAG = AppDetectionReceiver.class.getSimpleName();

    public AppDetectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentData = intent.toUri(Intent.URI_INTENT_SCHEME);
        Log.v(TAG, "intent: " + intentData);

        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_ADDED:
                Log.v(TAG, "Package install");
                ApplicationDetectionService.startAppInstall(context, intentData);
                break;
            case Intent.ACTION_PACKAGE_REMOVED:
                Log.v(TAG, "Package uninstall");
                ApplicationDetectionService.startAppRemove(context, intentData);
                break;
        }
    }
}
