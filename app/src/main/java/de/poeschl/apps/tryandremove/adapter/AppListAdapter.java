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

package de.poeschl.apps.tryandremove.adapter;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorRes;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.annotations.ColoredCellsEnabled;
import de.poeschl.apps.tryandremove.interfaces.PackageList;
import de.poeschl.apps.tryandremove.models.BooleanPreference;
import de.poeschl.apps.tryandremove.utils.BitmapHelper;
import timber.log.Timber;

/**
 * Created by markus on 16.12.14.
 */
public class AppListAdapter extends RecyclerSwipeAdapter<AppListAdapter.ViewHolder> implements View.OnClickListener {

    private List<String> packages;
    private PackageManager packageManager;
    private Application app;
    private BooleanPreference coloredCellsEnabled;
    private AppListAdapterListener listener;

    @Inject
    public AppListAdapter(PackageList packageList, PackageManager packageManager, Application app, @ColoredCellsEnabled BooleanPreference coloredCellsEnabled) {
        this.packages = new LinkedList<>();
        this.packages.addAll(packageList.getPackages());
        this.packageManager = packageManager;
        this.app = app;
        this.coloredCellsEnabled = coloredCellsEnabled;
    }

    public void setListener(AppListAdapterListener listener) {
        this.listener = listener;
    }

    public void updateApps(PackageList packageList) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_app_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String itemPackage = packages.get(position);
        ApplicationInfo appInfo = null;

        try {
            appInfo = packageManager.getApplicationInfo(itemPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Package not found");
        }

        String appName = app.getString(R.string.app_name_not_found);
        String appPackage = app.getString(R.string.app_package_not_found);

        if (appInfo != null) {
            Drawable appIcon = appInfo.loadIcon(packageManager);
            appName = appInfo.loadLabel(packageManager).toString();
            appPackage = appInfo.packageName;

            holder.appIcon.setImageDrawable(appIcon);
            holder.appName.setText(appName);
            holder.appPackage.setText(appPackage);

            holder.setClickClearListener(this, position);
            holder.setClickRemoveListener(this, position);

            if (coloredCellsEnabled.get()) {
                Palette.generateAsync(BitmapHelper.drawableToBitmap(appIcon), new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        holder.setOverlayColor(palette.getVibrantColor(0));
                    }
                });
            }

        } else {
            Timber.w("No matching app found - hide cell");
            holder.hide();
        }
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.app_list_cell_swipe;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = (Integer) v.getTag();
            switch (v.getId()) {
                case R.id.app_list_cell_clear:
                    listener.onItemClearClick(packages.get(position), position);
                    break;
                case R.id.app_list_cell_remove:
                    listener.onItemRemoveClick(packages.get(position), position);
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.app_list_cell_root)
        View cellRoot;
        @InjectView(R.id.app_list_cell_root_overlay)
        View cellRootColorOverlay;
        @InjectView(R.id.app_list_cell_app_icon)
        RoundedImageView appIcon;
        @InjectView(R.id.app_list_cell_app_name)
        TextView appName;
        @InjectView(R.id.app_list_cell_app_package)
        TextView appPackage;
        @InjectView(R.id.app_list_cell_swipe)
        SwipeLayout swipeLayout;
        @InjectView(R.id.app_list_cell_clear)
        ImageView clearButton;
        @InjectView(R.id.app_list_cell_remove)
        ImageView removeButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

            //FIXME: Fix for wrong background in circled image
            ShapeDrawable background = new ShapeDrawable(new OvalShape());
            background.getPaint().setColor(view.getResources().getColor(R.color.background));
            this.appIcon.setBackground(background);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
        }

        public void setClickRemoveListener(View.OnClickListener listener, int position) {
            removeButton.setOnClickListener(listener);
            removeButton.setTag(position);
        }

        public void setClickClearListener(View.OnClickListener listener, int position) {
            clearButton.setOnClickListener(listener);
            clearButton.setTag(position);

        }

        public void hide() {
            cellRoot.setVisibility(View.GONE);
        }

        public void setOverlayColor(@ColorRes int color) {
            if (color != 0) {
                if (cellRootColorOverlay.getVisibility() != View.VISIBLE) {
                    cellRootColorOverlay.setVisibility(View.VISIBLE);
                }

                Drawable cellBack = cellRootColorOverlay.getBackground();
                if (cellBack == null || ((ColorDrawable) cellBack).getColor() != color) {
                    cellRootColorOverlay.setBackgroundColor(color);
                }
            } else {
                cellRootColorOverlay.setVisibility(View.GONE);
            }
        }
    }

    public static interface AppListAdapterListener {
        public void onItemClearClick(String packageName, int position);

        public void onItemRemoveClick(String packageName, int position);
    }
}
