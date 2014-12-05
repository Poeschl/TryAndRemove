package de.poeschl.apps.debuganddelete;

import dagger.Module;

/**
 * Created by markus on 05.12.14.
 */

@Module(
        addsTo = AppModule.class,
        overrides = true
)
public class DebugAppModule {
}
