package com.prts.pickcustomer.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by administrator1 on 23-Feb-17.
 */

public class PermissionChecker {

    public static final String[] LOCATION_PERMISSIONS = { android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final String[] READ_WRITE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] CALL_PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PRIVILEGED};

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context,String[] permissions)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            int res = 0;
            for (String str : permissions) {

                res = context.checkCallingOrSelfPermission(str);

                if (!(res == PackageManager.PERMISSION_GRANTED)) {
                    return false;
                }
            }
            return true;


        } else {
            return true;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void reqPermissions(final Activity context, final String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(permissions, 100);
        }
    }
}