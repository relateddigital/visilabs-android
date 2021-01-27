package com.visilabs.api;

import com.visilabs.favs.FavsResponse;
import com.visilabs.gps.model.VisilabsGeofenceGetListResponse;

import java.util.List;

/**
 * An VisilabsGeofenceGetListCallback will be used to execute code after a Visilabs Geofence GetList API request finishes running on a background thread.
 *
 * */
public interface VisilabsGeofenceGetListCallback {
    /**
     * Will be run if the target call is successful
     *
     * @param message the response data
     * @param url requested url
     */
    void success(List<VisilabsGeofenceGetListResponse> message, String url);

    /**
     * Will be run if the target call is failed
     *
     * @param t for error message i.e. t.getMessage()
     * @param url requested url
     */
    void fail(Throwable t, String url);
}
