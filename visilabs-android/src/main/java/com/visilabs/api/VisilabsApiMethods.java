package com.visilabs.api;

import com.visilabs.favs.FavsResponse;
import com.visilabs.gps.model.VisilabsGeofenceGetListResponse;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.mailSub.VisilabsMailSubscriptionFormResponse;
import com.visilabs.story.model.storylookingbanners.VisilabsStoryLookingBannerResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface VisilabsApiMethods {
    @GET("{dataSource}/om.gif")
    Call<Void> sendToLogger(
            @Path("dataSource") String dataSource,
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("{dataSource}/om.gif")
    Call<Void> sendToRealTime(
            @Path("dataSource") String dataSource,
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("subsjson")
    Call<Void> sendSubsJsonRequestToS(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //////////////////////////////////////////////////////

    @GET("actjson")
    Call<ResponseBody> getGeneralRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("actjson")
    Call<List<InAppMessage>> getInAppRequestResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //////////////////////////////////////////////////////

    @GET("mobile")
    Call<ResponseBody> getGeneralActionRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("mobile")
    Call<VisilabsMailSubscriptionFormResponse> getMailSubsRequestResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("mobile")
    Call<FavsResponse> getFavsRequestResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //////////////////////////////////////////////////////

    @GET("json")
    Call<ResponseBody> getGeneralTargetRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //////////////////////////////////////////////////////

    @GET("geojson")
    Call<ResponseBody> getGeneralGeofenceRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("geojson")
    Call<List<VisilabsGeofenceGetListResponse>> getGeofenceListRequestResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );
}
