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

package de.poeschl.apps.tryandremove.models;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.poeschl.apps.tryandremove.modules.BooleanPreferenceTestModule;
import de.poeschl.apps.tryandremove.utils.RoboMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BooleanPreferenceTest {

    private static final String TEST_KEY = "TEST_KEY";
    private static final boolean DEFAULT_VALUE = false;

    private static ObjectGraph objectGraph;

    @RoboMock
    @Inject
    SharedPreferences mockSharedPreferences;

    private BooleanPreference testPreference;

    @BeforeClass
    public static void setupClass() {
        objectGraph = ObjectGraph.create(BooleanPreferenceTestModule.class);
    }

    @Before
    public void setUp() {
        objectGraph.inject(this);
        testPreference = new BooleanPreference(mockSharedPreferences, TEST_KEY, DEFAULT_VALUE);
    }

    @Test
    public void testGet() {
        assertEquals(DEFAULT_VALUE, testPreference.get());

        mockSharedPreferences.edit().putBoolean(TEST_KEY, true).commit();
        assertEquals(true, testPreference.get());

        mockSharedPreferences.edit().putBoolean(TEST_KEY, false).commit();
        assertEquals(false, testPreference.get());
    }

    @Test
    public void testIsSet() {
        assertFalse("Should not be set.", testPreference.isSet());

        mockSharedPreferences.edit().putBoolean(TEST_KEY, DEFAULT_VALUE).commit();
        assertTrue("Should be set.", testPreference.isSet());
    }

    @Test
    public void testSet() {
        assertFalse("Should not exist.", mockSharedPreferences.contains(TEST_KEY));

        testPreference.set(true);
        assertEquals(true, mockSharedPreferences.getBoolean(TEST_KEY, false));

        testPreference.set(false);
        assertEquals(false, mockSharedPreferences.getBoolean(TEST_KEY, true));
    }

    @Test
    public void testDelete() {
        mockSharedPreferences.edit().putBoolean(TEST_KEY, DEFAULT_VALUE).commit();

        testPreference.delete();
        assertFalse("Should be removed.", mockSharedPreferences.contains(TEST_KEY));
    }
}