/*
 * Copyright 2014 Markus Poeschl
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

package de.poeschl.apps.tryandremove.adapter;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import timber.log.Timber;

/**
 * Created by markus on 16.12.14.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<String> packages;
    private PackageManager packageManager;
    private Application app;

    @Inject
    public AppAdapter(PackageList packageList, PackageManager packageManager, Application app) {
        this.packages = new LinkedList<>();
        this.packages.addAll(packageList.getPackages());
        this.packageManager = packageManager;
        this.app = app;
    }

    public void updateAdapter(PackageList packageList) {
        packages.clear();
        packages.addAll(packageList.getPackages());
        Timber.v("Adapter updated");
        notifyDataSetChanged();
    }

    public String getItem(int position) {
        return packages.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_cell, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String itemPackage = packages.get(position);
        ApplicationInfo appInfo = null;

        try {
            appInfo = packageManager.getApplicationInfo(itemPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Package not found");
        }

        String appName = app.getString(R.string.app_name_not_found);

        if (appInfo != null) {
            Drawable appIcon = appInfo.loadIcon(packageManager);
            appName = appInfo.loadLabel(packageManager).toString();

            holder.appIcon.setImageDrawable(appIcon);
            holder.appName.setText(appName);
        } else {
            Timber.w("No matching app found - hide cell");
            holder.hide();
        }

        holder.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Make selector for single action
//                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", itemPackage, null));
//                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return packages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.app_list_cell_root)
        View cellRoot;
        @InjectView(R.id.app_list_cell_app_icon)
        ImageView appIcon;
        @InjectView(R.id.app_list_cell_app_name)
        TextView appName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setClickListener(View.OnClickListener clickListener) {
            cellRoot.setOnClickListener(clickListener);
        }

        public void hide() {
            cellRoot.setVisibility(View.GONE);
        }
    }

}
