package com.prts.pickcustomer.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.help.WebViewActivity;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.SnackbarHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.Utility;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.otpgeneration.OTPGenerationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prts.pickcustomer.helpers.Constants.HELP_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.WEB_API_ADDRESS;

public class LoginActivity extends AppCompatActivity implements LoginView, TextWatcher {

    @BindView(R.id.mobile_txt_et_til)
    TextInputLayout mMobileNumber;
    @BindView(R.id.password_txt_til)
    TextInputLayout mPassword;

    @BindView(R.id.password_txt_et)
    PickCCustomEditText password_txt_et;

    LoginPresenter mLoginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        try {

            mLoginPresenter = new LoginPresenterImpl(LoginActivity.this, this);

            if (mMobileNumber.getEditText() != null) {
                mMobileNumber.getEditText().addTextChangedListener(this);
            }

            if (mPassword.getEditText() != null)
                mPassword.getEditText().addTextChangedListener(this);

            password_txt_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_DONE)
                        callLoginRequest();
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(View view) {
        try {
            startActivity(new Intent(LoginActivity.this, OTPGenerationActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doLogin(View view) {
        callLoginRequest();
    }

    private void callLoginRequest(){
        try {
            //String mobileNumber = ViewHelper.getString(mMobileNumber);  don't create the variables until it's mandatory
            //String password = ViewHelper.getString(mPassword);
            mLoginPresenter.login(ViewHelper.getString(mMobileNumber), ViewHelper.getString(mPassword),false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void help(View view) {
        try {
            //String completeUrl ="http://pickcargo.in/Dashboard/helpmobile" /*WEB_API_ADDRESS + "Help/menu"*/;
            Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
            intent.putExtra("url", HELP_URL);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void enterMobileNumber(String mobile) {
        try {
            mMobileNumber.setError(getString(R.string.enter_phone_number));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enterPassword() {
        try {
            mPassword.setError(getString(R.string.enter_password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showProgressDialog(String title) {
        ProgressDialogHelper.showProgressDialog(LoginActivity.this, title);
    }

    @Override
    public void dismissProgressDialog() {

        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void enterValidCredentials() {
        ToastHelper.showToastLenShort(LoginActivity.this, getString(R.string.enter_correct_Phone_number_pwd));
    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(LoginActivity.this);
    }

    @Override
    public void navigateToHomePage() {
        //SnackbarHelper.showSnackBar(mPassword,getString(R.string.login_successfully));
        ToastHelper.showToastLenShort(LoginActivity.this,getString(R.string.login_successfully) );
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void serverBusy(String s) {
        ToastHelper.showToastLenShort(LoginActivity.this,s);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (ViewHelper.getString(mMobileNumber).length() > 0 && mLoginPresenter.validateMobileNumber(ViewHelper.getString(mMobileNumber))) {
            mMobileNumber.setErrorEnabled(false);
        }

        if (ViewHelper.getString(mPassword).length() > 0 && mLoginPresenter.validatePassword(ViewHelper.getString(mPassword))) {
            mPassword.setErrorEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onBackPressed() {
        Utility.existanceApplication(LoginActivity.this);
    }
}
