package com.visilabs.inApp;

import android.content.Context;
import android.util.Log;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.SApiClient;
import com.visilabs.api.VisilabsActionsCallback;
import com.visilabs.api.VisilabsApiMethods;
import com.visilabs.api.VisilabsFavsRequestCallback;
import com.visilabs.api.VisilabsGeofenceGetListCallback;
import com.visilabs.api.VisilabsInAppMessageCallback;
import com.visilabs.api.VisilabsRemote;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.favs.FavsResponse;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.model.VisilabsActionsResponse;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisilabsActionRequest extends VisilabsRemote {

    protected static final String LOG_TAG = "VisilabsNotifRequest";

    private String mApiVer = "Android";
    private String mPageName = "";
    private String mActionType  = "";
    private String mActionId = "";

    private String mPath = "";
    private HashMap<String, String> mHeaders = new HashMap<>();
    private JSONObject mArgs = new JSONObject();
    private int mTimeOutInSeconds;

    private String mVisitorData = "";
    private String mVisitData = "";

    private HashMap<String, String> mProperties;

    private VisilabsApiMethods mVisilabsSApiInterface;

    public VisilabsActionRequest(Context context) {
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

    public void setActionType(String actionType ) {
        mActionType = actionType;
    }

    public void setActionId(String actionId ) {
        mActionId = actionId;
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

        try {
            Call<ResponseBody> call = mVisilabsSApiInterface.getGeneralRequestJsonResponse(headers, queryParameters);
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
    public void executeAsyncAction(final VisilabsCallback pCallback) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillActionQueryMap(queryParameters);

        try {
            Call<ResponseBody> call = mVisilabsSApiInterface.getGeneralActionRequestJsonResponse(headers, queryParameters);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String rawJsonResponse = "";
                    try {
                        rawJsonResponse = response.body().string();

                        if (!rawJsonResponse.equals("")) {
                            Log.i(LOG_TAG, "Success Request : " + response.raw().request().url().toString());
                            VisilabsResponse visilabsResponse = new VisilabsResponse(new JSONObject(rawJsonResponse), null, null, null, null);
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
    public void executeSyncAction(final VisilabsCallback pCallback) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillActionQueryMap(queryParameters);

        try {
            Call<ResponseBody> call = mVisilabsSApiInterface.getGeneralActionRequestJsonResponse(headers, queryParameters);
            Response<ResponseBody> response = call.execute();
            String rawJsonResponse = "";
            try {
                rawJsonResponse = response.body().string();

                if (!rawJsonResponse.equals("")) {
                    Log.i(LOG_TAG, "Success Request : " + response.raw().request().url().toString());
                    VisilabsResponse visilabsResponse = new VisilabsResponse(new JSONObject(rawJsonResponse), null, null, null, null);
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not parse the response!");
        }
    }

    @Override
    public void executeAsync(final VisilabsInAppMessageCallback pCallback) {

        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillQueryMap(queryParameters);

        try {
            Call<List<InAppMessage>> call = mVisilabsSApiInterface.getInAppRequestResponse(headers, queryParameters);
            call.enqueue(new Callback<List<InAppMessage>>() {
                @Override
                public void onResponse(Call<List<InAppMessage>> call, Response<List<InAppMessage>> response) {
                    try {
                        List<InAppMessage> inAppMessages = response.body();
                        pCallback.success(inAppMessages, response.raw().request().url().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Could not parse the response for the request : " + response.raw().request().url().toString());
                        try {
                            pCallback.fail(new Throwable(response.body().toString()), call.request().url().toString());
                        } catch (Exception c){
                            c.printStackTrace();
                            pCallback.fail(new Throwable("The response is not in the correct format"), call.request().url().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<InAppMessage>> call, Throwable t) {
                    pCallback.fail(t, call.request().url().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not parse the response!");
        }
    }

    @Override
    public void executeAsync(VisilabsGeofenceGetListCallback pCallback) {

    }

    @Override
    public void executeAsyncAction(final VisilabsActionsCallback pCallback) {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillActionQueryMap(queryParameters);

        try {
            Call<VisilabsActionsResponse> call = mVisilabsSApiInterface.getActionRequestResponse(headers, queryParameters);
            call.enqueue(new Callback<VisilabsActionsResponse>() {
                @Override
                public void onResponse(Call<VisilabsActionsResponse> call, Response<VisilabsActionsResponse> response) {
                    try {
                        VisilabsActionsResponse visilabsActionsResponse = response.body();
                        pCallback.success(visilabsActionsResponse, response.raw().request().url().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Could not parse the response for the request : " + response.raw().request().url().toString());
                        try {
                            pCallback.fail(new Throwable(response.body().toString()), call.request().url().toString());
                        } catch (Exception c){
                            c.printStackTrace();
                            pCallback.fail(new Throwable("The response is not in the correct format"), call.request().url().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<VisilabsActionsResponse> call, Throwable t) {
                    pCallback.fail(t, call.request().url().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not parse the response!");
        }
    }

    @Override
    public void executeAsyncAction(final VisilabsFavsRequestCallback pCallback) {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillActionQueryMap(queryParameters);

        try {
            Call<FavsResponse> call = mVisilabsSApiInterface.getFavsRequestResponse(headers, queryParameters);
            call.enqueue(new Callback<FavsResponse>() {
                @Override
                public void onResponse(Call<FavsResponse> call, Response<FavsResponse> response) {
                    try {
                        FavsResponse favsResponse = response.body();
                        pCallback.success(favsResponse, response.raw().request().url().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Could not parse the response for the request : " + response.raw().request().url().toString());
                        try {
                            pCallback.fail(new Throwable(response.body().toString()), call.request().url().toString());
                        } catch (Exception c){
                            c.printStackTrace();
                            pCallback.fail(new Throwable("The response is not in the correct format"), call.request().url().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<FavsResponse> call, Throwable t) {
                    pCallback.fail(t, call.request().url().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not parse the response!");
        }
    }

    @Override
    public void executeAsyncPromotionCode(final VisilabsCallback pCallback, HashMap<String, String> extraQueryParameters) {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillPromotionCodeQueryMap(queryParameters);
        queryParameters.putAll(extraQueryParameters);

        try {
            Call<ResponseBody> call = mVisilabsSApiInterface.getPromotionCodeRequestJsonResponse(headers, queryParameters);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String rawJsonResponse = "";
                    try {
                        rawJsonResponse = response.body().string();

                        if (!rawJsonResponse.equals("")) {
                            JSONObject jsonResponse = new JSONObject(rawJsonResponse);
                            if(jsonResponse.getBoolean("success") && !jsonResponse.getString("promocode").equals("")) {
                                Log.i(LOG_TAG, "Success Request : " + response.raw().request().url().toString());
                                VisilabsResponse visilabsResponse = new VisilabsResponse(jsonResponse, null, null, null, null);
                                pCallback.success(visilabsResponse);
                            } else {
                                Log.e(LOG_TAG, "Empty promotion code - auth issue" + response.raw().request().url().toString());
                                VisilabsResponse visilabsResponse = new VisilabsResponse(null, null, "Empty promotion code - auth issue", null, "Empty promotion code - auth issue");
                                pCallback.fail(visilabsResponse);
                            }
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

        if(mApiVer != null && !mApiVer.equals("")){
            queryMap.put(VisilabsConstant.APIVER_KEY, mApiVer);
        }else{
            queryMap.put(VisilabsConstant.APIVER_KEY, "Android");
        }

        if(mPageName != null && !mPageName.equals("")){
            queryMap.put(VisilabsConstant.URI_KEY, mPageName);
        }else{
            queryMap.put(VisilabsConstant.URI_KEY, "");
        }

        if(mVisitorData != null && !mVisitorData.equals("")){
            queryMap.put(VisilabsConstant.VIS_CAP_KEY, mVisitorData);
        }

        if(mVisitData != null && !mVisitData.equals("")){
            queryMap.put(VisilabsConstant.V_CAP_KEY, mVisitData);
        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                queryMap.put(entry.getKey(), entry.getValue());
            }

            if(mProperties != null){
                mProperties.remove(entry.getKey());
            }
        }

        if(mProperties != null){
            for (Map.Entry<String, String> entry : mProperties.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals(VisilabsConstant.COOKIEID_KEY) ||
                        key.equals(VisilabsConstant.SITEID_KEY) ||
                        key.equals(VisilabsConstant.ORGANIZATIONID_KEY) ||
                        key.equals(VisilabsConstant.APIVER_KEY) ||
                        key.equals(VisilabsConstant.URI_KEY) ||
                        key.equals(VisilabsConstant.EXVISITORID_KEY) ||
                        key.equals(VisilabsConstant.APPID_KEY) ||
                        key.equals(VisilabsConstant.TOKENID_KEY)){
                    continue;
                }

                if(value != null && value.length() > 0){
                    queryMap.put(key, value);
                }
            }
        }
    }

    private void fillActionQueryMap(HashMap<String, String> queryMap){
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

        if(mApiVer != null && !mApiVer.equals("")){
            queryMap.put(VisilabsConstant.APIVER_KEY, mApiVer);
        }else{
            queryMap.put(VisilabsConstant.APIVER_KEY, "Android");
        }

        if(mPageName != null && !mPageName.equals("")){
            queryMap.put(VisilabsConstant.URI_KEY, mPageName);
        }else{
            queryMap.put(VisilabsConstant.URI_KEY, "");
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));

        if(Visilabs.CallAPI().getChannelName() != null && !Visilabs.CallAPI().getChannelName().equals("")){
            queryMap.put(VisilabsConstant.CHANNEL_KEY, Visilabs.CallAPI().getChannelName());
        }

        queryMap.put(VisilabsConstant.MAPPL_KEY, "true");

        if (Visilabs.CallAPI().getIdentifierForAdvertising() != null) {
            queryMap.put(VisilabsConstant.ADVERTISER_ID_KEY, Visilabs.CallAPI().getIdentifierForAdvertising());
        }

        if(mVisitorData != null && !mVisitorData.equals("")){
            queryMap.put(VisilabsConstant.VIS_CAP_KEY, mVisitorData);
        }

        if(mVisitData != null && !mVisitData.equals("")){
            queryMap.put(VisilabsConstant.V_CAP_KEY, mVisitData);
        }

        if( mActionId!= null && !mActionId.equals("")){
            queryMap.put(VisilabsConstant.ACTION_ID, mActionId);
        }

        if(mActionType != null && !mActionType.equals("")){
            queryMap.put(VisilabsConstant.ACTION_TYPE, mActionType);
        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                queryMap.put(entry.getKey(), entry.getValue());
            }

            if(mProperties != null){
                mProperties.remove(entry.getKey());
            }
        }

        if(mProperties != null){
            for (Map.Entry<String, String> entry : mProperties.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals(VisilabsConstant.COOKIEID_KEY) ||
                        key.equals(VisilabsConstant.SITEID_KEY) ||
                        key.equals(VisilabsConstant.ORGANIZATIONID_KEY) ||
                        key.equals(VisilabsConstant.APIVER_KEY) ||
                        key.equals(VisilabsConstant.URI_KEY) ||
                        key.equals(VisilabsConstant.EXVISITORID_KEY) ||
                        key.equals(VisilabsConstant.APPID_KEY) ||
                        key.equals(VisilabsConstant.TOKENID_KEY)){
                    continue;
                }

                if(value != null && value.length() > 0){
                    queryMap.put(key, value);
                }
            }
        }
    }

    private void fillPromotionCodeQueryMap(HashMap<String, String> queryMap){
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
    }
}
