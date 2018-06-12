package com.prts.pickcustomer.login;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getContentType;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;
import static com.prts.pickcustomer.restapi.RestClient.getRestService;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private final Context mContext;
    private final LoginView mLoginView;


    LoginPresenterImpl(Context loginActivity, LoginView loginView) {
        this.mContext = loginActivity;
        this.mLoginView = loginView;
    }

    @Override
    public void login(final String mobileNo, final String password ,final boolean isCallingFromRetry) {

        if (!validateAllFields(mobileNo, password))
            return;

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            ToastHelper.noInternet(mContext);
            return;
        }

        mLoginView.showProgressDialog(mContext.getString(R.string.verify_your_details));
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
                            try {
                                getCustomerDetails(mobileNo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mLoginView.dismissProgressDialog();
                            mLoginView.enterValidCredentials();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mLoginView.dismissProgressDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)&& !isCallingFromRetry) {
                                login(mobileNo,password,true);
                            }else if(message.contains("500")){
                                mLoginView.enterValidCredentials();
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

    private boolean validateAllFields(String mobileNo, String password) {
        return validateMobileNumber(mobileNo) && validatePassword(password);
    }

    @Override
    public boolean validateMobileNumber(String mobileNo) {

        if (mobileNo.isEmpty()) {
            mLoginView.enterMobileNumber(mContext.getString(R.string.enter_phone_number));
            return false;
        }

        if (mobileNo.length() != 10) {
            mLoginView.enterMobileNumber(mContext.getString(R.string.enter_10_digit_phone_number));
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePassword(String passwrd) {

        if (passwrd.isEmpty()) {
            mLoginView.enterPassword();
            return false;
        }

        return true;
    }

    private void getCustomerDetails(final String mobile) {

        getRestService(BASE_URL).getCustomerDetails(CredentialManager.getHeaders(mContext), mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Customer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Customer customer) {
                        mLoginView.dismissProgressDialog();

                        if (customer != null) {

                            if (customer.getStatus().equalsIgnoreCase("True")) {
                                CredentialManager.setName(mContext, customer.getName());
                                CredentialManager.setEmailId(mContext, customer.getEmailID());
                                mLoginView.navigateToHomePage();
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            mLoginView.dismissProgressDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getCustomerDetails(mobile);
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

    private void saveTokenAndMobileInServer(final String mobile, final String deviceId) {
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setMobileNo(mobile);

        getRestService(BASE_URL).sendDeviceIdToServer(CredentialManager.getHeaders(mContext), device)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                        if (aBoolean) {
                            if (!NetworkHelper.hasNetworkConnection(mContext)) {
                                mLoginView.noInternet();
                                return;
                            }

                            getCustomerDetails(mobile);
                        } else {
                            mLoginView.dismissProgressDialog();
                            mLoginView.enterValidCredentials();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {


                        try {
                            mLoginView.dismissProgressDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                saveTokenAndMobileInServer(mobile, deviceId);
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
