package com.visilabs.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.util.VisilabsLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisilabsTargetRequest extends VisilabsRemote {

    protected static final String LOG_TAG = "VisilabsTargetRequest";

    private String mApiVer = "Android";
    private String mZoneID;
    private String mProductCode;
    private String mPath = "";
    private HashMap<String, String> mHeaders = new HashMap<>();
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;
    private HashMap<String,String> mProperties = new HashMap<>();
    private List<VisilabsTargetFilter> mFilters = new ArrayList<>();
    private VisilabsApiMethods mVisilabsSApiInterface;

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
        if(SApiClient.getClient(Visilabs.CallAPI().getRequestTimeoutSeconds()) != null) {
            mVisilabsSApiInterface = SApiClient.getClient(Visilabs.CallAPI().getRequestTimeoutSeconds())
                    .create(VisilabsApiMethods.class);
        }
    }

    public void setPath(String pPath) {
        mPath = pPath;
    }

    public void setHeaders(HashMap<String, String> pHeaders) {
        if(pHeaders != null && pHeaders.size() > 0) {
            for (int i = 0; i < pHeaders.size(); i++) {
                mHeaders.put((String) pHeaders.keySet().toArray()[i], pHeaders.get(pHeaders.keySet().toArray()[i]));
            }
        }
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
    public void executeAsync() throws Exception {

    }

    @Override
    public void executeAsync(final VisilabsCallback pCallback) throws Exception {
        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            Log.e("Visilabs", "Visilabs SDK requires min API level 21!");
            return;
        }

        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillTargetQueryMap(queryParameters);

        try {
            Call<ResponseBody> call = mVisilabsSApiInterface.getGeneralTargetRequestJsonResponse(headers, queryParameters);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String rawJsonResponse = "";
                    try {
                        rawJsonResponse = response.body().string();
                        if (!rawJsonResponse.equals("")) {
                            Log.i(LOG_TAG, "Success Request : " + response.raw().request().url().toString());
                            VisilabsResponse visilabsResponse = new VisilabsResponse(null, new JSONArray(rawJsonResponse), null, null, null);
                            pCallback.success(visilabsResponse);
                        } else {
                            Log.e(LOG_TAG, "Empty response for the request : " + response.raw().request().url().toString());
                            VisilabsResponse visilabsResponse = new VisilabsResponse(null, null, "empty string", null, "empty string");
                            pCallback.fail(visilabsResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Could not parse the response for the request : " + response.raw().request().url().toString());
                        VisilabsResponse visilabsResponse = new VisilabsResponse(null, null, rawJsonResponse, null, rawJsonResponse);
                        pCallback.fail(visilabsResponse);

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(LOG_TAG, "Fail Request " + t.getMessage());
                    VisilabsResponse visilabsResponse = new VisilabsResponse(null, null, t.getMessage(), t, t.getMessage());
                    pCallback.fail(visilabsResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not parse the response!");
        }
    }

    @Override
    public void executeAsync(VisilabsInAppMessageCallback pCallback) {

    }

    @Override
    public void executeAsync(VisilabsGeofenceGetListCallback pCallback) {

    }

    @Override
    public void executeAsyncAction(VisilabsCallback pCallback) throws Exception {

    }

    @Override
    public void executeSyncAction(VisilabsCallback pCallback) throws Exception {

    }

    @Override
    public void executeAsyncAction(VisilabsActionsCallback pCallback) {

    }

    @Override
    public void executeAsyncAction(VisilabsFavsRequestCallback pCallback) {

    }

    @Override
    public void executeAsyncPromotionCode(VisilabsCallback pCallback, HashMap<String, String> extraQueryParameters) {

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

    private void fillHeaderMap(HashMap<String, String> headerMap){
        if(mHeaders != null && mHeaders.size() > 0){
            for (int i = 0 ; i < mHeaders.size() ; i++){
                headerMap.put((String)mHeaders.keySet().toArray()[i], mHeaders.get(mHeaders.keySet().toArray()[i]));
            }
        }

        headerMap.put("User-Agent", Visilabs.getUserAgent());
    }

    private void fillTargetQueryMap(HashMap<String, String> queryMap){
        if(Visilabs.CallAPI().getOrganizationID() != null && !Visilabs.CallAPI().getOrganizationID().equals("")){
            queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, Visilabs.CallAPI().getOrganizationID());
        }

        if(Visilabs.CallAPI().getSiteID() != null && !Visilabs.CallAPI().getSiteID().equals("")){
            queryMap.put(VisilabsConstant.SITEID_KEY, Visilabs.CallAPI().getSiteID());
        }

        if(Visilabs.CallAPI().getCookieID() != null && !Visilabs.CallAPI().getCookieID().equals("")){
            queryMap.put(VisilabsConstant.COOKIEID_KEY, Visilabs.CallAPI().getCookieID());
        }

        if(Visilabs.CallAPI().getExVisitorID() != null && !Visilabs.CallAPI().getExVisitorID().equals("")){
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, Visilabs.CallAPI().getExVisitorID());
        }

        if(Visilabs.CallAPI().getSysTokenID() != null && !Visilabs.CallAPI().getSysTokenID().equals("")){
            queryMap.put(VisilabsConstant.TOKENID_KEY, Visilabs.CallAPI().getSysTokenID());
        }

        if(Visilabs.CallAPI().getSysAppID() != null && !Visilabs.CallAPI().getSysAppID().equals("")){
            queryMap.put(VisilabsConstant.APPID_KEY, Visilabs.CallAPI().getSysAppID());
        }

        if(mZoneID != null && !mZoneID.equals("")){
            queryMap.put(VisilabsConstant.ZONE_ID_KEY, mZoneID);
        }
        if(mProductCode != null && !mProductCode.equals("")){
            queryMap.put(VisilabsConstant.BODY_KEY, mProductCode);
        }

        if(mApiVer != null && !mApiVer.equals("")){
            queryMap.put(VisilabsConstant.APIVER_KEY, mApiVer);
        }else{
            queryMap.put(VisilabsConstant.APIVER_KEY, "Android");
        }

        cleanParameters();

        if(mProperties != null && mProperties.size() > 0)
            for (Map.Entry<String, String> entry : this.mProperties.entrySet()) {
                if (!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())) {
                    queryMap.put(entry.getKey(), entry.getValue());
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
                queryMap.put(VisilabsConstant.FILTER_KEY, jsonString);
            }
        }
        catch (Exception e){
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())
                    && this.mProperties != null && !this.mProperties.containsKey(entry.getKey())){
                queryMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
