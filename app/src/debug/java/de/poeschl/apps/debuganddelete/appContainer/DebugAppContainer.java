package de.poeschl.apps.debuganddelete.appContainer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.poeschl.apps.debuganddelete.R;
import de.poeschl.apps.debuganddelete.app.appContainer.AppContainer;

import static butterknife.ButterKnife.findById;

/**
 * Created by markus on 05.12.14.
 */
public class DebugAppContainer implements AppContainer {


    @InjectView(R.id.debug_drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.debug_content)
    ViewGroup content;

    private Activity activity;
    private Context drawerContext;
    private boolean seenDebugDrawer = false;

    public DebugAppContainer() {

    }

    @Override
    public ViewGroup get(final Activity activity) {
        this.activity = activity;
        drawerContext = activity;

        activity.setContentView(R.layout.debug_activity_frame);

        // Manually find the debug drawer and inflate the drawer layout inside of it.
        ViewGroup drawer = findById(activity, R.id.debug_drawer);
        LayoutInflater.from(drawerContext).inflate(R.layout.drawer_debug_layout, drawer);

        // Inject after inflating the drawer layout so its views are available to inject.
        ButterKnife.inject(this, activity);

        drawerLayout.setDrawerShadow(R.drawable.debug_drawer_shadow, Gravity.END);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(drawerContext, "Refresh", Toast.LENGTH_SHORT).show();
            }
        });

        // If you have not seen the debug drawer before, show it with a message
        if (!seenDebugDrawer) {
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.openDrawer(Gravity.END);
                    Toast.makeText(activity, R.string.debug_drawer_welcome, Toast.LENGTH_LONG).show();
                }
            }, 1000);
            seenDebugDrawer = true;
        }

        return content;
    }

}
