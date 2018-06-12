package com.prts.pickcustomer.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import com.prts.pickcustomer.R;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class ProgressDialogHelper {
    private static ProgressDialog mProgressDialog;

    private static ProgressDialog getInstance(Context context) {
        return new ProgressDialog(context, R.style.AppDialogTheme1);
    }

    public static void showProgressDialog(Context context, String title) {
        try {
            if (mProgressDialog==null) {
                mProgressDialog = getInstance(context);
                mProgressDialog.setTitle(title);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void dismissDialog() {
        try {
            mProgressDialog.dismiss();
            mProgressDialog=null;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
