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

package de.poeschl.apps.tryandremove.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.interfaces.PackageList;

/**
 * Created by Markus Pöschl on 22.12.14.
 */
public class RemoveAppAdapter extends BaseAdapter {

    public static final String DEFAULT_STRING = "Choose app to remove";

    private List<String> installedApps;
    private LayoutInflater layoutInflater;

    public RemoveAppAdapter(PackageList packageList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        updateAdapter(packageList);
    }

    public void updateAdapter(PackageList packageList) {
        if (this.installedApps != null) {
            this.installedApps.clear();
        } else {
            this.installedApps = new ArrayList<>();
        }

        installedApps.add(DEFAULT_STRING);
        installedApps.addAll(packageList.getPackages());
    }

    @Override
    public int getCount() {
        return installedApps.size();
    }

    @Override
    public String getItem(int position) {
        return installedApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.debug_simple_spinner_item, parent, false);
        TextView tv = ButterKnife.findById(view, R.id.simple_spinner_item_text1);
        tv.setText(getItem(position));
        return view;
    }
}
