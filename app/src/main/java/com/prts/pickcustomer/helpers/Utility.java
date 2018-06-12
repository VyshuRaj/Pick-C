package com.prts.pickcustomer.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by LOGICON on 30-Dec-17.
 */

public class Utility {

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static String isNull(String value) {
        if (value != null && value.length() > 0) {
            return value.trim();
        }

        return "";
    }


    public static void existanceApplication(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AppDialogTheme1);
        @SuppressLint("InflateParams")
        final Activity activity = (Activity) context;
        View view = activity.getLayoutInflater().inflate(R.layout.exit_confirmation, null, false);
        alertDialog.setView(view);
        PickCCustomTextVIew yes = view.findViewById(R.id.yesBtn);
        PickCCustomTextVIew no = view.findViewById(R.id.noBtn);

        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                activity.finish();
                System.exit(0);
            }
        });
    }

    public static String addToPostParams(String paramKey, String paramValue) {
        if (paramValue != null)
            return paramKey.concat(Constants.PARAMETER_EQUALS).concat(paramValue).concat(Constants.PARAMETER_SEP);
        return "";
    }

    public static byte[] getBytesFromString(String values) {
        byte[] bytes = null;

        if (values != null && values.length() > 0) {

            try {
                bytes = values.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    public static Object chkNull(Object pData) {
        return (pData == null ? "" : pData);
    }

    public static String getString(InputStream inputStream){

        StringBuilder response = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("NearByPlacesResponse : -- " + response.toString());
        return response.toString();
    }

    public static String getStringFirstLetterCap(String inputStream){

        if (inputStream!=null && inputStream.length()>0) {
            return inputStream.substring(0, 1).toUpperCase() + inputStream.substring(1);
        }

        return null;
    }
}
