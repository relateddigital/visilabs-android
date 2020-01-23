package com.visilabs.android.api;


import android.content.Context;
import android.net.Uri;

import com.visilabs.android.Visilabs;
import com.visilabs.android.json.JSONArray;
import com.visilabs.android.json.JSONObject;
import com.visilabs.android.util.PersistentTargetManager;
import com.visilabs.android.util.StringUtils;
import com.visilabs.android.util.VisilabsConfig;
import com.visilabs.android.util.VisilabsEncoder;
import com.visilabs.android.util.VisilabsLog;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class VisilabsTargetRequest extends VisilabsRemote {

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

    protected static final String LOG_TAG = "VisilabsTargetRequest";

    private String mApiVer = "Android";

    private String mZoneID;
//    private String mContentID;
    private String mProductCode;

    private String mPath = "";
    private Methods mMethod = Methods.GET;
    private Header[] mHeaders = null;
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;
    private HashMap<String,String> mProperties = new HashMap<String, String>();
    private List<VisilabsTargetFilter> mFilters = new ArrayList<VisilabsTargetFilter>();

    public List<VisilabsTargetFilter> getFilters() {
        return mFilters;
    }

    public void setFilters(List<VisilabsTargetFilter> mFilters) {
        this.mFilters = mFilters;
    }

    public HashMap<String, String> getProperties() {
        return mProperties;
    }

    public void setProperties(HashMap<String, String> mProperties) {
        this.mProperties = mProperties;
    }

    public VisilabsTargetRequest(Context context) {
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

    public void setZoneID(String zoneID) {
        mZoneID = zoneID;
    }

    public void setTimeOutInSeconds(int timeOutInSeconds ) {
        mTimeOutInSeconds = timeOutInSeconds;
    }

    public void setApiVer(String apiVer) {
        mApiVer = apiVer;
    }

    public void setProductCode(String productCode) {
        mProductCode = productCode;
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

    private void cleanParameters(){
        if(this.mProperties != null && this.mProperties.size() > 0){
            for (String key : this.mProperties.keySet()) {
                if(!StringUtils.isNullOrWhiteSpace(key)){
                    if(key != VisilabsConfig.ORGANIZATIONID_KEY && key != VisilabsConfig.SITEID_KEY && key != VisilabsConfig.EXVISITORID_KEY && key != VisilabsConfig.COOKIEID_KEY
                            && key != VisilabsConfig.ZONE_ID_KEY && key != VisilabsConfig.BODY_KEY && key != VisilabsConfig.TOKENID_KEY && key != VisilabsConfig.APPID_KEY
                            && key != VisilabsConfig.APIVER_KEY && key != VisilabsConfig.FILTER_KEY){
                        continue;
                    }
                    else{
                        this.mProperties.remove(key);
                    }
                }else{
                    this.mProperties.remove(key);
                }
            }
        }
    }

    public String getURL() {
        String url = Visilabs.getTargetURL();
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

            if(mZoneID != null && mZoneID != ""){
                uriBuilder.appendQueryParameter(VisilabsConfig.ZONE_ID_KEY, VisilabsEncoder.encode(mZoneID));
            }
            if(mProductCode != null && mProductCode != ""){
                uriBuilder.appendQueryParameter(VisilabsConfig.BODY_KEY, VisilabsEncoder.encode(mProductCode));
            }

            if(mApiVer != null && mApiVer != ""){
                uriBuilder.appendQueryParameter(VisilabsConfig.APIVER_KEY, mApiVer);
            }else{
                uriBuilder.appendQueryParameter(VisilabsConfig.APIVER_KEY, "Android");
            }

            this.cleanParameters();
            if(this.mProperties != null && this.mProperties.size() > 0)
                for (Map.Entry<String, String> entry : this.mProperties.entrySet()) {
                    if (!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())) {
                        uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }
                }

            try {
                if(this.mFilters != null && this.mFilters.size() > 0 ){
                    JSONArray jsonArray = new JSONArray();

                    for (VisilabsTargetFilter filter : this.mFilters) {
                        if (!StringUtils.isNullOrWhiteSpace(filter.getAttribute()) && !StringUtils.isNullOrWhiteSpace(filter.getFilterType())
                                && !StringUtils.isNullOrWhiteSpace(filter.getValue())) {
                            //TODO:JSON CONVERT
                            JSONObject filterJSON = new JSONObject();
                            filterJSON.put("attr", filter.getAttribute());
                            filterJSON.put("ft", filter.getFilterType());
                            filterJSON.put("fv", filter.getValue());
                            jsonArray.put(filterJSON);
                            //uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    String jsonString =  jsonArray.toString();
                    //TODO: kontrol et
                    uriBuilder.appendQueryParameter(VisilabsConfig.FILTER_KEY, jsonString);
                }
            }
            catch (Exception e){
                VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            }


        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())
                    && this.mProperties != null && !this.mProperties.containsKey(entry.getKey())){
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        return uriBuilder.build().toString();
    }

    protected Header[] buildHeaders(Header[] pHeaders) throws Exception {
        return  null;
    }
}
