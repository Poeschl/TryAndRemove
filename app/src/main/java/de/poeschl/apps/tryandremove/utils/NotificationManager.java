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

package de.poeschl.apps.tryandremove.utils;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import de.poeschl.apps.tryandremove.R;

/**
 * Created by Markus Pöschl on 14.04.2015.
 */
public class NotificationManager {

    private static final int RECORD_ID = 18915651;

    private NotificationCompat.Builder recordNotificationBuilder;
    private android.app.NotificationManager notificationManager;

    protected NotificationManager(Context context) {

        notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        recordNotificationBuilder = new NotificationCompat.Builder(context);
        recordNotificationBuilder.setSmallIcon(R.drawable.ic_launcher_app);
        recordNotificationBuilder.setContentTitle(context.getString(R.string.notification_title));
        recordNotificationBuilder.setContentText(context.getString(R.string.notification_message));
    }

    public void createRecordNotification() {
        notificationManager.notify(RECORD_ID, recordNotificationBuilder.build());
    }

    public void hideRecordNotification() {
        notificationManager.cancel(RECORD_ID);
    }
}
