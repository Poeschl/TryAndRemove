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

import de.poeschl.apps.tryandremove.service.ApplicationDetectionService;
import timber.log.Timber;

public class AppDetectionReceiver extends BroadcastReceiver {

    private boolean registered;

    public AppDetectionReceiver() {
        registered = false;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentData = intent.toUri(Intent.URI_INTENT_SCHEME);
        Timber.v("intent: " + intentData);

        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_ADDED:
                Timber.d("Package install received");
                ApplicationDetectionService.startAppInstall(context, intentData);
                break;
            case Intent.ACTION_PACKAGE_REMOVED:
                Timber.d("Package uninstall received");
                ApplicationDetectionService.startAppRemove(context, intentData);
                break;
        }
    }
}
