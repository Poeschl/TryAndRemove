package de.poeschl.apps.tryandremove.utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.tryandremove.interfaces.AppManager;

/**
 * Created by Markus Pöschl on 18.12.2014.
 */

@Module(
        injects = {
                AndroidAppManager.class
        },
        library = true,
        complete = false
)
public class UtilsModule {

    @Provides
    @Singleton
    AppManager provideAppManager(AndroidAppManager androidAppManager) {
        return androidAppManager;
    }
}
