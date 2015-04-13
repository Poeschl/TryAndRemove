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

package de.poeschl.apps.tryandremove.utils;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;

import de.poeschl.apps.tryandremove.BuildConfig;
import de.poeschl.apps.tryandremove.R;
import timber.log.Timber;

/**
 * Created by Markus Pöschl on 23.03.2015.
 */
public class AdManager extends AdListener {

    private AdView adView;

    public void setAdView(AdView adView) {
        this.adView = adView;

        initAd();
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        adView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
        adView.setVisibility(View.GONE);
        Timber.i("Ad failed to load errorCode: " + errorCode);
    }

    public void onPause() {
        if (this.adView != null) {
            adView.pause();
        }
    }

    public void onResume() {
        if (this.adView != null) {
            adView.resume();
        }
    }

    public void onDestroy() {
        if (this.adView != null) {
            adView.destroy();
        }
    }


    @SuppressWarnings("ResourceType")
    private void initAd() {
        adView.setAdListener(this);

        AdRequest.Builder requestBuilder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            requestBuilder = requestBuilder.addTestDevice("1BF359B6E7518CE9D3BE96A3C894F4C3");
        }

        Resources resources = adView.getContext().getResources();
        Bundle bundle = new Bundle();
        bundle.putString("color_bg", resources.getString(R.color.background));
        bundle.putString("color_bg_top", resources.getString(R.color.background));
        bundle.putString("color_border", resources.getString(R.color.background));
        bundle.putString("color_link", resources.getString(R.color.secondary_text));
        bundle.putString("color_text", resources.getString(R.color.primary_dark));
        bundle.putString("color_url", resources.getString(R.color.accent_light));

        requestBuilder = requestBuilder.addNetworkExtras(new AdMobExtras(bundle));

        adView.loadAd(requestBuilder.build());
    }
}
