package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by LOGICON on 20-12-2017.
 */

public class SnackbarHelper {

    public static void noInternet(View view, final Context context) {

        Snackbar snackbar = Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG).setAction("Turn On", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }


    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#f8f206"));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(0, 0, 0, 380);
        snackView.setLayoutParams(params);
        snackbar.show();
    }
}
