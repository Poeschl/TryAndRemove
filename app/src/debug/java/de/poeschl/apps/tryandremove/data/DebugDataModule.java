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

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.annotations.IsMockMode;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
@Module(
        injects = {
                MockPackageList.class
        },
        library = true,
        complete = false,
        overrides = true
)
public class DebugDataModule {

    @Provides
    @Singleton
    PackageList providePackageList(SharedPreferencesPackageList sharedPreferencesPackageList, @IsMockMode BooleanPreference mockMode, MockPackageList mockPackageList) {
        if (mockMode.get()) {
            return mockPackageList;
        }
        return sharedPreferencesPackageList;
    }
}
