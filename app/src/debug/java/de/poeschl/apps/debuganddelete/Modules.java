package de.poeschl.apps.debuganddelete;

/**
 * Created by markus on 05.12.14.
 */
public class Modules {

    private Modules() {
    }

    static Object[] list(DebugAndDeleteApp app) {
        return new Object[]{
                new AppModule(app),
                new DebugAppModule()
        };
    }
}
