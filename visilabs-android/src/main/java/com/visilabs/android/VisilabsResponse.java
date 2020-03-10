package com.visilabs.android;

import com.visilabs.android.json.JSONArray;
import com.visilabs.android.json.JSONObject;

/**
 * Represents the response data from Visilabs when an API call completes.
 */

public class VisilabsResponse {

    private JSONObject mResults;
    private JSONArray mResultArray;
    private Throwable mError;
    private String mErrorMessage;

    public VisilabsResponse(JSONObject pResults, JSONArray pResultArray, Throwable e, String pError) {
        mResults = pResults;
        mResultArray = pResultArray;
        mError = e;
        mErrorMessage = pError;
    }

    public JSONObject getJson() {
        return mResults;
    }

    public JSONArray getArray() {
        return mResultArray;
    }

    public Throwable getError() {
        return mError;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getRawResponse() {
        if (mResults != null) {
            return mResults.toString();
        } else if (mResultArray != null) {
            return mResultArray.toString();
        } else {
            return mErrorMessage;
        }
    }
}
