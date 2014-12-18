/*
 * Copyright 2014 Markus Poeschl
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

package de.poeschl.apps.tryandremove.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.HashSet;
import java.util.Set;

import static de.poeschl.apps.tryandremove.data.MockPackageList.TEST_PACKAGE_0;
import static de.poeschl.apps.tryandremove.data.MockPackageList.TEST_PACKAGE_1;
import static de.poeschl.apps.tryandremove.data.MockPackageList.TEST_PACKAGE_2;
import static de.poeschl.apps.tryandremove.data.MockPackageList.TEST_PACKAGE_3;
import static de.poeschl.apps.tryandremove.data.MockPackageList.TEST_PACKAGE_NOT_ADDED;
import static de.poeschl.apps.tryandremove.data.SharedPreferencesPackageList.PREF_PACKAGE_LIST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SharedPreferencePackageListTest {

    private SharedPreferencesPackageList testList;
    private SharedPreferences preferences;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() {
        Set<String> dummys = new HashSet<>();
        dummys.add(TEST_PACKAGE_0);
        dummys.add(TEST_PACKAGE_1);
        dummys.add(TEST_PACKAGE_2);
        dummys.add(TEST_PACKAGE_3);

        preferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        testList = new SharedPreferencesPackageList(preferences, Robolectric.application);
        preferences.edit().putStringSet(PREF_PACKAGE_LIST, dummys).commit();
    }

    @Test
    public void testAddPackage() {
        testList.addPackage(TEST_PACKAGE_NOT_ADDED);
        assertTrue(preferences.getStringSet(PREF_PACKAGE_LIST, new HashSet<String>()).contains(TEST_PACKAGE_NOT_ADDED));
    }

    @Test
    public void testRemovePackage() {
        testList.removePackage(TEST_PACKAGE_NOT_ADDED);
        assertFalse(preferences.getStringSet(PREF_PACKAGE_LIST, new HashSet<String>()).contains(TEST_PACKAGE_NOT_ADDED));
    }

    @Test
    public void testContains() {
        assertTrue("Should contains " + TEST_PACKAGE_0, testList.contains(TEST_PACKAGE_0));
        assertFalse("Should not contain " + TEST_PACKAGE_NOT_ADDED, testList.contains(TEST_PACKAGE_NOT_ADDED));
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void testGetPackages() {
        int size = testList.getPackages().size();
        assertEquals("Set should contains the 4 dummys.", 4, size);

        preferences.edit().clear().commit();
        size = testList.getPackages().size();
        assertEquals("Set should be empty on fresh start.", 0, size);
    }

    @Test
    public void testValidatePackages() {
        RobolectricPackageManager pm = new RobolectricPackageManager();
        pm.addPackage(TEST_PACKAGE_2);

        testList.validatePackages(pm);

        assertEquals("List should only contain one item.", 1, testList.getPackages().size());
        assertTrue("Should contain " + TEST_PACKAGE_2, testList.getPackages().contains(TEST_PACKAGE_2));
    }
}
