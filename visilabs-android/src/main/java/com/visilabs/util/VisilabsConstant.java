package com.visilabs.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import com.visilabs.android.BuildConfig;
import com.visilabs.inApp.VisilabsInAppActivity;
import java.util.List;

public class VisilabsConstant {
    public static final String SDK_VERSION = BuildConfig.VERSION_NAME;

    public static final String LAST_EVENT_TIME_KEY = "last_event_time";
    public static final String PVIV_KEY = "pviv";
    public static final String TVC_KEY = "tvc";

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

    public static final int UI_FEATURES_MIN_API = 21;

    public static boolean DEBUG = false;

    public static final String LOGGER_URL = "lgr.visilabs.net";
    public static final String REAL_TIME_URL = "rt.visilabs.net";
    public static final String LOAD_BALANCE_PREFIX = "NSC";
    public static final String OM_3_KEY = "OM.3rd";
    public static final String COOKIEID_PREF = "VisilabsCookieIDPreferences";
    public static final String EXVISITORID_PREF = "VisilabsExVisitorIDPreferences";
    public static final String LAST_EVENT_TIME_PREF = "VisilabsLastEventTimePreferences";
    public static final String PVIV_PREF = "VisilabsPvivPreferences";
    public static final String TVC_PREF = "VisilabsTvcPreferences";
    public static final String TOKENID_PREF = "VisilabsTokenIDPreferences";
    public static final String APPID_PREF = "VisilabsAppIDPreferences";
    public static final String TARGET_PREF = "VisilabsTargetPreferences";
    public static final String SHOWN_STORIES_PREF = "VisilabsShownStories";
    public static final String VISILABS_PARAMETERS_PREF = "VisilabsParametersPreferences";

    public static final String VISITOR_DATA_PREF = "VisilabsVisitorDataPreferences";
    public static final String VISITOR_DATA_PREF_KEY = "visitorData";
    public static final String VISILABS_PARAMETERS_PREF_KEY = "visilabs_parameters";

    public static final String COOKIEID_PREF_KEY = "cookieID";
    public static final String EXVISITORID_PREF_KEY = "exVisitorID";
    public static final String TOKENID_PREF_KEY = "sysTokenID";
    public static final String APPID_PREF_KEY = "sysAppID";
    public static final String SHOWN_STORIES_PREF_KEY = "shownStories";

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
    public static final String SDK_VERSION_KEY = "sdk_version";
    public static final String APP_VERSION_KEY = "OM.appVersion";
    public static final String NRV_REQUEST_KEY = "OM.nrv";
    public static final String PVIV_REQUEST_KEY = "OM.pviv";
    public static final String TVC_REQUEST_KEY = "OM.tvc";
    public static final String LVT_REQUEST_KEY = "OM.lvt";

    public static final String FILTER_KEY = "OM.w.f";
    public static final String APIVER_KEY = "OM.apiver";

    public static final String DATE_KEY = "dat";
    public static final String URI_KEY = "OM.uri";
    public static final String CHANNEL_KEY = "OM.vchannel";
    public static final String DOMAIN_KEY = "OM.domain";
    public static final String TYPE_KEY = "type";
    public static final String ACTION_ID_KEY = "actionid";
    public static final String AUTH_KEY = "auth";
    public static final String SUBS_EMAIL_KEY = "OM.subsemail";
    public static final String SIGN_UP_KEY = "SignUp";
    public static final String B_SIGN_UP_KEY = "OM.b_sgnp";
    public static final String LOGIN_KEY = "Login";
    public static final String B_LOGIN_KEY = "OM.b_login";
    public static final String MAPPL_KEY = "OM.mappl";
    public static final String ADVERTISER_ID_KEY = "OM.m_adid";
    public static final String USER_AGENT_KEY = "User-Agent";
    public static final String VIS_CAP_KEY = "OM.viscap";
    public static final String V_CAP_KEY = "OM.vcap";

    //geofence için
    public static final String ACT_ID_KEY = "actid";
    public static final String ACT_KEY = "act";

    public static final String GEO_ID_KEY = "OM.locationid";

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

    public static final String STORY_CIRCLE = "50%";
    public static final String STORY_ROUNDED_RECTANGLE = "10%";
    public static final String STORY_RECTANGLE = "";
    public static final String STORY_LOOKING_BANNERS = "story_looking_banners";
    public static final String STORY_SKIN_BASED = "skin_based";
    public static final String STORY_ITEM_POSITION = "story_item_position";
    public static final String STORY_POSITION = "position";
    public static final String ACTION_DATA = "action";
    public static final String ACTION_ID = "action_id";
    public static final String ACTION_TYPE = "action_type";

    private static final String LOG_TAG = "VisilabsConfig";
    public static final String FavoriteAttributeAction = "FavoriteAttributeAction";

    public static final String SUBSJSON_ENDPOINT = "https://s.visilabs.net/subsjson";

    public static final String STORY_PHOTO_KEY = "photo";
    public static final String STORY_VIDEO_KEY = "video";

    public static boolean checkNotificationActivityAvailable(Context context) {
        if (Build.VERSION.SDK_INT < UI_FEATURES_MIN_API) {
            return false;
        }

        final Intent notificationIntent = new Intent(context, VisilabsInAppActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> intentActivities = packageManager.queryIntentActivities(notificationIntent, 0);
        if (intentActivities.size() == 0) {
            Log.w(LOG_TAG, VisilabsInAppActivity.class.getName() + " is not registered as an activity in your application, so notifications can't be shown.");
            Log.i(LOG_TAG, "Please add the child tag <activity android:name=\"com.visilabs.notifications.VisilabsNotificationActivity\" /> to your <application> tag.");
            return false;
        }
        return true;
    }
}
