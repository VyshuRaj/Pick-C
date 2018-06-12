package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.content.Intent;

import com.prts.pickcustomer.splash.SplashActivity;

/**
 * Created by LOGICON on 08-01-2018.
 */

public class ActivityHelper {

    private static void goTOSplashPage(Context activity) {
        try {
            CredentialManager.setMobileNumber(activity, "");
            Intent intent = new Intent(activity, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void handleException(final Context context, String errorMessage) {

        LogUtils.appendLog(context, "Exception " + errorMessage);

        if (errorMessage.contains("401")) {
            AlertDialogActivity.showAlertDialogActivity(context, "Invalid credentials",
                    "It seems to be credentials has been expired.Login again to continue", "Ok", null,
                    new AlertDialogActivity.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick() {
                            goTOSplashPage(context);
                        }
                    }, null);
        } else {
            ToastHelper.showToastLenShort(context, "Something went wrong.Please try again");
        }
    }
}
