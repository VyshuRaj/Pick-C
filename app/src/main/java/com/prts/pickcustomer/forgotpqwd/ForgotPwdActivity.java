package com.prts.pickcustomer.forgotpqwd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPwdActivity extends AppCompatActivity implements FPView, TextWatcher {
    @BindView(R.id.mobile_number_txt_et)
    PickCCustomEditText mOtp;
    @BindView(R.id.password_txt_et)
    PickCCustomEditText mPassword;
    @BindView(R.id.re_enter_password_txt_et)
    PickCCustomEditText mReEnteredPwd;
    private String mMobileNumber;
    private FPPresenter mFpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        ButterKnife.bind(ForgotPwdActivity.this);
        Intent intent = getIntent();
        mMobileNumber = intent.getStringExtra("mobile");
        mFpPresenter = new FPPresneterImpl(ForgotPwdActivity.this, this);
    }

    public void submit(View view) {
        Forgot forgot = new Forgot();
        forgot.setMobileNo(mMobileNumber);
        forgot.setOTP(ViewHelper.getString(mOtp));
        forgot.setNewPassword(ViewHelper.getString(mReEnteredPwd));
        forgot.setOldPassword(ViewHelper.getString(mPassword));

        mFpPresenter.submit(forgot);
    }

    public static void startActivity(Context context, String number) {
        Intent intent = new Intent(context, ForgotPwdActivity.class);
        intent.putExtra("mobile", number);
        context.startActivity(intent);
    }

    @Override
    public void enterOTP(String s) {
        mOtp.setError(s);
    }

    @Override
    public void enterPwd(String s) {
        mPassword.setError(s);
    }


    @Override
    public void enterRePwd(String s) {
        mReEnteredPwd.setError(s);
    }

    @Override
    public void showDialogBox() {
        ProgressDialogHelper.showProgressDialog(ForgotPwdActivity.this,getString(R.string.update));
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void pwdIsNotUpdated() {
        ToastHelper.showToastLenShort(ForgotPwdActivity.this, getString(R.string.pwd_not_updated));
    }

    @Override
    public void navigateToLoginPage() {
        ToastHelper.showToastLenShort(ForgotPwdActivity.this,getString(R.string.pwd_updated_successfully));
        startActivity(new Intent(ForgotPwdActivity.this, LoginActivity.class));
        finish();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (ViewHelper.getString(mOtp).length() > 0 && mFpPresenter.validateOTP(ViewHelper.getString(mOtp))) {
            mOtp.setError(null);
        }

        if (ViewHelper.getString(mPassword).length() > 0 && mFpPresenter.validatePwd(ViewHelper.getString(mPassword))) {
            mPassword.setError(null);
        }

        if (ViewHelper.getString(mReEnteredPwd).length() > 0 && mFpPresenter.checkMatchingPwds(ViewHelper.getString(mPassword), ViewHelper.getString(mReEnteredPwd))) {
            mReEnteredPwd.setError(null);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }
}
