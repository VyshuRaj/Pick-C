package com.prts.pickcustomer.payment.online;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.Constants;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.SnackbarHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.Utility;
import com.prts.pickcustomer.invoice.InvoiceActivity;
import com.prts.pickcustomer.payment.PaymentActivity;
import com.prts.pickcustomer.payment.PaymentKeys;
import com.prts.pickcustomer.restapi.RestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.invoice.InvoiceActivity.BOOKING_NO;
import static com.prts.pickcustomer.payment.PaymentActivity.DRIVERID;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.WEB_API_ADDRESS;

public class OnlinePaymentActivity extends AppCompatActivity /*implements Communicator */ {

    private Intent mMainIntent;
    private WebView mMyBrowser;
    int MyDeviceAPI;
    String mBankUrl = "";
    String mEncodingValue;
    private String SUCCESS_URL = "";
    private String CANCEL_URL = "";

    private static final String TAG = "OnlinePaymentActivity";
    private String mBookingNumber = "";
    private String mOrderId;
    private ProgressBar mProgressBar;
    private String mMobileNumber="";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        LogUtils.onLine(OnlinePaymentActivity.this, "OnCreate Called");
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMainIntent = getIntent();
        mMyBrowser = findViewById(R.id.webView);
        WebSettings webSettings = mMyBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        MyDeviceAPI = Build.VERSION.SDK_INT;

        getPaymentDoneStatus();

    }


    private void getPaymentDoneStatus() {

        mProgressBar.setVisibility(View.VISIBLE);

        if (!NetworkHelper.hasNetworkConnection(OnlinePaymentActivity.this)) {
            ToastHelper.noInternet(OnlinePaymentActivity.this);
            return;
        }


        String userAgent = "Mozilla/5.0 (Linux; U; Android-4.0.3; en-us; Galaxy Nexus Build/IML74K) AppleWebKit/535.7 (KHTML, like Gecko) CrMo/16.0.912.75 Mobile Safari/535.7";

        final String amount = mMainIntent.getStringExtra(PaymentKeys.AMOUNT);

        mMobileNumber = CredentialManager.getMobileNO(getApplicationContext());
        mBookingNumber = CredentialManager.getBookingNO(getApplicationContext());
        mOrderId = String.valueOf(Utility.randInt(0, 9999999));

        Online onlineObj = new Online();
        onlineObj.setAccessCode(getString(R.string.access_code));
        onlineObj.setOrderId(mOrderId);
       // AVJE76FB35BJ13EJJB
       // {"Access_code":"AVJX78FE15BT74XJTB","Order_id":"9999999"}

        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", userAgent);

        RestClient.getRestService(BASE_URL).getRSAKey(map, onlineObj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RSAKey>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RSAKey rsaKey) {
                        //mProgressDialog.dismiss();

                        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                            mProgressBar.setVisibility(GONE);
                        }

                        try {
                            if (!rsaKey.getRSAKey().isEmpty()) {
                                LogUtils.onLine(OnlinePaymentActivity.this, "getRSAKey Called");
                                Log.e("TAG", "rsaKey" + new Gson().toJson(rsaKey));
                                StringBuilder vEncVal = new StringBuilder("");
                                vEncVal.append(Utility.addToPostParams(CCAvenueParams.AMOUNT, /*amount*/"1"));
                                vEncVal.append(Utility.addToPostParams(CCAvenueParams.CURRENCY, "INR"));
                                mEncodingValue = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), rsaKey.getRSAKey());
                                setCCAvenueParams();
                            }

                        } catch (Exception e) {
                            Log.e("TAG", "Exce1" + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                            mProgressBar.setVisibility(GONE);
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setCCAvenueParams() {
        mMyBrowser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        mMyBrowser.setWebViewClient(mWebViewClient);

        try {
            StringBuilder params = new StringBuilder();
            params.append(Utility.addToPostParams(CCAvenueParams.ACCESS_CODE, URLEncoder.encode(getString(R.string.access_code),getString(R.string.char_set_8))));
            params.append(Utility.addToPostParams(CCAvenueParams.MERCHANT_ID, URLEncoder.encode(getString(R.string.merchant_id),getString(R.string.char_set_8))));
            params.append(Utility.addToPostParams(CCAvenueParams.ORDER_ID, URLEncoder.encode(mOrderId,getString(R.string.char_set_8))/*mMainIntent.getStringExtra(CCAvenueParams.ORDER_ID)*/));
            SUCCESS_URL = String.format("%sapi/opearation/payment/ccavenue/redirect/%s/%s", WEB_API_ADDRESS,"9291570524" /*mMobileNumber*/, "BK180500324"/*mBookingNumber*/);
            CANCEL_URL = String.format ("%sapi/opearation/payment/ccavenue/cancel/%s", WEB_API_ADDRESS,"9291570524" /*mMobileNumber*/);
            params.append(Utility.addToPostParams(CCAvenueParams.REDIRECT_URL, URLEncoder.encode(SUCCESS_URL,getString(R.string.char_set_8)) /*+ "/" + mBookingNumber*/));
            params.append(Utility.addToPostParams(CCAvenueParams.CANCEL_URL, URLEncoder.encode(CANCEL_URL,getString(R.string.char_set_8)) ));
            params.append(Utility.addToPostParams(CCAvenueParams.ENC_VAL, URLEncoder.encode(mEncodingValue)));
            String vPostParams = params.substring(0, params.length() - 1);

            Log.e(TAG, "vPostParams " + vPostParams);
            try {
                if (Utility.getBytesFromString(vPostParams) != null) {
                    mMyBrowser.postUrl(Constants.TRANS_URL, Utility.getBytesFromString(vPostParams));
                }
            } catch (Exception ignored) {
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            mBankUrl = url;
            Log.e("TAG", "mBankUrl:1:" + mBankUrl);
            LogUtils.onLine(OnlinePaymentActivity.this, "shouldOverrideUrlLoading Called121 " + url);
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBankUrl = request.getUrl().toString();
            }


            Log.e("TAG", "mBankUrl:2:" + mBankUrl);
            /*
            if (SUCCESS_URL.equalsIgnoreCase(url)) {
                notifyDriverAboutPaymentReceived();

            } else if (CANCEL_URL.equalsIgnoreCase(url)) {

                ToastHelper.showToastLenShort(OnlinePaymentActivity.this, "Payment not received");
                Intent intent = new Intent(OnlinePaymentActivity.this, PaymentActivity.class);
                intent.putExtra("bookingNo", mBookingNumber);
                startActivity(intent);
                finish();

            } else*/

            LogUtils.onLine(OnlinePaymentActivity.this, "shouldOverrideUrlLoading request Called122 " + mBankUrl);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            Log.e("TAG", "mBankUrl:3:" + url);
            Log.e("TAG", "onPageFinished: url " + url);
            // LogUtils.appendLog(OnlinePaymentActivity.this, "onPageFinished " + url);
            LogUtils.onLine(OnlinePaymentActivity.this, "onPageFinished Called12 " + mBankUrl);

            if (SUCCESS_URL.equalsIgnoreCase(url)) {
                notifyDriverAboutPaymentReceived();

            } else if (CANCEL_URL.equalsIgnoreCase(url)) {

                ToastHelper.showToastLenShort(OnlinePaymentActivity.this, "Payment not received");
                Intent intent = new Intent(OnlinePaymentActivity.this, PaymentActivity.class);
                intent.putExtra("bookingNo", mBookingNumber);
                startActivity(intent);
                finish();

            } else {

                super.onPageFinished(mMyBrowser, url);

                Log.e(TAG, "mMyBrowser.getUrl() " + mMyBrowser.getUrl());

                if (url.contains("/ccavResponseHandler.jsp")) {
                    mMyBrowser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    mMyBrowser.getUrl();
                    Log.e(TAG, "mMyBrowser.getUrl() " + mMyBrowser.getUrl());
                }

                LogUtils.onLine(OnlinePaymentActivity.this, "mMyBrowser.getUrl() " + mMyBrowser.getUrl());
/*
                if (mLoadCounter < 1) {
                    if (MyDeviceAPI >= 19) {
                        loadCitiBankAuthenticateOption(url);
                        loadWaitingFragment(url);
                    }
                }*/
                mBankUrl = url;
            }

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, "failingUrl" + failingUrl);


            LogUtils.onLine(OnlinePaymentActivity.this, "onReceivedError Called12 " + failingUrl);
            ToastHelper.showToastLenShort(OnlinePaymentActivity.this, "Payment not received");
            Intent intent = new Intent(OnlinePaymentActivity.this, PaymentActivity.class);
            intent.putExtra("bookingNo", mBookingNumber);
            startActivity(intent);
            finish();
        }
    };

    private void notifyDriverAboutPaymentReceived() {
        if (!NetworkHelper.hasNetworkConnection(OnlinePaymentActivity.this)) {
            SnackbarHelper.noInternet(mMyBrowser, OnlinePaymentActivity.this);
            return;
        }

        RestClient.getRestService(BASE_URL).payAmountByCash(getHeaders(OnlinePaymentActivity.this), mBookingNumber, CredentialManager.getDriverId(OnlinePaymentActivity.this), "1102").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {

                        if (s) {
                            ToastHelper.showToastLenShort(OnlinePaymentActivity.this, "Online payment received");
                            Intent usrating = new Intent(getApplicationContext(), InvoiceActivity.class);
                            usrating.putExtra(DRIVERID, CredentialManager.getDriverId(OnlinePaymentActivity.this));
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

                            {
                                ActivityHelper.handleException(OnlinePaymentActivity.this, message);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressWarnings("unused")
    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            try {
                String status = "";
                if (html.contains("Failure")) {
                    status = "Transaction Declined!";
                } else if (html.contains("Success")) {
                    status = "Transaction Successful!";
                } else if (html.contains("Aborted")) {
                    status = "Transaction Cancelled!";
                } else {
                    status = "Status Not Known!";
                }

                LogUtils.onLine(OnlinePaymentActivity.this, "processHTML Called12 " + html + "  \n status " + status);


                Log.e(TAG, "html");
                Log.e(TAG, "status");



            } catch (Exception e) {
                Log.e(TAG, "Exception " + e.getMessage());

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}