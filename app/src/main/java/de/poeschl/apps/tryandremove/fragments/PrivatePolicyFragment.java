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


import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.tryandremove.R;

public class PrivatePolicyFragment extends Fragment {

    @InjectView(R.id.private_policy_layout_policy_webView)
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_private_policy, container, false);

        ButterKnife.inject(this, root);

        webView.loadUrl("file:///android_asset/PrivacyPolicy.html");

        //Different transition for < API 21 is in the MainActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setLollipopTransitions();
        }

        return root;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setLollipopTransitions() {
        Fade in = new Fade(Fade.IN);
        Fade out = new Fade(Fade.OUT);
        setEnterTransition(in);
        setReenterTransition(in);
        setReturnTransition(out);
        setExitTransition(out);
    }
}
