package de.poeschl.apps.tryandremove.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import de.poeschl.apps.tryandremove.R;
import de.poeschl.apps.tryandremove.TryAndRemoveApp;
import de.poeschl.apps.tryandremove.interfaces.AppContainer;

/**
 * Created by Markus Pöschl on 17.12.2014.
 */
public class TryAndRemoveActivity extends ActionBarActivity {

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    AppContainer appContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryAndRemoveApp.get(this).inject(this);
    }

    protected void setupLayout(Activity activity) {
        ViewGroup container = appContainer.get(this);
        getLayoutInflater().inflate(R.layout.app_list_layout, container);

        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
