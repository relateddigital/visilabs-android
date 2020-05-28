package com.relateddigital.visilabs;

import android.app.Application;

import com.visilabs.Visilabs;

public class MainApplication extends Application {

    public static String ORGANIZATION_ID = "676D325830564761676D453D";
    public static String SITE_ID = "356467332F6533766975593D";
    public static String DATASOURCE = "visistore";

    @Override
    public void onCreate() {
        super.onCreate();

        Visilabs.CreateAPI(ORGANIZATION_ID, SITE_ID, "http://lgr.visilabs.net",
                DATASOURCE, "http://rt.visilabs.net", "Android", getApplicationContext(),  "http://s.visilabs.net/json", "http://s.visilabs.net/actjson", 30000, "http://s.visilabs.net/geojson", true);
    }
}
