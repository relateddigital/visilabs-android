package com.visilabs.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface VisilabsApiMethods {
    @POST("")
    Call<Void> sendLogs(@HeaderMap Map<String, String> headers);
}
