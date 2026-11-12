package com.visilabs.api;

import android.content.Context;

import com.visilabs.Visilabs;
import com.visilabs.json.JSONObject;
import com.visilabs.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;


public abstract class VisilabsRemote implements VisilabsAction {

    protected static String LOG_TAG = "VisilabsRemote";

    protected VisilabsCallback mCallback;
    protected Context mContext;

    public VisilabsRemote(Context context) {
        mContext = context;
    }

    public void setCallback(VisilabsCallback pCallback) {
        mCallback = pCallback;
    }

    protected String getApiURL() {
        return Visilabs.getTargetURL();
    }

    protected abstract String getPath();

    protected abstract JSONObject getRequestArgs();

    public static String getRequestUrl(Call<?> call) {
        try {
            return call.request().url().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void removeNullEntries(Map<String, String> map) {
        if (map == null) {
            return;
        }
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry.getKey() == null || entry.getValue() == null) {
                iterator.remove();
            }
        }
    }

    static void removeReservedKeys(Map<String, String> properties, Collection<String> reservedKeys) {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        Iterator<String> iterator = properties.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (StringUtils.isNullOrWhiteSpace(key) || reservedKeys.contains(key)) {
                iterator.remove();
            }
        }
    }
}
