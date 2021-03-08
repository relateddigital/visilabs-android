package com.visilabs.spinToWin;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.gson.Gson;
import com.visilabs.spinToWin.model.SpinToWinModel;

public class SpinToWinActivity extends FragmentActivity implements SpinToWinCompleteInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonStr = "";
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if(intent != null && intent.hasExtra("spin-to-win-data")) {
                final SpinToWinModel response = (SpinToWinModel) intent.getSerializableExtra("spin-to-win-data");
                if(response != null) {
                    jsonStr = new Gson().toJson(response);
                }
            }
        }
        if(!jsonStr.equals("")){
            WebViewDialogFragment webViewDialogFragment = new WebViewDialogFragment("spintowin.html", jsonStr);
            webViewDialogFragment.display(getSupportFragmentManager());
            webViewDialogFragment.setSpinToWinCompleteListener(this);
            webViewDialogFragment.getJavaScriptInterface().setSpinToWinCompleteListener(this);
        }
    }

    @Override
    public void onCompleted() {
        finish();
    }
}
