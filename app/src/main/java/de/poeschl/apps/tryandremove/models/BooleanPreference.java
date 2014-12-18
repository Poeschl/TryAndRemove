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

package de.poeschl.apps.tryandremove.models;

import android.content.SharedPreferences;

public class BooleanPreference {
    private final SharedPreferences preferences;
    private final String key;
    private final boolean defaultVal;

    public BooleanPreference(SharedPreferences preferences, String key, boolean defaultVal) {
        this.preferences = preferences;
        this.key = key;
        this.defaultVal = defaultVal;
    }

    public boolean get() {
        return preferences.getBoolean(key, defaultVal);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}