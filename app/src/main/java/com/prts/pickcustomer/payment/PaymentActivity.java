package com.prts.pickcustomer.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.Utility;
import com.prts.pickcustomer.payment.bycash.CashPaymentActivity;
import com.prts.pickcustomer.payment.online.OnlinePaymentActivity;
import com.prts.pickcustomer.payment.paytm.PaytmActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends AppCompatActivity implements PaymentView {

    @BindView(R.id.b_number)
    PickCCustomTextVIew mBookingNumberTV;
    @BindView(R.id.amount)
    TextView mAmountTV;
    public static final String BOOKING_NO = "bookingNo";//bookingNo
    public static final String AMOUNT = "amount";
    public static final String DRIVERID = "driver";
    private static final String TAG = "Payment";
    private String mBookingNumber;
    private String amountToPay;
    private String driverId;
    private AmountResponse mAmountResponse;
    private PaymentPresenter mPaymentPresenter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        try {

            PickCCustomTextVIew textView = findViewById(R.id.paytm_button);
            String text1 = "<font size='20' color='#213367'>PAY</font><font size='20' color='#00b7eb'>TM</font>";
            textView.setText(Html.fromHtml(text1));
            textView.setTextSize(18.0f);

            mBookingNumber = getIntent().getStringExtra(BOOKING_NO);
            amountToPay = getIntent().getStringExtra(AMOUNT);
            driverId = getIntent().getStringExtra(DRIVERID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mBookingNumber != null && mBookingNumber.length() > 0) {
            mBookingNumberTV.setText("CRN: " + mBookingNumber);
        } else {
            mBookingNumberTV.setText("CRN: " + "");
        }

        try {
            Log.e(TAG, "Payment Activity bookingNo " + mBookingNumber);
            mPaymentPresenter = new PaymentPresenterImpl(PaymentActivity.this, this);
            mPaymentPresenter.getAmountOfCurrentBooking(mBookingNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.cash_button)
    public void cashPayment(View view) {
        CredentialManager.setBookingNo(PaymentActivity.this, mBookingNumber);
        Intent casInt = new Intent(getApplicationContext(), CashPaymentActivity.class);
        casInt.putExtra(BOOKING_NO, mBookingNumber);
        casInt.putExtra(AMOUNT, amountToPay);
        casInt.putExtra(DRIVERID, driverId);
        startActivity(casInt);

    }

    @OnClick(R.id.online_button)
    public void onlinePayment(View view) {
        Integer randomNum = Utility.randInt(0, 9999999);
        Log.e(TAG, "randomNum" + randomNum);
        Intent intent = new Intent(getApplicationContext(), OnlinePaymentActivity.class);
        intent.putExtra(PaymentKeys.AMOUNT, Utility.isNull(amountToPay));
        intent.putExtra(PaymentKeys.BOOKING_NUBMER, Utility.isNull(mBookingNumber));
        startActivity(intent);
    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(PaymentActivity.this);
    }

    @Override
    public void setAmountOfCurrentBooking(AmountResponse amountResponse) {
        try {
            mAmountResponse = amountResponse;
            amountToPay = amountResponse.getTotalAmount() == null ? "" : amountResponse.getTotalAmount();
            mAmountTV.setText(String.format("₹ %s", amountToPay));
            driverId = amountResponse.getDriverID();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
        }
    }

    @Override
    public void showDialog() {
        try {
            ProgressDialogHelper.showProgressDialog(PaymentActivity.this, "Loading...");
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
        }
    }

    @Override
    public void dismissDialog() {
        try {
            ProgressDialogHelper.dismissDialog();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
        }

    }

    @Override
    public void paymentAmtNotAvailable(String s) {
        ToastHelper.showToastLenShort(PaymentActivity.this, s);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (mAmountResponse != null) {
                mAmountTV.setText(String.format("₹ %s", mAmountResponse.getTotalAmount()));
                driverId = mAmountResponse.getDriverID();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPaymentPresenter = null;
    }

    @Override
    public void onBackPressed() {

    }

    public void paytmNow(View view) {
        Intent intent = new Intent(getApplicationContext(), PaytmActivity.class);
        intent.putExtra(PaymentKeys.AMOUNT, Utility.isNull(amountToPay));
        intent.putExtra(PaymentKeys.BOOKING_NUBMER, Utility.isNull(mBookingNumber));
        startActivity(intent);
    }
}
