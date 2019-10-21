package com.example.hoopy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

public class Utils {
    private static AlertDialog dialog;

    public static void showprogressdialog(Context ctx)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog, null, false);
        builder.setView(v);
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    public static void dismissprogressdialog()
    {
        if (dialog!=null)
            dialog.dismiss();
    }
}
