package de.poeschl.apps.debuganddelete.app.activities;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.debuganddelete.app.appContainer.AppContainer;

/**
 * Created by markus on 09.12.14.
 */

@Module(
        injects = {
                MainActivity.class,
        },
        complete = false,
        library = true
)
public class ActivityModule {
    @Provides
    @Singleton
    AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }
}
