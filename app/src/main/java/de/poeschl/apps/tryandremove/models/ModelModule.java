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

package de.poeschl.apps.tryandremove.models;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.annotations.IsTracking;

/**
 * Created by Markus Pöschl on 09.12.2014.
 */
@Module(
        complete = false,
        library = true
)
public class ModelModule {

    @Provides
    @IsTracking
    BooleanPreference provideTrackingBoolean(SharedPreferences sharedPreferences) {
        return new BooleanPreference(sharedPreferences, "IS_TRACKING", false);
    }

}
