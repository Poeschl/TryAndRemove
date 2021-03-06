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

package de.poeschl.apps.tryandremove.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.scalpel.ScalpelFrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.BuildConfig;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapters.RemoveAppAdapter;
import de.poeschl.apps.tryandremove.annotations.IsMockMode;
import de.poeschl.apps.tryandremove.annotations.IsTracking;
import de.poeschl.apps.tryandremove.annotations.ScalpelEnabled;
import de.poeschl.apps.tryandremove.annotations.ScalpelWireframeEnabled;
import de.poeschl.apps.tryandremove.annotations.SettingsDrawerSeen;
import de.poeschl.apps.tryandremove.broadcastReciever.AppDetectionReceiver;
import de.poeschl.apps.tryandremove.interfaces.AppContainer;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import timber.log.Timber;

import static butterknife.ButterKnife.findById;

/**
 * Created by markus on 05.12.14.
 */
public class DebugAppContainer implements AppContainer {

    public static final int TOAST_TIME = 1000;

    @InjectView(R.id.debug_drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.debug_content)
    ScalpelFrameLayout scalpelFrameLayout;

    @InjectView(R.id.debug_app_mock_mode)
    Switch appMockModeSwitch;
    @InjectView(R.id.debug_app_install_button)
    Button appInstallButton;
    @InjectView(R.id.debug_app_remove_spinner)
    Spinner appRemoveSpinner;

    @InjectView(R.id.debug_ui_scalpel)
    Switch uiScalpelView;
    @InjectView(R.id.debug_ui_scalpel_wireframe)
    Switch uiScalpelWireframeView;

    @InjectView(R.id.debug_build_name)
    TextView buildNameView;
    @InjectView(R.id.debug_build_code)
    TextView buildCodeView;
    @InjectView(R.id.debug_build_sha)
    TextView buildShaView;
    @InjectView(R.id.debug_build_date)
    TextView buildDateView;

    @InjectView(R.id.debug_device_make)
    TextView deviceMakeView;
    @InjectView(R.id.debug_device_model)
    TextView deviceModelView;
    @InjectView(R.id.debug_device_resolution)
    TextView deviceResolutionView;
    @InjectView(R.id.debug_device_density)
    TextView deviceDensityView;
    @InjectView(R.id.debug_device_release)
    TextView deviceReleaseView;
    @InjectView(R.id.debug_device_api)
    TextView deviceApiView;


    PackageList packageList;

    private final Application app;
    private Activity activity;
    private Context drawerContext;
    private BooleanPreference seenDebugDrawer;
    private AppDetectionReceiver appDetectionReceiver;

    private BooleanPreference scalpelEnabled;
    private BooleanPreference scalpelWireframeEnabled;
    private final BooleanPreference mockMode;
    private final BooleanPreference tracking;

    private int lastAddedMockedIndex;

    @Inject
    public DebugAppContainer(@ScalpelEnabled BooleanPreference scalpel,
                             @ScalpelWireframeEnabled BooleanPreference scalpelWireframe,
                             @IsMockMode BooleanPreference mockMode,
                             @SettingsDrawerSeen BooleanPreference seenDebugDrawer,
                             @IsTracking BooleanPreference isTracking,
                             AppDetectionReceiver appDetectionReceiver,
                             PackageList packageList,
                             Application app) {
        this.app = app;
        this.scalpelEnabled = scalpel;
        this.scalpelWireframeEnabled = scalpelWireframe;
        this.seenDebugDrawer = seenDebugDrawer;
        this.mockMode = mockMode;
        this.appDetectionReceiver = appDetectionReceiver;
        lastAddedMockedIndex = 0;
        this.packageList = packageList;
        this.tracking = isTracking;
    }

    @Override
    public ViewGroup get(final Activity activity) {
        this.activity = activity;
        drawerContext = activity;

        activity.setContentView(R.layout.debug_activity_frame);

        // Manually find the debug drawer and inflate the drawer layout inside of it.
        ViewGroup drawer = findById(activity, R.id.debug_drawer);
        LayoutInflater.from(drawerContext).inflate(R.layout.drawer_debug_layout, drawer);

        // Inject after inflating the drawer layout so its views are available to inject.
        ButterKnife.inject(this, activity);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.END);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                Adapter temp = appRemoveSpinner.getAdapter();
                if (temp != null) {
                    RemoveAppAdapter removeAppAdapter = (RemoveAppAdapter) temp;
                    removeAppAdapter.updateAdapter(packageList);
                    appRemoveSpinner.setSelection(0);
                }
            }
        });

        // If you have not seen the debug drawer before, show a message
        if (!seenDebugDrawer.get()) {
            boolean b = drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    drawerLayout.openDrawer(Gravity.END);
//                    Toast.makeText(activity, R.string.debug_drawer_welcome, Toast.LENGTH_SHORT).show();
                }
            }, TOAST_TIME);
            seenDebugDrawer.set(true);
        }

        setUpAppInstall();
        setUpScalpel();
        setupBuildSection();
        setupDeviceSection();

        return scalpelFrameLayout;
    }

    private void setUpAppInstall() {
        appInstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mockMode.get()) {
                    if (appDetectionReceiver.isRegistered()) {
                        Intent dummyInstall = new Intent(Intent.ACTION_PACKAGE_ADDED);
                        dummyInstall.setData(Uri.parse("package:com.example.markus.Added " + lastAddedMockedIndex++));
                        appDetectionReceiver.onReceive(v.getContext(), dummyInstall);
                        Timber.v("Install mocked app");
                    }
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AssetManager assetManager = activity.getAssets();

                            try {
                                File copiedApk = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/temp.apk");

                                if (!copiedApk.exists()) {
                                    InputStream in = assetManager.open("app-debug.apk");
                                    OutputStream out = new FileOutputStream(copiedApk);

                                    byte[] buffer = new byte[1024];

                                    int read;
                                    while ((read = in.read(buffer)) != -1) {
                                        out.write(buffer, 0, read);
                                    }

                                    in.close();
                                    in = null;

                                    out.flush();
                                    out.close();
                                    out = null;

                                }

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(copiedApk),
                                        "application/vnd.android.package-archive");
                                activity.startActivity(intent);
                                Timber.v("Install real app.");

                            } catch (IOException e) {
                                Timber.e(e, e.getMessage());
                            }
                        }
                    }).start();
                }
            }
        });

        appRemoveSpinner.setAdapter(new RemoveAppAdapter(packageList, drawerContext));
        appRemoveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String removePackage = (String) appRemoveSpinner.getAdapter().getItem(position);

                //If the selected item is the default item, do nothing.
                if (removePackage.equals(RemoveAppAdapter.DEFAULT_STRING)) {
                    return;
                }

                if (mockMode.get()) {
                    if (appDetectionReceiver.isRegistered()) {
                        Intent dummyRemove = new Intent(Intent.ACTION_PACKAGE_REMOVED);
                        dummyRemove.setData(Uri.parse("package:" + removePackage));
                        lastAddedMockedIndex--;
                        appDetectionReceiver.onReceive(view.getContext(), dummyRemove);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", removePackage, null));
                    view.getContext().startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing.
            }
        });


        boolean mockModeActive = mockMode.get();
        appMockModeSwitch.setChecked(mockModeActive);
        appMockModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mockMode.set(isChecked);
                restartApp();
            }
        });

    }

    private void setUpScalpel() {
        boolean scalpelActive = scalpelEnabled.get();
        scalpelFrameLayout.setLayerInteractionEnabled(scalpelActive);
        uiScalpelView.setChecked(scalpelActive);
        uiScalpelWireframeView.setEnabled(scalpelActive);
        uiScalpelView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelEnabled.set(isChecked);
                scalpelFrameLayout.setLayerInteractionEnabled(isChecked);
                uiScalpelWireframeView.setEnabled(isChecked);
            }
        });

        boolean wireframeActive = scalpelWireframeEnabled.get();
        scalpelFrameLayout.setDrawViews(!wireframeActive);
        uiScalpelWireframeView.setChecked(wireframeActive);
        uiScalpelWireframeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelWireframeEnabled.set(isChecked);
                scalpelFrameLayout.setDrawViews(!isChecked);
            }
        });
    }

    private void setupBuildSection() {
        buildNameView.setText(BuildConfig.VERSION_NAME);
        buildCodeView.setText(String.valueOf(BuildConfig.VERSION_CODE));
        buildShaView.setText(BuildConfig.GIT_SHA);

        try {
            // Parse ISO8601-format time into local time.
            DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
            inFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date buildTime = inFormat.parse(BuildConfig.BUILD_TIME);
            buildDateView.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.US).format(buildTime));
        } catch (ParseException e) {
            throw new RuntimeException("Unable to decode build time: " + BuildConfig.BUILD_TIME, e);
        }
    }

    private void setupDeviceSection() {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        deviceMakeView.setText(truncString(Build.MANUFACTURER, 20));
        deviceModelView.setText(truncString(Build.MODEL, 20));
        deviceResolutionView.setText(displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
        deviceDensityView.setText(displayMetrics.densityDpi + "dpi (" + densityBucket + ")");
        deviceReleaseView.setText(Build.VERSION.RELEASE);
        deviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
    }

    private String truncString(String org, int length) {
        return org.length() > length ? org.substring(0, length) : org;
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return "unknown";
        }
    }

    private void restartApp() {
        Intent newApp = new Intent(app, AppListActivity.class);
        newApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        tracking.set(false);
        app.startActivity(newApp);
        TryAndRemoveApp.get(app).buildObjectGraphAndInject();
    }

}
