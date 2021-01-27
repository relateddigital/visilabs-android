package com.visilabs.api;


import android.content.Context;
import android.net.Uri;

import com.visilabs.Visilabs;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.util.VisilabsEncoder;
import com.visilabs.util.VisilabsLog;


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
    private String mProductCode;
    private String mPath = "";
    private Methods mMethod = Methods.GET;
    private Header[] mHeaders = null;
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;
    private HashMap<String,String> mProperties = new HashMap<>();
    private List<VisilabsTargetFilter> mFilters = new ArrayList<>();

    public List<VisilabsTargetFilter> getFilters() {
        return mFilters;
    }

    public void setFilters(List<VisilabsTargetFilter> filters) {
        mFilters = filters;
    }

    public HashMap<String, String> getProperties() {
        return mProperties;
    }

    public void setProperties(HashMap<String, String> properties) {
        mProperties = properties;
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
    public void executeAsync(VisilabsCallback pCallback) throws Exception {
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
    public void executeAsync(VisilabsInAppMessageCallback pCallback) {

    }

    @Override
    public void executeAsyncAction(VisilabsMailSubsFormRequestCallback pCallback) {

    }

    @Override
    public void executeAsyncAction(VisilabsFavsRequestCallback pCallback) {

    }

    private void cleanParameters(){
        if(this.mProperties != null && this.mProperties.size() > 0){
            for (String key : this.mProperties.keySet()) {
                if(!StringUtils.isNullOrWhiteSpace(key)){
                    if(!(!key.equals(VisilabsConstant.ORGANIZATIONID_KEY) && !key.equals(VisilabsConstant.SITEID_KEY)
                            && !key.equals(VisilabsConstant.EXVISITORID_KEY) && !key.equals(VisilabsConstant.COOKIEID_KEY)
                            && !key.equals(VisilabsConstant.ZONE_ID_KEY) && !key.equals(VisilabsConstant.BODY_KEY)
                            && !key.equals(VisilabsConstant.TOKENID_KEY) && !key.equals(VisilabsConstant.APPID_KEY)
                            && !key.equals(VisilabsConstant.APIVER_KEY) && !key.equals(VisilabsConstant.FILTER_KEY))){
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

            if(Visilabs.CallAPI().getOrganizationID() != null && !Visilabs.CallAPI().getOrganizationID().equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.ORGANIZATIONID_KEY, Visilabs.CallAPI().getOrganizationID());
            }
            if(Visilabs.CallAPI().getSiteID() != null && !Visilabs.CallAPI().getSiteID().equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.SITEID_KEY, Visilabs.CallAPI().getSiteID());
            }

            if(Visilabs.CallAPI().getCookieID() != null && !Visilabs.CallAPI().getCookieID().equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.COOKIEID_KEY, Visilabs.CallAPI().getCookieID());
            }
            if(Visilabs.CallAPI().getExVisitorID() != null && !Visilabs.CallAPI().getExVisitorID().equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.EXVISITORID_KEY, Visilabs.CallAPI().getExVisitorID());
            }

            if(Visilabs.CallAPI().getSysTokenID() != null && !Visilabs.CallAPI().getSysTokenID().equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.TOKENID_KEY, Visilabs.CallAPI().getSysTokenID());
            }
            if(Visilabs.CallAPI().getSysAppID() != null && !Visilabs.CallAPI().getSysAppID().equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.APPID_KEY, Visilabs.CallAPI().getSysAppID());
            }

            if(mZoneID != null && !mZoneID.equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.ZONE_ID_KEY, VisilabsEncoder.encode(mZoneID));
            }
            if(mProductCode != null && !mProductCode.equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.BODY_KEY, VisilabsEncoder.encode(mProductCode));
            }

            if(mApiVer != null && !mApiVer.equals("")){
                uriBuilder.appendQueryParameter(VisilabsConstant.APIVER_KEY, mApiVer);
            }else{
                uriBuilder.appendQueryParameter(VisilabsConstant.APIVER_KEY, "Android");
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
                            JSONObject filterJSON = new JSONObject();
                            filterJSON.put("attr", filter.getAttribute());
                            filterJSON.put("ft", filter.getFilterType());
                            filterJSON.put("fv", filter.getValue());
                            jsonArray.put(filterJSON);
                        }
                    }
                    String jsonString =  jsonArray.toString();
                    uriBuilder.appendQueryParameter(VisilabsConstant.FILTER_KEY, jsonString);
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
