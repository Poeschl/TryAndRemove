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

package de.poeschl.apps.tryandremove;

import android.app.Application;
import android.content.pm.PackageManager;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.activities.DebugActivityModule;
import de.poeschl.apps.tryandremove.annotations.IsMockMode;
import de.poeschl.apps.tryandremove.data.DebugDataModule;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import de.poeschl.apps.tryandremove.models.DebugModelModule;
import de.poeschl.apps.tryandremove.models.ModelModule;
import de.poeschl.apps.tryandremove.utils.DebugUtilsModule;
import de.poeschl.apps.tryandremove.utils.MockPackageManager;

/**
 * Created by Markus Pöschl on 05.12.14.
 */
@Module(
        addsTo = AppModule.class,
        injects = {
                ModelModule.class
        },
        includes = {
                DebugActivityModule.class,
                DebugModelModule.class,
                DebugUtilsModule.class,
                DebugDataModule.class
        },
        overrides = true
)
public final class DebugAppModule {

    @Provides
    PackageManager providePackageManager(Application application, @IsMockMode BooleanPreference isMockMode, MockPackageManager mockPackageManager) {
        if (isMockMode.get()) {
            return mockPackageManager;
        }
        return application.getPackageManager();
    }

}
