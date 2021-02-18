package com.visilabs.api;

import android.content.Context;
import android.util.Log;

import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.gps.model.VisilabsGeofenceGetListResponse;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VisilabsGeofenceRequest extends VisilabsRemote {

    protected static final String LOG_TAG = "VisilabsGeofenceRequest";

    private String mAction;
    private String mActionID;
    private String mPath = "";
    private HashMap<String, String> mHeaders = new HashMap<>();
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;
    private String mApiVer = "Android";
    private double mLatitude;
    private double mLongitude;
    private String mGeofenceID = "";

    private VisilabsApiMethods mVisilabsSApiInterface;

    public VisilabsGeofenceRequest(Context context) {
        super(context);
        mVisilabsSApiInterface = SApiClient.getClient(Visilabs.CallAPI().getRequestTimeoutSeconds())
                .create(VisilabsApiMethods.class);
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
    public void executeAsync() throws Exception {

    }

    @Override
    public void executeAsync(final VisilabsCallback pCallback) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillQueryMap(queryParameters);

        Call<ResponseBody> call = mVisilabsSApiInterface.getGeneralGeofenceRequestJsonResponse(headers, queryParameters);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String rawJsonResponse = "";
                try {
                    rawJsonResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!rawJsonResponse.equals("")) {
                    try {
                        Log.i(LOG_TAG, "Success Request : " + response.raw().request().url().toString());
                        VisilabsResponse visilabsResponse;
                        if (rawJsonResponse.equals("ok") || rawJsonResponse.equals("\"ok\"")) {
                            visilabsResponse = new VisilabsResponse(null, null, rawJsonResponse, null, null);
                        } else {
                            visilabsResponse = new VisilabsResponse(null, new JSONArray(rawJsonResponse), null, null, null);
                        }
                        pCallback.success(visilabsResponse);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(LOG_TAG, "Failed to get the json response");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "Fail Request " + t.getMessage());
                VisilabsResponse visilabsResponse = new VisilabsResponse(null, null, t.getMessage(), t, t.getMessage());
                pCallback.fail(visilabsResponse);
            }
        });
    }

    @Override
    public void executeAsync(final VisilabsGeofenceGetListCallback pCallback) {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillQueryMap(queryParameters);

        Call<List<VisilabsGeofenceGetListResponse>> call = mVisilabsSApiInterface.getGeofenceListRequestResponse(headers, queryParameters);
        call.enqueue(new Callback<List<VisilabsGeofenceGetListResponse>>() {
            @Override
            public void onResponse(Call<List<VisilabsGeofenceGetListResponse>> call, Response<List<VisilabsGeofenceGetListResponse>> response) {
                List<VisilabsGeofenceGetListResponse> visilabsGeofenceGetListResponse = response.body();
                pCallback.success(visilabsGeofenceGetListResponse, response.raw().request().url().toString());
            }

            @Override
            public void onFailure(Call<List<VisilabsGeofenceGetListResponse>> call, Throwable t) {
                pCallback.fail(t, call.request().url().toString());
            }
        });
    }

    @Override
    public void executeAsyncAction(VisilabsCallback pCallback) throws Exception {

    }

    @Override
    public void executeAsync(VisilabsInAppMessageCallback pCallback) {

    }

    @Override
    public void executeAsyncAction(VisilabsMailSubsFormRequestCallback pCallback){

    }

    @Override
    public void executeAsyncAction(VisilabsFavsRequestCallback pCallback) {

    }

    private void fillHeaderMap(HashMap<String, String> headerMap){
        if(mHeaders != null && mHeaders.size() > 0){
            for (int i = 0 ; i < mHeaders.size() ; i++){
                headerMap.put((String)mHeaders.keySet().toArray()[i], mHeaders.get(mHeaders.keySet().toArray()[i]));
            }
        }

        headerMap.put("User-Agent", Visilabs.getUserAgent());
    }

    private void fillQueryMap(HashMap<String, String> queryMap){
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

        if(mActionID != null && !mActionID.equals("")){
            queryMap.put(VisilabsConstant.ACT_ID_KEY, mActionID);
        }
        if(mAction != null && !mAction.equals("")){
            queryMap.put(VisilabsConstant.ACT_KEY, mAction);
        }

        DecimalFormat df = new DecimalFormat("0.0000000000000");

        if(mLatitude > 0){
            String latitudeString = df.format(mLatitude);
            queryMap.put(VisilabsConstant.LATITUDE_KEY, latitudeString);
        }

        if(mLongitude > 0){
            String longitudeString = df.format(mLongitude);
            queryMap.put(VisilabsConstant.LONGITUDE_KEY, longitudeString);
        }

        if(!StringUtils.isNullOrWhiteSpace(mGeofenceID)){
            queryMap.put(VisilabsConstant.GEO_ID_KEY, mGeofenceID);
        }

        if(mApiVer != null && !mApiVer.equals("")){
            queryMap.put(VisilabsConstant.APIVER_KEY, mApiVer);
        }else{
            queryMap.put(VisilabsConstant.APIVER_KEY, "Android");
        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                queryMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
