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

package de.poeschl.apps.tryandremove;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;

import com.robotium.solo.Solo;

/**
 * Created by Markus Pöschl on 25.02.2015.
 */
public class TestUtils {

    Solo solo;

    public TestUtils(Solo solo) {
        this.solo = solo;
    }

    public void setMockMode(boolean enabled) {
        SharedPreferences prefs = solo.getCurrentActivity().getSharedPreferences(solo.getCurrentActivity().getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putBoolean("debug_mock_mode_boolean", enabled).apply();
    }

    public void resetSharedPreferences() {
        SharedPreferences prefs = solo.getCurrentActivity().getSharedPreferences(solo.getCurrentActivity().getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().clear();
    }

    /**
     * Open the navigation drawer with a drag gesture. Click based triggering is
     * flaky on SDK < 18
     */
    public void openNavigationDrawer() {
        Point deviceSize = new Point();
        solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getSize(deviceSize);

        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;
        int fromX = 0;
        int toX = screenWidth / 2;
        int fromY = screenHeight / 2;
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 1);
    }
}
