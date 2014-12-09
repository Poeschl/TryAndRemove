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
