package com.richsjeson.wcd;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by richsjeson on 17/2/28.
 */

public class WebViewClientLoader extends AsyncTaskLoader<String> {

    public WebViewClientLoader(Activity context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Refer","2")
                .url("http://www.test6000.com/test.html")
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    @Override
    public boolean isLoadInBackgroundCanceled() {
        return super.isLoadInBackgroundCanceled();
    }


    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
