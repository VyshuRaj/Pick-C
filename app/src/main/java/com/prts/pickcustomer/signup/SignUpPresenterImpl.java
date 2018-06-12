package com.prts.pickcustomer.signup;

import android.content.Context;
import android.util.Log;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ValidationHelper;
import com.prts.pickcustomer.login.Credentials;
import com.prts.pickcustomer.login.Token;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getContentType;
import static com.prts.pickcustomer.helpers.ValidationHelper.isValidPassword;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;
import static com.prts.pickcustomer.restapi.RestClient.getRestService;

/**
 * Created by LOGICON on 17-Dec-17.
 */

public class SignUpPresenterImpl implements SignUpPresenter {
    private SignUpView mSignUpView;
    private SignUp mSignUp;
    private Context mContext;

    SignUpPresenterImpl(SignUpView signUpView, Context signUpActivity) {
        mSignUpView = signUpView;
        mContext = signUpActivity;
    }

    @Override
    public void register(SignUp signUp) {
        mSignUp = signUp;
        boolean areverified = validateAllFileds(signUp);
        if (areverified) {

            if (!NetworkHelper.hasNetworkConnection(mContext)) {
                mSignUpView.noInternet();
                return;
            }

            mSignUpView.showProgressDialog("Processing registration");
            try {
                checkMobileInServer(signUp.getMobileNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private boolean validateAllFileds(SignUp signUp) {

        if (!validateName(signUp.getUserName()))
            return false;

        if (!validateMobileNumber(signUp.getMobileNumber()))
            return false;

        if (!validateEmail(signUp.getEmail()))
            return false;

        if (!validatePassword(signUp.getPassword()))
            return false;

        if (!checkMatchingOfPwds(signUp.getPassword(), signUp.getReEnterPwd()))
            return false;

        return true;
    }


    @Override
    public void checkMobileInServer(final String mobile) {

        getRestService(BASE_URL).isExistedNumber(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isExisetdNumber) {

                        try {
                            if (!isExisetdNumber) {
                                saveCustomerDetails();
                            } else {
                                mSignUpView.numberAlreadyExisted();
                            }
                        } catch (Exception e) {
                            mSignUpView.dismissProgressDialog();
                            mSignUpView.mobileVerificationFailed();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSignUpView.dismissProgressDialog();

                        try {
                            mSignUpView.dismissProgressDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                checkMobileInServer(mobile);
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

    @Override
    public boolean validateName(String name) {

        if (name.isEmpty()) {
            mSignUpView.enterName(mContext.getString(R.string.enter_name));
            return false;
        }

        return true;
    }

    @Override
    public boolean validateMobileNumber(String number) {

        if (number.isEmpty()) {
            mSignUpView.enterMobileNumber(mContext.getString(R.string.enter_phone_number));
            return false;
        }

        if (number.length() != 10) {
            mSignUpView.enterMobileNumber(mContext.getString(R.string.enter_10_digit_phone_number));
            return false;
        }

        return true;
    }

    @Override
    public boolean validateEmail(String email) {

        if (email.isEmpty()) {
            mSignUpView.enterValidEmail(mContext.getString(R.string.enter_email));
            return false;
        }
        if (!ValidationHelper.isValidEmail(email)) {
            mSignUpView.enterValidEmail(mContext.getString(R.string.valid_email));
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePassword(String password) {

        if (password.isEmpty()) {
            mSignUpView.enterPassword(mContext.getString(R.string.enter_password));
            return false;
        }
        if (password.length() < 8) {
            mSignUpView.enterPassword(mContext.getString(R.string.pass_min_8));
            return false;

        }
        if (password.length() > 20) {
            mSignUpView.enterPassword(mContext.getString(R.string.password_max_20));
            return false;
        }
        if (!isValidPassword(password)) {
            mSignUpView.enterPassword(mContext.getString(R.string.password_conditions));
            return false;
        }

        return true;
    }

    @Override
    public boolean checkMatchingOfPwds(String pwd, String newPwd) {

        if (newPwd.isEmpty()) {
            mSignUpView.reEnterPwd(mContext.getString(R.string.enter_password));
            return false;
        }
        if (!pwd.equals(newPwd)) {
            mSignUpView.reEnterPwd(mContext.getString(R.string.pwds_not_matched));
            return false;
        }

        return true;
    }

/*

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

                            mSignUpView.navigateToHomePage();

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
*/
/*

    @Override
    public void verfyOTP(final String string, final String enteredOTP) {
        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mSignUpView.noInternet();
            return;
        }

        mSignUpView.showProgressDialog(mContext.getString(R.string.validate_otp));
        getRestService(BASE_URL).verifyOTP(string, enteredOTP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        mSignUpView.dismissProgressDialog();

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
                            mSignUpView.dismissProgressDialog();
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
*/

    private void saveCustomerDetails() {
        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mSignUpView.noInternet();
            return;
        }
        getRestService(BASE_URL).saveCustomerDetails(getContentType(), mSignUp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mSignUpView.dismissProgressDialog();

                        if (aBoolean)
                            mSignUpView.navigateToTCPage();
                        else
                            mSignUpView.registrationFailed();

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            mSignUpView.dismissProgressDialog();
                            String message=e.getMessage();
                            ActivityHelper.handleException(mContext,message);
                           /* if (message.contains(TIME_OUT)) {
                                saveCustomerDetails();
                            }else{
                                ActivityHelper.handleException(mContext,message);
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
}
