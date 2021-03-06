/*
 * Copyright (c) 2015 Markus Poeschl
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

package de.poeschl.apps.tryandremove.utils;

import android.app.Application;

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

    @Provides
    AdManager provideAdManager() {
        return new AdManager();
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(Application app) {
        return new NotificationManager(app);
    }
}
