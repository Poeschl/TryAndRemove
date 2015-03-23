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

package de.poeschl.apps.tryandremove.models;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.annotations.ColoredCellsEnabled;
import de.poeschl.apps.tryandremove.annotations.CrashlyticsEnabled;
import de.poeschl.apps.tryandremove.annotations.IsTracking;

/**
 * Created by Markus Pöschl on 09.12.2014.
 */
@Module(
        complete = false,
        library = true
)
public class ModelModule {

    private static final String IS_TRACKING_KEY = "IS_TRACKING";
    private static final String CRASHLYTICS_ENABLED_KEY = "CRASHLYTICS_ENABLED";
    private static final String COLORED_CELLS_ENABLED_KEY = "COLORED_CELLS_ENABLED";

    @Provides
    @IsTracking
    BooleanPreference provideTrackingBoolean(SharedPreferences sharedPreferences) {
        return new BooleanPreference(sharedPreferences, IS_TRACKING_KEY, false);
    }

    @Provides
    @CrashlyticsEnabled
    BooleanPreference provideCrashlyticsEnabled(SharedPreferences sharedPreferences) {
        return new BooleanPreference(sharedPreferences, CRASHLYTICS_ENABLED_KEY, true);
    }

    @Provides
    @ColoredCellsEnabled
    BooleanPreference provideColoredCellsEnabled(SharedPreferences sharedPreferences) {
        return new BooleanPreference(sharedPreferences, COLORED_CELLS_ENABLED_KEY, false);
    }

}
