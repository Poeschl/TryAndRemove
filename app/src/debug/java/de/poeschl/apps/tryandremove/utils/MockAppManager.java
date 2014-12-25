/*
 * Copyright (c) 2014 Markus Poeschl
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

import java.util.List;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.service.ApplicationDetectionService;

/**
 * This manager mocks the app management from android and manually call the specific service methods.
 * <p/>
 * Created by markus on 22.12.14.
 */
public class MockAppManager implements AppManager {

    private Application app;

    @Inject
    public MockAppManager(Application app) {
        this.app = app;

    }

    @Override
    public boolean exists(String appPackage) {
        return true;
    }

    @Override
    public void remove(String appPackage) {
        String pareFormattedPackage = "intent:" + appPackage + "#Intent";
        ApplicationDetectionService.startAppRemove(app, pareFormattedPackage);
    }

    @Override
    public void remove(List<String> appPackages) {
        for (String appPackage : appPackages) {
            remove(appPackage);
        }
    }
}
