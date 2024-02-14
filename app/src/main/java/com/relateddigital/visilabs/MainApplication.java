package com.relateddigital.visilabs;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.relateddigital.visilabs.model.Profile;
import com.visilabs.Visilabs;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import euromsg.com.euromobileandroid.EuroMobileManager;

public class MainApplication extends MultiDexApplication {

    private String appAlias;
    private EuroMobileManager euroMobileManager;
    private Boolean isTest;
    private String organizationId;
    private String profileId;
    private String dataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationInfo appInfo = null;
        Bundle bundle = null;

        isTest = false;

        organizationId = "676D325830564761676D453D";
        profileId = "356467332F6533766975593D";
        dataSource = "visistore";

        //search-test
        if(getProfile("search-test") != null){
            organizationId = Objects.requireNonNull(getProfile("search-test")).getOrganizationId();
            profileId = Objects.requireNonNull(getProfile("search-test")).getProfileId();
            dataSource = Objects.requireNonNull(getProfile("search-test")).getDataSource();
        }


        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (appInfo != null) {
            bundle = appInfo.metaData;
        }
        if (isTest) {
            organizationId = "394A48556A2F76466136733D";
            profileId = "75763259366A3345686E303D";
            dataSource = "mrhp";
            VisilabsConstant.ACTION_ENDPOINT = "http://tests.visilabs.net/";
        }

        if (bundle != null) {
            appAlias = bundle.getString("AppAlias", "");
            Visilabs.CreateAPI(
                    organizationId,
                    profileId,
                    bundle.getString("VisilabsSegmentURL", ""),
                    dataSource,
                    bundle.getString("VisilabsRealTimeURL", ""),
                    bundle.getString("VisilabsChannel", ""),
                    getApplicationContext(),
                    bundle.getString("VisilabsTargetURL", ""),
                    bundle.getString("VisilabsActionURL", ""),
                    bundle.getInt("VisilabsRequestTimeoutInSeconds", 30),
                    bundle.getString("VisilabsGeofenceURL", ""),
                    bundle.getBoolean("VisilabsGeofenceEnabled", false),
                    bundle.getString("VisilabsSdkType", "native")
            );
        }

        //Check if the location permission is granted
        //If not, warn the user that it is needed to start gps.
        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (bundle != null) {
            if (bundle.getBoolean("VisilabsGeofenceEnabled", false)
                    && !StringUtils.isNullOrWhiteSpace(bundle.getString("VisilabsGeofenceURL", ""))) {
                if (!(accessFineLocationPermission || accessCoarseLocationPermission)) {
                    Toast.makeText(this, "Location permission is needed to start geofence!", Toast.LENGTH_LONG).show();
                }
            }
        }

        VisilabsConstant.DEBUG = true;


        euroMobileManager = EuroMobileManager.init(appAlias, null, getApplicationContext());
        euroMobileManager.registerToFCM(getBaseContext());
        setExistingFirebaseTokenToEuroMessage();

        if (!EuroMobileManager.checkPlayService(getApplicationContext())) {
            setHuaweiTokenToEuromessage();
        }
    }

    private void setExistingFirebaseTokenToEuroMessage() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        HashMap<String, String> parameters = new HashMap<String, String>();
                        parameters.put("OM.sys.TokenID", token);
                        parameters.put("OM.sys.AppID", appAlias);
                        Visilabs.CallAPI().customEvent("Register Token", parameters);
                        euroMobileManager.subscribe(token, getApplicationContext());
                    }
                });
    }

    private void setHuaweiTokenToEuromessage() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = AGConnectServicesConfig.fromContext(getApplicationContext()).getString("client/app_id");
                    final String token = HmsInstanceId.getInstance(getApplicationContext()).getToken(appId, "HCM");

                    HashMap<String, String> parameters = new HashMap<String, String>();
                    parameters.put("OM.sys.TokenID", token);
                    parameters.put("OM.sys.AppID", appAlias);
                    Visilabs.CallAPI().customEvent("Register Token", parameters);
                    euroMobileManager.subscribe(token, getApplicationContext());

                } catch (ApiException e) {
                    Log.e("Huawei Token", "get token failed, " + e);
                }
            }
        }.start();
    }

    private String getProfiles() {
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("profiles.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Profile getProfile(String key) {
        try {
            String profilesString = getProfiles();
            Gson gson = new Gson();
            Type listUserType = new TypeToken<Map<String, Profile>>() {
            }.getType();
            Map<String, Profile> profiles = gson.fromJson(profilesString, listUserType);
            return profiles != null ? profiles.get(key) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Profile();
        }
    }

}
