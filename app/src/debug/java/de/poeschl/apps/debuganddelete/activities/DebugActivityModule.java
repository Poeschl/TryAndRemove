package de.poeschl.apps.debuganddelete.activities;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.debuganddelete.app.appContainer.AppContainer;
import de.poeschl.apps.debuganddelete.appContainer.DebugAppContainer;

/**
 * Created by markus on 09.12.14.
 */

@Module(
        injects = DebugAppContainer.class,
        complete = false,
        library = true,
        overrides = true
)
public class DebugActivityModule {

    @Provides
    @Singleton
    AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
        return debugAppContainer;
    }
}
