package de.poeschl.apps.debuganddelete;

import dagger.Module;
import de.poeschl.apps.debuganddelete.activities.DebugActivityModule;

/**
 * Created by markus on 05.12.14.
 */

@Module(
        addsTo = AppModule.class,
        includes = {
                DebugActivityModule.class
        },
        overrides = true
)
public final class DebugAppModule {
}
