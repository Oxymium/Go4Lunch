package com.raspberyl.go4lunch.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.RestaurantActivity;

public class AlertDialogUtil {

    public void createAlertDialog(Context context, String title, String message, String buttonNeutral) {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog_Style);
        // Set Title and Message content
        builder.setTitle(title);
        builder.setMessage(message);
        // Neutral button
        builder.setNeutralButton(buttonNeutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

}
