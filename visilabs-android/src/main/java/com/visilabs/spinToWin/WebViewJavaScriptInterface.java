package com.visilabs.spinToWin;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.json.JSONObject;
import com.visilabs.mailSub.Report;
import com.visilabs.spinToWin.model.Slice;
import com.visilabs.spinToWin.model.SpinToWinModel;
import com.visilabs.util.VisilabsConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WebViewJavaScriptInterface {
    private static final String LOG_TAG = "Spin to Win : ";
    WebViewDialogFragment mWebViewDialogFragment;
    private SpinToWinCompleteInterface mListener;
    private SpinToWinCopyToClipboardInterface mCopyToClipboardInterface;
    private SpinToWinShowCodeInterface mSpinToWinShowCodeInterface;
    private String mResponse;
    private SpinToWinModel spinToWinModel;
    private String subEmail = "";
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    int selectedIndex = -1;

    WebViewJavaScriptInterface(WebViewDialogFragment webViewDialogFragment, String response) {
        this.mWebViewDialogFragment = webViewDialogFragment;
        mResponse = response;
        try {
            spinToWinModel = new Gson().fromJson(mResponse, SpinToWinModel.class);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Spin to win model could not be parsed", e);
        }
    }

    /**
     * This method closes SpinToWinActivity
     */
    @JavascriptInterface
    public void close() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    dismissDialog();
                    if (mListener != null) {
                        mListener.onCompleted();
                    } else if (mWebViewDialogFragment.getActivity() != null) {
                        mWebViewDialogFragment.getActivity().finish();
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Spin to win could not be closed properly", e);
                }
            }
        });
    }

    /**
     * This method copies the coupon code to clipboard
     * and ends the activity
     *
     * @param couponCode - String: coupon code
     */
    @JavascriptInterface
    public void copyToClipboard(final String couponCode, final String link) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    dismissDialog();
                    String sliceLink = getSelectedSliceLink();
                    if (mCopyToClipboardInterface == null) {
                        if (mWebViewDialogFragment.getActivity() != null) {
                            mWebViewDialogFragment.getActivity().finish();
                        }
                        return;
                    }
                    if (spinToWinModel != null && spinToWinModel.getActiondata() != null &&
                            "copy_redirect".equals(spinToWinModel.getActiondata().getCopyButtonFunction()) &&
                            sliceLink != null && !sliceLink.equals("")) {
                        mCopyToClipboardInterface.copyToClipboard(couponCode, sliceLink);
                    } else {
                        mCopyToClipboardInterface.copyToClipboard(couponCode, null);
                        mCopyToClipboardInterface.copyToClipboard(couponCode, link);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Coupon code could not be copied", e);
                }
            }
        });
    }

    /**
     * This method sends a subscription request for the email entered
     *
     * @param email : String - the value entered for email
     */
    @JavascriptInterface
    public void subscribeEmail(String email) {
        if (email != null && !email.equals("")) {
            try {
                subEmail = email;
                Visilabs.CallAPI().createSubsJsonRequest(spinToWinModel.getActiondata().getType(),
                        String.valueOf(spinToWinModel.getActid()), spinToWinModel.getActiondata().getAuth(),
                        email);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Subscription request could not be sent", e);
            }
        } else {
            Log.e(LOG_TAG, "Email entered is not valid!");
        }
    }

    /**
     * This method sends the report to the server
     */
    @JavascriptInterface
    public void sendReport() {
        Report report = null;

        try {
            report = new Report();
            report.setClick(spinToWinModel.getActiondata().getReport().getClick());
        } catch (Exception e) {
            Log.e(LOG_TAG, "There is no report to send!");
            e.printStackTrace();
            report = null;
        }

        if (report != null && Visilabs.CallAPI() != null) {
            Visilabs.CallAPI().trackActionClick(report);
        }
    }

    @JavascriptInterface
    public void sendImpression() {
        Report report = null;

        try {
            report = new Report();
            report.setImpression(spinToWinModel.getActiondata().getReport().getImpression());
        } catch (Exception e) {
            Log.e(LOG_TAG, "There is no report to send!");
            e.printStackTrace();
            report = null;
        }

        if (report != null && Visilabs.CallAPI() != null) {
            Visilabs.CallAPI().trackActionImpression(report);
        }
    }

    /**
     * This method makes a request to the ad server to get the coupon code
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    public void getPromotionCode() {
        String selectedCode = "";
        String selectedSliceText = "";
        String promoAuth;
        List<String> promotionCodes = new ArrayList<String>();
        List<String> sliceTexts = new ArrayList<String>();
        List<Integer> promotionIndexes = new ArrayList<Integer>();
        List<Slice> slices = getSlices();

        promoAuth = spinToWinModel != null && spinToWinModel.getActiondata() != null ?
                spinToWinModel.getActiondata().getPromoAuth() : null;
        String actId = spinToWinModel != null ? String.valueOf(spinToWinModel.getActid()) : "";

        for (int i = 0; i < slices.size(); i++) {
            Slice slice = slices.get(i);
            if (slice != null && "promotion".equals(slice.getType()) && slice.getIsAvailable()) {
                promotionCodes.add(slice.getCode());
                promotionIndexes.add(i);
                sliceTexts.add(slice.getDisplayName());
            }
        }

        if (promotionCodes.size() > 0) {
            try {
                Random random = new Random();
                int randomIndex = random.nextInt(promotionCodes.size());
                selectedCode = promotionCodes.get(randomIndex);
                selectedIndex = promotionIndexes.get(randomIndex);
                selectedSliceText = sliceTexts.get(randomIndex);
                HashMap<String, String> queryParameters = new HashMap<>();
                queryParameters.put("promotionid", selectedCode);
                queryParameters.put("promoauth", promoAuth);
                queryParameters.put("actionid", actId);
                VisilabsActionRequest visilabsActionRequest = new VisilabsActionRequest(
                        mWebViewDialogFragment.getContext());
                visilabsActionRequest.executeAsyncPromotionCode(getVisilabsCallback(selectedIndex,
                        selectedSliceText), queryParameters);
            } catch (Exception e) {
                e.printStackTrace();
                selectedIndex = -1;
            }
        }

        if(selectedIndex == -1) {
            List<String> staticCodes = new ArrayList<String>();
            List<String> staticSliceTexts = new ArrayList<String>();
            List<Integer> staticIndexes = new ArrayList<Integer>();
            for (int i = 0; i < slices.size(); i++) {
                Slice slice = slices.get(i);
                if (slice != null && "staticcode".equals(slice.getType())) {
                    staticCodes.add(slice.getCode());
                    staticIndexes.add(i);
                    staticSliceTexts.add(slice.getDisplayName());
                }
            }

            if (staticCodes.size() > 0) {
                try {
                    Random random = new Random();
                    int randomIndex = random.nextInt(staticCodes.size());
                    selectedCode = staticCodes.get(randomIndex);
                    selectedIndex = staticIndexes.get(randomIndex);
                    selectedSliceText = staticSliceTexts.get(randomIndex);
                    showCode(selectedIndex, selectedCode, selectedSliceText);
                } catch (Exception e) {
                    e.printStackTrace();
                    selectedIndex = -1;
                }
            }
        }

        if(selectedIndex == -1) {
            showNoCode();
        }
    }

    private VisilabsCallback getVisilabsCallback(final int idx, final String sliceText) {
        return new VisilabsCallback() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void success(VisilabsResponse response) {
                try {
                    String rawResponse = response.getRawResponse();
                    JSONObject jsonResponse = new JSONObject(rawResponse);
                    final String promotionCode = jsonResponse.getString("promocode");
                    showCode(idx, promotionCode, sliceText);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Promotion code response could not be parsed", e);
                    fail(response);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void fail(VisilabsResponse response) {
                Log.e("SpinToWin", "Could not get the promotion code!");
                List<String> staticCodes = new ArrayList<String>();
                List<String> staticSliceTexts = new ArrayList<String>();
                List<Integer> staticIndexes = new ArrayList<Integer>();
                List<Slice> slices = getSlices();
                int selectedIndex = -1;
                for (int i = 0; i < slices.size(); i++) {
                    Slice slice = slices.get(i);
                    if (slice != null && "staticcode".equals(slice.getType())) {
                        staticCodes.add(slice.getCode());
                        staticIndexes.add(i);
                        staticSliceTexts.add(slice.getDisplayName());
                    }
                }

                if (staticCodes.size() > 0) {
                    try {
                        Random random = new Random();
                        int randomIndex = random.nextInt(staticCodes.size());
                        selectedIndex = staticIndexes.get(randomIndex);
                        showCode(selectedIndex, staticCodes.get(randomIndex), staticSliceTexts.get(randomIndex));
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectedIndex = -1;
                    }
                }

                if(selectedIndex == -1) {
                    showNoCode();
                }
            }
        };
    }

    private void showCode(final int index, final String code, final String sliceText) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSpinToWinShowCodeInterface != null) {
                        mSpinToWinShowCodeInterface.onCodeShown(code);
                    }
                    sendPromotionCodeInfo(code, sliceText);
                    WebView webView = mWebViewDialogFragment.getWebView();
                    if (webView != null) {
                        webView.evaluateJavascript(
                                "window.chooseSlice(" + index + ",'" + code + "');",
                                null);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Promotion code could not be shown", e);
                }
            }
        });
    }

    private void showNoCode() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                WebView webView = mWebViewDialogFragment.getWebView();
                if (webView != null) {
                    webView.evaluateJavascript("window.chooseSlice(-1, undefined);", null);
                }
            }
        });
    }

    private void dismissDialog() {
        if (mWebViewDialogFragment.isAdded()) {
            mWebViewDialogFragment.dismissAllowingStateLoss();
        }
    }

    private List<Slice> getSlices() {
        if (spinToWinModel == null || spinToWinModel.getActiondata() == null ||
                spinToWinModel.getActiondata().getSlices() == null) {
            return new ArrayList<>();
        }
        return spinToWinModel.getActiondata().getSlices();
    }

    private String getSelectedSliceLink() {
        List<Slice> slices = getSlices();
        if (selectedIndex < 0 || selectedIndex >= slices.size() || slices.get(selectedIndex) == null) {
            return null;
        }
        return slices.get(selectedIndex).getAndroidLink();
    }

    /**
     * This method gives the initial response to js side.
     *
     * @return String - string version of the json response got from the server
     */
    @JavascriptInterface
    public String getResponse() {
        return mResponse;
    }

    public void setSpinToWinListeners(SpinToWinCompleteInterface listener,
                                      SpinToWinCopyToClipboardInterface copyToClipboardInterface,
                                      SpinToWinShowCodeInterface spinToWinShowCodeInterface) {
        mListener = listener;
        mCopyToClipboardInterface = copyToClipboardInterface;
        mSpinToWinShowCodeInterface = spinToWinShowCodeInterface;
        sendImpression();

    }

    private void sendPromotionCodeInfo(final String promotionCode, final String sliceText) {
        if (spinToWinModel == null || Visilabs.CallAPI() == null) {
            return;
        }
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put(VisilabsConstant.PROMOTION_CODE_REQUEST_KEY, promotionCode);
        parameters.put(VisilabsConstant.ACTION_ID_REQUEST_KEY, "act-" + spinToWinModel.getActid());
        if (!subEmail.isEmpty()) {
            parameters.put(VisilabsConstant.PROMOTION_CODE_EMAIL_REQUEST_KEY,
                    subEmail);
        }
        if (spinToWinModel.getActiondata() != null) {
            parameters.put(VisilabsConstant.PROMOTION_CODE_TITLE_REQUEST_KEY,
                    spinToWinModel.getActiondata().getPromocodeTitle());
        }
        if (sliceText != null && !sliceText.isEmpty()) {
            parameters.put(VisilabsConstant.PROMOTION_CODE_SLICE_TEXT_REQUEST_KEY,
                    sliceText);
        }
        Visilabs.CallAPI().customEvent(VisilabsConstant.PAGE_NAME_REQUEST_VAL,
                parameters);
    }
}
