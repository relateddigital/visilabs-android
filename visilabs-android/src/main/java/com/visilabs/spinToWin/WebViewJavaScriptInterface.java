package com.visilabs.spinToWin;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.json.JSONObject;
import com.visilabs.mailSub.Report;
import com.visilabs.spinToWin.model.Slice;
import com.visilabs.spinToWin.model.SpinToWinModel;

import java.util.HashMap;

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
    public void closeButtonClicked() {
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
    public void subsEmail(String email) {
        SpinToWinModel spinToWinModel = new Gson().fromJson(mResponse, SpinToWinModel.class);
        Report report = new Report();
        report.setImpression(spinToWinModel.getActiondata().getReport().getImpression());
        report.setClick(spinToWinModel.getActiondata().getReport().getClick());

        Visilabs.CallAPI().trackMailSubscriptionFormClick(report);

        Visilabs.CallAPI().createSubsJsonRequest(spinToWinModel.getActiondata().getType(),
                spinToWinModel.getActid().toString(), spinToWinModel.getActiondata().getAuth(), email);
    }

    /**
     * This method makes a request to the ad server to get the coupon code
     */
    @JavascriptInterface
    public void getPromotionCode() {
        String promotionId = "";
        String promoAuth;
        int actId;

        SpinToWinModel spinToWinModel = new Gson().fromJson(mResponse, SpinToWinModel.class);

        promoAuth = spinToWinModel.getActiondata().getPromoAuth();
        actId = spinToWinModel.getActid();

        for(int i = 0 ; i < spinToWinModel.getActiondata().getSliceCount().length() ; i++) {
            Slice slice = spinToWinModel.getActiondata().getSlices().get(i);
            if(slice.getType().equals("promotion")) {
                promotionId = slice.getCode();
            }
        }

        if(!promotionId.equals("")) {
            try {
                HashMap<String, String> queryParameters = new HashMap<>();
                queryParameters.put("promotionid", promotionId);
                queryParameters.put("promoauth", promoAuth);
                queryParameters.put("actionid", String.valueOf(actId));
                VisilabsActionRequest visilabsActionRequest = new VisilabsActionRequest(mWebViewDialogFragment.getContext());
                visilabsActionRequest.executeAsyncPromotionCode(getVisilabsCallback(), queryParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //TODO: there is no type promotion in the slices.
            //TODO: inform the js side that this method should not have been called.
            //TODO: maybe string "invalid"
        }
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

    private VisilabsCallback getVisilabsCallback () {
        return new VisilabsCallback() {

            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                JSONObject jsonResponse = new JSONObject(rawResponse);
                String promotionCode = jsonResponse.getString("promocode");
                promotionCode = promotionCode + promotionCode;
                //TODO: pass the promotion code to js side here
            }

            @Override
            public void fail(VisilabsResponse response) {
                //TODO: inform the js side here that the request failed
                //TODO: maybe string "fail"
            }
        };
    }
}
