/*
 * Copyright 2014 Markus Poeschl
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

package de.poeschl.apps.tryandremove;

import dagger.Module;
import de.poeschl.apps.tryandremove.activities.DebugActivityModule;
import de.poeschl.apps.tryandremove.models.DebugModelModule;
import de.poeschl.apps.tryandremove.models.ModelModule;

/**
 * Created by Markus Pöschl on 05.12.14.
 */
@Module(
        addsTo = AppModule.class,
        injects = {
                ModelModule.class
        },
        includes = {
                DebugActivityModule.class,
                DebugModelModule.class
        },
        overrides = true
)
public final class DebugAppModule {

}
