package com.prts.pickcustomer.helpers;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class ViewHelper {

    public static String getString(TextInputLayout textInputLayout) {
        String text = "";

        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            text = textInputLayout.getEditText().getText().toString().trim();
        }

        return text;
    }
    public static String getString(EditText textInputLayout) {
        String text = "";

        if (textInputLayout != null ) {
            text = textInputLayout.getText().toString().trim();
        }

        return text;
    }
    public static String getString(TextView textInputLayout) {
        String text = "";

        if (textInputLayout != null ) {
            text = textInputLayout.getText().toString().trim();
        }

        return text;
    }

    public static boolean isNull(EditText textInputLayout) {
        return textInputLayout == null;
    }
}
