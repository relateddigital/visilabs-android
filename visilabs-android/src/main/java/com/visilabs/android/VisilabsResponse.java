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

    /**
     * Gets the response data as a JSONObject.
     *
     * @return a JSONObject
     */
    public JSONObject getJson() {
        return mResults;
    }

    /**
     * Gets the response data as a JSONArray.
     *
     * @return a JSONArray
     */
    public JSONArray getArray() {
        return mResultArray;
    }

    /**
     * Gets the error.
     *
     * @return the error
     */
    public Throwable getError() {
        return mError;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Gets the raw response content.
     *
     * @return the raw response content
     */
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
