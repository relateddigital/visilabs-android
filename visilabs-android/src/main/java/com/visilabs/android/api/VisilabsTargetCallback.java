package com.visilabs.android.api;

import com.visilabs.android.VisilabsResponse;

/**
 * An VisilabsTargetCallback will be used to execute code after a Visilabs API request finishes running on a background thread.
 *
 * */
public interface VisilabsTargetCallback {
    /**
     * Will be run if the target call is successful
     *
     * @param response the response data
     */
    void success(VisilabsResponse response);

    /**
     * Will be run if the target call is failed
     *
     * @param response the response data
     */
    void fail(VisilabsResponse response);
}
