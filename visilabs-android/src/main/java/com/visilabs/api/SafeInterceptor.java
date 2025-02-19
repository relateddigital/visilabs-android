package com.visilabs.api;

import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;

public class SafeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            return chain.proceed(request); // Normal isteği yap
        } catch (IOException e) {
            Log.e("SDK", "Network hatası: " + e.getMessage() + ", boş yanıt döndürülüyor.");
            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(503) // Fake bir "Service Unavailable" yanıtı döndür
                    .message("Fallback response")
                    .body(ResponseBody.create(MediaType.parse("application/json"), "{}"))
                    .build();
        }
    }
}