package com.visilabs.util;

import android.util.Log;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class VisilabsEncoder {
    private static final String LOG_TAG = "VisilabsAPI.Encoder";

    public static String encode(String value){
        try {
            return URLEncoder.encode(value, VisilabsConstant.VISILABS_ENCODING_CHARSET).replace("+", "%20");
        }catch (Exception e) {
            Log.e(LOG_TAG, "Can't encode value.");
            return "";
        }
    }

    public static String decode(String value){
        try {
            return URLDecoder.decode(value, VisilabsConstant.VISILABS_ENCODING_CHARSET);
        }catch (Exception e) {
            Log.e(LOG_TAG, "Can't decode value.");
            return "";
        }
    }
}
