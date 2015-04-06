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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.NavigationItemAdapter;
import de.poeschl.apps.tryandremove.interfaces.NavigationDrawerListener;
import de.poeschl.apps.tryandremove.layoutManager.SmallLayoutManager;
import de.poeschl.apps.tryandremove.models.MenuItem;

/**
 * Created by Markus Pöschl on 17.12.2014.
 */
public class NavigationActivity extends ToolbarActivity implements NavigationDrawerListener<NavigationActivity.NavItem> {

    private static final String PRIVACY_POLICY_URL = "file:///android_asset/PrivacyPolicy.html";
    private static final String IMPRINT_URL = "file:///android_asset/Imprint.html";

    private RecyclerView topRecyclerView;
    private RecyclerView bottomRecyclerView;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    protected void setupLayout(int layout) {
        ViewGroup container = appContainer.get(this);
        //Inflate navigation drawer layout
        getLayoutInflater().inflate(R.layout.activity_navigation_drawer, container);

        //Inflate navigation drawer
        FrameLayout navigationDrawer = ButterKnife.findById(container, R.id.navigation_drawer);
        getLayoutInflater().inflate(R.layout.navigation_drawer_layout, navigationDrawer);

        toolbar = ButterKnife.findById(container, R.id.toolbar);
        topRecyclerView = ButterKnife.findById(navigationDrawer, R.id.navigation_drawer_top_recyclerView);
        bottomRecyclerView = ButterKnife.findById(navigationDrawer, R.id.navigation_drawer_bottom_recyclerView);
        drawerLayout = ButterKnife.findById(container, R.id.drawer_layout);
        ViewGroup mainContent = ButterKnife.findById(container, R.id.mainContent);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name_not_found);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        setUpTopNavPart();
        setUpBottomNavPart();

        //Inflate the real content into the contentView
        getLayoutInflater().inflate(layout, mainContent);
    }

    private void setUpTopNavPart() {
        topRecyclerView.setHasFixedSize(true);
        topRecyclerView.setLayoutManager(new SmallLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<MenuItem<NavItem>> upItemList = new ArrayList<>();
        upItemList.add(new de.poeschl.apps.tryandremove.models.MenuItem<>(
                getString(R.string.navigation_drawer_app_list_title), R.drawable.ic_menu_collection, NavItem.APP_LIST));

        NavigationItemAdapter<NavItem> adapter = new NavigationItemAdapter<>(upItemList);
        adapter.setNavigationListener(this);
        topRecyclerView.setAdapter(adapter);
    }

    private void setUpBottomNavPart() {
        bottomRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.setLayoutManager(new SmallLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<MenuItem<NavItem>> bottomItemList = new ArrayList<>();
        bottomItemList.add(new MenuItem<NavItem>(getString(R.string.settings_title), R.drawable.ic_menu_settings, NavItem.SETTINGS));
        bottomItemList.add(new MenuItem<>(getString(R.string.private_policy_title), R.drawable.ic_menu_info, NavItem.PRIVACY_POLICY));
        bottomItemList.add(new MenuItem<>(getString(R.string.imprint_title), R.drawable.ic_menu_imprint, NavItem.IMPRINT));


        NavigationItemAdapter<NavItem> adapter = new NavigationItemAdapter<>(bottomItemList);
        adapter.setNavigationListener(this);
        bottomRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onNavigationItemClick(NavItem targetViewMode) {
        Intent openIntent;
        switch (targetViewMode) {
            case APP_LIST:
                //Ignore when we are in the app list
                if (!this.getClass().equals(AppListActivity.class)) {
                    openIntent = new Intent(this, AppListActivity.class);
                    startActivity(openIntent);
                }
                break;

            case SETTINGS:
                openIntent = new Intent(this, SettingActivity.class);
                startChildrenActivity(openIntent);
                break;

            case PRIVACY_POLICY:
                openIntent = new Intent(this, WebViewActivity.class);
                openIntent.putExtra(WebViewActivity.URL_EXTRA_KEY, PRIVACY_POLICY_URL);
                openIntent.putExtra(WebViewActivity.ACTIONBAR_TITLE_KEY, getString(R.string.private_policy_title));
                startChildrenActivity(openIntent);
                break;

            case IMPRINT:
                openIntent = new Intent(this, WebViewActivity.class);
                openIntent.putExtra(WebViewActivity.URL_EXTRA_KEY, IMPRINT_URL);
                openIntent.putExtra(WebViewActivity.ACTIONBAR_TITLE_KEY, getString(R.string.imprint_title));
                startChildrenActivity(openIntent);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawers();
    }

    private void startChildrenActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
    }

    protected enum NavItem {
        APP_LIST, PRIVACY_POLICY, IMPRINT, SETTINGS;
    }


}
