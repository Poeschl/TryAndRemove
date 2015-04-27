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

import de.poeschl.apps.tryandremove.BaseInstrumentTestCase;

/**
 * Created by Markus Pöschl on 25.02.2015.
 */
public class SettingActivityTest extends BaseInstrumentTestCase<SettingActivity> {

    public SettingActivityTest() {
        super(SettingActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testStart() {
        solo.assertCurrentActivity("Assume SettingActivity is started", SettingActivity.class.getSimpleName());
    }

    public void testTopBar() {
        assertTrue("Title in toolbar not correct", solo.searchText("Settings"));
    }

    //More in fragment test
}
