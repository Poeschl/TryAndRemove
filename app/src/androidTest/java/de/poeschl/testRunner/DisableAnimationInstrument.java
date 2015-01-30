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

package de.poeschl.testRunner;

import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import timber.log.Timber;


/**
 * Created by Markus Pöschl on 30.01.15.
 */
public final class DisableAnimationInstrument extends Instrumentation {
    private static final String ANIMATION_PERMISSION = "android.permission.SET_ANIMATION_SCALE";

    @Override
    public void onCreate(Bundle args) {
        // as time goes on we may actually need to process our arguments.
        Timber.plant(new Timber.DebugTree());
        Timber.i("Try Disable Animations");
        disableAnimation();
    }

    private void disableAnimation() {
        int permStatus = getContext().checkCallingOrSelfPermission(ANIMATION_PERMISSION);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            if (reflectivelyDisableAnimation()) {
                Timber.i("All animations disabled.");
            } else {
                Timber.i("Could not disable animations.");
            }
        } else {
            Timber.i("Cannot disable animations due to lack of permission.");
        }
    }

    private boolean reflectivelyDisableAnimation() {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales",
                    float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = 0.0f;
            }
            setAnimationScales.invoke(windowManagerObj, currentScales);
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | RuntimeException ex) {
            Timber.w(ex, "Cannot disable animations reflectively.");
        }
        return false;
    }
}
