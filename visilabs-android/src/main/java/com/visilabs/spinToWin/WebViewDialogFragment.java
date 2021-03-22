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
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.visilabs.android.R;

public class WebViewDialogFragment extends DialogFragment {

    public static final String TAG = "WebViewDialogFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "filename";
    private static final String ARG_PARAM2 = "response";

    WebView webView;
    private static WebViewJavaScriptInterface mJavaScriptInterface;
    private String mResponse;
    private String fileName = "";
    private boolean mIsRotation = false;
    private SpinToWinCompleteInterface mListener;
    private SpinToWinCopyToClipboardInterface mCopyToClipboardInterface;

    public WebViewDialogFragment (){}

    public static WebViewDialogFragment newInstance(String filename, String response) {
        WebViewDialogFragment fragment = new WebViewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, filename);
        args.putString(ARG_PARAM2, response);
        mJavaScriptInterface = new WebViewJavaScriptInterface(fragment, response);
        fragment.setArguments(args);
        return fragment;
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
        if(getArguments() != null) {
            this.fileName = getArguments().getString("filename");
            mResponse = getArguments().getString("response");
            mJavaScriptInterface = new WebViewJavaScriptInterface(this, mResponse);
            mJavaScriptInterface.setSpinToWinListeners(mListener, mCopyToClipboardInterface);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsRotation = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!mIsRotation) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
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

    public WebView getWebView() {
        return webView;
    }

    public void setSpinToWinListeners(SpinToWinCompleteInterface listener, SpinToWinCopyToClipboardInterface copyToClipboardInterface){
        mListener = listener;
        mCopyToClipboardInterface = copyToClipboardInterface;
    }
}
