package com.relateddigital.visilabs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebViewJavaScriptInterface {
    WebViewDialogFragment mWebViewDialogFragment;

    WebViewJavaScriptInterface(WebViewDialogFragment webViewDialogFragment) {
        this.mWebViewDialogFragment = webViewDialogFragment;
    }

    @JavascriptInterface
    public void closeButtonClicked() {
        this.mWebViewDialogFragment.dismiss();
    }

    @JavascriptInterface
    public void copyToClipboard(String couponCode) {
        ClipboardManager clipboard = (ClipboardManager) this.mWebViewDialogFragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        //TODO:
        ClipData clip = ClipData.newPlainText("Coupon Code", couponCode);
        clipboard.setPrimaryClip(clip);

        this.mWebViewDialogFragment.dismiss();
    }
}
