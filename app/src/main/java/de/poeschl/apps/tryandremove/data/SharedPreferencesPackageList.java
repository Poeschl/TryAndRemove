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

package de.poeschl.apps.tryandremove.data;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.handler.ListUpdateHandler;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.interfaces.PackageList;

/**
 * Created by Markus Pöschl on 11.12.2014.
 */
public class SharedPreferencesPackageList implements PackageList {

    static final String PREF_PACKAGE_LIST = "PREF_PACKAGE_LIST";

    private SharedPreferences preferences;
    private AppManager appManager;
    private Handler updateHandler;


    @Inject
    SharedPreferencesPackageList(SharedPreferences preferences, AppManager appManager) {
        this.preferences = preferences;
        this.appManager = appManager;

        //Check if these packages still exist on system
        validatePackages();
    }

    /**
     * Check if the app are really installed right now. Removes them if not found by the package manager.
     */
    public void validatePackages() {
        List<String> savedPackages = getPackages();

        List<String> packagesToRemove = new LinkedList<>();

        for (String packageStr : savedPackages) {
            if (!appManager.exists(packageStr)) {
                packagesToRemove.add(packageStr);
            }
        }

        for (String packageStr : packagesToRemove) {
            removePackage(packageStr);
        }
    }


    /**
     * @param packageName The package string of the new package.
     * @return Was the add successful.
     * @inheritDoc
     */
    @Override
    public boolean addPackage(String packageName) {
        Set<String> saved = getSavedSet();
        boolean success = saved.add(packageName);
        saveSet(saved);
        triggerUpdateHandler();
        return success;
    }

    /**
     * @param packageName The package string of the remove package.
     * @return Was the removal successful.
     * @inheritDoc
     */
    @Override
    public boolean removePackage(String packageName) {
        Set<String> saved = getSavedSet();
        boolean success = saved.remove(packageName);
        saveSet(saved);
        triggerUpdateHandler();
        return success;
    }

    @Override
    public boolean contains(String packageName) {
        Set<String> saved = getSavedSet();
        return saved.contains(packageName);
    }

    @Override
    public List<String> getPackages() {
        return new LinkedList<>(getSavedSet());
    }

    @Override
    public void clear() {
        triggerUpdateHandler();
        saveSet(new HashSet<String>());
    }

    private Set<String> getSavedSet() {
        return preferences.getStringSet(PREF_PACKAGE_LIST, new HashSet<String>());
    }

    private void saveSet(Set<String> set) {
        preferences.edit().putStringSet(PREF_PACKAGE_LIST, set).apply();
    }

    @Override
    public boolean isEmpty() {
        return getSavedSet().isEmpty();
    }

    private void triggerUpdateHandler() {
        Message message = Message.obtain(updateHandler);
        Bundle data = new Bundle();
        data.putString(ListUpdateHandler.CHANGE_KEY, ListUpdateHandler.APP_LIST_CHANGE);
        message.setData(data);
        updateHandler.sendMessage(message);
    }

    @Override
    public void setPackageUpdateHandler(ListUpdateHandler handler) {
        this.updateHandler = handler;
    }
}
