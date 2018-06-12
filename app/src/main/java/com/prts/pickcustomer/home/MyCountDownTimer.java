package com.prts.pickcustomer.home;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;

/**
 * Created by LOGICON on 02-01-2018.
 */

public class MyCountDownTimer extends CountDownTimer {

    public boolean isRunning;
    int count=1;
    int mRepeatTime;
    long totalTime;
    Context mContext;
    CountDownListener mDownListener;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, int repeatTimes, long totalTime, Context context,CountDownListener countDownListener) {
        super(millisInFuture, countDownInterval);
        mRepeatTime=repeatTimes;
        this.totalTime=totalTime;
        mContext=context;
        mDownListener=countDownListener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (isRunning) {
            int currentMillis = (int) (millisUntilFinished - ((mRepeatTime-count)*totalTime));
            int progress = (int) (((double) currentMillis / (double) totalTime) * (double) 100);

            if (currentMillis <= 0){
                count++;
            }

            String time2=count+"/"+mRepeatTime;
            String time1=count+"/"+mRepeatTime;
            mDownListener.setTimerValues(time1,time2);

        }
    }

    @Override
    public void onFinish() {
       // tvForTime.setText("0 s");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mDownListener.hideTheViews();

            }
        }, 100);

        if (isRunning) {
            isRunning = false;
            mDownListener.retryBookingDialog();

        }
    }
}
