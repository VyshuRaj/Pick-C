package com.prts.pickcustomer.changepassword;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.prts.pickcustomer.helpers.SnackbarHelper.showSnackBar;

public class ChangePasswordActivity extends AppCompatActivity implements TextWatcher, ChangePwdView, DialogBox {
    private static final String TAG = "ChangePasswordAct";

    @BindView(R.id.old_pwd_til)
    TextInputLayout mOldPasswordEt;
    @BindView(R.id.new_pwd_til)
    TextInputLayout mNewPasswordEt;
    @BindView(R.id.confirm_pwd_til)
    TextInputLayout mRenterNewPasswordEt;

    @BindView(R.id.continueTV)
    PickCCustomTextVIew mContinueTV;
    ChangePwdPresenter mChangePwdPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        mChangePwdPresenter = new ChangePwdImpl(ChangePasswordActivity.this, this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (mOldPasswordEt.getEditText()!=null) {
            mOldPasswordEt.getEditText().addTextChangedListener(this);
        }

        if (mNewPasswordEt.getEditText()!=null) {
            mNewPasswordEt.getEditText().addTextChangedListener(this);
        }

        if (mRenterNewPasswordEt.getEditText()!=null) {
            mRenterNewPasswordEt.getEditText().addTextChangedListener(this);
        }
    }


    @OnClick(R.id.continueTV)
    public void submit(View view) {
        String oldPassword = ViewHelper.getString(mOldPasswordEt);
        String newPassword = ViewHelper.getString(mNewPasswordEt);
        String newRePassword = ViewHelper.getString(mRenterNewPasswordEt);
        mChangePwdPresenter.submitNewPassword(oldPassword, newPassword, newRePassword);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (ViewHelper.getString(mOldPasswordEt).length() > 0 && mChangePwdPresenter.validateOldPwd(ViewHelper.getString(mOldPasswordEt))) {
            mOldPasswordEt.setErrorEnabled(false);
        }

        if (ViewHelper.getString(mNewPasswordEt).length() > 0 && mChangePwdPresenter.validateOldPwd(ViewHelper.getString(mNewPasswordEt))) {
            mNewPasswordEt.setErrorEnabled(false);
        }
        if (ViewHelper.getString(mRenterNewPasswordEt).length() > 0 && mChangePwdPresenter.validateOldPwd(ViewHelper.getString(mRenterNewPasswordEt))) {
            mRenterNewPasswordEt.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void noInterNet() {
        ToastHelper.noInternet(ChangePasswordActivity.this);
    }

    @Override
    public void navigateToProfilePage() {
        showSnackBar(mContinueTV, getString(R.string.pwd_changed));
        onBackPressed();
    }

    @Override
    public void showFailureDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this, R.style.AppDialogTheme1);
        builder.setTitle("Failure");
        builder.setCancelable(false);
        builder.setMessage("Wrong password. Please check your current password and try again");
        builder.setNegativeButton("OK", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public DialogBox getInstance() {
        return this;
    }


    @Override
    public void enterOldPassword(String string) {
        mOldPasswordEt.setError(string);
    }

    @Override
    public void enterNewPwd(String string) {
        mNewPasswordEt.setError(string);
    }

    @Override
    public void enterConfrimPwd(String string) {
        mRenterNewPasswordEt.setError(string);
    }


    @Override
    public void showDialog(String title) {
        ProgressDialogHelper.showProgressDialog(ChangePasswordActivity.this, title);

    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }
}
