package de.poeschl.apps.debuganddelete.app.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import de.poeschl.apps.debuganddelete.R;

/**
 * Created by mpo on 04.12.14.
 */
public class SimpleAdapter extends ArrayAdapter<String> {
    public SimpleAdapter(Context context, List<String> objects) {
        super(context, R.layout.cell, R.id.cell_text, objects);
    }
}
