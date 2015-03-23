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

package de.poeschl.apps.tryandremove.trees;/*
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

import android.provider.Settings;

import com.crashlytics.android.Crashlytics;

import de.poeschl.apps.tryandremove.BuildConfig;
import timber.log.Timber;

/**
 * It manages the report of log calls to Crashlytics. It only sends warnings or errors.
 * It must be planted after Crashlytics is activated.
 * Created by Markus Pöschl on 12.12.2014.
 */
public final class CrashlyticsReportTree extends Timber.HollowTree {

    private static final String ERROR_TAG = "ERROR: ";
    private static final String WARNING_TAG = "WARN: ";
    private static final String INFO_TAG = "";

    private static final String GIT_SHA_PREF = "GIT_SHA";
    private static final String ANDROID_ID = "ANDROID_ID";

    public CrashlyticsReportTree() {
        Crashlytics.setString(GIT_SHA_PREF, BuildConfig.GIT_SHA);
        Crashlytics.setString(ANDROID_ID, Settings.Secure.ANDROID_ID);
    }

    @Override
    public void i(String message, Object... args) {
        logMessage(INFO_TAG + message, args);
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        logMessage(INFO_TAG + message, args);
        // NOTE: explicitly not sending the exception
    }

    @Override
    public void w(String message, Object... args) {
        logMessage(WARNING_TAG + message, args);
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        logMessage(WARNING_TAG + message, args);
        // NOTE: explicitly not sending the exception
    }

    @Override
    public void e(String message, Object... args) {
        logMessage(ERROR_TAG + message, args);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        logMessage(ERROR_TAG + message, args);
        Crashlytics.logException(t);
    }

    private void logMessage(String message, Object... args) {
        Crashlytics.log(String.format(message, args));
    }
}
