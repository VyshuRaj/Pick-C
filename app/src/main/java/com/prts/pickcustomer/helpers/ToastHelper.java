package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class ToastHelper {
    public static void showToastLenShort(Context context,String message){
        try {
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showToastLenLong(Context context,String message){
        try {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void noInternet(final Context context){
        try {
            ToastHelper.showToastLenShort(context,"No internet connection");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   }
