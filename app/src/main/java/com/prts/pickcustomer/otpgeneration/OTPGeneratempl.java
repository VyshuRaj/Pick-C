package com.prts.pickcustomer.otpgeneration;

import android.content.Context;
import android.text.TextUtils;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.forgotpqwd.ForgotPwdActivity;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;
import static com.prts.pickcustomer.restapi.RestClient.getRestService;

/**
 * Created by LOGICON on 18-Dec-17.
 */

public class OTPGeneratempl implements OTPGeneratePresenter {
    private Context mContext;
    private OTPGenerateView mOTPView;

    OTPGeneratempl(Context forgotPasswordActivity,  OTPGenerateView TPGenerateView) {
        mContext = forgotPasswordActivity;
        mOTPView = TPGenerateView;
    }


    @Override
    public void getOTP(String number) {

        if (validateMobileNumber(number))
            checkMobileNumber(number);
    }

    @Override
    public boolean validateMobileNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            mOTPView.enterMobileNumber(mContext.getString(R.string.enter_phone_number));
            return false;
        }
        if (number.charAt(0) == '0') {
            mOTPView.enterMobileNumber(mContext.getString(R.string.phone_number_not_start_with_0));
            return false;
        }

        if (number.length() != 10) {
            mOTPView.enterMobileNumber(mContext.getString(R.string.enter_10_digit_phone_number));
            return false;
        }

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            ToastHelper.noInternet(mContext);
            return false;
        }

        return true;
    }

    private void checkMobileNumber(final String mobile) {
        mOTPView.showProgressDialog();
        getRestService(BASE_URL).isExistedNumber(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean status) {

                        if (!status) {//new number
                            mOTPView.isNewNumber();
                        } else {//old number

                            if (!NetworkHelper.hasNetworkConnection(mContext)) {
                                ToastHelper.noInternet(mContext);
                                return;
                            }

                            generateOTP(mobile);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mOTPView.dismissDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                checkMobileNumber(mobile);
                            }else{
                                ActivityHelper.handleException(mContext,message);
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

    private void generateOTP(final String mobile) {
        getRestService(BASE_URL).generateOTP(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean s) {
                        mOTPView.dismissDialog();

                        if (s) {
                            mOTPView.otpNotGenerated("OTP generated send to " + mobile);
                            ForgotPwdActivity.startActivity(mContext, mobile);
                        } else {
                            mOTPView.otpNotGenerated("OTP not generated try again");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mOTPView.dismissDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                generateOTP(mobile);
                            }else{
                                ActivityHelper.handleException(mContext,message);
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
}
