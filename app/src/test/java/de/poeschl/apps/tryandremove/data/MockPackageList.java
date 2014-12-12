/*
 * Copyright 2014 Markus Pöschl
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

import java.util.HashSet;
import java.util.Set;

import de.poeschl.apps.tryandremove.interfaces.PackageList;

/**
 * Return mocking data.
 * Created by Markus Pöschl on 11.12.2014.
 */
public class MockPackageList implements PackageList {

    public static final String TEST_PACKAGE_0 = "com.dummy.0";
    public static final String TEST_PACKAGE_1 = "com.dummy.1";
    public static final String TEST_PACKAGE_2 = "com.dummy.2";
    public static final String TEST_PACKAGE_3 = "com.dummy.3";

    public static final String TEST_PACKAGE_NOT_ADDED = "com.dummy.new0";

    private Set<String> stringSet = new HashSet<>();

    public MockPackageList() {
        stringSet.add(TEST_PACKAGE_0);
        stringSet.add(TEST_PACKAGE_1);
        stringSet.add(TEST_PACKAGE_2);
        stringSet.add(TEST_PACKAGE_3);
    }

    @Override
    public boolean addPackage(String packageName) {
        return stringSet.add(packageName);
    }

    @Override
    public boolean removePackage(String packageName) {
        return stringSet.remove(packageName);
    }

    @Override
    public boolean contains(String packageName) {
        return stringSet.contains(packageName);
    }

    @Override
    public Set<String> getPackages() {
        return stringSet;
    }
}
