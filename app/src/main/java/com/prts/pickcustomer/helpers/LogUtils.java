package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LOGICON on 15-02-2018.
 */

public class LogUtils {

    public static void appendLog(Context context, String text) {


        File file = null;
        try {
            file = new File(context.getFilesDir(),"Pick-C");

            if( !file.exists()){
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File gpxfile = new File(file, "log"+DateHelper.getCurrentDateForLog());

        String filepath =""+gpxfile+".txt";
        FileOutputStream fos = null;
        try {
            String message=text+"\n";
            fos = new FileOutputStream(filepath,true);
            byte[] buffer = message.getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (IOException e) {
            Log.e("LogUtils", "Exception1 "+e.getMessage());
        }finally{
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                Log.e("LogUtils", "Exception2 "+e.getMessage());
            }
        }
    }


    public static void writeToLog(Context context, String text) {

        File file = new File(context.getFilesDir(),"Pick-CC");

        if(!file.exists()){
            file.mkdir();
        }

        File gpxfile = new File(file, "log"+DateHelper.getCurrentDateForLog());
        String filepath =""+gpxfile+".txt";
        FileOutputStream fos = null;

        try {
            String message=text+"\n";
            fos = new FileOutputStream(filepath,true);
            byte[] buffer = message.getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (IOException e) {
            Log.e("LogUtils", "Exception1 "+e.getMessage());
        }finally{
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                Log.e("LogUtils", "Exception2 "+e.getMessage());
            }
        }
    }

    public static void onLine(Context context, String text) {

        File file = new File(context.getFilesDir(),"Pick-COnline");

        if(!file.exists()){
            file.mkdir();
        }

        File gpxfile = new File(file, "log"+DateHelper.getCurrentDateForLog());
        String filepath =""+gpxfile+".txt";
        FileOutputStream fos = null;

        try {
            String message=text+"\n";
            fos = new FileOutputStream(filepath,true);
            byte[] buffer = message.getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (IOException e) {
            Log.e("LogUtils", "Exception1 "+e.getMessage());
        }finally{
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                Log.e("LogUtils", "Exception2 "+e.getMessage());
            }
        }
    }
}
