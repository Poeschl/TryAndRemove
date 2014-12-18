package de.poeschl.apps.tryandremove.utils;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

import javax.inject.Inject;

import de.poeschl.apps.tryandremove.interfaces.AppManager;

/**
 * This class cares about the app management on the android device.
 * <p/>
 * Created by Markus Pöschl on 18.12.2014.
 */
public class AndroidAppManager implements AppManager {

    private PackageManager packageManager;
    private Application application;

    @Inject
    public AndroidAppManager(PackageManager packageManager, Application application) {
        this.packageManager = packageManager;
        this.application = application;
    }

    /**
     * Check if the given package exists on the phone.
     *
     * @param appPackage The package name to check.
     * @return True if the package exists.
     */
    @Override
    public boolean exists(String appPackage) {
        try {
            packageManager.getPackageInfo(appPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Uninstalls a app from the device.
     *
     * @param appPackage The package to be removed.
     */
    @Override
    public void remove(String appPackage) {
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", appPackage, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    /**
     * Uninstall a bundle of apps from the device.
     *
     * @param appPackages The package ids of the packages.
     */
    @Override
    public void remove(List<String> appPackages) {
        for (String appPackage : appPackages) {
            remove(appPackage);
        }
    }
}
