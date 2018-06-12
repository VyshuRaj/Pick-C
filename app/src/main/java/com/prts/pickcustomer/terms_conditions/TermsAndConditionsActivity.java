package com.prts.pickcustomer.terms_conditions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.otpverify.OTPVerificationActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static com.prts.pickcustomer.helpers.Constants.TERMS_AND_CONDITIONS;

public class TermsAndConditionsActivity extends AppCompatActivity {
    public static final String URL = "url";
    private static final String TAG = "WebViewAct";
    private boolean mIsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadWebViewWithSettings(TERMS_AND_CONDITIONS);

    }

    WebView webview;

    private void loadWebViewWithSettings(String url) {
        try {
            webview = (WebView) findViewById(R.id.webView);
            CustomWebViewClient customWebViewClient = new CustomWebViewClient();
            webview.setWebViewClient(customWebViewClient);
            WebSettings settings = webview.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setSupportZoom(true);
            settings.setJavaScriptEnabled(true);
            customWebViewClient.shouldOverrideUrlLoading(webview, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        // ProgressDialog progressDialog;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ProgressDialogHelper.showProgressDialog(TermsAndConditionsActivity.this, "Loading");

            new ParseURLAsync().execute(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ProgressDialogHelper.dismissDialog();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class ParseURLAsync extends AsyncTask<String, Void, String> {
        String title2;

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder buffer = new StringBuilder();
            String completeUrl = urls[0];
            try {
                Document doc = Jsoup.connect(completeUrl).get();
                String title = doc.title();
                title2 = title;
                buffer.append("Title: ").append(title).append("\n");
                Elements metaElems = doc.select("h3");
                buffer.append("META DATA\n");

                if (metaElems != null && !metaElems.isEmpty()) {
                    title2 = metaElems.get(0).html();
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!mIsChecked)
            ToastHelper.showToastLenShort(TermsAndConditionsActivity.this, "Please accept the terms & conditions");
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_pirates:
                if (checked) {
                    mIsChecked = true;
                    Intent intent = new Intent(getApplicationContext(), OTPVerificationActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
