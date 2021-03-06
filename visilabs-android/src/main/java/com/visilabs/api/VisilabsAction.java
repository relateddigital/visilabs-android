package com.visilabs.api;

import java.util.HashMap;

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

    /**
     * Use this function when you want to get raw JSON response
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsync(VisilabsCallback pCallback) throws Exception;

    /**
     * Use this function when you want to get InApp-type response
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsync(VisilabsInAppMessageCallback pCallback);

    /**
     * Use this function when you want to get GeofenceList-type response
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsync(VisilabsGeofenceGetListCallback pCallback);

    /**
     * Use this function when you want to get raw JSON response for the action
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsyncAction(VisilabsCallback pCallback) throws Exception;

    /**
     * Use this function when you want to get raw JSON response synchronously for the action
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeSyncAction(final VisilabsCallback pCallback) throws Exception;

    /**
     * Use this function when you want to get MailSubsForm-type response
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsyncAction(VisilabsActionsCallback pCallback);

    /**
     * Use this function when you want to get Favourites-type response
     *
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsyncAction(VisilabsFavsRequestCallback pCallback);

    /**
     * Use this function to get promotion code from ad server (/promotion endpoint)
     * @param pCallback the callback function
     * @throws Exception
     */
    void executeAsyncPromotionCode(VisilabsCallback pCallback, HashMap<String, String> extraQueryParameters);
}
