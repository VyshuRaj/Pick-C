package com.prts.pickcustomer.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.splash.SplashActivity;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class NetworkHelper {

    public static boolean hasNetworkConnection(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if (cm != null) {
                networkInfo = cm.getActiveNetworkInfo();

                if (networkInfo != null) {

                    if (networkInfo.isConnected() && (ConnectivityManager.TYPE_WIFI == networkInfo.getType() || ConnectivityManager.TYPE_MOBILE == networkInfo.getType()))
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public static void turnOnNetworkSettings(final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Activity activity = (Activity) context;
        View view = activity.getLayoutInflater().inflate(R.layout.no_internet_layout, null, false);
        builder.setView(view);
        PickCCustomTextVIew wifi = view.findViewById(R.id.wifiBtn);
        PickCCustomTextVIew data = view.findViewById(R.id.dataBtn);
        PickCCustomTextVIew refresh = view.findViewById(R.id.refreshBtn);
        final AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.cancel();
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

            }
        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.cancel();
                context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.cancel();

                if (!hasNetworkConnection(context)) {
                    turnOnNetworkSettings(context);
                    ToastHelper.noInternet(context);
                    return;
                }

                if(CredentialManager.getPresentState(context))
                    context.startActivity(new Intent(context,SplashActivity.class));
            }
        });
    }


}
