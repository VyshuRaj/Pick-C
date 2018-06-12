package com.prts.pickcustomer.otpverify;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.SnackbarHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.home.HomeActivity;

public class OTPVerificationActivity extends AppCompatActivity implements OTPVerifyView {

    TextInputLayout mOTPET;
    OTPVerifyPresenter mFpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mOTPET = findViewById(R.id.eneteredMobNoET);
        mFpPresenter = new OTPVerifympl(OTPVerificationActivity.this, this);

        if (mOTPET.getEditText()!=null) {
            mOTPET.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String otp=ViewHelper.getString(mOTPET);

                    if(otp.length()<4){
                        mOTPET.setError("OTP must be 4 digits");
                    }else
                    {
                        mOTPET.setErrorEnabled(false);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void continueOnClick(View view) {

        String otp=ViewHelper.getString(mOTPET);

        if(otp.isEmpty()){
            mOTPET.setError("Enter otp");
            return;
        }

        if(otp.length()<4){
            mOTPET.setError("OTP must be 4 digits");
            return;
        }

        if(!NetworkHelper.hasNetworkConnection(OTPVerificationActivity.this)){
            SnackbarHelper.noInternet(mOTPET,OTPVerificationActivity.this);
            return;
        }

        ProgressDialogHelper.showProgressDialog(OTPVerificationActivity.this,"Verifying OTP");
        mFpPresenter.verifyOtp(CredentialManager.getMobileNO(OTPVerificationActivity.this),ViewHelper.getString(mOTPET));
    }


    @Override
    public void onBackPressed() {

        ToastHelper.showToastLenShort(OTPVerificationActivity.this, "Please verify OTP");

    }

    @Override
    public void navigateToHomePage() {
        ProgressDialogHelper.dismissDialog();
        ToastHelper.showToastLenShort(OTPVerificationActivity.this, "Registration completed successfully");
        startActivity(new Intent(OTPVerificationActivity.this, HomeActivity.class));
        finish();
    }
}
