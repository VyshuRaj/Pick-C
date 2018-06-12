package com.prts.pickcustomer.otpgeneration;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ViewHelper;

public class OTPGenerationActivity  extends AppCompatActivity implements OTPGenerateView {

    EditText mMobileNumebr;
    OTPGeneratePresenter mFpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpgeneration);
        mMobileNumebr = findViewById(R.id.eneteredMobNoET);
        mFpPresenter = new OTPGeneratempl(OTPGenerationActivity.this, this);
        mMobileNumebr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mFpPresenter.validateMobileNumber(ViewHelper.getString(mMobileNumebr))) {
                    mMobileNumebr.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void continueOnClick(View view) {
        mFpPresenter.getOTP(ViewHelper.getString(mMobileNumebr));
    }

    @Override
    public void enterMobileNumber(String s) {
        // ToastHelper.showToastLenShort(OTPGenerationActivity.this, s);
        mMobileNumebr.setError(s);
    }

    @Override
    public void isNewNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OTPGenerationActivity.this, R.style.AppDialogTheme1);
        builder.setTitle("Verifying phone number");
        builder.setMessage("This phone number is not registered.");
        builder.setCancelable(false);
        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

       final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                alertDialog.dismiss();
                mMobileNumebr.setFocusable(true);
            }
        });
        ToastHelper.showToastLenShort(OTPGenerationActivity.this, "This phone number is not registered");
    }

    @Override
    public void showProgressDialog() {
        ProgressDialogHelper.showProgressDialog(OTPGenerationActivity.this,getString(R.string.verify_number));
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void notAbleToVerifyNumber() {
        ToastHelper.showToastLenShort(OTPGenerationActivity.this, getString(R.string.not_verfy));
    }

    @Override
    public void otpNotGenerated(String s) {
        ToastHelper.showToastLenShort(OTPGenerationActivity.this, s);
    }


}










