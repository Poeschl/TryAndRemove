package de.poeschl.apps.tryandremove.interfaces;

import java.util.List;

/**
 * Created by Markus Pöschl on 18.12.2014.
 */
public interface AppManager {

    public boolean exists(String appPackage);

    public void remove(String appPackage);

    public void remove(List<String> appPackages);
}
