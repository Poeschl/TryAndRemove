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

import com.google.android.gms.ads.AdView;

import de.poeschl.apps.tryandremove.BaseInstrumentTestCase;
import de.poeschl.apps.tryandremove.BuildConfig;
import de.poeschl.apps.tryandremove.R;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class AppListActivityTest extends BaseInstrumentTestCase<AppListActivity> {

    private static final String MOCK_DUMMY = "de.mock.Dummy0";

    public AppListActivityTest() {
        super(AppListActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setMockMode(true);
        getActivity();
        //Ensure that we always have at least this dummy on the list.
        getActivity().packageListData.addPackage(MOCK_DUMMY);
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
        assertTrue("Version is missing", solo.searchText("Version: " + BuildConfig.VERSION_NAME + " - " + BuildConfig.GIT_SHA));
    }

    public void testToolbar() {
        View recordButton = solo.getView(R.id.app_list_toolbar_action_record);
        View reloadButton = solo.getView(R.id.app_list_toolbar_action_refresh);

        assertTrue("Record button not showing", recordButton.getVisibility() == View.VISIBLE);
        assertTrue("Refresh button not showing", reloadButton.getVisibility() == View.VISIBLE);

        solo.clickOnView(recordButton);
        solo.sleep(MEDIUM_SLEEP_INTERVAL);
        assertEquals("Tracking should be enabled after click", true, getActivity().isTracking.get());

        solo.clickOnView(recordButton);
        solo.sleep(MEDIUM_SLEEP_INTERVAL);
        assertEquals("Tracking should be disabled after click", false, getActivity().isTracking.get());

        String testAppName = "Added";
        getActivity().packageListData.addPackage("de.dummy." + testAppName);
        solo.sleep(MEDIUM_SLEEP_INTERVAL);
        assertTrue("Test package not found", solo.searchText(testAppName, true));
    }

    public void testClearButton() {
        View clearButton = solo.getView(R.id.app_list_layout_clear_action_button);

        solo.clickOnView(solo.getView(R.id.fab_expand_menu_button));
        solo.sleep(LONG_SLEEP_INTERVAL);

        //Check if button is expanded
        assertThat("Clear button is not expanded", clearButton.getTranslationY(), equalTo(0f));

        solo.clickOnView(clearButton);

        solo.waitForDialogToOpen();
        assertTrue("Wrong dialog message", solo.searchText("Clear app list?", true));

        solo.clickOnButton("Cancel");
        solo.waitForDialogToClose();

        assertTrue("List shouldn't be cleared on cancel", solo.searchText("Dummy0", true));

        solo.clickOnView(clearButton);
        solo.waitForDialogToOpen();

        solo.clickOnButton("Clear");
        solo.waitForDialogToClose();

        //Check if button is hidden in menu again
        assertThat("Clear button is not collapsed", clearButton.getTranslationY(), greaterThan(0f));
        assertFalse("List should be cleared on clear", solo.searchText("Dummy0", true));
    }

    public void testRemoveButton() {
        View removeButton = solo.getView(R.id.app_list_layout_remove_action_button);

        solo.clickOnView(solo.getView(R.id.fab_expand_menu_button));
        solo.sleep(LONG_SLEEP_INTERVAL);

        //Check if button is expanded
        assertThat("Remove button is not expanded", removeButton.getTranslationY(), equalTo(0f));

        solo.clickOnView(removeButton);

        solo.waitForDialogToOpen();
        assertTrue("Wrong dialog message", solo.searchText("Remove all apps in the list from your phone?", true));

        solo.clickOnButton("Cancel");
        solo.waitForDialogToClose();

        assertTrue("List shouldn't be cleared on cancel", solo.searchText("Dummy0", true));

        solo.clickOnView(removeButton);
        solo.waitForDialogToOpen();

        solo.clickOnButton("Remove");
        solo.waitForDialogToClose();

        solo.sleep(SHORT_SLEEP_INTERVAL);

        //Check if button is hidden in menu again
        assertThat("Remove button is not collapsed", removeButton.getTranslationY(), greaterThan(0f));
        assertFalse("List should be clean on clear", solo.searchText("Dummy0", true));
    }

    public void testFloatingButton() {
        solo.clickOnView(solo.getView(R.id.fab_expand_menu_button));
        solo.sleep(LONG_SLEEP_INTERVAL);

        assertTrue("Clear list label is not shown.", solo.searchText("Clear list", true));
        assertTrue("Remove app label not shown.", solo.searchText("Remove all shown apps", true));
    }

    public void testNavigationToImprint() {
        testUtils.openNavigationDrawer();

        solo.clickOnText("Imprint");

        assertTrue("WebViewActivity should be started.", solo.waitForActivity(WebViewActivity.class));

        assertTrue("Imprint should be shown", solo.searchText("Imprint"));
        assertTrue("Imprint content should be shown", solo.searchText("Impressum"));
    }

    public void testNavigationToPrivacyPolicy() {
        testUtils.openNavigationDrawer();

        solo.clickOnText("Privacy Policy");

        assertTrue("WebViewActivity should be started.", solo.waitForActivity(WebViewActivity.class));

        assertTrue("Policy should be shown", solo.searchText("Privacy Policy", 2));
        assertTrue("Policy content should be shown", solo.searchText("Contact"));
    }

    public void testNavigationToSettings() {
        testUtils.openNavigationDrawer();

        solo.clickOnText("Settings");

        assertTrue("SettingActivity should be started.", solo.waitForActivity(SettingActivity.class));
    }

    public void testAd() {

        boolean adInit = solo.waitForView(AdView.class) || solo.waitForLogMessage("Ad failed to load");

        assertTrue("Ad should be shown", adInit);
    }
}