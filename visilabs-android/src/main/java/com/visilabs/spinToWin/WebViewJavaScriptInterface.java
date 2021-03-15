package com.visilabs.spinToWin;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WebViewJavaScriptInterface {
    WebViewDialogFragment mWebViewDialogFragment;
    private SpinToWinCompleteInterface mListener;
    private SpinToWinCopyToClipboardInterface mCopyToClipboardInterface;
    private String mResponse;

    WebViewJavaScriptInterface(WebViewDialogFragment webViewDialogFragment, String response) {
        this.mWebViewDialogFragment = webViewDialogFragment;
        mResponse = response;
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
     * @param couponCode - String: coupon code
     */
    @JavascriptInterface
    public void copyToClipboard(String couponCode) {
        this.mWebViewDialogFragment.dismiss();
        mCopyToClipboardInterface.copyToClipboard(couponCode);
    }

    /**
     * This method sends a subscription request for the email entered
     * @param email : String - the value entered for email
     */
    @JavascriptInterface
    public void subscribeEmail(String email) {
        Report report = null;

        SpinToWinModel spinToWinModel = new Gson().fromJson(mResponse, SpinToWinModel.class);
        try {
            report = new Report();
            report.setImpression(spinToWinModel.getActiondata().getReport().getImpression());
            report.setClick(spinToWinModel.getActiondata().getReport().getClick());
        } catch (Exception e) {
            Log.w("Spin to Win : ", "There is no report to send");
            e.printStackTrace();
            report = null;
        }

        if(!email.equals("")) {
            Visilabs.CallAPI().createSubsJsonRequest(spinToWinModel.getActiondata().getType(),
                    spinToWinModel.getActid().toString(), spinToWinModel.getActiondata().getAuth(), email);
        }

        if(report != null) {
            Visilabs.CallAPI().trackActionClick(report);
        }
    }

    /**
     * This method makes a request to the ad server to get the coupon code
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    public void getPromotionCode() {
        String promotionId = "";
        String promoAuth;
        int actId;
        List<String> promotionCodes = new ArrayList<String>();
        int selectedIndex = -1;

        SpinToWinModel spinToWinModel = new Gson().fromJson(mResponse, SpinToWinModel.class);

        promoAuth = spinToWinModel.getActiondata().getPromoAuth();
        actId = spinToWinModel.getActid();

        for(int i = 0 ; i < spinToWinModel.getActiondata().getSlices().size() ; i++) {
            Slice slice = spinToWinModel.getActiondata().getSlices().get(i);
            if(slice.getType().equals("promotion")) {
                promotionCodes.add(slice.getCode());
                promotionId = slice.getCode();
            }
        }

        if(promotionCodes.size() > 0) {
            try {
                Random random = new Random();
                selectedIndex = random.nextInt(promotionCodes.size());
                promotionId = promotionCodes.get(selectedIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(selectedIndex != -1) {
            try {
                HashMap<String, String> queryParameters = new HashMap<>();
                queryParameters.put("promotionid", promotionId);
                queryParameters.put("promoauth", promoAuth);
                queryParameters.put("actionid", String.valueOf(actId));
                VisilabsActionRequest visilabsActionRequest = new VisilabsActionRequest(mWebViewDialogFragment.getContext());
                visilabsActionRequest.executeAsyncPromotionCode(getVisilabsCallback(selectedIndex), queryParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mWebViewDialogFragment.getWebView().evaluateJavascript("window.chooseSlice(-1, undefined);", null);
        }
    }

    private VisilabsCallback getVisilabsCallback (final int idx) {
        return new VisilabsCallback() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                JSONObject jsonResponse = new JSONObject(rawResponse);
                String promotionCode = jsonResponse.getString("promocode");
                mWebViewDialogFragment.getWebView().evaluateJavascript("window.chooseSlice("+idx+",'"+promotionCode+"');", null);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void fail(VisilabsResponse response) {
                mWebViewDialogFragment.getWebView().evaluateJavascript("window.chooseSlice(-1, undefined);", null);
            }
        };
    }

    /**
     * This method gives the initial response to js side.
     * @return String - string version of the json response got from the server
     */
    @JavascriptInterface
    public String getResponse() {
        return mResponse;
    }

    public void setSpinToWinListeners(SpinToWinCompleteInterface listener, SpinToWinCopyToClipboardInterface copyToClipboardInterface){
        mListener = listener;
        mCopyToClipboardInterface = copyToClipboardInterface;
    }
}
