package com.prts.pickcustomer.forgotpqwd;

import android.content.Context;

import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.restapi.RestClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getContentType;
import static com.prts.pickcustomer.helpers.ValidationHelper.isValidPassword;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by LOGICON on 18-12-2017.
 */

public class FPPresneterImpl implements FPPresenter {

    private Context mContext;
    private FPView mFpView;

    FPPresneterImpl(Context forgotPwdActivity, FPView fpView) {
        mContext = forgotPwdActivity;
        mFpView = fpView;
    }

    @Override
    public void submit(final Forgot forgot) {

        if (!validateAllFields(forgot)) {
            return;
        }

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            ToastHelper.noInternet(mContext);
            return;
        }
        mFpView.showDialogBox();

        RestClient.getRestService(BASE_URL).updatePassord(getContentType(),forgot)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        mFpView.dismissDialog();

                        if(s){
                           mFpView.navigateToLoginPage();
                        }else{
                            mFpView.pwdIsNotUpdated();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mFpView.dismissDialog();
                            String message = e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                submit(forgot);
                            } else {
                                ActivityHelper.handleException(mContext, message);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    private boolean validateAllFields(Forgot forgot) {
        return validateOTP(forgot.getOTP()) && validatePwd(forgot.getOldPassword()) && checkMatchingPwds(forgot.getOldPassword(), forgot.getNewPassword());

    }

    @Override
    public boolean validateOTP(String string) {

        if (string.isEmpty()) {
            mFpView.enterOTP("Please enter OTP");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkMatchingPwds(String old, String newp) {

        if (newp.isEmpty()) {
            mFpView.enterRePwd("Please re-enter password");
            return false;
        }
        if (!old.equals(newp)) {
            mFpView.enterRePwd("Password and Re-enter password are not matched");
            return false;
        }
        return true;
    }

    @Override
    public boolean validatePwd(String password) {
        if (password.isEmpty()) {
            mFpView.enterPwd("Please enter password");

            return false;
        }
        if (password.length() < 8) {
            mFpView.enterPwd("Password must be more than 8 characters");
            return false;
        }
        if (password.length() > 20) {
            mFpView.enterPwd("Password must be below 20 characters");
            return false;
        }
        if (!isValidPassword(password)) {
            mFpView.enterPwd("Enter strong password" + "\n" + "Password must contain 1 Uppercase, 1 Lowercase, 1 Special character and 1 Number");
            return false;
        }
        return true;
    }
}
