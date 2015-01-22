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

package de.poeschl.apps.tryandremove.service;

import android.content.Intent;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dagger.ObjectGraph;

import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_0;
import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_2;
import static de.poeschl.apps.tryandremove.TestConstants.TEST_PACKAGE_NOT_ADDED;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ApplicationDetectionServiceTest {

    private static ObjectGraph objectGraph;
    private static final String DUMMY_INTENT_STRING = "intent:" + TEST_PACKAGE_0 + "#Intent.PACKAGE_ADDED,0x5250 some more stuff";

    private ApplicationDetectionService testClass;

    @BeforeClass
    public static void setUpClass() {
        objectGraph = ObjectGraph.create(ServiceTestModule.class);
    }

    @Before
    public void setUp() {
        testClass = new ApplicationDetectionService();
        objectGraph.inject(testClass);
    }

    @Test
    public void testExtractPackageString() {
        String result = testClass.extractPackageString(DUMMY_INTENT_STRING);

        assertEquals(TEST_PACKAGE_0, result);
    }

    @Test
    public void testHandleActionAppRemoved() {
        testClass.handleActionAppRemoved(TEST_PACKAGE_2);
        assertFalse(testClass.packageList.contains(TEST_PACKAGE_2));
    }

    @Test
    public void testHandleActionAppInstalled() {
        testClass.handleActionAppInstalled(TEST_PACKAGE_NOT_ADDED);
        assertTrue(testClass.packageList.contains(TEST_PACKAGE_NOT_ADDED));
    }

    @Test
    public void testHandleIntent() {
        Intent dummyInstall = new Intent(ApplicationDetectionService.ACTION_APP_INSTALLED);
        dummyInstall.putExtra(ApplicationDetectionService.EXTRA_INTENT_STRING, DUMMY_INTENT_STRING);
        testClass.onHandleIntent(dummyInstall);

        assertTrue(testClass.packageList.contains(TEST_PACKAGE_0));

        Intent dummyRemove = new Intent(ApplicationDetectionService.ACTION_APP_REMOVED);
        dummyRemove.putExtra(ApplicationDetectionService.EXTRA_INTENT_STRING, DUMMY_INTENT_STRING);
        testClass.onHandleIntent(dummyRemove);

        assertFalse(testClass.packageList.contains(TEST_PACKAGE_0));
    }
}
