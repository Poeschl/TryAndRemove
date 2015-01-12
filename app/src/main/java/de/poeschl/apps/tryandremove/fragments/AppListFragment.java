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

package de.poeschl.apps.tryandremove.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.adapter.AppAdapter;
import de.poeschl.apps.tryandremove.dialogs.ClearWarningDialogFragment;
import de.poeschl.apps.tryandremove.dialogs.RemoveWarningDialogFragment;
import de.poeschl.apps.tryandremove.interfaces.AppManager;
import de.poeschl.apps.tryandremove.interfaces.PackageList;


public class AppListFragment extends Fragment implements ClearWarningDialogFragment.ButtonListener, RemoveWarningDialogFragment.ButtonListener {

    @InjectView(R.id.app_list_layout_apps_recyclerView)
    RecyclerView appListView;
    @InjectView(R.id.app_list_layout_floating_menu)
    FloatingActionsMenu floatMenu;

    @Inject
    PackageList packageListData;
    @Inject
    AppAdapter appAdapter;
    @Inject
    AppManager appManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_app_list, container, false);

        ButterKnife.inject(this, root);

        appListView.setAdapter(appAdapter);
        appListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TryAndRemoveApp.get(getActivity()).inject(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePackageList();
    }

    @OnClick(R.id.app_list_layout_clear_action_button)
    void clearList() {
        if (!packageListData.isEmpty()) {
            ClearWarningDialogFragment wf = new ClearWarningDialogFragment();
            wf.setButtonListener(this);
            wf.show(getFragmentManager());
        }
    }

    @Override
    public void onUserConfirmedClear() {
        packageListData.clear();
        updatePackageList();
        floatMenu.collapse();
    }

    @OnClick(R.id.app_list_layout_remove_action_button)
    void removeAllApps() {
        if (!packageListData.isEmpty()) {
            RemoveWarningDialogFragment rf = new RemoveWarningDialogFragment();
            rf.setButtonListener(this);
            rf.show(getFragmentManager());
        }
    }

    @Override
    public void onUserConfirmedRemove() {
        appManager.remove(packageListData.getPackages());
        floatMenu.collapse();
    }

    public void updatePackageList() {
        appAdapter.updateAdapter(packageListData);
    }
}
