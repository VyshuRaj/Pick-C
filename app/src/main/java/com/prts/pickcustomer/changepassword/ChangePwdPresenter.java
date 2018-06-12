package com.prts.pickcustomer.changepassword;

/**
 * Created by LOGICON on 29-12-2017.
 */

public interface ChangePwdPresenter {
    void submitNewPassword(String oldPassword, String newPassword, String newRePassword);
    boolean validateOldPwd(String oldPwd);
    boolean validateNewOldPwd(String newPwd);
    boolean validateConfirmPwd(String confirmPwd);
    boolean isBothPwdMatched(String old,String newp);

}
