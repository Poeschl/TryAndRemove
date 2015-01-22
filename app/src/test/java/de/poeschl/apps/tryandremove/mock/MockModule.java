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

import android.app.Application;
import android.content.pm.PackageManager;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.data.MockPackageList;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.service.ServiceTestModule;
import de.poeschl.apps.tryandremove.utils.MockAppManager;
import de.poeschl.apps.tryandremove.utils.MockPackageManager;
import de.poeschl.apps.tryandremove.utils.RoboMock;
import de.poeschl.apps.tryandremove.utils.RobolectricModule;

/**
 * Created by Markus Pöschl on 22.01.2015.
 */
@Module(
        includes = {
                RobolectricModule.class,
        },
        injects = {
                ServiceTestModule.class
        },
        library = true
)
public class MockModule {
    @Mock
    @Provides
    PackageList providesPackageList(PackageManager packageManager) {
        return new MockPackageList(packageManager);
    }

    @Provides
    PackageManager providePackageManager(@RoboMock Application application) {
        return new MockPackageManager(application);
    }

    @Mock
    @Provides
    AppManager providesAppManager(@RoboMock Application application) {
        return new MockAppManager(application);
    }
}
