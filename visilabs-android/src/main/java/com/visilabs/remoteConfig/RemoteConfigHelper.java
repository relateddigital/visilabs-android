package com.visilabs.remoteConfig;

import com.visilabs.api.RemoteConfigApiClient;
import com.visilabs.api.VisilabsApiMethods;

public class RemoteConfigHelper {
    private static final String LOG_TAG = "RemoteConfigHelper";

    public static void checkRemoteConfigs() {
        if(RemoteConfigApiClient.getClient()!=null) {
            VisilabsApiMethods remoteConfigApiInterface = RemoteConfigApiClient.getClient().create(VisilabsApiMethods.class);

            // TODO : Make the request here for remote config
            // According to response save "t" or "f" to the shared pref and return
        }
    }
}
