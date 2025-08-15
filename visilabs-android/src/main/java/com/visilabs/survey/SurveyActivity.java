package com.visilabs.survey;

import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.api.JSApiClient;
import com.visilabs.api.VisilabsApiMethods;
import com.visilabs.survey.WebViewDialogFragment;
import com.visilabs.survey.model.ExtendedProps;
import com.visilabs.survey.model.SurveyModel;
import com.visilabs.util.ActivityUtils;
import com.visilabs.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends FragmentActivity implements SurveyCompleteInterface
{
    private static final String LOG_TAG = "survey";

    private String jsonStr = "";
    private SurveyModel response;
    private FragmentActivity activity = null;
    private SurveyCompleteInterface completeInterface = null;
    private String surveyJsStr = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        completeInterface = this;

        VisilabsApiMethods jsApi = JSApiClient.getClient(Visilabs.CallAPI().getRequestTimeoutSeconds()).create(VisilabsApiMethods.class);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", Visilabs.getUserAgent());
        Call<ResponseBody> call = jsApi.getSurveyJsFile(headers);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseJs) {
                if (responseJs.isSuccessful()) {
                    Log.i(LOG_TAG, "Getting survey.js is successful!");
                    try {
                        surveyJsStr = responseJs.body().string();
                        if (surveyJsStr.isEmpty()) {
                            Log.e(LOG_TAG, "Getting survey.js failed!");
                            finish();
                        } else {
                            if (savedInstanceState != null) {
                                jsonStr = savedInstanceState.getString("survey-json-str", "");
                            } else {
                                Intent intent = getIntent();
                                if (intent != null && intent.hasExtra("survey-data")) {
                                    response = (SurveyModel) intent.getSerializableExtra("survey-data");
                                    if (response != null) {
                                        jsonStr = new Gson().toJson(response);
                                    } else {
                                        Log.e(LOG_TAG, "Could not get the survey data properly!");
                                        finish();
                                    }
                                } else {
                                    Log.e(LOG_TAG, "Could not get the survey data properly!");
                                    finish();
                                }

                                if (jsonStr != null && !jsonStr.equals("")) {
                                    ArrayList<String> res = AppUtils.createSurveyFiles(activity, jsonStr, surveyJsStr);
                                    if(res == null) {
                                        Log.e(LOG_TAG, "Could not get the survey data properly!");
                                        finish();
                                    } else {
                                        com.visilabs.survey.WebViewDialogFragment webViewDialogFragment = WebViewDialogFragment.newInstance(res.get(0), res.get(1), res.get(2));
                                        webViewDialogFragment.setSurveyListeners(completeInterface);
                                        webViewDialogFragment.display(getSupportFragmentManager());
                                    }
                                } else {
                                    Log.e(LOG_TAG, "Could not get the survey data properly!");
                                    finish();
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Getting survey.js failed! - " + e.getMessage());
                        finish();
                    }
                } else {
                    Log.e(LOG_TAG, "Getting survey.js failed!");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "Getting survey.js failed! - " + t.getMessage());
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("survey-json-str", jsonStr);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCompleted() {
        finish();
    }

    private void sendDeeplinkToApp(String deeplink) {
        Intent intent = new Intent();
        intent.setAction("InAppLink");
        intent.putExtra("link", deeplink);
        sendBroadcast(intent);
        Log.i("LOG_TAG", "Link sent successfully!");
    }


}