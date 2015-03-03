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

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;

/**
 * Created by Markus Pöschl on 03.03.15.
 */
public class RecordAdapter extends ArrayAdapter<RecordAdapter.RecordState> {

    public RecordAdapter(Context context) {
        super(context, R.layout.cell_record_spinner, RecordState.generateStates());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View v;
        ViewHolder viewHolder;

        if (convertView == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_record_spinner, parent, false);
            viewHolder = new ViewHolder(v);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RecordState state = getItem(position);

        viewHolder.icon.setImageResource(state.getIconRes());
        viewHolder.title.setText(state.getTitleRes());

        v.setTag(viewHolder);
        return v;
    }

    protected class ViewHolder {
        @InjectView(R.id.cell_record_spinner_icon)
        ImageView icon;

        @InjectView(R.id.cell_record_spinner_text)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public static class RecordState {

        @DrawableRes
        private int iconRes;
        @StringRes
        private int titleRes;
        private boolean enable;

        public RecordState(int iconRes, int titleRes, boolean enable) {
            this.iconRes = iconRes;
            this.titleRes = titleRes;
            this.enable = enable;
        }

        public static List<RecordState> generateStates() {
            List<RecordState> states = new ArrayList<>();
            states.add(new RecordState(R.drawable.ic_action_record, R.string.record_state_active_title, true));
            states.add(new RecordState(R.drawable.ic_action_record, R.string.record_state_inactive_title, false));
            return states;
        }

        public int getIconRes() {
            return iconRes;
        }

        public int getTitleRes() {
            return titleRes;
        }

        public boolean isEnable() {
            return enable;
        }
    }
}
