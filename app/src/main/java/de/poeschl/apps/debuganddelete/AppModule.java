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
