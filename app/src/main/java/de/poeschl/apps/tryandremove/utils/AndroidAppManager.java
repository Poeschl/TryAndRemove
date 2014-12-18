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

package de.poeschl.apps.tryandremove.utils;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.interfaces.AppManager;

/**
 * This class cares about the app management on the android device.
 * <p/>
 * Created by Markus Pöschl on 18.12.2014.
 */
public class AndroidAppManager implements AppManager {

    private PackageManager packageManager;
    private Application application;

    @Inject
    public AndroidAppManager(PackageManager packageManager, Application application) {
        this.packageManager = packageManager;
        this.application = application;
    }

    /**
     * Check if the given package exists on the phone.
     *
     * @param appPackage The package name to check.
     * @return True if the package exists.
     */
    @Override
    public boolean exists(String appPackage) {
        try {
            packageManager.getPackageInfo(appPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Uninstalls a app from the device.
     *
     * @param appPackage The package to be removed.
     */
    @Override
    public void remove(String appPackage) {
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", appPackage, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    /**
     * Uninstall a bundle of apps from the device.
     *
     * @param appPackages The package ids of the packages.
     */
    @Override
    public void remove(List<String> appPackages) {
        for (String appPackage : appPackages) {
            remove(appPackage);
        }
    }
}
