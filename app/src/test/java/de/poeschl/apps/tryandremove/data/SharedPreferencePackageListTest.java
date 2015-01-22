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

package de.poeschl.apps.tryandremove.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.mock.Mock;
import de.poeschl.apps.tryandremove.utils.MockAppManager;
import de.poeschl.apps.tryandremove.utils.RoboMock;

import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_0;
import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_1;
import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_2;
import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_3;
import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_NOT_ADDED;
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

    private static ObjectGraph objectGraph;

    @RoboMock
    @Inject
    SharedPreferences mockedPreferences;

    @Mock
    @Inject
    AppManager mockedAppManager;

    private SharedPreferencesPackageList testList;

    @BeforeClass
    public static void setUpClass() {
        objectGraph = ObjectGraph.create(DataTestModule.class);
    }

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() {

        objectGraph.inject(this);

        testList = new SharedPreferencesPackageList(mockedPreferences, mockedAppManager);

        Set<String> dummys = new HashSet<>();
        dummys.add(TEST_PACKAGE_0);
        dummys.add(TEST_PACKAGE_1);
        dummys.add(TEST_PACKAGE_2);
        dummys.add(TEST_PACKAGE_3);

        mockedPreferences.edit().putStringSet(PREF_PACKAGE_LIST, dummys).commit();
    }

    @SuppressLint("CommitPrefEdits")
    @After
    public void tearDown() {
        mockedPreferences.edit().clear().commit();
        ((MockAppManager) mockedAppManager).setNotExistingApp("");
    }

    @Test
    public void testAddPackage() {
        testList.addPackage(TEST_PACKAGE_NOT_ADDED);
        assertTrue(mockedPreferences.getStringSet(PREF_PACKAGE_LIST, new HashSet<String>()).contains(TEST_PACKAGE_NOT_ADDED));
    }

    @Test
    public void testRemovePackage() {
        testList.removePackage(TEST_PACKAGE_NOT_ADDED);
        assertFalse(mockedPreferences.getStringSet(PREF_PACKAGE_LIST, new HashSet<String>()).contains(TEST_PACKAGE_NOT_ADDED));
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

        mockedPreferences.edit().clear().commit();
        size = testList.getPackages().size();
        assertEquals("Set should be empty on fresh start.", 0, size);
    }

    @Test
    public void testValidatePackages() {
        testList.validatePackages();

        assertEquals("List should contain four item.", 4, testList.getPackages().size());

        //Simulate the external uninstall of the app.
        ((MockAppManager) mockedAppManager).setNotExistingApp(TEST_PACKAGE_2);

        testList.validatePackages();

        assertEquals("List should be of size 3.", 3, testList.getPackages().size());

    }
}
