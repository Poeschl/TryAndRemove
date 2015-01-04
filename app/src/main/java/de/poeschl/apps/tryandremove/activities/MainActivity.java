/*
 * Copyright (c) 2014 Markus Poeschl
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

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.fragments.AppListFragment;


public class MainActivity extends TryAndRemoveActivity {

    @InjectView(R.id.navigation_drawer_top_recyclerView)
    RecyclerView topRecyclerView;
    @InjectView(R.id.navigation_drawer_bottom_recyclerView)
    RecyclerView bottomRecyclerView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private FrameLayout navigationDrawer;
    private Mode displayMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

        setupLayout(R.layout.activity_navigation_drawer);

        //Inflate the navigation drawer
        navigationDrawer = ButterKnife.findById(this, R.id.navigation_drawer);
        getLayoutInflater().inflate(R.layout.navigation_drawer, navigationDrawer);

        ButterKnife.inject(this);

        setUpTopPart();
        setUpBottomPart();

        openAppList();
    }

    private void setUpTopPart() {
        topRecyclerView.setHasFixedSize(true);
        topRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpBottomPart() {
        bottomRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void openAppList() {
        if (displayMode != Mode.APP_LIST) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content, AppListFragment.newInstance());
            ft.commit();

            displayMode = Mode.APP_LIST;
        }
    }

    private enum Mode {
        APP_LIST;
    }
}
