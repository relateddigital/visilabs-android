package com.visilabs.spinToWin;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

public class SpinToWinActivity extends FragmentActivity implements SpinToWinCompleteInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebViewDialogFragment webViewDialogFragment = new WebViewDialogFragment("spintowin.html");
        webViewDialogFragment.display(getSupportFragmentManager());
        webViewDialogFragment.getJavaScriptInterface().setSpinToWinCompleteListener(this);
    }

    @Override
    public void onCompleted() {
        finish();
    }
}
