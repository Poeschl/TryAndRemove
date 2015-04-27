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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.interfaces.NavigationDrawerListener;
import de.poeschl.apps.tryandremove.models.MenuItem;

/**
 * Created by Markus Pöschl on 20.01.15.
 */
public class NavigationItemAdapter<T> extends RecyclerView.Adapter<NavigationItemAdapter.ViewHolder> {

    private List<MenuItem<T>> itemList;
    private NavigationDrawerListener<T> navListener;

    public NavigationItemAdapter(List<MenuItem<T>> menuItems) {
        this.itemList = menuItems;
    }

    public void setNavigationListener(NavigationDrawerListener<T> listener) {
        this.navListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_navigation_drawer_item, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(NavigationItemAdapter.ViewHolder holder, int position) {
        final MenuItem<T> current = itemList.get(position);

        int iconResource = current.getIconResource();
        if (iconResource == -1) {
            holder.icon.setVisibility(View.GONE);
        } else {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(iconResource);
        }

        holder.title.setText(current.getTitle());


        holder.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navListener.onNavigationItemClick(current.getTarget());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.cell_navigation_root)
        View cellRoot;
        @InjectView(R.id.cell_navigation_icon)
        ImageView icon;
        @InjectView(R.id.cell_navigation_title)
        TextView title;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setClickListener(View.OnClickListener clickListener) {
            cellRoot.setOnClickListener(clickListener);
        }
    }
}
