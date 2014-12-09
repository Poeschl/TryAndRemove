package de.poeschl.apps.debuganddelete;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.debuganddelete.app.activities.ActivityModule;

/**
 * Created by markus on 05.12.14.
 */

@Module(
        includes = {
                ActivityModule.class
        },
        injects = {
                DebugAndDeleteApp.class
        },
        library = true
)
public class AppModule {
    private final DebugAndDeleteApp app;

    public AppModule(DebugAndDeleteApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }
}
