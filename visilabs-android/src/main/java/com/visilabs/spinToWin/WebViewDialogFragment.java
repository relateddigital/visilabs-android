package com.visilabs.spinToWin;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.visilabs.android.R;

//https://android--code.blogspot.com/2015/05/android-webview-local-html-javascript.html
//https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
//TODO: animasyon gerekiyorsa ekle
public class WebViewDialogFragment extends DialogFragment {

    public static final String TAG = "WebViewDialogFragment";
    WebView webView;
    private final WebViewJavaScriptInterface mJavaScriptInterface;
    private SpinToWinCompleteInterface mListener;

    private String fileName = "";

    WebViewDialogFragment(String fileName){
        this.fileName = fileName;
        mJavaScriptInterface = new WebViewJavaScriptInterface(this);
    }

    public WebViewDialogFragment display(FragmentManager fragmentManager) {
        this.show(fragmentManager, TAG);
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener.onCompleted();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_web_view, container, false);
        webView = view.findViewById(R.id.webview);
        webView.setWebChromeClient(getWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(mJavaScriptInterface, "Android");
        String folderPath = "file:android_asset/";
        String fileName = this.fileName;
        String file = folderPath + fileName;
        webView.loadUrl(file);
        webView.reload();
        return view;
    }

    private WebChromeClient getWebViewClient(){
        WebChromeClient client = new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );
                return true;
            }
        };
        return client;
    }

    public WebViewJavaScriptInterface getJavaScriptInterface(){
        return mJavaScriptInterface;
    }

    public void setSpinToWinCompleteListener(SpinToWinCompleteInterface listener){
        mListener = listener;
    }

}
