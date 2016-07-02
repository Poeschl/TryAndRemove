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

import android.content.Intent;

import de.poeschl.apps.tryandremove.BaseInstrumentTestCase;

/**
 * Created by Markus Pöschl on 25.02.2015.
 */
public class WebViewActivityTest extends BaseInstrumentTestCase<WebViewActivity> {

    public WebViewActivityTest() {
        super(WebViewActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra(WebViewActivity.URL_EXTRA_KEY, "file:///android_asset/PrivacyPolicy.html");
        intent.putExtra(WebViewActivity.ACTIONBAR_TITLE_KEY, "TITLE");
        setActivityIntent(intent);
        getActivity();
    }

    public void testStart() {
        solo.assertCurrentActivity("Assume AppListActivity is started", WebViewActivity.class.getSimpleName());
    }

//    public void testWebView() {
//        assertTrue("Dummy article should be displayed", solo.searchText("Contact", 1, true));
//    }

    public void testToolbarTitle() {
        assertTrue("Dummy article should be displayed", solo.searchText("TITLE"));
    }

}