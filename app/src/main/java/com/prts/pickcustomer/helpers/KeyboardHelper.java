package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by LOGICON on 01-01-2018.
 */

public class KeyboardHelper {

    public static void openKeyboard(Context context, View v, boolean wantToShow) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm!=null) {
            if(wantToShow){
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
            }else {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

    }

}
