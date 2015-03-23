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

import android.view.View;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import de.poeschl.apps.tryandremove.BuildConfig;
import de.poeschl.apps.tryandremove.R;

/**
 * Created by Markus Pöschl on 23.03.2015.
 */
public class AdManager implements MoPubView.BannerAdListener {

    private MoPubView adView;
    private String adUnitId;

    public void setAdView(MoPubView adView) {
        this.adView = adView;

        if (this.adUnitId != null) {
            initAd();
        }
    }

    public void registerAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;

        if (this.adView != null) {
            initAd();
        }
    }

    @Override
    public void onBannerLoaded(MoPubView moPubView) {
        //Be lazy do nothing.
    }

    @Override
    public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
        adView.setVisibility(View.GONE);
    }

    @Override
    public void onBannerClicked(MoPubView moPubView) {
        //Be lazy do nothing.
    }

    @Override
    public void onBannerExpanded(MoPubView moPubView) {
        //Be lazy do nothing.
    }

    @Override
    public void onBannerCollapsed(MoPubView moPubView) {
        //Be lazy do nothing.
    }

    private void initAd() {
        adView.setAdUnitId(adUnitId);
        adView.setBannerAdListener(this);
        if (BuildConfig.DEBUG) {
//            adView.setTesting(true);
            adView.setBackgroundColor(adView.getResources().getColor(R.color.primary_main));
        }
    }
}
