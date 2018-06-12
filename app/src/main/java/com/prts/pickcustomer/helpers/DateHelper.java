package com.prts.pickcustomer.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by LOGICON on 02-01-2018.
 */

public class DateHelper {


    public static String getCurrentDateAndTime(){

        return getCurrentDate()+" "+getCurrentTime();
    }
    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");


        return df.format(c.getTime());
    }

    public static String getCurrentDateForLog(){
        Calendar c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");


        return df.format(c.getTime());
    }
    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(c.getTime());
        currentTime = currentTime.replaceAll(".","");

        return currentTime;
    }
}
