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
import android.widget.Toast;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.annotations.CrashlyticsEnabled;
import de.poeschl.apps.tryandremove.models.BooleanPreference;

/**
 * Created by Markus Pöschl on 24.02.2015.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    protected static final String CRASHLYTICS_ENABLED_KEY = "preferences_crashlytics_enabled";

    @Inject
    @CrashlyticsEnabled
    protected BooleanPreference crashlyticsEnabled;

    private SwitchPreference crashlyticsEnabledPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(getActivity()).inject(this);

        addPreferencesFromResource(R.xml.preferences);

        crashlyticsEnabledPreference = (SwitchPreference) findPreference(CRASHLYTICS_ENABLED_KEY);
        crashlyticsEnabledPreference.setChecked(crashlyticsEnabled.get());
        crashlyticsEnabledPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(CRASHLYTICS_ENABLED_KEY)) {
            boolean newState = (boolean) newValue;
            ((SwitchPreference) preference).setChecked(newState);
            crashlyticsEnabled.set(newState);

            if (!newState) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.preferences_crashlytics_enabled_toast), Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
