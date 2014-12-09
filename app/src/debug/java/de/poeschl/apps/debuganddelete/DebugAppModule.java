package de.poeschl.apps.debuganddelete;

import dagger.Module;
import de.poeschl.apps.debuganddelete.activities.DebugActivityModule;
import de.poeschl.apps.debuganddelete.preferences.PreferenceModule;

/**
 * Created by markus on 05.12.14.
 */

@Module(
        addsTo = AppModule.class,
        injects = {
                PreferenceModule.class
        },
        includes = {
                DebugActivityModule.class,
                PreferenceModule.class
        },
        overrides = true
)
public final class DebugAppModule {

}
