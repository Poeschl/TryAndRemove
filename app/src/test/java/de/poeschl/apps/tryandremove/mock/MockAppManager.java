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

package de.poeschl.apps.tryandremove.mock;

import java.util.List;

import de.poeschl.apps.tryandremove.interfaces.AppManager;

/**
 * This manager mocks the app management from android and manually call the specific service methods.
 * <p/>
 * Created by markus on 22.12.14.
 */
public class MockAppManager implements AppManager {

    private String notExistingApp = "";

    public MockAppManager() {

    }

    public void setNotExistingApp(String appPackage) {
        this.notExistingApp = appPackage;
    }

    @Override
    public boolean exists(String appPackage) {
        return !notExistingApp.equals(appPackage);
    }

    @Override
    public void remove(String appPackage) {
        String pareFormattedPackage = "intent:" + appPackage + "#Intent";
//        ApplicationDetectionService.startAppRemove(app, pareFormattedPackage);
    }

    @Override
    public void remove(List<String> appPackages) {
        for (String appPackage : appPackages) {
            remove(appPackage);
        }
    }
}
