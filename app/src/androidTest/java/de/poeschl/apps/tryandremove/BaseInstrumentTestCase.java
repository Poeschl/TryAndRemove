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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


/**
 * Created by Markus Pöschl on 30.01.15.
 */
public class BaseInstrumentTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public static final int TIMEOUT = 500;

    protected Solo solo;

    public BaseInstrumentTestCase(Class<T> testClass) {
        super(testClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    @Override
    public T getActivity() {
        return super.getActivity();
    }

    public void setMockMode(boolean enabled) {
        SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putBoolean("debug_mock_mode_boolean", enabled).commit();
    }
}