package com.prts.pickcustomer.forgotpqwd;

/**
 * Created by LOGICON on 18-12-2017.
 */

public interface FPPresenter {
    void submit(Forgot forgot);

    boolean validateOTP(String string);

    boolean checkMatchingPwds(String string, String string1);

    boolean validatePwd(String string);
}
