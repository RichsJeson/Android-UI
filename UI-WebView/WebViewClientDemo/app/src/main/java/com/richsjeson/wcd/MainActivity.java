package com.richsjeson.wcd;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.*;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by richsjeson on 17/2/28.
 */

public class MainActivity extends Activity {

    private WebView wvRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wvRequest= (WebView) findViewById(R.id.wv_request);

        wvRequest.loadUrl("http://www.baidu.com");

        wvRequest.setWebViewClient(new WebViewRequestClient(this,wvRequest));

    }


}
