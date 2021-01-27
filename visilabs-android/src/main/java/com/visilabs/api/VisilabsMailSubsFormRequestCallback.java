package com.visilabs.api;

import com.visilabs.mailSub.VisilabsMailSubscriptionFormResponse;

/**
 * An VisilabsMailSubsFormRequestCallback will be used to execute code after a Visilabs Action API request finishes running on a background thread.
 *
 * */
public interface VisilabsMailSubsFormRequestCallback {
    /**
     * Will be run if the target call is successful
     *
     * @param message the response data
     * @param url requested url
     */
    void success(VisilabsMailSubscriptionFormResponse message, String url);

    /**
     * Will be run if the target call is failed
     *
     * @param t for error message i.e. t.getMessage()
     * @param url requested url
     */
    void fail(Throwable t, String url);
}
