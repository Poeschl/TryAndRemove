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

package de.poeschl.apps.tryandremove.activities;


import android.view.View;

import de.poeschl.apps.tryandremove.BaseInstrumentTestCase;
import de.poeschl.apps.tryandremove.R;

public class AppListActivityTest extends BaseInstrumentTestCase<AppListActivity> {

    public AppListActivityTest() {
        super(AppListActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testUtils.resetSharedPreferences();

    }

    public void testStart() {
        solo.assertCurrentActivity("Assume AppListActivity is started", AppListActivity.class.getSimpleName());
    }

    public void testNavDrawer() {
        testUtils.openNavigationDrawer();

        assertTrue("App Name not visible in nav drawer", solo.searchText("Try and Remove", true));
        assertTrue("App List entry is missing", solo.searchText("App List", true));
        assertTrue("Settings entry is missing", solo.searchText("Settings", true));
        assertTrue("Privacy Policy entry is missing", solo.searchText("Privacy Policy", true));
        assertTrue("Imprint entry is missing", solo.searchText("Imprint", true));
    }

    public void testToolbar() {
        View recordButton = solo.getView(R.id.app_list_toolbar_action_record);
        View reloadButton = solo.getView(R.id.app_list_toolbar_action_refresh);

        assertTrue("Record button not showing", recordButton.getVisibility() == View.VISIBLE);
        assertTrue("Refresh button not showing", reloadButton.getVisibility() == View.VISIBLE);

        solo.clickOnView(recordButton);
        solo.sleep(SHORT_SLEEP_INTERVAL);
        assertEquals("Tracking should be enabled after click", true, getActivity().isTracking.get());

        solo.clickOnView(recordButton);
        solo.sleep(SHORT_SLEEP_INTERVAL);
        assertEquals("Tracking should be disabled after click", false, getActivity().isTracking.get());

        String testAppName = "Telefon";
        getActivity().packageListData.addPackage("com.android.phone");
        assertFalse(solo.searchText(testAppName, true));
        solo.clickOnView(reloadButton);
        solo.sleep(500);
        assertTrue("Dummy list entry should be visible after reload click", solo.searchText(testAppName));
    }

    public void testFloatingButton() {
        //TODO: Write tests
    }
}