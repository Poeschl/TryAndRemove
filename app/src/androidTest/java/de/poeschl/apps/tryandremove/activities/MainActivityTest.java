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

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.poeschl.apps.tryandremove.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    @Test
    public void testToolbarStateOnStart() {
        onView(withId(R.id.app_list_toolbar_action_record)).check(matches(isDisplayed()));
        onView(withId(R.id.app_list_toolbar_action_refresh)).check(matches(isDisplayed()));
    }

    @Test
    public void testRightFragmentOnStart() {
        //Checks for the recycler view on start.
        //TODO: Check the list fragment on start with switching to mock mode and check for items in the recycler view

    }

    @Test
    public void testFloatingButtonExists() {
        int floatMenuId = R.id.app_list_layout_floating_menu;

        onView(withId(floatMenuId)).check(matches(isDisplayed()));
        onView(withId(floatMenuId)).perform(ViewActions.click());
        onView(withId(floatMenuId)).perform(ViewActions.click());
    }
}