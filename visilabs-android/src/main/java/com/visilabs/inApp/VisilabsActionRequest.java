package com.visilabs.inApp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.visilabs.Visilabs;
import com.visilabs.api.SApiClient;
import com.visilabs.api.VisilabsApiMethods;
import com.visilabs.api.VisilabsHttpClient;
import com.visilabs.api.VisilabsInAppMessageCallback;
import com.visilabs.api.VisilabsRemote;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.json.JSONObject;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.util.VisilabsLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisilabsActionRequest extends VisilabsRemote {

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
    private String mActionType  = "";
    private String mActionId = "";

    private String mPath = "";
    private Methods mMethod = Methods.GET;
    private Header[] mHeaders = null;
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
    public void executeAsync(final VisilabsInAppMessageCallback pCallback) {

        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> queryParameters = new HashMap<>();

        //Put headers
        fillHeaderMap(headers);

        //Put query parameters
        fillQueryMap(queryParameters);

        Call<List<InAppMessage>> call = mVisilabsSApiInterface.getInAppRequestJsonResponse(headers, queryParameters);
        call.enqueue(new Callback<List<InAppMessage>>() {
            @Override
            public void onResponse(Call<List<InAppMessage>> call, Response<List<InAppMessage>> response) {
                List<InAppMessage> inAppMessages = response.body();
                pCallback.success(inAppMessages, response.raw().request().url().toString());
            }

            @Override
            public void onFailure(Call<List<InAppMessage>> call, Throwable t) {
                pCallback.fail(t, call.request().url().toString());
            }
        });
    }

    @Override
    public void execute(VisilabsCallback pCallback) throws Exception {
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

    @Override
    public void executeAsyncAction(VisilabsCallback pCallback) throws Exception {
        try {
            VisilabsHttpClient.get(getActionUrl(), buildHeaders(mHeaders), mArgs, pCallback, false, mTimeOutInSeconds);
        } catch (Exception e) {
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    public String getURL() {
        String url = Visilabs.getActionURL();
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

            if(mApiVer != null && !mApiVer.equals("")){
                uriBuilder.appendQueryParameter("OM.apiver", mApiVer);
            }else{
                uriBuilder.appendQueryParameter("OM.apiver", "Android");
            }

            if(mPageName != null && !mPageName.equals("")){
                uriBuilder.appendQueryParameter("OM.uri", mPageName);
            }else{
                uriBuilder.appendQueryParameter("OM.uri", "");
            }

            if(mVisitorData != null && !mVisitorData.equals("")){
                uriBuilder.appendQueryParameter("OM.viscap", mVisitorData);
            }

            if(mVisitData != null && !mVisitData.equals("")){
                uriBuilder.appendQueryParameter("OM.vcap", mVisitData);
            }
        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }

            if(mProperties != null){
                mProperties.remove(entry.getKey());
            }

        }

        if(mProperties != null){
            for (Map.Entry<String, String> entry : mProperties.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals(VisilabsConstant.COOKIEID_KEY) || key.equals("OM.siteID")
                        || key.equals("OM.oid") || key.equals("OM.apiver")
                        || key.equals("OM.uri") || key.equals(VisilabsConstant.EXVISITORID_KEY)
                        || key.equals(VisilabsConstant.APPID_KEY) || key.equals(VisilabsConstant.TOKENID_KEY)){
                    continue;
                }

                if(value != null && value.length() > 0){
                    uriBuilder.appendQueryParameter(key, value);
                }

            }
        }

        String finalURI = uriBuilder.build().toString();

        if(VisilabsConstant.DEBUG){
            Log.v(LOG_TAG, finalURI);
        }

        return finalURI;
    }


    public String getActionUrl() {
        String url = "https://s.visilabs.net/mobile?";
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

            if(mApiVer != null && !mApiVer.equals("")){
                uriBuilder.appendQueryParameter("OM.apiver", mApiVer);
            }else{
                uriBuilder.appendQueryParameter("OM.apiver", "Android");
            }

            if(mPageName != null && !mPageName.equals("")){
                uriBuilder.appendQueryParameter("OM.uri", mPageName);
            }else{
                uriBuilder.appendQueryParameter("OM.uri", "");
            }

            if(mVisitorData != null && !mVisitorData.equals("")){
                uriBuilder.appendQueryParameter("OM.viscap", mVisitorData);
            }

            if(mVisitData != null && !mVisitData.equals("")){
                uriBuilder.appendQueryParameter("OM.vcap", mVisitData);
            }

            if( mActionId!= null && !mActionId.equals("")){
                uriBuilder.appendQueryParameter("action_id", mActionId);
            }

            if(mActionType != null && !mActionType.equals("")){
                uriBuilder.appendQueryParameter("action_type", mActionType);
            }
        }

        HashMap<String, String> parameters = PersistentTargetManager.with(mContext).getParameters();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if(!StringUtils.isNullOrWhiteSpace(entry.getKey()) && !StringUtils.isNullOrWhiteSpace(entry.getValue())){
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }

            if(mProperties != null){
                mProperties.remove(entry.getKey());
            }

        }

        if(mProperties != null){
            for (Map.Entry<String, String> entry : mProperties.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals(VisilabsConstant.COOKIEID_KEY) || key.equals("OM.siteID") || key.equals("OM.oid") || key.equals("OM.apiver")
                        || key.equals("OM.uri") || key.equals(VisilabsConstant.EXVISITORID_KEY)
                        || key.equals(VisilabsConstant.APPID_KEY) || key.equals(VisilabsConstant.TOKENID_KEY)){
                    continue;
                }

                if(value != null && value.length() > 0){
                    uriBuilder.appendQueryParameter(key, value);
                }

            }
        }

        String finalURI = uriBuilder.build().toString();

        if(VisilabsConstant.DEBUG){
            Log.v(LOG_TAG, finalURI);
        }

        return finalURI;
    }

    protected Header[] buildHeaders(Header[] pHeaders) throws Exception {
        return  null;
    }

    private void fillHeaderMap(HashMap<String, String> headerMap){
        if(mHeaders != null && mHeaders.length > 0){
            for (Header mHeader : mHeaders) {
                headerMap.put(mHeader.getName(), mHeader.getValue());
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
}
