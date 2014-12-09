package de.poeschl.apps.debuganddelete;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;

/**
 * Created by markus on 05.12.14.
 */
public class DebugAndDeleteApp extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        buildObjectGraphAndInject();

    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static DebugAndDeleteApp get(Context context) {
        return (DebugAndDeleteApp) context.getApplicationContext();
    }
}
