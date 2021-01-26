package com.visilabs.api;

import com.visilabs.inApp.InAppMessage;

import java.util.List;
import java.util.Map;

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

    @GET("actjson")
    Call<List<InAppMessage>> getInAppRequestJsonResponse(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queryParameters
    );
}
