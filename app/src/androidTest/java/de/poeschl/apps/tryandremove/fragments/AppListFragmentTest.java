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

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import butterknife.ButterKnife;
import de.poeschl.apps.tryandremove.BaseInstrumentTestCase;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Markus Pöschl on 30.01.2015.
 */
public class AppListFragmentTest extends BaseInstrumentTestCase<MainActivity> {

    public AppListFragmentTest() {
        super(MainActivity.class);
    }

    private void setUpFragment() {
        testActivity.onNavigationItemClick(MainActivity.Mode.APP_LIST);
    }

    @Before
    public void setUp() throws Exception {
        super.setMockMode(true);
        super.setUp();
    }

    @Test
    public void testCleanMenu() {
        ViewInteraction menuButton = onView(withId(R.id.app_list_layout_floating_menu));
        menuButton.check(matches(isDisplayed()));

        FloatingActionButton clearButton = ButterKnife.findById(testActivity, R.id.app_list_layout_clear_action_button);
        float clearYPositionCollapsed = clearButton.getY();

        menuButton.perform(ViewActions.click());
        waitTime();

        assertThat("Clear button position", clearYPositionCollapsed, Matchers.greaterThan(clearButton.getY()));
    }
}
