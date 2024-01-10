package com.visilabs.inApp.customactions;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.visilabs.android.R;

import java.net.URI;
import java.net.URISyntaxException;

/*public class CustomActionFragment extends Fragment {

    private static final String LOG_TAG = "CustomActionNotification";
    private static final String ARG_PARAM1 = "dataKey";

    private CustomActions response;
    private CustomActionsExtendedProps mExtendedProps;
    private String position;
    private Integer width, height;
    private String combined, customActionJsStr, jsonStr;
    private String combinedHtml = "";
    private String jsCode, htmlContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        response = savedInstanceState != null ?
                (CustomActions) savedInstanceState.getSerializable("customactions") :
                (CustomActions) requireArguments().getSerializable(ARG_PARAM1);

        if (response == null) {
            Log.e(LOG_TAG, "The data could not get properly!");
            endFragment();
        } else {
            try {
                mExtendedProps = new Gson().fromJson(
                        new URI(response.getActiondata().getExtendedProps()).getPath(),
                        CustomActionsExtendedProps.class
                );
            } catch (URISyntaxException e) {
                e.printStackTrace();
                endFragment();
            } catch (Exception e) {
                e.printStackTrace();
                endFragment();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        ImageView closeButton = view.findViewById(R.id.closeButton);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        if (!response.getActiondata().getJavascript().isEmpty()) {
            if (!response.getActiondata().getContent().isEmpty()) {
                jsCode = response.getActiondata().getJavascript();
                htmlContent = response.getActiondata().getContent();
                combineHtmlCode(jsCode, htmlContent);
            } else {
                Log.e(LOG_TAG, "html could not get properly!");
            }
        } else {
            Log.e(LOG_TAG, "javascript could not get properly!");
        }

        webView.loadDataWithBaseURL(null, combinedHtml, "text/html", "utf-8", null);

        if (mExtendedProps != null) {
            position = mExtendedProps.getPosition();
            height = mExtendedProps.getHeight() != null ? mExtendedProps.getHeight() : 100;
            width = mExtendedProps.getWidth() != null ? mExtendedProps.getWidth() : 100;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // ... (Devamı aynı şekilde çevrilebilir)

        webView.setLayoutParams(params);
        closeButton.setLayoutParams(closeParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(jsCode, result -> {

            });
        }

        webView.setWebViewClient(new WebViewClient());
        closeButton.setOnClickListener(v -> endFragment());
    }

    private void endFragment() {
        if (getActivity() != null) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .remove(this)
                    .commit();
        }
    }

    private void combineHtmlCode(String jsCode, String htmlCode) {
        combinedHtml = String.format("<html><head><script>%s</script></head><body>%s</body></html>",
                jsCode, htmlCode);
    }

    // Diğer metotlar ve iç sınıflar aynı şekilde çevrilebilir
} */