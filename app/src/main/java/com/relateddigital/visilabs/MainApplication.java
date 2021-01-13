package com.relateddigital.visilabs;

import android.app.Application;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.visilabs.Visilabs;
import com.visilabs.util.VisilabsConstant;
import java.util.HashMap;
import euromsg.com.euromobileandroid.EuroMobileManager;

public class MainApplication extends Application {

    public static String ORGANIZATION_ID = "676D325830564761676D453D";
    public static String SITE_ID = "356467332F6533766975593D";
    public static String DATASOURCE = "visistore";
    public static String APPALIAS = "visilabs-android-test";


    EuroMobileManager euroMobileManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Visilabs.CreateAPI(ORGANIZATION_ID, SITE_ID, "http://lgr.visilabs.net",
                DATASOURCE, "http://rt.visilabs.net", "Android", getApplicationContext(),  "http://s.visilabs.net/json"
                , "http://s.visilabs.net/actjson", 30000, "http://s.visilabs.net/geojson", true);
        VisilabsConstant.DEBUG = true;


        euroMobileManager = EuroMobileManager.init(APPALIAS, null, getApplicationContext());
        euroMobileManager.registerToFCM(getBaseContext());
        setExistingFirebaseTokenToEuroMessage();
    }

    private void setExistingFirebaseTokenToEuroMessage() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult().getToken();
                        HashMap<String, String> parameters= new HashMap<String, String>();
                        parameters.put("OM.sys.TokenID", token);
                        parameters.put("OM.sys.AppID", APPALIAS);
                        Visilabs.CallAPI().customEvent("Register Token", parameters);
                        euroMobileManager.subscribe(token, getApplicationContext());
                    }
                });
    }

}
