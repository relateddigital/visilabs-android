package com.visilabs.api;

/**
 * Represents a request call to Visilabs.
 *
 * When asynchronous mode is used, the request will be executed on a separate thread and the
 * calling thread will be returned immediately.
 * When the request is completed, the callback to process the response will be executed on the
 * calling thread using the Looper class.
 * The calling thread needs to be alive for the callback to be processed properly.
 *
 * If you are making API calls on short-lived threads, consider using synchronous mode instead.
 */

public interface VisilabsAction {
    /**
     * Sets the callback function to be executed when the action is finished.
     *
     * @param pCallback the callback function
     */
    void setCallback(VisilabsCallback pCallback);

    void executeAsync() throws Exception;

    void executeAsync(VisilabsCallback pCallback) throws Exception;

    void executeAsync(VisilabsInAppMessageCallback pCallback);

    void executeAsyncAction(VisilabsCallback pCallback) throws Exception;

    void execute(VisilabsCallback pCallback) throws Exception;
}
