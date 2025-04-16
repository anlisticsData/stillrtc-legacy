package br.com.stilldistribuidora.partners.CallbacksWebViews;

import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jetbrains.annotations.Nullable;

public class CallbackWebView  extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }
}