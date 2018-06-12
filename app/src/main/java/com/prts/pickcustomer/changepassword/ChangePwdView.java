package com.prts.pickcustomer.changepassword;

import com.prts.pickcustomer.helpers.DialogBox;

/**
 * Created by LOGICON on 29-12-2017.
 */

public interface ChangePwdView {

    void noInterNet();

    void navigateToProfilePage();

    void showFailureDialog();

    DialogBox getInstance();

    void enterOldPassword(String string);

    void enterNewPwd(String string);

    void enterConfrimPwd(String string);
}
