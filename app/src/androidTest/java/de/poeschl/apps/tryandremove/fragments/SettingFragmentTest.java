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

import de.poeschl.apps.tryandremove.BaseInstrumentTestCase;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.activities.SettingActivity;

/**
 * Created by Markus Pöschl on 25.02.2015.
 */
public class SettingFragmentTest extends BaseInstrumentTestCase<SettingActivity> {

    private SettingsFragment settingsFragment;

    public SettingFragmentTest() {
        super(SettingActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        getActivity();

        settingsFragment = (SettingsFragment) getActivity().getFragmentManager().findFragmentById(R.id.settings_fragment);
    }

    public void testCrashlyticsSettings() {
        assertTrue("Crashlytics should be enabled by default", settingsFragment.crashlyticsEnabled.get());

        solo.clickOnText("Collect Analysis Data");
        assertTrue("Toast with hint is not shown", solo.waitForText("Crashlytics gets disabled the next", 1, TIMEOUT));
        assertFalse("Crashlytics should be disabled after click", settingsFragment.crashlyticsEnabled.get());
    }

    public void testColoredCellsSetting() {
        assertFalse("Colored cells should be disabled per default.", settingsFragment.coloredCellsEnabled.get());

        solo.clickOnText("Colored Cells");
        //Set the sleep to wait for the shared preferences writing.
        solo.sleep(MEDIUM_SLEEP_INTERVAL);
        assertTrue("Colored cells should be disabled after click", settingsFragment.coloredCellsEnabled.get());

    }
}
