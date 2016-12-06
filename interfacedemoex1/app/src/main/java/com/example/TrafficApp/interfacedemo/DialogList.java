package com.example.rasheduzzaman.interfacedemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
public class DialogList extends DialogFragment{

    public DialogList(){
    }
    public static DialogList newInstance(String title){
        DialogList dialogList = new DialogList();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialogList.setArguments(bundle);
        return dialogList;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setTitle(R.string.pick_color).setItems(R.array.color_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}