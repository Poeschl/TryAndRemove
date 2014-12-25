/*
 * Copyright (c) 2014 Markus Poeschl
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

package de.poeschl.apps.tryandremove.broadcastReciever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
@Module(
        library = true
)
public class BroadcastReceiverModule {
    @Provides
    @Singleton
    AppDetectionReceiver provideAppDetectionReceiver() {
        return new AppDetectionReceiver();
    }
}
