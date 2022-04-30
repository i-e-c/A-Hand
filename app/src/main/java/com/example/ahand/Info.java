package com.example.ahand;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class Info {
    Context context;

    public Info(Context context) {
        this.context = context;
    }

    public void message(String title, String mess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(mess);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Ok", null);
        builder.show();
    }
}