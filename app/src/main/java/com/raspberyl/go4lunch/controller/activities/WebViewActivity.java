package com.raspberyl.go4lunch.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.raspberyl.go4lunch.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private String mRestaurantUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mWebView = findViewById(R.id.activity_web_view);
        mRestaurantUrl = getIntent().getStringExtra("RestaurantWebViewUrl");

        // WebView init
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mRestaurantUrl);

    }


}
