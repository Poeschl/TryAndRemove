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
import de.poeschl.apps.tryandremove.activities.AppListActivity;

/**
 * Created by Markus Pöschl on 30.01.2015.
 */
public class AppListFragmentTest extends BaseInstrumentTestCase<AppListActivity> {

    public AppListFragmentTest() {
        super(AppListActivity.class);
    }

    public void setUp() throws Exception {
        super.setMockMode(true);
        super.setUp();
    }

    public void testAppInstall() {
//        assertTrue("Fragment not found.", solo.waitForFragmentByTag("AppListFragment"));
        //Click on Empty Text View
        solo.clickOnActionBarItem(R.id.app_list_toolbar_action_record);
        //Click on Install
        solo.clickOnView(solo.getView("debug_app_install_button"));
        //Click on Empty Text View
        solo.clickOnActionBarItem(R.id.app_list_toolbar_action_refresh);
        //Check if the new dummy item is appeared
        assertTrue("There should be a new item on app install", solo.searchText("Added 1"));

        //Click on Empty Text View
        solo.clickOnActionBarItem(R.id.app_list_toolbar_action_record);
        //Click on Install
        solo.clickOnView(solo.getView("debug_app_install_button"));
        //Click on Empty Text View
        solo.clickOnActionBarItem(R.id.app_list_toolbar_action_refresh);

        //Check if the no new dummy item is appeared
        assertTrue("There should not be a new item on app install", solo.searchText("Added 2"));
    }
}
