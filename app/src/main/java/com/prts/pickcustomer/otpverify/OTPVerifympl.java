package com.prts.pickcustomer.otpverify;

import android.content.Context;
import android.util.Log;

import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.login.Credentials;
import com.prts.pickcustomer.login.Token;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getContentType;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestClient.getRestService;

/**
 * Created by LOGICON on 18-Dec-17.
 */

public class OTPVerifympl implements OTPVerifyPresenter {
    private Context mContext;
    private OTPVerifyView mOTPView;

    OTPVerifympl(Context forgotPasswordActivity, OTPVerifyView OTPView) {
        mContext = forgotPasswordActivity;
        mOTPView = OTPView;
    }


    private void login(final String mobileNo, final String password ) {


        Credentials credentials = new Credentials();
        credentials.setMobileNo(mobileNo);
        credentials.setPassword(password);

        getRestService(BASE_URL).getToken(getContentType(), credentials)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Token token) {
                        Log.e("Login","status "+token.getStatus());

                        if (token.getStatus()) {
                            Log.e("Login","token "+token);
                            CredentialManager.setAuthToken(mContext, token.getToken());
                            CredentialManager.setMobileNumber(mContext,mobileNo);

                            mOTPView.navigateToHomePage();

                        }
                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void verifyOtp(String number,String otp) {


        getRestService(BASE_URL).verifyOTP(number, otp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {

                        if (s) {
                            login(CredentialManager.getMobileNO(mContext),CredentialManager.getPassword(mContext));
                        } else
                        {
                            ToastHelper.showToastLenShort(mContext,"Enter valid otp");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            String message=e.getMessage();
                            ActivityHelper.handleException(mContext,message);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
