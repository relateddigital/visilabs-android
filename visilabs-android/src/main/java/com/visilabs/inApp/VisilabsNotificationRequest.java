package com.visilabs.inApp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.visilabs.Visilabs;
import com.visilabs.api.VisilabsHttpClient;
import com.visilabs.api.VisilabsRemote;
import com.visilabs.api.VisilabsTargetCallback;
import com.visilabs.json.JSONObject;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConfig;
import com.visilabs.util.VisilabsLog;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class VisilabsNotificationRequest extends VisilabsRemote {

    public enum Methods {
        GET, POST, PUT, DELETE;

        public static Methods parse(String pMethod) throws Exception {
            try {
                return Methods.valueOf(pMethod.toUpperCase());
            } catch (Exception e) {
                throw new Exception("Unsupported HTTP method: " + pMethod);
            }
        }
    }

    protected static final String LOG_TAG = "VisilabsNotifRequest";

    private String mApiVer = "Android";
    private String mPageName = "";

    private String mPath = "";
    private Methods mMethod = Methods.GET;
    private Header[] mHeaders = null;
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;


    private String mVisitorData = "";
    private String mVisitData = "";


    private HashMap<String, String> mProperties;

    public VisilabsNotificationRequest(Context context) {
        super(context);
    }

    public void setPath(String pPath) {
        mPath = pPath;
    }

    public void setMethod(Methods pMethod) {
        mMethod = pMethod;
    }

    public void setHeaders(Header[] pHeaders) {
        mHeaders = pHeaders;
    }

    public void setRequestArgs(JSONObject pArgs) {
        mArgs = pArgs;
    }

    public void setTimeOutInSeconds(int timeOutInSeconds ) {
        mTimeOutInSeconds = timeOutInSeconds;
    }

    public void setApiVer(String apiVer) {
        mApiVer = apiVer;
    }

    public void setPageName(String pageName) {
        mPageName = pageName;
    }

    public void setVisitorData(String pVisitorData) {
        mVisitorData = pVisitorData;
    }

    public void setVisitData(String pVisitData) {
        mVisitData = pVisitData;
    }

    public void setProperties(HashMap<String, String> pProperties){
        mProperties = pProperties;
    }

    @Override
    protected String getPath() {
        return mPath;
    }

    @Override
    protected JSONObject getRequestArgs() {
        return mArgs;
    }

    @Override
    public void executeAsync(VisilabsTargetCallback pCallback) throws Exception {
        try {
            switch (mMethod) {
                case GET:
                    VisilabsHttpClient.get(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                case PUT:
                    VisilabsHttpClient.put(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                case POST:
                    VisilabsHttpClient.post(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                case DELETE:
                    VisilabsHttpClient.delete(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void execute(VisilabsTargetCallback pCallback) throws Exception {
        try {
            switch (mMethod) {
                case GET:
                    VisilabsHttpClient.get(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true, mTimeOutInSeconds);
                    break;
                case PUT:
                    VisilabsHttpClient.put(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true, mTimeOutInSeconds);
                    break;
                case POST:
                    VisilabsHttpClient.post(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true, mTimeOutInSeconds);
                    break;
                case DELETE:
                    VisilabsHttpClient.delete(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true, mTimeOutInSeconds);
                    break;
            }
        } catch (Exception e) {
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    public String getURL() {
        String url = Visilabs.getActionURL();
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if(url != null && url.length() > 0){

            if(Visilabs.CallAPI().getOrganizationID() != null && Visilabs.CallAPI().getOrganizationID() != "" ){
                uriBuilder.appendQueryParameter(VisilabsConfig.ORGANIZATIONID_KEY, Visilabs.CallAPI().getOrganizationID());
            }
            if(Visilabs.CallAPI().getSiteID() != null && Visilabs.CallAPI().getSiteID() != "" ){
                uriBuilder.appendQueryParameter(VisilabsConfig.SITEID_KEY, Visilabs.CallAPI().getSiteID());
            }

            if(Visilabs.CallAPI().getCookieID() != null && Visilabs.CallAPI().getCookieID() != "" ){
                uriBuilder.appendQueryParameter(VisilabsConfig.COOKIEID_KEY, Visilabs.CallAPI().getCookieID());
            }
            if(Visilabs.CallAPI().getExVisitorID() != null && Visilabs.CallAPI().getExVisitorID() != "" ){
                uriBuilder.appendQueryParameter(VisilabsConfig.EXVISITORID_KEY, Visilabs.CallAPI().getExVisitorID());
            }

            if(Visilabs.CallAPI().getSysTokenID() != null && Visilabs.CallAPI().getSysTokenID() != "" ){
                uriBuilder.appendQueryParameter(VisilabsConfig.TOKENID_KEY, Visilabs.CallAPI().getSysTokenID());
            }
            if(Visilabs.CallAPI().getSysAppID() != null && Visilabs.CallAPI().getSysAppID() != "" ){
                uriBuilder.appendQueryParameter(VisilabsConfig.APPID_KEY, Visilabs.CallAPI().getSysAppID());
            }

            if(mApiVer != null && mApiVer != ""){
                uriBuilder.appendQueryParameter("OM.apiver", mApiVer);
            }else{
                uriBuilder.appendQueryParameter("OM.apiver", "Android");
            }

            if(mPageName != null && mPageName != ""){
                uriBuilder.appendQueryParameter("OM.uri", mPageName);
            }else{
                uriBuilder.appendQueryParameter("OM.uri", "");
            }

            if(mVisitorData != null && mVisitorData != ""){
                uriBuilder.appendQueryParameter("OM.viscap", mVisitorData);
            }

            if(mVisitData != null && mVisitData != ""){
                uriBuilder.appendQueryParameter("OM.vcap", mVisitData);
            }

        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }

            if(mProperties != null && mProperties.containsKey(entry.getKey())){
                mProperties.remove(entry.getKey());
            }

        }

        if(mProperties != null){
            for (Map.Entry<String, String> entry : mProperties.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key == VisilabsConfig.COOKIEID_KEY || key == "OM.siteID" || key == "OM.oid" || key == "OM.apiver"
                        || key == "OM.uri" || key == VisilabsConfig.EXVISITORID_KEY
                        || key == VisilabsConfig.APPID_KEY || key == VisilabsConfig.TOKENID_KEY){
                    continue;
                }

                if(value != null && value.length() > 0){
                    uriBuilder.appendQueryParameter(key, value);
                }

            }
        }


        String finalURI = uriBuilder.build().toString();

        if(VisilabsConfig.DEBUG){
            Log.v(LOG_TAG, finalURI);
        }

        return finalURI;
    }

    protected Header[] buildHeaders(Header[] pHeaders) throws Exception {
        return  null;
    }
}
