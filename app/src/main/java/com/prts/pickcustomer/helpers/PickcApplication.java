package com.prts.pickcustomer.helpers;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.util.Log;


/**
 * Created by LOGICON on 29-12-2017.
 */

public class PickcApplication extends Application implements TextToSpeech.OnInitListener {


    @Override
    public void onInit(int status) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
