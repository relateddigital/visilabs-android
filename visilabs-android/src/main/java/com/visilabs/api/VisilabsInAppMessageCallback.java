package com.visilabs.api;

import com.visilabs.inApp.InAppMessage;

import java.util.List;

/**
 * An VisilabsInAppMessageCallback will be used to execute code after a Visilabs InApp API request finishes running on a background thread.
 *
 * */
public interface VisilabsInAppMessageCallback {
    /**
     * Will be run if the target call is successful
     *
     * @param messages the response data
     * @param url requested url
     */
    void success(List<InAppMessage> messages, String url);

    /**
     * Will be run if the target call is failed
     *
     * @param t for error message i.e. t.getMessage()
     * @param url requested url
     */
    void fail(Throwable t, String url);
}
