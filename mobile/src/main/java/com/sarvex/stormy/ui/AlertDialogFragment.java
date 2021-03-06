package com.sarvex.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.sarvex.stormy.R.string;


public class AlertDialogFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Context context = getActivity();
    return new AlertDialog.Builder(context)
        .setTitle(context.getString(string.error_title))
        .setMessage(context.getString(string.error_message))
        .setPositiveButton(context.getString(string.error_ok_button_text), null).create();
  }
}
