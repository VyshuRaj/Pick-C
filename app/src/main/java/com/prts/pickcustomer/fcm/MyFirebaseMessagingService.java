package com.prts.pickcustomer.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.home.HomeActivity;

import java.util.List;

import static com.prts.pickcustomer.helpers.Constants.BODY_KEY;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFMService";


    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        LogUtils.appendLog(MyFirebaseMessagingService.this, "Driver Notify " + remoteMessage.getData());

        if (remoteMessage.getNotification()!= null)
        {
            Log.e(TAG,"Notification"+remoteMessage.getNotification());
        }


        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            final String bodyMessage = remoteMessage.getData().get(BODY_KEY);

            try {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = null;

                if (pm != null) {
                    wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
                }

                if (wakeLock != null) {
                    wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Log.e(TAG, "Data" + remoteMessage.getData());
                ObservableObject.getInstance().updateValue(remoteMessage.getData());

            } catch (Exception e) {
                LogUtils.appendLog(MyFirebaseMessagingService.this, " OnMessageReceived Exception " + remoteMessage.getData());
            }


            try {
                if (isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    sendNotification(bodyMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (am != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                {
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    if (componentInfo.getPackageName().equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }

        return isInBackground;
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.pick_c_100x100)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager!=null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
