package com.richsjeson.wcd;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.support.v4.app.LoaderManager.enableDebugLogging;

/**
 * Created by richsjeson on 17/2/28.
 */

public class WebViewRequestClient extends WebViewClient implements  LoaderManager.LoaderCallbacks<String> {

    private Activity mContext;
    private static final int LOADER_ID = 1;
    private WebView mWebView;
    public  WebViewRequestClient(Activity context,WebView webView){

        this.mContext=context;
        this.mWebView=webView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new WebViewClientLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data != null) {
            mWebView.loadData(data, "text/html; charset=UTF-8", null);
        }else{
            //跳转至404 失败页面
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest re) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if(re.getRequestHeaders() ==null){
                mContext.getLoaderManager().initLoader(LOADER_ID,null,WebViewRequestClient.this).forceLoad();
            }
        }
        return true;
    }

}
