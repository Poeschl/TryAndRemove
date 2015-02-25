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
import android.webkit.WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;

/**
 * Created by Markus Pöschl on 09.02.15.
 */
public class WebViewActivity extends ChildrenActivity {

    public static final String URL_EXTRA_KEY = "URL_EXTRA";
    public static final String ACTIONBAR_TITLE_KEY = "ACTIONBAR_EXTRA";

    @InjectView(R.id.private_policy_layout_policy_webView)
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout(R.layout.activity_privacy_policy);

        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(URL_EXTRA_KEY)) {
            webView.loadUrl(extras.getString(URL_EXTRA_KEY));

            if (extras.containsKey(ACTIONBAR_TITLE_KEY)) {
                getSupportActionBar().setTitle(extras.getString(ACTIONBAR_TITLE_KEY));
            }

        } else {
            finish();
        }
    }
}
