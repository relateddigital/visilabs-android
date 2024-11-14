package com.visilabs.spinToWin;

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
import com.visilabs.spinToWin.model.ExtendedProps;
import com.visilabs.spinToWin.model.SpinToWinModel;
import com.visilabs.util.ActivityUtils;
import com.visilabs.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinToWinActivity extends FragmentActivity implements SpinToWinCompleteInterface,
        SpinToWinCopyToClipboardInterface, SpinToWinShowCodeInterface {
    private static final String LOG_TAG = "SpinToWin";

    private String jsonStr = "";
    private SpinToWinModel response;
    private String spinToWinPromotionCode = "";
    private String sliceLink = "";
    private FragmentActivity activity = null;
    private SpinToWinCompleteInterface completeInterface = null;
    private SpinToWinCopyToClipboardInterface copyToClipboardInterface = null;
    private SpinToWinShowCodeInterface showCodeInterface = null;
    private String spinToWinJsStr = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        completeInterface = this;
        copyToClipboardInterface = this;
        showCodeInterface = this;

        VisilabsApiMethods jsApi = JSApiClient.getClient(Visilabs.CallAPI().getRequestTimeoutSeconds()).create(VisilabsApiMethods.class);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", Visilabs.getUserAgent());
        Call<ResponseBody> call = jsApi.getSpinToWinJsFile(headers);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseJs) {
                if (responseJs.isSuccessful()) {
                    Log.i(LOG_TAG, "Getting spintowin.js is successful!");
                    try {
                        spinToWinJsStr = responseJs.body().string();
                        if (spinToWinJsStr.isEmpty()) {
                            Log.e(LOG_TAG, "Getting spintowin.js failed!");
                            finish();
                        } else {
                            if (savedInstanceState != null) {
                                jsonStr = savedInstanceState.getString("spin-to-win-json-str", "");
                            } else {
                                Intent intent = getIntent();
                                if (intent != null && intent.hasExtra("spin-to-win-data")) {
                                    response = (SpinToWinModel) intent.getSerializableExtra("spin-to-win-data");
                                    if (response != null) {
                                        jsonStr = new Gson().toJson(response);
                                    } else {
                                        Log.e(LOG_TAG, "Could not get the spin-to-win data properly!");
                                        finish();
                                    }
                                } else {
                                    Log.e(LOG_TAG, "Could not get the spin-to-win data properly!");
                                    finish();
                                }
                            }

                            if (jsonStr != null && !jsonStr.equals("")) {
                                ArrayList<String> res = AppUtils.createSpinToWinCustomFontFiles(activity, jsonStr, spinToWinJsStr);
                                if(res == null) {
                                    Log.e(LOG_TAG, "Could not get the spin-to-win data properly!");
                                    finish();
                                } else {
                                    WebViewDialogFragment webViewDialogFragment = WebViewDialogFragment.newInstance(res.get(0), res.get(1), res.get(2));
                                    webViewDialogFragment.setSpinToWinListeners(completeInterface, copyToClipboardInterface, showCodeInterface);
                                    webViewDialogFragment.display(getSupportFragmentManager());
                                }
                            } else {
                                Log.e(LOG_TAG, "Could not get the spin-to-win data properly!");
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Getting spintowin.js failed! - " + e.getMessage());
                        finish();
                    }
                } else {
                    Log.e(LOG_TAG, "Getting spintowin.js failed!");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(LOG_TAG, "Getting spintowin.js failed! - " + t.getMessage());
                    finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("spin-to-win-json-str", jsonStr);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(spinToWinPromotionCode != null && !spinToWinPromotionCode.isEmpty()) {
            try {
                ExtendedProps extendedProps = new Gson().fromJson(new java.net.URI(response.getActiondata().
                        getExtendedProps()).getPath(), ExtendedProps.class);

                if(extendedProps.getPromocodeBannerButtonLabel() != null &&
                        !extendedProps.getPromocodeBannerButtonLabel().isEmpty()) {
                    if(ActivityUtils.getParentActivity() != null) {
                        SpinToWinCodeBannerFragment spinToWinCodeBannerFragment =
                                SpinToWinCodeBannerFragment.newInstance(extendedProps, spinToWinPromotionCode);

                        spinToWinCodeBannerFragment.setRetainInstance(true);

                        FragmentTransaction transaction = ActivityUtils.getParentActivity().getFragmentManager().beginTransaction();
                        transaction.add(android.R.id.content, spinToWinCodeBannerFragment);
                        transaction.commit();
                        ActivityUtils.setParentActivity(null);
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "SpinToWinCodeBanner : " + e.getMessage());
            }
        }
        if (!sliceLink.isEmpty()) {
            Uri uri;
            try {
                uri = Uri.parse(sliceLink);
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(viewIntent);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Can't parse notification URI, will not take any action", e);
            }
        }
    }

    @Override
    public void onCompleted() {
        finish();
    }

    @Override
    public void copyToClipboard(String couponCode, String link) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Coupon Code", couponCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();

        if(link != null && !link.isEmpty()) {
            sliceLink = link;
            sendDeeplinkToApp(link);
        }

        finish();
    }

    private void sendDeeplinkToApp(String deeplink) {
        Intent intent = new Intent();
        intent.setAction("InAppLink");
        intent.putExtra("link", deeplink);
        sendBroadcast(intent);
        Log.i("LOG_TAG", "Link sent successfully!");
    }


    @Override
    public void onCodeShown(String code) {
        spinToWinPromotionCode = code;
    }
}
