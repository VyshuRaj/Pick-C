package com.prts.pickcustomer.changepassword;

import android.content.Context;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getContentType;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by LOGICON on 29-12-2017.
 */

public class ChangePwdImpl implements ChangePwdPresenter {
    Context mContext;
    private ChangePwdView mChangePwdView;
    private DialogBox mDialogBox;


    public ChangePwdImpl(Context changePasswordActivity, ChangePwdView changePasswordActivity1) {
        mContext = changePasswordActivity;
        mChangePwdView = changePasswordActivity1;
        mDialogBox=mChangePwdView.getInstance();
    }

    @Override
    public void submitNewPassword(String oldPassword, String newPassword, String newRePassword) {

        if (oldPassword.isEmpty()) {
            mChangePwdView.enterOldPassword(mContext.getString(R.string.enter_old_pwd));
            return ;
        }
        if (newPassword.isEmpty()) {
            mChangePwdView.enterNewPwd(mContext.getString(R.string.enter_new_pwd));
            return;
        }

        if (newPassword.length() < 8) {
            mChangePwdView.enterNewPwd(mContext.getString(R.string.min_six_let));
            return;
        }

        if (newPassword.length() > 20) {
            mChangePwdView.enterNewPwd(mContext.getString(R.string.max_len_20));
            return ;
        }

        if (newRePassword.isEmpty()) {
            mChangePwdView.enterConfrimPwd(mContext.getString(R.string.enter_new_pwd));
            return ;
        }


        if(!newPassword.equals(newRePassword)){
            mChangePwdView.enterConfrimPwd("New and confirm password are not matched");
            return;
        }

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mChangePwdView.noInterNet();
            return;
        }

        mDialogBox.showDialog(mContext.getString(R.string.update_pwd));
        updatePassword(oldPassword, newPassword);
    }

    private boolean checkAllFields(String oldPassword, String newPassword, String newRePassword){
        return validateOldPwd(oldPassword) && validateNewOldPwd(newPassword) && validateConfirmPwd(newRePassword);
    }
    @Override
    public boolean validateOldPwd(String oldPwd) {
        if (oldPwd.isEmpty()) {
            mChangePwdView.enterOldPassword(mContext.getString(R.string.enter_old_pwd));
            return false;
        }
        return true;
    }

    @Override
    public boolean validateNewOldPwd(String newPassword) {
        if (newPassword.isEmpty()) {
            mChangePwdView.enterNewPwd(mContext.getString(R.string.enter_new_pwd));
            return false;
        }

        if (newPassword.length() < 8) {
            mChangePwdView.enterNewPwd(mContext.getString(R.string.min_six_let));
            return false;
        }

        if (newPassword.length() > 20) {
            mChangePwdView.enterNewPwd(mContext.getString(R.string.max_len_20));
            return false;
        }

        return true;
    }

    @Override
    public boolean validateConfirmPwd(String confirmPwd) {

        if (confirmPwd.isEmpty()) {
            mChangePwdView.enterConfrimPwd(mContext.getString(R.string.enter_new_pwd));
            return false;
        }

        return false;
    }

    private void updatePassword(final String oldPassword, final String newPassword) {
        ChangePwd changePwd = new ChangePwd();
        changePwd.setMobileNo(CredentialManager.getMobileNO(mContext));
        changePwd.setPassword(oldPassword);
        changePwd.setNewPassword(newPassword);

        RestClient.getRestService(BASE_URL).changePassword(getContentType(), changePwd, changePwd.getMobileNo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mDialogBox.dismissDialog();

                        if (aBoolean) {
                            CredentialManager.setPassword(mContext, newPassword);
                            mChangePwdView.navigateToProfilePage();
                        } else {
                            mChangePwdView.showFailureDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mDialogBox.dismissDialog();
                            ActivityHelper.handleException(mContext, String.valueOf(e.getMessage()));
                           /* if () {
                                updatePassword(oldPassword,newPassword);
                            }*/
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public boolean isBothPwdMatched(String old, String newp) {

        if (!old.equals(newp)) {
            mChangePwdView.enterConfrimPwd(mContext.getString(R.string.not_matched));
            return false;
        }
        return true;
    }
}
