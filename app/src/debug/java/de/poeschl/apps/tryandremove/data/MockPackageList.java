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

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.interfaces.PackageList;
import timber.log.Timber;

/**
 * Created by Markus Pöschl on 22.12.14.
 */
public class MockPackageList implements PackageList {

    List<String> apps;

    @Inject
    public MockPackageList(PackageManager packageManager) {
        apps = new LinkedList<>();
        for (PackageInfo info : packageManager.getInstalledPackages(PackageManager.GET_META_DATA)) {
            apps.add(info.packageName);
        }
    }

    @Override
    public boolean addPackage(String packageName) {
        if (!apps.contains(packageName)) {
            return apps.add(packageName);
        } else {
            return false;
        }
    }

    @Override
    public boolean removePackage(String packageName) {
        return apps.remove(packageName);
    }

    @Override
    public boolean contains(String packageName) {
        return apps.contains(packageName);
    }

    @Override
    public List<String> getPackages() {
        Timber.v("Get mocked app list");
        return apps;
    }

    @Override
    public void clear() {
        apps.clear();
    }

    @Override
    public boolean isEmpty() {
        return apps.isEmpty();
    }
}
