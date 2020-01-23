package com.visilabs.android;

//import com.visilabs.android.Visilabs;
//import com.visilabs.android.VisilabsApp;
//import com.visilabs.android.dagger.component.AppContextComponent;
//import com.visilabs.android.dagger.component.DaggerAppContextComponent;
//import com.visilabs.android.dagger.component.DaggerVisilabsManagerComponent;
//import com.visilabs.android.dagger.component.VisilabsManagerComponent;
//import com.visilabs.android.dagger.module.AppContextModule;
//import com.visilabs.android.dagger.module.VisilabsManagerModule;
import com.visilabs.android.gps.manager.GpsManager;
//import com.visilabs.android.manager.VisilabsManager;

public enum Injector {
    INSTANCE;

    //private AppContextComponent appContextComponent;
    //private VisilabsManagerComponent visilabsManagerComponent;
    //private GpsManagerComponent gpsManagerComponent;

    private GpsManager gpsManager;
    //private VisilabsApp visilabsApplication;


    private Injector(){
    }

    /*
    public void initApplicationComponent(VisilabsApp visilabsApplication) {


        AppContextComponent appContextComponent = DaggerAppContextComponent.builder()
                .appContextModule(new AppContextModule(visilabsApplication))
                .build();
        this.appContextComponent = appContextComponent;
    }

    public AppContextComponent getAppContextComponent() {
        return appContextComponent;
    }

    public void initVisilabsManagerComponent(VisilabsManager visilabsManager) {
        VisilabsManagerComponent visilabsManagerComponent = DaggerVisilabsManagerComponent.builder()
                .visilabsManagerModule(new VisilabsManagerModule(visilabsManager))
                .build();
        this.visilabsManagerComponent = visilabsManagerComponent;
    }
    */


/*    public void initGpsManagerComponent(GpsManager gpsManager) {
        GpsManagerComponent gpsManagerComponent = DaggerGpsManagerComponent.builder()
                .gpsManagerModule(new GpsManagerModule(gpsManager))
                .build();
        this.gpsManagerComponent = gpsManagerComponent;
    }

    public GpsManagerComponent getGpsManagerComponent() {
        return gpsManagerComponent;
    }*/


    public void initGpsManager(GpsManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public GpsManager getGpsManager() {
        return gpsManager;
    }

    /*
    public void initApplication(VisilabsApp visilabsApplication){
        this.visilabsApplication = visilabsApplication;
    }
    */

}
