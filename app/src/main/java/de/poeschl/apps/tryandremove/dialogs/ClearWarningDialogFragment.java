/*
 * Copyright (c) 2014 Markus Poeschl
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

package de.poeschl.apps.tryandremove.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

import de.poeschl.apps.tryandremove.R;


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
        builder.setMessage(getString(R.string.clear_warning_dialog_message))
                .setPositiveButton(getString(R.string.clear_warning_dialog_agree), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onUserConfirmedClear();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.clear_warning_dialog_cancel), new DialogInterface.OnClickListener() {
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
