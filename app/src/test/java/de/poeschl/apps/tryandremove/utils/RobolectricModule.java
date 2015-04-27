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

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowPreferenceManager;

import dagger.Module;
import dagger.Provides;


/**
 * Created by Markus Pöschl on 21.01.2015.
 */
@Module(
        library = true
)
public class RobolectricModule {
    @RoboMock
    @Provides
    SharedPreferences provideSharedPreferences(@RoboMock Application application) {
        return ShadowPreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    @RoboMock
    @Provides
    PackageManager providePackageManager() {
        return Robolectric.application.getPackageManager();
    }

    @RoboMock
    @Provides
    Application providesApplication() {
        return Robolectric.application;
    }
}
