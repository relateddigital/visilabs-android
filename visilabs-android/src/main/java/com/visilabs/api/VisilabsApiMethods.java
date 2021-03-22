package com.visilabs.api;

import com.visilabs.favs.FavsResponse;
import com.visilabs.gps.model.VisilabsGeofenceGetListResponse;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.model.VisilabsActionsResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface VisilabsApiMethods {

    //Methods to send info to the server

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

    //Methods to get InApp-type responses

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

    //Methods to get Story, MailSubsForm, FAvs-type reponses

    @GET("mobile")
    Call<ResponseBody> getGeneralActionRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("mobile")
    Call<VisilabsActionsResponse> getActionRequestResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    @GET("mobile")
    Call<FavsResponse> getFavsRequestResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //Methods to get target responses

    @GET("json")
    Call<ResponseBody> getGeneralTargetRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //Method to get promotion code

    @GET("promotion")
    Call<ResponseBody> getPromotionCodeRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );

    //Methods to get Geofence List and to send info of a geofence trigger

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
