package com.visilabs.survey;

import android.app.Dialog;
import android.os.Build;
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
import com.visilabs.util.VisilabsConstant;

public class WebViewDialogFragment extends DialogFragment {

    public static final String TAG = "WebViewDialogFragment";
    private static final String ARG_PARAM1 = "response";
    private static final String ARG_PARAM2 = "baseUrl";
    private static final String ARG_PARAM3 = "htmlString";

    WebView webView;
    private static WebViewJavaScriptInterface mJavaScriptInterface;
    private String mResponse;
    private String baseUrl = "";
    private String htmlString = "";
    private boolean mIsRotation = false;
    private SurveyCompleteInterface mListener;

    public WebViewDialogFragment (){}

    public static WebViewDialogFragment newInstance(String baseUrl, String htmlString , String response) {
        WebViewDialogFragment fragment = new WebViewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, response);
        args.putString(ARG_PARAM2, baseUrl);
        args.putString(ARG_PARAM3, htmlString);
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
            this.baseUrl = getArguments().getString("baseUrl");
            this.htmlString = getArguments().getString("htmlString");
            mResponse = getArguments().getString("response");
            mJavaScriptInterface = new WebViewJavaScriptInterface(this, mResponse);
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

        // --- DÜZENLEME: Loglamanın doğru çalışması için bu sırayı takip ediyoruz ---

        // 1. WebView Ayarlarını Yap
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= VisilabsConstant.UI_FEATURES_MIN_API) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        // 2. WebChromeClient'ı set et. Bu, console.log'ları yakalayacak.
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                // Logcat'te "WebViewConsole" etiketiyle arama yapabilirsiniz.
                Log.d("WebViewConsole", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });

        // 3. JavaScript Arayüzünü Ekle
        if(mJavaScriptInterface != null) {
            webView.addJavascriptInterface(mJavaScriptInterface, "Android");
        } else {
            Log.e(TAG, "JavaScript Interface null! Köprü kurulamadı.");
        }

        // 4. Veriyi Yükle
        // Native tarafta, WebView'a gönderilen HTML ve JSON'ı logluyoruz.
        Log.d(TAG, "loadDataWithBaseURL çağrılıyor.");
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Response (JSON): " + mResponse);
        // Log.d(TAG, "HTML String: " + htmlString); // HTML çok uzun olabilir, gerekirse açın.

        webView.loadDataWithBaseURL(baseUrl, htmlString, "text/html", "utf-8", "about:blank");

        // webView.reload(); Bu komut genellikle gereksizdir ve yükleme sorunlarına yol açabilir.
        // Yükleme zaten loadDataWithBaseURL ile başlar.

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

    public void setSurveyListeners(SurveyCompleteInterface listener){
        mListener = listener;
    }

}
