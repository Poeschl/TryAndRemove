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

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.annotations.IsMockMode;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.models.BooleanPreference;

/**
 * Created by markus on 22.12.14.
 */
@Module(
        injects = {
                MockPackageManager.class,
                MockAppManager.class
        },
        library = true,
        complete = false,
        overrides = true
)
public class DebugUtilsModule {
    @Provides
    @Singleton
    AppManager provideAppManager(AndroidAppManager androidAppManager, @IsMockMode BooleanPreference
            isMockMode, MockAppManager mockManager) {
        if (isMockMode.get()) {
            return mockManager;
        }
        return androidAppManager;
    }
}