package com.visilabs.util;

import android.util.Log;
import com.visilabs.Visilabs;

public class VisilabsLog {
    private static void log(int pLogLevel, String pTag, String pMessage, Throwable pThrowable) {
        if (pLogLevel >= Visilabs.getLogLevel()) {
            if (pLogLevel == VisilabsConstant.LOG_LEVEL_VERBOSE) {
                Log.v(pTag, pMessage);
            } else if (pLogLevel == VisilabsConstant.LOG_LEVEL_DEBUG) {
                Log.d(pTag, pMessage);
            } else if (pLogLevel == VisilabsConstant.LOG_LEVEL_INFO) {
                Log.i(pTag, pMessage);
            } else if (pLogLevel == VisilabsConstant.LOG_LEVEL_WARNING) {
                Log.w(pTag, pMessage);
            } else if (pLogLevel == VisilabsConstant.LOG_LEVEL_ERROR) {
                if (null == pThrowable) {
                    Log.e(pTag, pMessage);
                } else {
                    Log.e(pTag, pMessage, pThrowable);
                }
            }
        }
    }

    public static void v(String pTag, String pMessage) {
        log(VisilabsConstant.LOG_LEVEL_VERBOSE, pTag, pMessage, null);
    }

    public static void d(String pTag, String pMessage) {
        log(VisilabsConstant.LOG_LEVEL_DEBUG, pTag, pMessage, null);
    }

    public static void i(String pTag, String pMessage) {
        log(VisilabsConstant.LOG_LEVEL_INFO, pTag, pMessage, null);
    }

    public static void w(String pTag, String pMessage) {
        log(VisilabsConstant.LOG_LEVEL_WARNING, pTag, pMessage, null);
    }

    public static void e(String pTag, String pMessage, Throwable pThrowable) {
        log(VisilabsConstant.LOG_LEVEL_ERROR, pTag, pMessage, pThrowable);
    }
}
