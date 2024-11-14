package com.visilabs.spinToWin;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

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
    WebViewDialogFragment mWebViewDialogFragment;
    private SpinToWinCompleteInterface mListener;
    private SpinToWinCopyToClipboardInterface mCopyToClipboardInterface;
    private SpinToWinShowCodeInterface mSpinToWinShowCodeInterface;
    private String mResponse;
    private SpinToWinModel spinToWinModel;
    private String subEmail = "";
    int selectedIndex = -1;

    WebViewJavaScriptInterface(WebViewDialogFragment webViewDialogFragment, String response) {
        this.mWebViewDialogFragment = webViewDialogFragment;
        mResponse = response;
        spinToWinModel = new Gson().fromJson(mResponse, SpinToWinModel.class);
    }

    /**
     * This method closes SpinToWinActivity
     */
    @JavascriptInterface
    public void close() {
        this.mWebViewDialogFragment.dismiss();
        mListener.onCompleted();
    }

    /**
     * This method copies the coupon code to clipboard
     * and ends the activity
     *
     * @param couponCode - String: coupon code
     */
    @JavascriptInterface
    public void copyToClipboard(String couponCode, String link) {
        this.mWebViewDialogFragment.dismiss();

        if (spinToWinModel.getActiondata().getCopyButtonFunction().equals("copy_redirect") &&
                spinToWinModel.getActiondata().getSlices().get(selectedIndex).getAndroidLink() != null &&
                !spinToWinModel.getActiondata().getSlices().get(selectedIndex).getAndroidLink().equals("")) {
            mCopyToClipboardInterface.copyToClipboard(couponCode, spinToWinModel.getActiondata().getSlices().get(selectedIndex).getAndroidLink());
        } else {
            mCopyToClipboardInterface.copyToClipboard(couponCode, null);
            mCopyToClipboardInterface.copyToClipboard(couponCode, link);
        }
    }

    /**
     * This method sends a subscription request for the email entered
     *
     * @param email : String - the value entered for email
     */
    @JavascriptInterface
    public void subscribeEmail(String email) {
        if (!email.equals("") && email != null) {
            subEmail = email;
            Visilabs.CallAPI().createSubsJsonRequest(spinToWinModel.getActiondata().getType(),
                    spinToWinModel.getActid().toString(), spinToWinModel.getActiondata().getAuth(),
                    email);
        } else {
            Log.e("Spin to Win : ", "Email entered is not valid!");
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
            report.setImpression(spinToWinModel.getActiondata().getReport().getImpression());
            report.setClick(spinToWinModel.getActiondata().getReport().getClick());
        } catch (Exception e) {
            Log.e("Spin to Win : ", "There is no report to send!");
            e.printStackTrace();
            report = null;
        }

        if (report != null) {
            Visilabs.CallAPI().trackActionClick(report);
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
        int actId;
        List<String> promotionCodes = new ArrayList<String>();
        List<String> sliceTexts = new ArrayList<String>();
        List<Integer> promotionIndexes = new ArrayList<Integer>();

        promoAuth = spinToWinModel.getActiondata().getPromoAuth();
        actId = spinToWinModel.getActid();

        for (int i = 0; i < spinToWinModel.getActiondata().getSlices().size(); i++) {
            Slice slice = spinToWinModel.getActiondata().getSlices().get(i);
            if (slice.getType().equals("promotion") && slice.getIsAvailable()) {
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
                queryParameters.put("actionid", String.valueOf(actId));
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
            for (int i = 0; i < spinToWinModel.getActiondata().getSlices().size(); i++) {
                Slice slice = spinToWinModel.getActiondata().getSlices().get(i);
                if (slice.getType().equals("staticcode")) {
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
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    final String finalSelectedCode = selectedCode;
                    final String finalSelectedSliceText = selectedSliceText;
                    final int finalSelectedIndex = selectedIndex;
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            mSpinToWinShowCodeInterface.onCodeShown(finalSelectedCode);
                            sendPromotionCodeInfo(finalSelectedCode, finalSelectedSliceText);
                            mWebViewDialogFragment.getWebView().evaluateJavascript(
                                    "window.chooseSlice(" + finalSelectedIndex + ",'" + finalSelectedCode + "');",
                                    null);
                        }
                    };
                    mainHandler.post(myRunnable);
                } catch (Exception e) {
                    e.printStackTrace();
                    selectedIndex = -1;
                }
            }
        }

        if(selectedIndex == -1) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    mWebViewDialogFragment.getWebView().evaluateJavascript(
                            "window.chooseSlice(-1, undefined);", null);
                }
            };
            mainHandler.post(myRunnable);
        }
    }

    private VisilabsCallback getVisilabsCallback(final int idx, final String sliceText) {
        return new VisilabsCallback() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                JSONObject jsonResponse = new JSONObject(rawResponse);
                final String promotionCode = jsonResponse.getString("promocode");
                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mSpinToWinShowCodeInterface.onCodeShown(promotionCode);
                        sendPromotionCodeInfo(promotionCode, sliceText);
                        mWebViewDialogFragment.getWebView().evaluateJavascript(
                                "window.chooseSlice(" + idx + ",'" + promotionCode + "');",
                                null);
                    }
                };
                mainHandler.post(myRunnable);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void fail(VisilabsResponse response) {
                Log.e("SpinToWin", "Could not get the promotion code!");
                List<String> staticCodes = new ArrayList<String>();
                List<String> staticSliceTexts = new ArrayList<String>();
                List<Integer> staticIndexes = new ArrayList<Integer>();
                int selectedIndex = -1;
                for (int i = 0; i < spinToWinModel.getActiondata().getSlices().size(); i++) {
                    Slice slice = spinToWinModel.getActiondata().getSlices().get(i);
                    if (slice.getType().equals("staticcode")) {
                        staticCodes.add(slice.getCode());
                        staticIndexes.add(i);
                        staticSliceTexts.add(slice.getDisplayName());
                    }
                }

                if (staticCodes.size() > 0) {
                    final String finalSelectedCode;
                    final String finalSelectedSliceText;
                    try {
                        Random random = new Random();
                        int randomIndex = random.nextInt(staticCodes.size());
                        finalSelectedCode = staticCodes.get(randomIndex);
                        selectedIndex = staticIndexes.get(randomIndex);
                        finalSelectedSliceText = staticSliceTexts.get(randomIndex);
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        final int finalSelectedIndex = selectedIndex;
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                mSpinToWinShowCodeInterface.onCodeShown(finalSelectedCode);
                                sendPromotionCodeInfo(finalSelectedCode, finalSelectedSliceText);
                                mWebViewDialogFragment.getWebView().evaluateJavascript(
                                        "window.chooseSlice(" + finalSelectedIndex + ",'" + finalSelectedCode + "');",
                                        null);
                            }
                        };
                        mainHandler.post(myRunnable);
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectedIndex = -1;
                    }
                }

                if(selectedIndex == -1) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            mWebViewDialogFragment.getWebView().evaluateJavascript(
                                    "window.chooseSlice(-1, undefined);", null);
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            }
        };
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
    }

    private void sendPromotionCodeInfo(final String promotionCode, final String sliceText) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put(VisilabsConstant.PROMOTION_CODE_REQUEST_KEY, promotionCode);
        parameters.put(VisilabsConstant.ACTION_ID_REQUEST_KEY, "act-" + spinToWinModel.
                getActid().toString());
        if (!subEmail.isEmpty()) {
            parameters.put(VisilabsConstant.PROMOTION_CODE_EMAIL_REQUEST_KEY,
                    subEmail);
        }
        parameters.put(VisilabsConstant.PROMOTION_CODE_TITLE_REQUEST_KEY,
                spinToWinModel.getActiondata().getPromocodeTitle());
        if (!sliceText.isEmpty()) {
            parameters.put(VisilabsConstant.PROMOTION_CODE_SLICE_TEXT_REQUEST_KEY,
                    sliceText);
        }
        Visilabs.CallAPI().customEvent(VisilabsConstant.PAGE_NAME_REQUEST_VAL,
                parameters);
    }
}
