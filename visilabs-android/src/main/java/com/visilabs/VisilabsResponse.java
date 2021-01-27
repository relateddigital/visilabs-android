package com.visilabs;

import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;

/**
 * Represents the response data from Visilabs when an API call completes.
 */

public class VisilabsResponse {

    private final JSONObject mResults;
    private final JSONArray mResultArray;
    private final String mResultOtherThanJSON;
    private final Throwable mError;
    private final String mErrorMessage;

    public VisilabsResponse(JSONObject pResults, JSONArray pResultArray, String resultOtherThanJSON, Throwable e, String pError) {
        mResults = pResults;
        mResultArray = pResultArray;
        mResultOtherThanJSON = resultOtherThanJSON;
        mError = e;
        mErrorMessage = pError;
    }

    public JSONObject getJson() {
        return mResults;
    }

    public JSONArray getArray() {
        return mResultArray;
    }

    public String getResultOtherThanJSON() {
        return mResultOtherThanJSON;
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
        } else if (mResultOtherThanJSON != null && !mResultOtherThanJSON.equals("")) {
            return mResultOtherThanJSON;
        } else {
            return mErrorMessage;
        }
    }
}
