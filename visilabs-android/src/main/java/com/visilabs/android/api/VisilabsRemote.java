package com.visilabs.android.api;

import android.content.Context;

import com.visilabs.android.Visilabs;
import com.visilabs.android.json.JSONObject;
import com.visilabs.android.util.VisilabsLog;

import cz.msebera.android.httpclient.Header;


public abstract class VisilabsRemote implements VisilabsAction {

    public static final String PATH_PREFIX = "/box/srv/1.1/";

    protected static String LOG_TAG = "VisilabsRemote";

    protected VisilabsTargetCallback mCallback;
    protected Context mContext;

    public VisilabsRemote(Context context) {
        mContext = context;
    }

    @Override
    public void executeAsync() throws Exception {
        executeAsync(mCallback);
    }

    @Override
    public void executeAsync(VisilabsTargetCallback pCallback) throws Exception {
        try {
            VisilabsHttpClient.post(getApiURL(), buildHeaders(null), getRequestArgs(), pCallback, false, 10);
        } catch (Exception e) {
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void execute(VisilabsTargetCallback pCallback) throws Exception {
        try {
            VisilabsHttpClient.post(getApiURL(), buildHeaders(null), getRequestArgs(), pCallback, true, 10);
        } catch (Exception e) {
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    public void setCallback(VisilabsTargetCallback pCallback) {
        mCallback = pCallback;
    }

    protected String getApiURL() {
        return Visilabs.getTargetURL();
//        String apiUrl = StringUtils.removeTrailingSlash(AppProps.getInstance().getHost());
//        return apiUrl + PATH_PREFIX + getPath();
    }

    protected abstract String getPath();

    protected abstract JSONObject getRequestArgs();

    protected abstract Header[] buildHeaders(Header[] pHeaders) throws Exception;
}
