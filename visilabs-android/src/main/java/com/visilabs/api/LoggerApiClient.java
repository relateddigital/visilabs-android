package com.visilabs.api;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggerApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient(int connectTimeOutInSec) {

        if(retrofit == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient =
                    new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .connectTimeout(connectTimeOutInSec, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS);

            synchronized (LoggerApiClient.class) {
                if(retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl("https://lgr.visilabs.net/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
