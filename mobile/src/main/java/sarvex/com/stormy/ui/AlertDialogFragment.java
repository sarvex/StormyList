package sarvex.com.stormy.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import sarvex.com.stormy.R.string;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        Builder builder = new Builder(context)
                .setTitle(context.getString(string.error_title))
                .setMessage(context.getString(string.error_message))
                .setPositiveButton(context.getString(string.error_ok_button_text), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
