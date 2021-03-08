package com.visilabs.spinToWin;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.mailSub.Report;
import com.visilabs.spinToWin.model.SpinToWinModel;

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
     * @return String: coupon code got from the ad server
     */
    @JavascriptInterface
    public String getPromotionCode() {
        //TODO: make a request here
        //TODO: wait the answer and then return
        return "dummy";
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
