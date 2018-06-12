package com.prts.pickcustomer.payment.bycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.SocketManager;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.invoice.InvoiceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prts.pickcustomer.helpers.Constants.rupee;
import static com.prts.pickcustomer.invoice.InvoiceActivity.BOOKING_NO;
import static com.prts.pickcustomer.payment.PaymentActivity.DRIVERID;
import static com.prts.pickcustomer.payment.PaymentKeys.AMOUNT;

public class CashPaymentActivity extends AppCompatActivity implements CashPaymentView {

    private static final String TAG = "CashPaymentActivity";
    private String mBookingNumber;
    private String mPayableAmt;
    private String mDriverId;
    @BindView((R.id.amount_pays))
    TextView mAmountTv;
    @BindView(R.id.booking_num)
    PickCCustomTextVIew mBookingNumberTV;
    CashPaymentPresnter mCashPaymentPresnter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_payment);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mCashPaymentPresnter = new CashPaymentPresenterImpl(CashPaymentActivity.this, this);

        try {
            Intent frintent = getIntent();
            mBookingNumber = frintent.getStringExtra(BOOKING_NO) == null ? "" : frintent.getStringExtra(BOOKING_NO);
            mPayableAmt = frintent.getStringExtra(AMOUNT) == null ? "" : frintent.getStringExtra(AMOUNT);
            Log.e(TAG, "mPayableAmt" + mPayableAmt);
            mDriverId = frintent.getStringExtra(DRIVERID) == null ? "" : frintent.getStringExtra(DRIVERID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBookingNumberTV.setText(String.format("CRN %s", mBookingNumber));
        mAmountTv.setText(String.format("%s %s", rupee, mPayableAmt));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void payNow(View view) {
        mCashPaymentPresnter.payBookingAmount(mBookingNumber, mDriverId);
        view.setEnabled(false);
        view.setOnClickListener(null);
    }

    @Override
    public void noInterNet() {
        ToastHelper.noInternet(CashPaymentActivity.this);
    }

    @Override
    public void showProgressDialog() {
        ProgressDialogHelper.showProgressDialog(CashPaymentActivity.this, "Processing payment");
    }

    @Override
    public void tryAgainMessage() {
        ToastHelper.showToastLenShort(CashPaymentActivity.this, "Server busy try again!");
    }

    @Override
    public void dismissDialog() {

        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void navigateToInvoicePage(String paymentReceived) {

        ToastHelper.showToastLenShort(CashPaymentActivity.this, paymentReceived);
        Intent usrating = new Intent(getApplicationContext(), InvoiceActivity.class);
        usrating.putExtra(DRIVERID, mDriverId);
        Log.e(TAG,"mDriverId "+mDriverId+" BookingNo"+mBookingNumber);
        usrating.putExtra(BOOKING_NO, mBookingNumber);
        usrating.putExtra("SendInvoice", "yes");
        startActivity(usrating);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
