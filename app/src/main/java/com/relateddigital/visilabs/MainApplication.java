package com.relateddigital.visilabs;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.visilabs.Visilabs;
import com.visilabs.util.VisilabsConstant;
import java.util.HashMap;
import euromsg.com.euromobileandroid.EuroMobileManager;

public class MainApplication extends Application {

    private String appAlias;
    private EuroMobileManager euroMobileManager;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationInfo appInfo = null;
        Bundle bundle = null;

        try{
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        if(appInfo!=null){
            bundle = appInfo.metaData;
        }

        if(bundle!=null){
            appAlias = bundle.getString("AppAlias", "");
            Visilabs.CreateAPI(
                    bundle.getString("VisilabsOrganizationID", ""),
                    bundle.getString("VisilabsSiteID", ""),
                    bundle.getString("VisilabsSegmentURL", ""),
                    bundle.getString("VisilabsDataSource", ""),
                    bundle.getString("VisilabsRealTimeURL", ""),
                    bundle.getString("VisilabsChannel", ""),
                    getApplicationContext(),
                    bundle.getString("VisilabsTargetURL", ""),
                    bundle.getString("VisilabsActionURL", ""),
                    bundle.getInt("VisilabsRequestTimeoutInSeconds", 30),
                    bundle.getString("VisilabsGeofenceURL", ""),
                    bundle.getBoolean("VisilabsGeofenceEnabled", false)
            );
        }

        VisilabsConstant.DEBUG = true;


        euroMobileManager = EuroMobileManager.init(appAlias, null, getApplicationContext());
        euroMobileManager.registerToFCM(getBaseContext());
        setExistingFirebaseTokenToEuroMessage();
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
                        HashMap<String, String> parameters= new HashMap<String, String>();
                        parameters.put("OM.sys.TokenID", token);
                        parameters.put("OM.sys.AppID", appAlias);
                        Visilabs.CallAPI().customEvent("Register Token", parameters);
                        euroMobileManager.subscribe(token, getApplicationContext());
                    }
                });
    }
}
