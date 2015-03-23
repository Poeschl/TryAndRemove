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

package de.poeschl.apps.tryandremove;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.mopub.common.MoPub;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.poeschl.apps.tryandremove.annotations.CrashlyticsEnabled;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import de.poeschl.apps.tryandremove.trees.CrashlyticsReportTree;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import timber.log.Timber;

/**
 * Created by markus on 05.12.14.
 */
public class TryAndRemoveApp extends Application {
    @Inject
    @CrashlyticsEnabled
    protected BooleanPreference crashlyticsEnabled;

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        buildObjectGraphAndInject();

        initFabric();

        Timber.v("Crashlytics enabled: " + crashlyticsEnabled.get());
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);
        Timber.v("Objectgraph created");
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static TryAndRemoveApp get(Context context) {
        return (TryAndRemoveApp) context.getApplicationContext();
    }

    private void initFabric() {
        List<Kit> kits = new ArrayList<>();
        kits.add(new MoPub());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        } else {
            if (crashlyticsEnabled.get()) {
                kits.add(new Crashlytics());
            }
        }

        Kit[] kitArray = new Kit[kits.size()];
        for (int i = 0; i < kits.size(); i++) {
            kitArray[i] = kits.get(i);
        }

        Fabric.with(this, kitArray);

        if (!BuildConfig.DEBUG && crashlyticsEnabled.get()) {
            Timber.plant(new CrashlyticsReportTree());
        }
    }
}
