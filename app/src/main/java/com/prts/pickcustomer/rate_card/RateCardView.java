package com.prts.pickcustomer.rate_card;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;

import static android.view.View.GONE;
import static com.prts.pickcustomer.helpers.Constants.RATE_CARD_URL;

public class RateCardView extends AppCompatActivity {
    String URL = "http://pickcargo.in/RateCard/mobile";
    WebView webView;
    private static final String TAG ="RateCardView" ;
    private RelativeLayout mNoDataTV;
  //  ProgressDialog progressDialog;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_card_view);
        webView = (WebView) findViewById(R.id.webview);
        mNoDataTV = findViewById(R.id.main_layout);
        mNoDataTV.setVisibility(GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (!NetworkHelper.hasNetworkConnection(RateCardView.this)) {
            ToastHelper.noInternet(RateCardView.this);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
               /* if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(50);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.loadUrl(RATE_CARD_URL);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }
}
