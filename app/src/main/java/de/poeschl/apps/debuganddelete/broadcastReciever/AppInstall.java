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

package de.poeschl.apps.debuganddelete.broadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import de.poeschl.apps.debuganddelete.service.HandleInstallService;

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
