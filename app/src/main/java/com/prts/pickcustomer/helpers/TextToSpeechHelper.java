package com.prts.pickcustomer.helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by LOGICON on 28-12-2017.
 */

public class TextToSpeechHelper {

    private static TextToSpeech mTextToSpeech;

    public static TextToSpeech getInstance(final Context activity, final OnInitListener on) {
        try {
            mTextToSpeech = new TextToSpeech(activity, on);
        } catch (Exception e) {
            Log.e("TextToSpeech","Exception "+e.getMessage());
        }
        return mTextToSpeech;
    }

    public static void speakOut(Context activity, String message) {

        boolean announcementStatus = CredentialManager.getVolumeStatus(activity);

        if (!announcementStatus) {
            return;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21(message, (Activity) activity);
            } else {
                ttsUnder20(message);
            }
        } catch (Exception e) {
            Log.e("TextToSpeech","speakOut Exception "+e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    private static void ttsUnder20(String text) {
        try {
            if (mTextToSpeech != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
            }
        } catch (Exception e) {
            Log.e("TextToSpeech","ttsUnder20 Exception "+e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void ttsGreater21(String text, Activity activity) {
        try {
            if (mTextToSpeech != null) {
                String utteranceId = activity.hashCode() + "";
                mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            }
        } catch (Exception e) {
            Log.e("TextToSpeech","ttsGreater21 Exception "+e.getMessage());
        }
    }

    public static void destroyTTS() {

        try {
            if (mTextToSpeech != null) {
                mTextToSpeech.stop();
                mTextToSpeech.shutdown();
            }
        } catch (Exception e) {
            Log.e("TextToSpeech","destroyTTS Exception "+e.getMessage());
        }

    }
}
