package com.visilabs.api;

import android.content.Context;
import android.net.Uri;

import com.visilabs.Visilabs;
import com.visilabs.json.JSONObject;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConfig;
import com.visilabs.util.VisilabsEncoder;
import com.visilabs.util.VisilabsLog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class VisilabsGeofenceRequest extends VisilabsRemote {

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

    protected static final String LOG_TAG = "VisilabsGeofenceRequest";

    private String mAction;
    private String mActionID;
    //    private String mContentID;

    private String mPath = "";
    private Methods mMethod = Methods.GET;
    private Header[] mHeaders = null;
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;
    private String mApiVer = "Android";

    private double mLatitude;
    private double mLongitude;
    private String mGeofenceID = "";

    public VisilabsGeofenceRequest(Context context) {
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

    public void setActionID(String actionID) {
        mActionID = actionID;
    }

    public void setGeofenceID(String geofenceID) {
        mGeofenceID = geofenceID;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public void setTimeOutInSeconds(int timeOutInSeconds ) {
        mTimeOutInSeconds = timeOutInSeconds;
    }

    public void setApiVer(String apiVer) {
        mApiVer = apiVer;
    }

    public void setLatitude(double pLatitude) {
        mLatitude = pLatitude;
    }
    public void setLongitude(double pLongitude) {
        mLongitude = pLongitude;
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
            String url = getURL();
            VisilabsLog.v(LOG_TAG, "Geofence Request: " + url);
            switch (mMethod) {
                case GET:
                    VisilabsHttpClient.get(url, buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                case PUT:
                    VisilabsHttpClient.put(url, buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                case POST:
                    VisilabsHttpClient.post(url, buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
                    break;
                case DELETE:
                    VisilabsHttpClient.delete(url, buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
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
        String url = Visilabs.CallAPI().getGeofenceURL();
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




            if(mActionID != null && mActionID != ""){
                uriBuilder.appendQueryParameter(VisilabsConfig.ACT_ID_KEY, VisilabsEncoder.encode(mActionID));
            }
            if(mAction != null && mAction != ""){
                uriBuilder.appendQueryParameter(VisilabsConfig.ACT_KEY, VisilabsEncoder.encode(mAction));
            }

            DecimalFormat df = new DecimalFormat("0.0000000000000");

            if(mLatitude > 0){
                String latitudeString = df.format(mLatitude);
                uriBuilder.appendQueryParameter(VisilabsConfig.LATITUDE_KEY, VisilabsEncoder.encode(latitudeString));
            }

            if(mLongitude > 0){
                String longitudeString = df.format(mLongitude);
                uriBuilder.appendQueryParameter(VisilabsConfig.LONGITUDE_KEY, VisilabsEncoder.encode(longitudeString));
            }

            if(!StringUtils.isNullOrWhiteSpace(mGeofenceID)){
                uriBuilder.appendQueryParameter(VisilabsConfig.GEO_ID_KEY, VisilabsEncoder.encode(mGeofenceID));
            }


            if(mApiVer != null && mApiVer != ""){
                uriBuilder.appendQueryParameter("OM.apiver", mApiVer);
            }else{
                uriBuilder.appendQueryParameter("OM.apiver", "Android");
            }

        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        return uriBuilder.build().toString();
    }

    protected Header[] buildHeaders(Header[] pHeaders) throws Exception {
        return  null;
    }
}
