package com.visilabs.api;

import android.content.Context;

import com.visilabs.Visilabs;
import com.visilabs.json.JSONObject;


public abstract class VisilabsRemote implements VisilabsAction {

    protected static String LOG_TAG = "VisilabsRemote";

    protected VisilabsCallback mCallback;
    protected Context mContext;

    public VisilabsRemote(Context context) {
        mContext = context;
    }

    public void setCallback(VisilabsCallback pCallback) {
        mCallback = pCallback;
    }

    protected String getApiURL() {
        return Visilabs.getTargetURL();
    }

    protected abstract String getPath();

    protected abstract JSONObject getRequestArgs();
}
