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

package de.poeschl.apps.tryandremove.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import de.poeschl.apps.tryandremove.R;

/**
 * Created by Markus Pöschl on 24.02.2015.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String CRASHLYTICS_ENABLED_KEY = "preferences_crashlytics_enabled";

    private SwitchPreference crashlyticsEnabledPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        crashlyticsEnabledPreference = (SwitchPreference) findPreference(CRASHLYTICS_ENABLED_KEY);
        crashlyticsEnabledPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(CRASHLYTICS_ENABLED_KEY)) {

            ((SwitchPreference) preference).setChecked((boolean) newValue);

            //TODO: Set the boolean and disable / enable crashlytics
        }
        return false;
    }
}
