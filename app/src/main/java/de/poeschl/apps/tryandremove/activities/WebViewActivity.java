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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;

/**
 * Created by Markus Pöschl on 09.02.15.
 */
public class WebViewActivity extends ChildrenActivity {

    public static final String URL_EXTRA_KEY = "URL_EXTRA";
    public static final String ACTIONBAR_TITLE_KEY = "ACTIONBAR_EXTRA";

    @InjectView(R.id.webView_layout_policy_webView)
    WebView webView;
    @InjectView(R.id.webView_layout_progressbar)
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout(R.layout.activity_webview);

        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();

        webView.setWebViewClient(new CustomWebViewClient());

        if (extras != null && extras.containsKey(URL_EXTRA_KEY)) {
            webView.loadUrl(extras.getString(URL_EXTRA_KEY));

            if (extras.containsKey(ACTIONBAR_TITLE_KEY)) {
                getSupportActionBar().setTitle(extras.getString(ACTIONBAR_TITLE_KEY));
            }

        } else {
            finish();
        }
    }

    private void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoadingIndicator();
            view.setVisibility(View.INVISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            hideLoadingIndicator();
            super.onPageFinished(view, url);
        }
    }
}
