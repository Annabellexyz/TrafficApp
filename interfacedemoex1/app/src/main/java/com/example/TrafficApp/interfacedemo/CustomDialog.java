package com.example.rasheduzzaman.interfacedemo;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


public class CustomDialog extends DialogFragment {

    private EditText editText;
    public static final int SEND_CODE = 3;

    public CustomDialog() {
        // Required empty public constructor
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface CustomDialogListener {
        public void onDialogPositiveClick(String str);
    }

    // Use this instance of the interface to deliver action events
    CustomDialogListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CustomDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.custom_dialog_layout, null);

        builder.setView(view)
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity(), "You click positive button", Toast.LENGTH_SHORT).show();
                        //sendDataBackToCaller();
                        //dismiss();

                        editText = (EditText) view.findViewById(R.id.username);
                        mListener.onDialogPositiveClick(editText.getText().toString());
                        //setCon

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomDialog.this.getDialog().cancel();
                    }
                });

        //AlertDialog dialog = builder.create();
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //return dialog;

        return builder.create();
    }
}
