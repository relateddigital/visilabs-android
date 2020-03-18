package com.visilabs.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.util.VisilabsLog;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.conn.params.ConnRoutePNames;
import cz.msebera.android.httpclient.entity.StringEntity;

public class VisilabsHttpClient {

    private static AsyncHttpClient mClient = new AsyncHttpClient();
    private static SyncHttpClient mSyncClient = new SyncHttpClient();

    private static final String LOG_TAG = "VisilabsAPI.VisilabsHttpClient";

    public static void put(
            String pUrl,
            Header[] pHeaders,
            JSONObject pParams,
            VisilabsTargetCallback pCallback,
            boolean pUseSync,
            int pTimeOutInSeconds)
            throws Exception {
        if (Visilabs.isOnline()) {
            StringEntity entity = new StringEntity(new JSONObject().toString());
            if (pParams != null) {
                entity = new StringEntity(pParams.toString(), "UTF-8");
            }
            if (pUseSync) {
                mSyncClient.setUserAgent(Visilabs.getUserAgent());
                mSyncClient.setTimeout(pTimeOutInSeconds * 1000);
                mSyncClient.put(
                        null,
                        pUrl,
                        pHeaders,
                        entity,
                        "application/json",
                        new FHJsonHttpResponseHandler(pCallback));
            } else {
                mClient.setUserAgent(Visilabs.getUserAgent());
                mClient.setTimeout(pTimeOutInSeconds * 1000);
                mClient.put(
                        null,
                        pUrl,
                        pHeaders,
                        entity,
                        "application/json",
                        new FHJsonHttpResponseHandler(pCallback));
            }
        } else {
            VisilabsResponse res = new VisilabsResponse(null, null, new Exception("offline"), "offline");
            pCallback.fail(res);
        }
    }

    public static void get(
            String pUrl,
            Header[] pHeaders,
            JSONObject pParams,
            VisilabsTargetCallback pCallback,
            boolean pUseSync,
            int pTimeOutInSeconds)
            throws Exception {
        if (Visilabs.isOnline()) {
            if (pUseSync) {
                mSyncClient.setUserAgent(Visilabs.getUserAgent());
                mSyncClient.setTimeout(pTimeOutInSeconds * 1000);
                mSyncClient.get(
                        null,
                        pUrl,
                        pHeaders,
                        convertToRequestParams(pParams),
                        new FHJsonHttpResponseHandler(pCallback));
            } else {
                mClient.setUserAgent(Visilabs.getUserAgent());
                mClient.setTimeout(pTimeOutInSeconds * 1000);
                mClient.get(
                        null,
                        pUrl,
                        pHeaders,
                        convertToRequestParams(pParams),
                        new FHJsonHttpResponseHandler(pCallback));
            }
        } else {
            VisilabsResponse res = new VisilabsResponse(null, null, new Exception("offline"), "offline");
            pCallback.fail(res);
        }
    }

    public static void post(
            String pUrl,
            Header[] pHeaders,
            JSONObject pParams,
            VisilabsTargetCallback pCallback,
            boolean pUseSync,
            int pTimeOutInSeconds)
            throws Exception {
        if (Visilabs.isOnline()) {

            StringEntity entity = new StringEntity(new JSONObject().toString());
            if (pParams != null) {
                entity = new StringEntity(pParams.toString(), "UTF-8");
            }
            if (pUseSync) {
                mSyncClient.setUserAgent(Visilabs.getUserAgent());
                mSyncClient.setTimeout(pTimeOutInSeconds * 1000);
                mSyncClient.post(
                        null,
                        pUrl,
                        pHeaders,
                        entity,
                        "application/json",
                        new FHJsonHttpResponseHandler(pCallback));
            } else {
                mClient.setUserAgent(Visilabs.getUserAgent());
                mClient.setTimeout(pTimeOutInSeconds * 1000);
                mClient.post(
                        null,
                        pUrl,
                        pHeaders,
                        entity,
                        "application/json",
                        new FHJsonHttpResponseHandler(pCallback));
            }
        } else {
            VisilabsResponse res = new VisilabsResponse(null, null, new Exception("offline"), "offline");
            pCallback.fail(res);
        }
    }

    public static void delete(
            String pUrl,
            Header[] pHeaders,
            JSONObject pParams,
            VisilabsTargetCallback pCallback,
            boolean pUseSync,
            int pTimeOutInSeconds)
            throws Exception {
        if (Visilabs.isOnline()) {
            if (pUseSync) {
                mSyncClient.setUserAgent(Visilabs.getUserAgent());
                mSyncClient.setTimeout(pTimeOutInSeconds * 1000);
                mSyncClient.delete(
                        null,
                        pUrl,
                        pHeaders,
                        convertToRequestParams(pParams),
                        new FHJsonHttpResponseHandler(pCallback));
            } else {
                mClient.setUserAgent(Visilabs.getUserAgent());
                mClient.setTimeout(pTimeOutInSeconds * 1000);
                mClient.delete(
                        null,
                        pUrl,
                        pHeaders,
                        convertToRequestParams(pParams),
                        new FHJsonHttpResponseHandler(pCallback));
            }
        } else {
            VisilabsResponse res = new VisilabsResponse(null, null, new Exception("offline"), "offline");
            pCallback.fail(res);
        }
    }

    private static RequestParams convertToRequestParams(JSONObject pIn) {
        RequestParams rp = null;
        if (pIn != null) {
            rp = new RequestParams();
            for (Iterator<String> it = pIn.keys(); it.hasNext(); ) {
                String key = it.next();
                rp.put(key, pIn.get(key));
            }
        }
        return rp;
    }

    static class FHJsonHttpResponseHandler extends JsonHttpResponseHandler {

        private VisilabsTargetCallback callback;

        public FHJsonHttpResponseHandler(VisilabsTargetCallback pCallback) {
            super();
            callback = pCallback;
        }

        @Override
        public void onSuccess(int pStatusCode, Header[] pHeaders, org.json.JSONObject pRes) {
            VisilabsLog.v(LOG_TAG, "Got response : " + pRes.toString());
            if (callback != null) {
                VisilabsResponse fhres = new VisilabsResponse(new JSONObject(pRes.toString()), null, null, null);
                callback.success(fhres);
            }
        }

        @Override
        public void onSuccess(int pStatusCode, Header[] pHeaders, org.json.JSONArray pRes) {
            VisilabsLog.v(LOG_TAG, "Got response : " + pRes.toString());
            if (callback != null) {
                VisilabsResponse fhres = new VisilabsResponse(null, new JSONArray(pRes.toString()), null, null);
                callback.success(fhres);
            }
        }

        @Override
        public void onFailure(int pStatusCode, Header[] pHeaders, String pContent, Throwable pError) {
            /*
            VisilabsLog.e(LOG_TAG, pError.getMessage(), pError);
            */
            if (callback != null) {
                VisilabsResponse fhres = new VisilabsResponse(null, null, pError, pContent);
                callback.fail(fhres);
            }
        }

        @Override
        public void onFailure(
                int pStatusCode,
                Header[] pHeaders,
                Throwable pError,
                org.json.JSONObject pErrorResponse) {
            //VisilabsLog.e(LOG_TAG, pError.getMessage(), pError);
            String errorResponse = (pErrorResponse != null) ? pErrorResponse.toString() : "{}";
            if (callback != null) {
                VisilabsResponse fhres = new VisilabsResponse(
                        new JSONObject(errorResponse),
                        null,
                        pError,
                        errorResponse);
                callback.fail(fhres);
            }
        }

        @Override
        public void onFailure(int pStatusCode, Header[] pHeaders, Throwable pError, org.json.JSONArray pErrorResponse) {
            VisilabsLog.e(LOG_TAG, pError.getMessage(), pError);
            if (callback != null) {
                VisilabsResponse fhres = new VisilabsResponse(
                        null,
                        new JSONArray(pErrorResponse.toString()),
                        pError,
                        pErrorResponse.toString());
                callback.fail(fhres);
            }
        }
    }

    /**
     * Set both the connection and socket timeouts. By default, both are set to
     * 10 seconds.
     *
     * @param milliseconds the connect/socket timeout in milliseconds, at least 1 second
     */
    public static void setTimeout(int milliseconds) {
        mClient.setResponseTimeout(milliseconds);
        mSyncClient.setResponseTimeout(milliseconds);
    }

    public static void setHttpProxy(HttpHost proxy) {
        mClient.getHttpClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
        mSyncClient.getHttpClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
    }

}
