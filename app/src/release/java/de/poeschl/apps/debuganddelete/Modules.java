package de.poeschl.apps.debuganddelete;

import de.poeschl.apps.debuganddelete.AppModule;
import de.poeschl.apps.debuganddelete.DebugAndDeleteApp;

/**
 * Created by markus on 05.12.14.
 */
public class Modules {

    private Modules(){}

    static Object[] list(DebugAndDeleteApp app){
        return new Object[] {
                AppModule.class,
        };
    }
}
