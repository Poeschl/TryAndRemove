package de.poeschl.apps.tryandremove.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;


/**
 * Created by Markus Pöschl on 18.12.2014.
 */
public class ClearWarningDialogFragment extends DialogFragment {

    private static final String FRAGMENT_TAG = "ClearWarningDialogFragment";

    private ButtonListener listener;

    public void setButtonListener(ButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Clear app list?")
                .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onUserConfirmedClear();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public static interface ButtonListener {
        public void onUserConfirmedClear();
    }

    public void show(FragmentManager manager) {
        super.show(manager, FRAGMENT_TAG);
    }
}
