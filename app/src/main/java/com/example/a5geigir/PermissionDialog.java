package com.example.a5geigir;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PermissionDialog extends DialogFragment {

    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {  //Basic Dialog with 2 possible answers
        super.onCreateDialog(savedInstanceState);

        listener = (DialogListener) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permissions_title));
        builder.setMessage(getString(R.string.dialog_permissions_desc));

        builder.setPositiveButton(getString(R.string.dialog_permissions_accept), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {  //Will only call the listener
               listener.positiveAnswer();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_permissions_deny), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {  //Will only call the listener
                listener.negativeAnswer();
            }
        });

        return builder.create();
    }
}
