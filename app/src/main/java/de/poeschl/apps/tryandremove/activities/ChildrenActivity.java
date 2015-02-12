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

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;

/**
 * Created by Markus Pöschl on 17.12.2014.
 */
public class ChildrenActivity extends ToolbarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);

    }

    protected void setupLayout(int layout) {
        ViewGroup container = appContainer.get(this);
        //Inflate navigation drawer layout
        getLayoutInflater().inflate(R.layout.activity_toolbar_layout, container);

        toolbar = ButterKnife.findById(container, R.id.toolbar);
        ViewGroup mainContent = ButterKnife.findById(container, R.id.mainContent);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inflate the real content into the contentView
        getLayoutInflater().inflate(layout, mainContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_left);
    }
}
