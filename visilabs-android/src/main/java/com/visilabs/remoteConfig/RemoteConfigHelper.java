package com.visilabs.remoteConfig;

import android.content.Context;
import android.util.Log;

import com.visilabs.Visilabs;
import com.visilabs.api.RemoteConfigApiClient;
import com.visilabs.api.VisilabsApiMethods;
import com.visilabs.util.Prefs;
import com.visilabs.util.VisilabsConstant;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteConfigHelper {
    private static final String LOG_TAG = "RemoteConfigHelper";

    public static void checkRemoteConfigs(final Context context) {
        if(RemoteConfigApiClient.getClient()!=null) {
            VisilabsApiMethods remoteConfigApiInterface = RemoteConfigApiClient.getClient().create(VisilabsApiMethods.class);

            HashMap<String, String> headers = new HashMap<>();
            headers.put(VisilabsConstant.USER_AGENT_KEY, Visilabs.getUserAgent());
            Call<List<String>> call = remoteConfigApiInterface.getRemoteConfig(headers);
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    try {
                        Log.i(LOG_TAG, "Successful Request : " + response.raw().request().url().toString());
                        List<String> profileIds = response.body();
                        boolean isMatch = false;
                        if (profileIds != null) {
                            if(!profileIds.isEmpty()) {
                            for (int i = 0; i < profileIds.size(); i++) {
                                if (Visilabs.CallAPI().getSiteID().equals(profileIds.get(i))) {
                                    isMatch = true;
                                    setBlockState(context, true);
                                    break;
                                }
                            }
                            if (!isMatch) {
                                setBlockState(context, false);
                            }
                           }
                            else {
                                setBlockState(context, false);
                                Log.w(LOG_TAG, "ProfileIds is empty!");
                            }
                        }else {
                            Log.w(LOG_TAG, "ProfileIds is null!");
                            setBlockState(context, false);
                        }
                    } catch (Exception e) {
                        Log.w(LOG_TAG, "Could not parse the response!");
                        setBlockState(context, false);
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Log.w(LOG_TAG, "Fail Request : " + call.request().url().toString());
                    setBlockState(context, false);
                }
            });
        }
    }

    public static void setBlockState(Context context, boolean isBlock) {
        if(isBlock) {
            Prefs.saveToPrefs(context, VisilabsConstant.VISILABS_BLOCK_PREF,
                    VisilabsConstant.VISILABS_BLOCK_PREF_KEY, "t");
        } else {
            Prefs.saveToPrefs(context, VisilabsConstant.VISILABS_BLOCK_PREF,
                    VisilabsConstant.VISILABS_BLOCK_PREF_KEY, "f");
        }
    }
}
