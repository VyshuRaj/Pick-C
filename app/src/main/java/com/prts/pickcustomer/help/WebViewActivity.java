package com.prts.pickcustomer.help;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Locale;

import static android.view.View.GONE;

public class WebViewActivity extends AppCompatActivity {
    public static final String URL = "url";
    public static final String TITLE = "title";

    private static final String TAG = "WebViewAct";
    private WebView webview;
    RelativeLayout mNoDataTV;

    private ProgressBar mProgressBar;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        String url = intent.getStringExtra(URL);
        mTitle=intent.getStringExtra(TITLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTitle);
        }

        mNoDataTV = findViewById(R.id.main_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mNoDataTV.setVisibility(GONE);

        if (!NetworkHelper.hasNetworkConnection(WebViewActivity.this)) {
            ToastHelper.noInternet(WebViewActivity.this);
            return;
        }


        loadWebViewWithSettings(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebViewWithSettings(String url) {
        try {
            webview = (WebView) findViewById(R.id.webView);
            CustomWebViewClient customWebViewClient = new CustomWebViewClient();
            webview.setWebViewClient(customWebViewClient);
            WebSettings settings = webview.getSettings();
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            settings.setSupportZoom(true);
            settings.setJavaScriptEnabled(true);
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

            if (VERSION.SDK_INT >= 19) {
                webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            customWebViewClient.shouldOverrideUrlLoading(webview, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            try {

                mProgressBar.setVisibility(View.VISIBLE);
                new ParseURLAsync().execute(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.toLowerCase(Locale.getDefault()).startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                }
                return true;
            } else if (url.toLowerCase(Locale.getDefault()).startsWith("mailto:")) {
                String mailTo = url.replace("mailto:", "");
                mailTo = mailTo.replaceAll("%40", "@");
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));
                return true;
            } else {
                try {
                    view.loadUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e(TAG, "url " + url);

            if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(GONE);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);


            if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(GONE);
            }

            view.loadUrl("about:blank");
            mNoDataTV.setVisibility(View.VISIBLE);

        }


    }


    @SuppressLint("StaticFieldLeak")
    private class ParseURLAsync extends AsyncTask<String, Void, String> {
        String title2;

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder buffer = new StringBuilder();
            String completeUrl = urls[0];
            try {
                Log.d("JSwa", "Connecting to [" + completeUrl + "]");
                Document doc = Jsoup.connect(completeUrl).get();
                Log.d("JSwa", "Connected to [" + completeUrl + "]");
// Get document (HTML page) title
                String title = doc.title();

                Log.d("JSwA", "Title [" + title + "]");
                title2 = title;
                buffer.append("Title: ").append(title).append("\n");

// Get meta info
                Elements metaElems = doc.select("h3");

                Log.d(TAG, "doInBackground: " + metaElems);
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

            try {
                getSupportActionBar().setTitle(mTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    try {
                        if (webview.canGoBack()) {
                            webview.goBack();
                        } else {
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
