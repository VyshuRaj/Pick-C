package com.prts.pickcustomer.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.changepassword.ChangePasswordActivity;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ValidationHelper;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.login.Customer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements ProfileView, TextWatcher, DialogBox {

    ProfileViewPresenter mProfileViewPresenter;
    private static final String TAG = "ProfileActivity";
    @BindView(R.id.name_txt_et)
    PickCCustomEditText nameET;
    @BindView(R.id.email_txt_et)
    PickCCustomEditText eMailIDET;
    @BindView(R.id.password_txt_et)
    PickCCustomTextVIew passwordTV;
    @BindView(R.id.mobile_number_txt_et)
    PickCCustomTextVIew mobileNoTV;
    String name, emailID, mobileNo;
    @BindView(R.id.dummyView_name)
    View nameDummyView;
    @BindView(R.id.dummyView_email)
    View emailDummyView;
    private ProgressDialog mProgressDialog;
    private Dialog mDialogsCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        try {
            mProfileViewPresenter = new ProfileViewPresenterImpl(ProfileActivity.this, this);
            nameDummyView.setVisibility(View.VISIBLE);
            emailDummyView.setVisibility(View.VISIBLE);
            passwordTV.setTextSize(16);

            if (NetworkHelper.hasNetworkConnection(ProfileActivity.this)) {
                mProfileViewPresenter.getUserDataFromServer(CredentialManager.getMobileNO(ProfileActivity.this));
            } else {
                ToastHelper.noInternet(ProfileActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //button click
    public void updateUserData(View view) {
        name = ViewHelper.getString(nameET);
        mobileNo = ViewHelper.getString(mobileNoTV);
        emailID = ViewHelper.getString(eMailIDET);

        if (name.isEmpty()) {
            ToastHelper.showToastLenShort(ProfileActivity.this, "Please enter the name");
            return;
        }

        if (name.contains(" ")) {
            int maxIndex = name.length() - 1;
            int spaceIndex = name.indexOf(" ");
            while (spaceIndex < maxIndex) {

                if ((name.charAt(spaceIndex + 1) + "").equals(" ")) {
                    ToastHelper.showToastLenShort(ProfileActivity.this, "Multiple Spaces not Allowed");
                    return;
                } else {
                    spaceIndex = name.indexOf(" ", spaceIndex + 1);
                }
            }
        }

        if (mobileNo.isEmpty()) {
            ToastHelper.showToastLenShort(ProfileActivity.this, "Please enter mobile no");
            return;
        }

        if (mobileNo.length() != 10) {
            ToastHelper.showToastLenShort(ProfileActivity.this, "Enter 10 digit mobile no");
            return;
        }


        if (!emailID.isEmpty()) {

            if (!ValidationHelper.isValidEmail(emailID)) {
                ToastHelper.showToastLenShort(ProfileActivity.this, "Enter valid email");
                return;
            }
        }

        mProfileViewPresenter.updateUserData(name, mobileNo, emailID);
    }

    public void onCLickEtIv(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (view.getId()) {
            case R.id.nameET_IV:
                nameET.requestFocus();
                nameET.setFocusableInTouchMode(true);

                if (imm != null) {
                    imm.showSoftInput(nameET, InputMethodManager.SHOW_FORCED);
                }
                break;
            case R.id.email_et_iv:
                eMailIDET.requestFocus();
                eMailIDET.setFocusableInTouchMode(true);
                if (imm != null) {
                    imm.showSoftInput(eMailIDET, InputMethodManager.SHOW_FORCED);
                }
                break;
            case R.id.password_et_iv:
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
                break;
        }
    }

    public void dummyOnClickLL(View view) {
    }

    @Override
    public void showInternet() {
        ToastHelper.noInternet(ProfileActivity.this);

    }

    private void showAskPwdDialog() {
        try {
            mDialogsCall = new Dialog(ProfileActivity.this);
            mDialogsCall.setContentView(R.layout.activity_enter_your_password);
            mDialogsCall.setCancelable(false);
            mDialogsCall.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogsCall.show();

            PickCCustomTextVIew closeIv = (PickCCustomTextVIew) mDialogsCall.findViewById(R.id.enter_pwd_txt);
            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogsCall.dismiss();
                }
            });

            PickCCustomTextVIew submit = (PickCCustomTextVIew) mDialogsCall.findViewById(R.id.submitTV);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickCCustomEditText old_password_txt_et = (PickCCustomEditText) mDialogsCall.findViewById(R.id.old_password_txt_et);
                    String password = old_password_txt_et.getText().toString();

                    if (password.isEmpty()) {
                        ToastHelper.showToastLenShort(ProfileActivity.this, "Enter password");
                        return;
                    }


                    mProfileViewPresenter.validatePassword(password, mobileNo, name, emailID);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setDataToViews(Customer customer) {

        nameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameDummyView.setVisibility((hasFocus) ? View.GONE : View.VISIBLE);
            }
        });
        eMailIDET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailDummyView.setVisibility((hasFocus) ? View.GONE : View.VISIBLE);
            }
        });
        mobileNoTV.setText(customer.getMobileNo());
        nameET.setText(customer.getName());
        eMailIDET.setText(customer.getEmailID());
        nameET.addTextChangedListener(this);
        mobileNoTV.addTextChangedListener(this);
        eMailIDET.addTextChangedListener(this);
        passwordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
            }
        });

    }

    @Override
    public void enterName(String s) {
        ToastHelper.showToastLenShort(ProfileActivity.this, s);
    }

    @Override
    public void checkMultipleSpacesInName(String s) {
        ToastHelper.showToastLenShort(ProfileActivity.this, s);
    }

    @Override
    public void enterValidMail(String s) {

        ToastHelper.showToastLenShort(ProfileActivity.this, s);
    }

    @Override
    public DialogBox getDialogBox() {
        return this;
    }

    @Override
    public void errorMessage(String s) {
        ToastHelper.showToastLenShort(ProfileActivity.this, s);

    }

    @Override
    public void askPassworddToUpdateUserData() {
        showAskPwdDialog();
    }

    @Override
    public void unableToProcessRequest(String s) {
        ToastHelper.showToastLenShort(ProfileActivity.this, s);

        if (mDialogsCall != null) {
            mDialogsCall.cancel();
            mDialogsCall = null;
        }
    }


    @Override
    public void updateSuccessfully(String s) {
        CredentialManager.setEmailId(ProfileActivity.this, emailID);
        ToastHelper.showToastLenShort(ProfileActivity.this, s);
    }

    @Override
    public void navigateToHomePage() {
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void enterMobileNumber(String string) {
        ToastHelper.showToastLenShort(ProfileActivity.this, string);
    }

    @Override
    public void enterValidEmail(String string) {
        ToastHelper.showToastLenShort(ProfileActivity.this, string);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void showDialog(String title) {
        ProgressDialogHelper.showProgressDialog(ProfileActivity.this, title);
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }
/*
    @Override
    public void onBackPressed() {

        if (mDialogsCall != null && mDialogsCall.isShowing()) {
            mDialogsCall.cancel();
            mDialogsCall.dismiss();

        } else {
            super.onBackPressed();
        }

    }*/
}
