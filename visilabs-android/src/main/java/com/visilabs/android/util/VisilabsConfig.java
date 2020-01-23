package com.visilabs.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.visilabs.android.notifications.VisilabsNotificationActivity;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class VisilabsConfig {

    private static final Object sInstanceLock = new Object();
    private static VisilabsConfig sInstance;
    private SSLSocketFactory mSSLSocketFactory;

    public static VisilabsConfig getInstance(Context context) {
        Object var1 = sInstanceLock;
        synchronized(sInstanceLock) {
            if(null == sInstance) {
                Context appContext = context.getApplicationContext();
                sInstance = readConfig(appContext);
            }
        }

        return sInstance;
    }

    static VisilabsConfig readConfig(Context appContext) {
        String packageName = appContext.getPackageName();

        try {
            ApplicationInfo e = appContext.getPackageManager().getApplicationInfo(packageName, 128);
            Bundle configBundle = e.metaData;
            if(null == configBundle) {
                configBundle = new Bundle();
            }

            return new VisilabsConfig(configBundle, appContext);
        } catch (PackageManager.NameNotFoundException var4) {
            throw new RuntimeException("Can\'t configure Visilabs with package name " + packageName, var4);
        }
    }

    VisilabsConfig(Bundle metaData, Context context) {
        SSLSocketFactory foundSSLFactory;
        try {
            SSLContext surveysAutoCheck = SSLContext.getInstance("TLS");
            surveysAutoCheck.init((KeyManager[]) null, (TrustManager[]) null, (SecureRandom) null);
            foundSSLFactory = surveysAutoCheck.getSocketFactory();
        } catch (GeneralSecurityException var14) {
            Log.i("VisilabsAPI.Conf", "System has no SSL support.", var14);
            foundSSLFactory = null;
        }

        this.mSSLSocketFactory = foundSSLFactory;
    }

    public synchronized SSLSocketFactory getSSLSocketFactory() {
        return this.mSSLSocketFactory;
    }


    public static final String VISILABS_ORGANIZATION_ID = "VisilabsOrganizationID";
    public static final String VISILABS_SITE_ID = "VisilabsSiteID";
    public static final String VISILABS_SEGMENT_URL = "VisilabsSegmentURL";
    public static final String VISILABS_DATA_SOURCE = "VisilabsDataSource";
    public static final String VISILABS_REAL_TIME_URL = "VisilabsRealTimeURL";
    public static final String VISILABS_CHANNEL = "VisilabsChannel";

    public static final String VISILABS_REQUEST_TIMEOUT_IN_SECONDS = "VisilabsRequestTimeoutInSeconds";
    public static final String VISILABS_REST_URL = "VisilabsRESTURL";
    public static final String VISILABS_ENCRYPTED_DATA_SOURCE = "VisilabsEncryptedDataSource";
    public static final String VISILABS_TARGET_URL = "VisilabsTargetURL";
    public static final String VISILABS_ACTION_URL = "VisilabsActionURL";
    public static final String VISILABS_GEOFENCE_URL = "VisilabsGeofenceURL";
    public static final String VISILABS_GEOFENCE_ENABLED = "VisilabsGeofenceEnabled";



    public static final String VERSION = "2.3.24";

    public static final int UI_FEATURES_MIN_API = 16;

    public static boolean DEBUG = false;


    public static final String LOGGER_URL = "lgr.visilabs.net";
    public static final String REAL_TIME_URL = "rt.visilabs.net";
    public static final String LOAD_BALANCE_PREFIX = "NSC";
    public static final String OM_3_KEY = "OM.3rd";


    public static final String COOKIEID_PREF = "VisilabsCookieIDPreferences";
    public static final String EXVISITORID_PREF = "VisilabsExVisitorIDPreferences";
    public static final String TOKENID_PREF = "VisilabsTokenIDPreferences";
    public static final String APPID_PREF = "VisilabsAppIDPreferences";
    public static final String TARGET_PREF = "VisilabsTargetPreferences";

    public static final String VISITOR_DATA_PREF = "VisilabsVisitorDataPreferences";
    public static final String VISITOR_DATA_PREF_KEY = "visitorData";

    public static final String COOKIEID_PREF_KEY = "cookieID";
    public static final String EXVISITORID_PREF_KEY = "exVisitorID";
    public static final String TOKENID_PREF_KEY = "sysTokenID";
    public static final String APPID_PREF_KEY = "sysAppID";

    public static final String VISILABS_ENCODING_CHARSET = "UTF-8";
    public static List<VisilabsParameter> VISILABS_PARAMETERS;

    public static final String LATITUDE_KEY = "OM.latitude";
    public static final String LONGITUDE_KEY = "OM.longitude";

    public static final String ORGANIZATIONID_KEY = "OM.oid";
    public static final String SITEID_KEY = "OM.siteID";

    public static final String COOKIEID_KEY = "OM.cookieID";
    public static final String EXVISITORID_KEY = "OM.exVisitorID";
    public static final String ZONE_ID_KEY = "OM.zid";
    public static final String BODY_KEY = "OM.body";

    public static final String TOKENID_KEY = "OM.sys.TokenID";
    public static final String APPID_KEY = "OM.sys.AppID";


    public static final String FILTER_KEY = "OM.w.f";
    public static final String APIVER_KEY = "OM.apiver";

    //geofence için
    public static final String ACT_ID_KEY = "actid";
    public static final String ACT_KEY = "act";

    public static final String TARGET_PREF_VOSS_STORE_KEY = "OM.voss";
    public static final String TARGET_PREF_VCNAME_STORE_KEY = "OM.vcname";
    public static final String TARGET_PREF_VCMEDIUM_STORE_KEY = "OM.vcmedium";
    public static final String TARGET_PREF_VCSOURCE_STORE_KEY = "OM.vcsource";
    public static final String TARGET_PREF_VSEG1_STORE_KEY = "OM.vseg1";
    public static final String TARGET_PREF_VSEG2_STORE_KEY = "OM.vseg2";
    public static final String TARGET_PREF_VSEG3_STORE_KEY = "OM.vseg3";
    public static final String TARGET_PREF_VSEG4_STORE_KEY = "OM.vseg4";
    public static final String TARGET_PREF_VSEG5_STORE_KEY = "OM.vseg5";
    public static final String TARGET_PREF_BD_STORE_KEY = "OM.bd";
    public static final String TARGET_PREF_GN_STORE_KEY = "OM.gn";
    public static final String TARGET_PREF_LOC_STORE_KEY = "OM.loc";
    public static final String TARGET_PREF_VPV_STORE_KEY = "OM.vpv";
    public static final String TARGET_PREF_LPVS_STORE_KEY = "OM.lpvs";
    public static final String TARGET_PREF_LPP_STORE_KEY = "OM.lpp";
    public static final String TARGET_PREF_VQ_STORE_KEY = "OM.vq";
    public static final String TARGET_PREF_VRDOMAIN_STORE_KEY = "OM.vrDomain";

    public static final String TARGET_PREF_VOSS_KEY = "OM.OSS";
    public static final String TARGET_PREF_VCNAME_KEY = "OM.cname";
    public static final String TARGET_PREF_VCMEDIUM_KEY = "OM.cmedium";
    public static final String TARGET_PREF_VCSOURCE_KEY = "OM.csource";
    public static final String TARGET_PREF_VSEG1_KEY = "OM.vseg1";
    public static final String TARGET_PREF_VSEG2_KEY = "OM.vseg2";
    public static final String TARGET_PREF_VSEG3_KEY = "OM.vseg3";
    public static final String TARGET_PREF_VSEG4_KEY = "OM.vseg4";
    public static final String TARGET_PREF_VSEG5_KEY = "OM.vseg5";
    public static final String TARGET_PREF_BD_KEY = "OM.bd";
    public static final String TARGET_PREF_GN_KEY = "OM.gn";
    public static final String TARGET_PREF_LOC_KEY = "OM.loc";
    public static final String TARGET_PREF_VPV_KEY = "OM.pv";
    public static final String TARGET_PREF_LPVS_KEY = "OM.pv";
    public static final String TARGET_PREF_LPP_KEY = "OM.pp";
    public static final String TARGET_PREF_VQ_KEY = "OM.q";
    public static final String TARGET_PREF_VRDOMAIN_KEY = "OM.rDomain";


    public static final String TARGET_PREF_PPR_KEY = "OM.ppr";

    public static final int LOG_LEVEL_VERBOSE = 1;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_WARNING = 4;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_NONE = Integer.MAX_VALUE;

    private static final String LOG_TAG = "VisilabsConfig";

    public static boolean checkNotificationActivityAvailable(Context context) {
        if (Build.VERSION.SDK_INT < UI_FEATURES_MIN_API) {
            return false;
        }

        final Intent notificationIntent = new Intent(context, VisilabsNotificationActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> intentActivities = packageManager.queryIntentActivities(notificationIntent, 0);
        if (intentActivities.size() == 0) {
            Log.w(LOG_TAG, VisilabsNotificationActivity.class.getName() + " is not registered as an activity in your application, so notifications can't be shown.");
            Log.i(LOG_TAG, "Please add the child tag <activity android:name=\"com.visilabs.android.notifications.VisilabsNotificationActivity\" /> to your <application> tag.");
            return false;
        }

        return true;
    }
}