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

package de.poeschl.apps.tryandremove.interfaces;

import java.util.List;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
public interface PackageList {

    /**
     * Adds a package to the list.
     *
     * @param packageName The package string of the new package.
     * @return Was the package inserted correctly.
     */
    public boolean addPackage(String packageName);

    /**
     * Removes a package from the list.
     *
     * @param packageName The package string of the package to remove.
     * @return Was the package removed correctly.
     */
    public boolean removePackage(String packageName);

    public boolean contains(String packageName);

    public List<String> getPackages();

    public void clear();

    public boolean isEmpty();
}
