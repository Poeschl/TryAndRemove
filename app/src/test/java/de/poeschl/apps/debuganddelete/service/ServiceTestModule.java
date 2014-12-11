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

package de.poeschl.apps.debuganddelete.service;

import dagger.Module;
import dagger.Provides;
import de.poeschl.apps.debuganddelete.data.MockPackageList;
import de.poeschl.apps.debuganddelete.interfaces.PackageList;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
@Module(
        injects = {
                ApplicationDetectionService.class,
        },
        library = true
)
public class ServiceTestModule {

    @Provides
    PackageList providesPackageList() {
        return new MockPackageList();
    }
}
