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

package de.poeschl.apps.tryandremove.utils;

/**
 * Created by Markus Pöschl on 23.01.15.
 */

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import de.poeschl.apps.tryandremove.TryAndRemoveApp;

public class RobolectricGradleTestRunner extends RobolectricTestRunner {

    public RobolectricGradleTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String appFolder = "app";

        String myAppPath = TryAndRemoveApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        myAppPath = myAppPath.substring(0, myAppPath.indexOf(appFolder) + appFolder.length());
        try {
            myAppPath = URLDecoder.decode(myAppPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //Nothing to do here
        }

        String manifestPath = myAppPath + "/build/intermediates/manifests/full/debug/AndroidManifest.xml";
        String resPath = myAppPath + "/src/main/res";
        String assetPath = myAppPath + "/src/main/assets";
        return createAppManifest(Fs.fileFromPath(manifestPath), Fs.fileFromPath(resPath), Fs.fileFromPath(assetPath));
    }
}
