package com.prts.pickcustomer.payment.paytm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.SnackbarHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.Utility;
import com.prts.pickcustomer.invoice.InvoiceActivity;
import com.prts.pickcustomer.restapi.RestClient;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.Constants.rupee;
import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.invoice.InvoiceActivity.BOOKING_NO;
import static com.prts.pickcustomer.payment.PaymentActivity.DRIVERID;
import static com.prts.pickcustomer.payment.PaymentKeys.AMOUNT;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.CALLBACK_URL;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.CALLBACK_URL_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.CHANNEL_ID_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.CHANNLE_ID;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.CHECKSUMHASH_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.CUST_ID_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.INDUSTRY_TYPE_ID;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.INDUSTRY_TYPE_ID_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.MERCHANT_ID;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.MID_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.ORDER_ID_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.TXN_AMOUNT_KEY;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.WEBSITE;
import static com.prts.pickcustomer.payment.paytm.PaytmParams.WEBSITE_KEY;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;

public class PaytmActivity extends AppCompatActivity {

    @BindView((R.id.amount_pays))
    TextView mAmountTv;
    @BindView(R.id.booking_num)
    PickCCustomTextVIew mBookingNumberTV;
    private String mBookingNumber, mPayableAmt;
    private String TAG = "PaytmActivity";
    private String mDriverId;
    String mOrderId = "";
    private String mCheckSum;
    private String mCustomerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        ButterKnife.bind(this);
        mOrderId = String.format("ORDER%s", String.valueOf(Utility.randInt(0, 999999999)));

        Log.e(TAG, "Order Id" + mOrderId);
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
        mCustomerId = String.format("CUST%s", "9291570524"/*CredentialManager.getMobileNO(PaytmActivity.this)*/);
        mPayableAmt = "1.00";
        generateCheckSum(0);


    }

    private void generateCheckSum(final int i) {

        if (!NetworkHelper.hasNetworkConnection(PaytmActivity.this)) {
            ToastHelper.noInternet(PaytmActivity.this);
            return;
        }

        Paytm paytm = new Paytm();
        paytm.setCustomerId(mCustomerId);
        paytm.setOrderNo(mOrderId);
        paytm.setAmount(mPayableAmt);
        Log.e("TAG", "Paytm Object " + new Gson().toJson(paytm));

        RestClient.getRestService(BASE_URL).getCheckSum(getHeaders(PaytmActivity.this), paytm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String checksum) {
                        mCheckSum = checksum;
                        ToastHelper.showToastLenShort(PaytmActivity.this, "mCheckSum " + mCheckSum);
                        Log.e("TAG", "checksum " + checksum);
                      //  new AsynTask().execute();

                        if (i > 0)
                            postDataToPaytmServer();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "checksum Throwable " + e.getMessage());
                        mCheckSum = "";
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void notifyDriverAboutPaymentReceived() {

        if (!NetworkHelper.hasNetworkConnection(PaytmActivity.this)) {
            SnackbarHelper.noInternet(mAmountTv, PaytmActivity.this);
            return;
        }

        RestClient.getRestService(BASE_URL).payAmountByCash(getHeaders(PaytmActivity.this), mBookingNumber, null/*CredentialManager.getDriverId(PaytmActivity.this)*/, "1510")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {

                        if (s) {
                            ToastHelper.showToastLenShort(PaytmActivity.this, "Online payment received");
                            Intent usrating = new Intent(getApplicationContext(), InvoiceActivity.class);
                            usrating.putExtra(DRIVERID, mDriverId);
                            usrating.putExtra(BOOKING_NO, mBookingNumber);
                            usrating.putExtra("SendInvoice", "yes");
                            startActivity(usrating);
                            finish();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            String message = e.getMessage();
                            ActivityHelper.handleException(PaytmActivity.this, message);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void payNow(View view) {


        if (!NetworkHelper.hasNetworkConnection(PaytmActivity.this)) {
            ToastHelper.noInternet(PaytmActivity.this);
            return;
        }

        if (mCheckSum == null || mCheckSum.isEmpty()) {
            generateCheckSum(1);
            return;
        }

        postDataToPaytmServer();

    }

    private void postDataToPaytmServer() {

        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(CHANNEL_ID_KEY, CHANNLE_ID);
        paramMap.put(WEBSITE_KEY, WEBSITE);
        paramMap.put(INDUSTRY_TYPE_ID_KEY, INDUSTRY_TYPE_ID);
        paramMap.put(MID_KEY, MERCHANT_ID);

        //variable fields
        paramMap.put(CALLBACK_URL_KEY, String.format("%s%s", CALLBACK_URL, mOrderId));
        paramMap.put(ORDER_ID_KEY, mOrderId);
        paramMap.put(TXN_AMOUNT_KEY, mPayableAmt);
        paramMap.put(CHECKSUMHASH_KEY, mCheckSum);
        paramMap.put(CUST_ID_KEY, mCustomerId);


        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Log.e("LOG", "someUIErrorOccurred " + inErrorMessage);
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {

                  
                        notifyDriverAboutPaymentReceived();

                        // getTransactionStatus();

                        Log.e(TAG, "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.e("LOG", "networkNotAvailable lost");
                        ToastHelper.noInternet(PaytmActivity.this);
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Log.e("LOG", "clientAuthenticationFailed " + inErrorMessage);
                        ToastHelper.showToastLenShort(PaytmActivity.this, "Someting went wrong try again,please tryagain");
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        Log.e("LOG", "onErrorLoadingWebPage " + inErrorMessage);
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Log.e("LOG", "onBackPressedCancelTransaction ");
                        Toast.makeText(PaytmActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.e("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }

   
}
