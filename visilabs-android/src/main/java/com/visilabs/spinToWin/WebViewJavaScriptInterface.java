package com.visilabs.spinToWin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebViewJavaScriptInterface {
    WebViewDialogFragment mWebViewDialogFragment;
    private SpinToWinCompleteInterface mListener;

    WebViewJavaScriptInterface(WebViewDialogFragment webViewDialogFragment) {
        this.mWebViewDialogFragment = webViewDialogFragment;
    }

    @JavascriptInterface
    public void closeButtonClicked() {
        this.mWebViewDialogFragment.dismiss();
        mListener.onCompleted();
    }

    @JavascriptInterface
    public void copyToClipboard(String couponCode) {
        ClipboardManager clipboard = (ClipboardManager) this.mWebViewDialogFragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        //TODO:
        ClipData clip = ClipData.newPlainText("Coupon Code", couponCode);
        clipboard.setPrimaryClip(clip);

        this.mWebViewDialogFragment.dismiss();
        mListener.onCompleted();
    }

    public void setSpinToWinCompleteListener(SpinToWinCompleteInterface listener){
        mListener = listener;
    }
}
