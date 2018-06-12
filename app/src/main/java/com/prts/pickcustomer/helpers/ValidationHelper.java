package com.prts.pickcustomer.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LOGICON on 18-12-2017.
 */

public class ValidationHelper {
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }
    public static boolean isValidEmail(final String email) {
       return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
