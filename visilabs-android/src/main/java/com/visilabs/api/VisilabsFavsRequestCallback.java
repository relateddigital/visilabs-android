package com.visilabs.api;

import com.visilabs.favs.FavsResponse;

/**
 * An VisilabsFavsRequestCallback will be used to execute code after a Visilabs Action API request finishes running on a background thread.
 *
 * */
public interface VisilabsFavsRequestCallback {
    /**
     * Will be run if the target call is successful
     *
     * @param message the response data
     * @param url requested url
     */
    void success(FavsResponse message, String url);

    /**
     * Will be run if the target call is failed
     *
     * @param t for error message i.e. t.getMessage()
     * @param url requested url
     */
    void fail(Throwable t, String url);
}
