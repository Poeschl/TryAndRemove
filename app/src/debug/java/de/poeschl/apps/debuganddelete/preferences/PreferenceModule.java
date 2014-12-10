/*
 * Copyright 2014 Markus Pöschl
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

package de.poeschl.apps.debuganddelete.preferences;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.debuganddelete.annotations.ScalpelEnabled;
import de.poeschl.apps.debuganddelete.annotations.ScalpelWireframeEnabled;
import de.poeschl.apps.debuganddelete.annotations.SettingsDrawerSeen;
import de.poeschl.apps.debuganddelete.appContainer.DebugAppContainer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Markus Pöschl on 09.12.2014.
 */
@Module(
        injects = {
                DebugAppContainer.class
        },
        library = true,
        complete = false
)
public class PreferenceModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences(app.getPackageName(), MODE_PRIVATE);
    }

    @Provides
    @Singleton
    @ScalpelEnabled
    BooleanPreference provideScalpelEnabled(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_scalpel_enabled", false);
    }

    @Provides
    @Singleton
    @ScalpelWireframeEnabled
    BooleanPreference provideScalpelWireframeEnabled(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_scalpel_wireframe_enabled", false);
    }

    @Provides
    @Singleton
    @SettingsDrawerSeen
    BooleanPreference provideSettingsDrawerSeen(SharedPreferences preferences) {
        return new BooleanPreference(preferences, "debug_settings_seen", false);
    }
}
