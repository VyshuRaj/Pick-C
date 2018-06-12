package com.prts.pickcustomer.queries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ViewHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;

public class SendingQueriesToPickCActivity extends AppCompatActivity implements TextWatcher, SendQueryView {

    @BindView(R.id.mobile_et)
    PickCCustomEditText mobile_et;
    @BindView(R.id.queryBox)
    EditText queryBox;
    @BindView(R.id.name_et)
    PickCCustomEditText name_et;
    @BindView(R.id.email_et)
    PickCCustomEditText email_et;
    @BindView(R.id.subj_et)
    PickCCustomEditText subj_et;
    private static final String TAG = "SendingQueriesToPickc";
    @BindView(R.id.send_queries)
    Button send_queries;
    SendQueryPresenter mSendQueryPresenter;
    ProgressDialogHelper mProgressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_queries_to_pick_c);
        ButterKnife.bind(this);
        String mobileno = CredentialManager.getMobileNO(SendingQueriesToPickCActivity.this);
        mobile_et.setText(mobileno);
        mSendQueryPresenter = new SendingQueryPreseneterImpl(SendingQueriesToPickCActivity.this, this);
    }

    @OnClick(R.id.send_queries)
    public void sendQuery(View view) {
        sendEmailToPickCargo();
    }

    private void sendEmailToPickCargo() {
        mobile_et.addTextChangedListener(this);
        subj_et.addTextChangedListener(this);
        email_et.addTextChangedListener(this);
        name_et.addTextChangedListener(this);

        Query qu = new Query();
        qu.setName(ViewHelper.getString(name_et));
        qu.setMobile(ViewHelper.getString(mobile_et));
        qu.setEmail(ViewHelper.getString(email_et));
        qu.setSubject(ViewHelper.getString(subj_et));
        qu.setQuery(ViewHelper.getString(queryBox));
        qu.setContactUs("ContactUs");
        mSendQueryPresenter.validateTheFields(qu);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String mobile = ViewHelper.getString(mobile_et);

        if (mobile.length() > 0 && !mSendQueryPresenter.isValidMobileNumber(mobile))
            mobile_et.setError(getString(R.string.enter_valid_Phone));

        String email = ViewHelper.getString(email_et);
        if (email.length() > 0 && !mSendQueryPresenter.isValidEmailCheck(email))
            email_et.setError(getString(R.string.valid_email));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(SendingQueriesToPickCActivity.this);
    }

    @Override
    public void enterName(String name) {
        ToastHelper.showToastLenShort(SendingQueriesToPickCActivity.this, name);
        name_et.setError(name);
    }

    @Override
    public void enterEmail(String s) {
        ToastHelper.showToastLenShort(SendingQueriesToPickCActivity.this, s);
        email_et.setError(s);
    }

    @Override
    public void enterMobileNumber(String s) {
        ToastHelper.showToastLenShort(SendingQueriesToPickCActivity.this, s);
        mobile_et.setError(s);
    }

    @Override
    public void enterSuject(String s) {
        ToastHelper.showToastLenShort(SendingQueriesToPickCActivity.this, s);
        subj_et.setError(s);
    }

    @Override
    public void enterQuery(String s) {
        ToastHelper.showToastLenShort(SendingQueriesToPickCActivity.this, s);
        queryBox.setError(s);
    }

    @Override
    public void showProgressDialog() {
        ProgressDialogHelper.showProgressDialog(SendingQueriesToPickCActivity.this, "Sending Email");

    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void tryAgain(String string) {
        ToastHelper.showToastLenShort(SendingQueriesToPickCActivity.this, string);
    }

    @Override
    public void getToPreviousPage() {
        finish();
    }

    @Override
    public void proceedFurther() {
        showProgressDialog();

    }


}
