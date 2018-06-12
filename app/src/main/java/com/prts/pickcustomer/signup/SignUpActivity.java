package com.prts.pickcustomer.signup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.login.LoginActivity;
import com.prts.pickcustomer.terms_conditions.TermsAndConditionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpView, TextWatcher {

    SignUpPresenter mSignUpPresenter;
    private static final String TAG = "SignUpActivity";


    @BindView(R.id.full_name_til)
    TextInputLayout mNameTIL;
    @BindView(R.id.mobile_til)
    TextInputLayout mMobileNoTIL;
    @BindView(R.id.email_til)
    TextInputLayout mEmailTIL;
    @BindView(R.id.password_til)
    TextInputLayout mPasswordTIL;
    @BindView(R.id.cofirm_password_til)
    TextInputLayout mConfirmPwdTIL;

    @BindView(R.id.signUpTV)
    PickCCustomTextVIew signUpBtnTV;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mSignUpPresenter = new SignUpPresenterImpl(this, SignUpActivity.this);

        if (mNameTIL.getEditText() != null) {
            mNameTIL.getEditText().addTextChangedListener(this);
        }
        if (mMobileNoTIL.getEditText() != null)
            mMobileNoTIL.getEditText().addTextChangedListener(this);

        if (mEmailTIL.getEditText() != null)
            mEmailTIL.getEditText().addTextChangedListener(this);

        if (mPasswordTIL.getEditText() != null)
            mPasswordTIL.getEditText().addTextChangedListener(this);

        if (mConfirmPwdTIL.getEditText() != null)
            mConfirmPwdTIL.getEditText().addTextChangedListener(this);

    }

    @OnClick(R.id.signUpTV)
    public void signUp(View view) {
        SignUp signUp = new SignUp();
        signUp.setEmail(ViewHelper.getString(mEmailTIL));
        signUp.setMobileNumber(ViewHelper.getString(mMobileNoTIL));
        signUp.setUserName(ViewHelper.getString(mNameTIL));
        signUp.setPassword(ViewHelper.getString(mPasswordTIL));
        signUp.setReEnterPwd(ViewHelper.getString(mConfirmPwdTIL));
        mSignUpPresenter.register(signUp);
    }

    @Override
    public void enterName(String s) {
        mNameTIL.setError(s);
    }

    @Override
    public void enterMobileNumber(String s) {
        mMobileNoTIL.setError(s);
    }


    @Override
    public void enterValidEmail(String s) {
        mEmailTIL.setError(s);
    }

    @Override
    public void enterPassword(String s) {
        mPasswordTIL.setError(s);
    }


    @Override
    public void numberAlreadyExisted() {
        createAlertDialogIfMobNoExists();
        ToastHelper.showToastLenShort(SignUpActivity.this, "Phone number already existed login or try with new number");
    }

    private void createAlertDialogIfMobNoExists() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this, R.style.AppDialogTheme1);
        builder.setTitle("Verifying phone number");
        builder.setMessage("This phone number is already registered.");
        builder.setCancelable(false);

        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                dismissProgressDialog();
                mMobileNoTIL.requestFocus();
            }
        });

    }

    @Override
    public void navigateToTCPage() {
        CredentialManager.setMobileNumber(SignUpActivity.this, ViewHelper.getString(mMobileNoTIL));
        CredentialManager.setPassword(SignUpActivity.this, ViewHelper.getString(mPasswordTIL));

        ToastHelper.showToastLenShort(SignUpActivity.this, "OTP will be generated");
        Intent signIntent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
        startActivity(signIntent);
        finish();
    }

/*

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getStringExtra("GettingBack").equals("gotBack")) {
            final Dialog dialog = new Dialog(SignUpActivity.this, R.style.AppDialogTheme1);
            dialog.setTitle("Enter OTP");
            dialog.setContentView(R.layout.otp_dialog_layout);
            dialog.show();
            final EditText enteredOTP_ET = (EditText) dialog.findViewById(R.id.editText_otp);
            TextView submitBtn = (TextView) dialog.findViewById(R.id.submitBtn);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String enteredOTP = enteredOTP_ET.getText().toString();

                    if (enteredOTP.isEmpty()) {
                        enteredOTP_ET.setError(getString(R.string.enter_4_digit_number));
                    } else {
                        enteredOTP_ET.setError(null);
                       // mSignUpPresenter.verfyOTP(ViewHelper.getString(mMobileNoTIL), enteredOTP);
                    }


                }
            });
        }

    }
*/

    @Override
    public void reEnterPwd(String s) {
        mConfirmPwdTIL.setError(s);
    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(SignUpActivity.this);
    }

    @Override
    public void registrationFailed() {
        ToastHelper.showToastLenShort(SignUpActivity.this, "Registration failed try again");
    }

    @Override
    public void showProgressDialog(String s) {
        ProgressDialogHelper.showProgressDialog(SignUpActivity.this, s);
    }

    @Override
    public void dismissProgressDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void mobileVerificationFailed() {
        ToastHelper.showToastLenShort(SignUpActivity.this, "Phone number verification failed");
    }

    @Override
    public void serverBusy(String s) {
        ToastHelper.showToastLenShort(SignUpActivity.this, s);
    }

    @Override
    public void navigateToHomePage() {
        ToastHelper.showToastLenShort(SignUpActivity.this, "Registration completed successfully");
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (ViewHelper.getString(mNameTIL).length() > 0 && mSignUpPresenter.validateName(ViewHelper.getString(mNameTIL))) {
            mNameTIL.setErrorEnabled(false);
        }

        if (ViewHelper.getString(mMobileNoTIL).length() > 0 && mSignUpPresenter.validateMobileNumber(ViewHelper.getString(mMobileNoTIL))) {
            mMobileNoTIL.setErrorEnabled(false);
        }

        if (ViewHelper.getString(mEmailTIL).length() > 0 && mSignUpPresenter.validateEmail(ViewHelper.getString(mEmailTIL))) {
            mEmailTIL.setErrorEnabled(false);
        }

        if (ViewHelper.getString(mPasswordTIL).length() > 0 && mSignUpPresenter.validatePassword(ViewHelper.getString(mPasswordTIL))) {
            mPasswordTIL.setErrorEnabled(false);
        }

        if (ViewHelper.getString(mConfirmPwdTIL).length() > 0 && mSignUpPresenter.checkMatchingOfPwds(ViewHelper.getString(mConfirmPwdTIL), ViewHelper.getString(mConfirmPwdTIL))) {
            mConfirmPwdTIL.setErrorEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
