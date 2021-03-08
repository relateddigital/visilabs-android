
package com.visilabs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.visilabs.api.LoggerApiClient;
import com.visilabs.api.RealTimeApiClient;
import com.visilabs.api.SApiClient;
import com.visilabs.api.VisilabsAction;
import com.visilabs.api.VisilabsActionsCallback;
import com.visilabs.api.VisilabsApiMethods;
import com.visilabs.api.VisilabsInAppMessageCallback;
import com.visilabs.api.VisilabsTargetFilter;
import com.visilabs.api.VisilabsTargetRequest;
import com.visilabs.exceptions.VisilabsNotReadyException;
import com.visilabs.gps.factory.GpsFactory;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.inApp.InAppMessageManager;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.mailSub.MailSubscriptionForm;
import com.visilabs.mailSub.Report;
import com.visilabs.model.VisilabsActionsResponse;
import com.visilabs.spinToWin.SpinToWinActivity;
import com.visilabs.spinToWin.model.SpinToWinModel;
import com.visilabs.util.NetworkManager;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.Prefs;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.util.VisilabsEncoder;
import com.visilabs.util.VisilabsLog;
import com.visilabs.util.VisilabsParameter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Visilabs {
    private static final String LOG_TAG = "VisilabsAPI";
    private static int mLogLevel = VisilabsConstant.LOG_LEVEL_VERBOSE;
    private static boolean mIsCreated = false;
    private static String mTargetURL;
    private static String mActionURL;
    private static String mUserAgent;
    private List<HashMap<String, String>> mSendQueue;

    private static Visilabs visilabs = null;
    private String mOrganizationID;
    private String mSiteID;
    private String mSegmentURL;
    private String mRealTimeURL;

    private String mDataSource;
    private String mExVisitorID;
    private String mCookieID;
    private String mChannel;
    private Context mContext;
    private int mRequestTimeoutSeconds = 30;
    private String mRESTURL;
    private String mEncryptedDataSource;

    private String mVisitData = "";
    private String mVisitorData = "";

    private Cookie mCookie;

    private Boolean mCheckForNotificationsOnLoggerRequest;

    private String mIdentifierForAdvertising = null;

    private String mGeofenceURL;
    private boolean mGeofenceEnabled;

    private String mSysAppID;
    private String mSysTokenID;

    private GpsManager gpsManager = null;

    private VisilabsApiMethods mVisilabsLoggerApiInterface;
    private VisilabsApiMethods mVisilabsRealTimeApiInterface;
    private VisilabsApiMethods mVisilabsSApiInterface;


    private Visilabs(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String RESTURL, String encryptedDataSource, String targetURL, String actionURL, String geofenceURL, boolean geofenceEnabled) {
        if (context == null) {
            return;
        }

        mVisilabsLoggerApiInterface = LoggerApiClient.getClient(requestTimeoutSeconds).create(VisilabsApiMethods.class);
        mVisilabsRealTimeApiInterface = RealTimeApiClient.getClient(requestTimeoutSeconds).create(VisilabsApiMethods.class);
        mVisilabsSApiInterface = SApiClient.getClient(requestTimeoutSeconds).create(VisilabsApiMethods.class);

        mGeofenceURL = geofenceURL;
        mGeofenceEnabled = geofenceEnabled;

        mCheckForNotificationsOnLoggerRequest = true;

        mContext = context;

        if (requestTimeoutSeconds > 0) {
            mRequestTimeoutSeconds = requestTimeoutSeconds;
        }
        mRESTURL = RESTURL;
        mEncryptedDataSource = encryptedDataSource;
        mOrganizationID = VisilabsEncoder.encode(organizationID);
        mSiteID = VisilabsEncoder.encode(siteID);
        mChannel = (channel != null) ? channel : "ANDROID";
        mUserAgent = System.getProperty("http.agent");

        initVisilabsParameters();

        mSegmentURL = segmentURL;
        mDataSource = dataSource;
        mRealTimeURL = realTimeURL;
        mTargetURL = targetURL;
        mActionURL = actionURL;

        mExVisitorID = Prefs.getFromPrefs(mContext, VisilabsConstant.EXVISITORID_PREF,
                VisilabsConstant.EXVISITORID_PREF_KEY, null);
        mCookieID = Prefs.getFromPrefs(mContext, VisilabsConstant.COOKIEID_PREF,
                VisilabsConstant.COOKIEID_PREF_KEY, null);

        mSysTokenID = Prefs.getFromPrefs(mContext, VisilabsConstant.TOKENID_PREF,
                VisilabsConstant.TOKENID_PREF_KEY, null);
        mSysAppID = Prefs.getFromPrefs(mContext, VisilabsConstant.APPID_PREF,
                VisilabsConstant.APPID_PREF_KEY, null);


        mVisitorData = Prefs.getFromPrefs(mContext, VisilabsConstant.VISITOR_DATA_PREF,
                VisilabsConstant.VISITOR_DATA_PREF_KEY, null);

        if (mCookieID == null) {
            setCookieID(null);
        }

        mCookie = new Cookie();

        mSendQueue = new ArrayList<>();

        if (!mIsCreated) {
            checkNetworkStatus(mContext);
            mIsCreated = true;
        }
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID,
                                                  String segmentURL, String dataSource,
                                                  String realTimeURL, String channel, Context context) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource,
                    realTimeURL, channel, context, 30
                    , null, null, null, null,
                    null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL,
                                                  String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL,
                    channel, context, requestTimeoutSeconds
                    , null, null, null, null,
                    null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL,
                                                  String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String targetURL) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL,
                    channel, context, requestTimeoutSeconds
                    , null, null, targetURL, null, null,
                    false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL,
                                                  String dataSource, String realTimeURL, String channel, Context context
            , String targetURL, String actionURL, int requestTimeoutSeconds) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel,
                    context, requestTimeoutSeconds
                    , null, null, targetURL, actionURL, null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL,
                                                  String dataSource, String realTimeURL, String channel, Context context
            , String targetURL, String actionURL, int requestTimeoutSeconds, String geofenceURL, boolean geofenceEnabled) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel,
                    context, requestTimeoutSeconds
                    , null, null, targetURL, actionURL, geofenceURL, geofenceEnabled);
            if (geofenceEnabled && !StringUtils.isNullOrWhiteSpace(geofenceURL)) {
                Visilabs.CallAPI().startGpsManager();
            }

        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL,
                                                  String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String RESTURL, String encryptedDataSource) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel,
                    context, requestTimeoutSeconds
                    , RESTURL, encryptedDataSource, null, null, null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(Context context) {
        if (visilabs == null) {
            String organizationID = null;
            String siteID = null;
            String segmentURL = null;
            String dataSource = null;
            String realTimeURL = null;
            String channel = null;
            int requestTimeoutSeconds = 30;
            String RESTURL = null;
            String encryptedDataSource = null;
            String targetURL = null;
            String actionURL = null;
            String geofenceURL = null;
            boolean geofenceEnabled = false;

            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
                organizationID = ai.metaData.getString(VisilabsConstant.VISILABS_ORGANIZATION_ID);
                siteID = ai.metaData.getString(VisilabsConstant.VISILABS_SITE_ID);
                segmentURL = ai.metaData.getString(VisilabsConstant.VISILABS_SEGMENT_URL);
                dataSource = ai.metaData.getString(VisilabsConstant.VISILABS_DATA_SOURCE);
                realTimeURL = ai.metaData.getString(VisilabsConstant.VISILABS_REAL_TIME_URL);
                channel = ai.metaData.getString(VisilabsConstant.VISILABS_CHANNEL);

                try {
                    requestTimeoutSeconds = ai.metaData.getInt(VisilabsConstant.VISILABS_REQUEST_TIMEOUT_IN_SECONDS,
                            30);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    RESTURL = ai.metaData.getString(VisilabsConstant.VISILABS_REST_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    encryptedDataSource = ai.metaData.getString(VisilabsConstant.VISILABS_ENCRYPTED_DATA_SOURCE);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    targetURL = ai.metaData.getString(VisilabsConstant.VISILABS_TARGET_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    actionURL = ai.metaData.getString(VisilabsConstant.VISILABS_ACTION_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    geofenceURL = ai.metaData.getString(VisilabsConstant.VISILABS_GEOFENCE_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    geofenceEnabled = ai.metaData.getBoolean(VisilabsConstant.VISILABS_GEOFENCE_ENABLED,
                            false);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }


            } catch (Exception e) {
                Log.d("CreateApi", e.toString());
            }
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel,
                    context, requestTimeoutSeconds
                    , RESTURL, encryptedDataSource, targetURL, actionURL, geofenceURL, geofenceEnabled);
            if (geofenceEnabled && !StringUtils.isNullOrWhiteSpace(geofenceURL)) {
                Visilabs.CallAPI().startGpsManager();
            }
        }
        return visilabs;
    }

    public static synchronized Visilabs CallAPI() {
        if (visilabs == null) {
            Log.e(LOG_TAG, "Visilabs API has not been initialized, please call the method Visilabs() with parameters");
        }
        return visilabs;
    }

    public void setIdentifierForAdvertising(String identifierForAdvertising) {
        mIdentifierForAdvertising = identifierForAdvertising;
    }

    public String getIdentifierForAdvertising() {
        return mIdentifierForAdvertising;
    }

    public void setCheckForNotificationsOnLoggerRequest(Boolean checkForNotificationsOnLoggerRequest) {
        mCheckForNotificationsOnLoggerRequest = checkForNotificationsOnLoggerRequest;
    }

    /**
     * This method is used to send the list of the
     * applications installed from a store in the device to the server.
     * With Android 11, to get the list of the apps installed
     * in the device, you have 2 options:
     * 1-) You can add the package names of the applications
     * that you are interested in into the AndroidManifest.xml file
     * like below:
     * <manifest package="com.example.myApp">
     *     <queries>
     *         <package android:name="com.example.app1" />
     *         <package android:name="com.example.app2" />
     *     </queries>
     *     ...
     * </manifest>
     * 2-) You can add the permission below to the
     * AndroidManifest.xml files like below:
     * <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
     *     tools:ignore="QueryAllPackagesPermission" />
     *
     * For the 2nd method: Google might expect you to
     * explain why you need this permission when you upload
     * the app to Play Store.
     * https://developer.android.com/training/basics/intents/package-visibility
     */
    public void sendTheListOfAppsInstalled(){
        PackageManager packageManager = mContext.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> appsInstalled = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        JSONArray appsArray = new JSONArray();
        for(int i = 0 ; i < appsInstalled.size() ; i++) {
            ApplicationInfo currentAppInfo = appsInstalled.get(i);
            if(isSystemApp(currentAppInfo)){
                continue;
            }
            if(!isFromStore(packageManager, currentAppInfo)){
                continue;
            }
            JSONObject currentApp = new JSONObject();
            currentApp.put("app_name", currentAppInfo.loadLabel(packageManager));
            appsArray.put(currentApp);
        }
        String result = appsArray.toString();
    }


    public Cookie getCookie() {
        return mCookie;
    }

    public void setCookie(Cookie cookie) {
        mCookie = cookie;
    }

    public void trackInAppMessageClick(InAppMessage inAppMessage, String rating) {

        if (inAppMessage == null || inAppMessage.getActionData().getQs() == null || inAppMessage.getActionData().getQs().equals("")) {
            Log.w(LOG_TAG, "Notification or query string is null or empty.");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, "/OM_evt.gif");
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.DOMAIN_KEY, mDataSource + "_Android");

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        if(inAppMessage.getActionData().getQs()!=null && !inAppMessage.getActionData().getQs().equals("")) {
            String[] tempMultiQuery = inAppMessage.getActionData().getQs().split("&");
            for (String s : tempMultiQuery) {
                String[] tempQueryString = s.split("=", 2);
                if (tempQueryString.length == 2) {
                    queryMap.put(tempQueryString[0], tempQueryString[1]);
                }
            }
        }

        if(rating != null && !rating.equals("")) {
            String[] tempMultiQuery = rating.split("&");
            for (String s : tempMultiQuery) {
                String[] tempQueryString = s.split("=", 2);
                if(tempQueryString.length == 2) {
                    queryMap.put(tempQueryString[0], tempQueryString[1]);
                }
            }
        }

        queryMap.put("client", "logger");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }

        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, "Notification button tapped");
        }

        send();
    }

    public void trackStoryClick(String report) {

        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, "/OM_evt.gif");
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.DOMAIN_KEY, mDataSource + "_Android");

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        if(report != null && !report.equals("")) {
            String[] tempMultiQuery = report.split("&");
            for (String s : tempMultiQuery) {
                String[] tempQueryString = s.split("=", 2);
                if(tempQueryString.length == 2) {
                    queryMap.put(tempQueryString[0], tempQueryString[1]);
                }
            }
        }

        queryMap.put("client", "logger");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, "Notification button tapped");
        }

        send();
    }


    public void impressionStory(String report) {

        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, "/OM_evt.gif");
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.DOMAIN_KEY, mDataSource + "_Android");

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        if(report != null && !report.equals("")) {
            String[] tempMultiQuery = report.split("&");
            for (String s : tempMultiQuery) {
                String[] tempQueryString = s.split("=", 2);
                if(tempQueryString.length == 2) {
                    queryMap.put(tempQueryString[0], tempQueryString[1]);
                }
            }
        }

        queryMap.put("client", "logger");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, "Notification button tapped");
        }

        send();
    }


    private void showActions(String pageName, Activity parent, HashMap<String, String> properties) {
        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            return;
        }
        try {
            VisilabsActionRequest visilabsActionRequest = requestAction("MailSubscriptionForm~SpinToWin");
            visilabsActionRequest.setPageName(pageName);
            visilabsActionRequest.setProperties(properties);
            visilabsActionRequest.executeAsyncAction(getVisilabsActionsCallback(parent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VisilabsActionsCallback getVisilabsActionsCallback(final Activity parent) {

        return new VisilabsActionsCallback() {
            @Override
            public void success(VisilabsActionsResponse response, String url) {
                Log.i(LOG_TAG, "Success Request : " + url);

                if(response != null) {

                    if (!response.getSpinToWinList().isEmpty()) {
                        Intent intent = new Intent(parent, SpinToWinActivity.class);
                        SpinToWinModel spinToWinModel = (SpinToWinModel) response.getSpinToWinList().get(0);
                        intent.putExtra("spin-to-win-data", spinToWinModel);
                        parent.startActivity(intent);
                    } else if (!response.getMailSubscriptionForm().isEmpty()) {
                        MailSubscriptionForm mailSubscriptionForm = (MailSubscriptionForm) response.getMailSubscriptionForm().get(0);
                        new InAppMessageManager(mCookieID, mDataSource).showMailSubscriptionForm(mailSubscriptionForm, parent);
                    }
                } else {
                    Log.e(LOG_TAG, "Response is null : " + url);
                }

            }

            @Override
            public void fail(Throwable t, String url) {
                Log.e(LOG_TAG, "Fail Request : " + url);
                Log.e(LOG_TAG, "Fail Request Message : " + t.getMessage());
            }
        };
    }

    public void trackMailSubscriptionFormClick(Report report) {

        if (report == null || report.getClick() == null || report.getClick().equals("")) {
            Log.w(LOG_TAG, "Mail subs form report click is null or empty.");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, "/OM_evt.gif");
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.DOMAIN_KEY, mDataSource + "_Android");

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        if(report.getClick() != null && !report.getClick().equals("")) {
            String[] tempMultiQuery = report.getClick().split("&");
            for (String s : tempMultiQuery) {
                String[] tempQueryString = s.split("=", 2);
                if(tempQueryString.length == 2) {
                    queryMap.put(tempQueryString[0], tempQueryString[1]);
                }
            }
        }

        queryMap.put("client", "logger");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, "Notification button tapped");
        }

        send();
    }

    public void createSubsJsonRequest(String type, String actId, String auth, String email) {
        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.TYPE_KEY, type);
        queryMap.put(VisilabsConstant.ACTION_ID_KEY, actId);
        queryMap.put(VisilabsConstant.AUTH_KEY, auth);
        queryMap.put(VisilabsConstant.SUBS_EMAIL_KEY, email);

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        queryMap.put("client", "s");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
        }

        send();
    }

    public void showNotification(String pageName, Activity parent) {
        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            return;
        }
        showNotification(pageName, null, 0, parent, null);
    }

    public void showNotification(String pageName, Activity parent, HashMap<String, String> properties) {
        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            return;
        }
        showNotification(pageName, null, 0, parent, properties);
    }

    private void showNotification(String pageName, String type, int id, Activity parent, HashMap<String, String> properties) {
        if (mActionURL == null || mActionURL.equals("")) {
            Log.e(LOG_TAG, "Action URL is empty.");
        }

        VisilabsActionRequest request = new VisilabsActionRequest(mContext);
        request.setApiVer("Android");
        request.setPageName(pageName);
        request.setPath(null);
        request.setHeaders(null);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(mRequestTimeoutSeconds);

        request.setVisitorData(mVisitorData);
        request.setVisitData(mVisitData);
        request.setProperties(properties);

        try {
            request.executeAsync(getInAppMessageCallback(type, id, parent));
        } catch (Exception ex) {
            if (VisilabsConstant.DEBUG) {
                Log.e(LOG_TAG, ex.toString());
            }
        }
    }

    private VisilabsInAppMessageCallback getInAppMessageCallback(final String type, final int actId, final Activity parent) {

        return new VisilabsInAppMessageCallback() {
            @Override
            public void success(List<InAppMessage> messages, String url) {
                Log.i(LOG_TAG, "Success InApp Request : " + url);

                InAppMessage message = null;
                if (actId > 0) {
                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i) != null && messages.get(i).getActId() == actId) {
                            message = messages.get(i);
                            break;
                        }
                    }
                }
                if (message == null && type != null) {
                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i) != null && messages.get(i).getActionData().getMsgType().toString().equals(type)) {
                            message = messages.get(i);
                            break;
                        }
                    }
                }

                if (message == null && messages.size() != 0) {
                    message = messages.get(0);
                }

                if(message != null) {
                    new InAppMessageManager(mCookieID, mDataSource).showInAppMessage(message, parent);

                    if (message.getActionData().getVisitData() != null && !message.getActionData().getVisitData().equals("")) {
                        Log.v("mVisitData", message.getActionData().getVisitData());
                        mVisitData = message.getActionData().getVisitData();
                    }

                    if (message.getActionData().getVisitorData() != null && !message.getActionData().getVisitorData().equals("")) {
                        Prefs.saveToPrefs(parent, VisilabsConstant.VISITOR_DATA_PREF,
                                VisilabsConstant.VISITOR_DATA_PREF_KEY, message.getActionData().getVisitorData());

                        mVisitorData = message.getActionData().getVisitorData();

                        Log.v("mVisitorData", message.getActionData().getVisitorData());
                    }
                }
            }

            @Override
            public void fail(Throwable t, String url) {
                Log.i(LOG_TAG, "Fail InApp Request : " + url);
                Log.d(LOG_TAG, "Fail Request Message : " + t.getMessage());
            }
        };
    }


    public VisilabsTargetRequest buildTargetRequest(String zoneID, String productCode)
            throws Exception {
        VisilabsTargetRequest request = (VisilabsTargetRequest) buildAction();
        request.setZoneID(zoneID);
        request.setProductCode(productCode);
        request.setPath(null);
        request.setHeaders(null);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(mRequestTimeoutSeconds);
        request.setApiVer("Android");
        return request;
    }

    public VisilabsTargetRequest buildTargetRequest(String zoneID, String productCode, HashMap<String,
            String> properties, List<VisilabsTargetFilter> filters)
            throws Exception {
        VisilabsTargetRequest request = (VisilabsTargetRequest) buildAction();
        request.setZoneID(zoneID);
        request.setProductCode(productCode);
        request.setPath(null);
        request.setHeaders(null);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(mRequestTimeoutSeconds);
        request.setFilters(filters);
        request.setProperties(properties);
        request.setApiVer("Android");
        return request;
    }

    public VisilabsActionRequest requestActionType(String actionType) throws Exception {

        VisilabsActionRequest request = new VisilabsActionRequest(mContext);
        request.setApiVer("Android");
        request.setPath(null);
        request.setHeaders(null);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(mRequestTimeoutSeconds);
        request.setActionType(actionType);

        request.setVisitorData(mVisitorData);
        request.setVisitData(mVisitData);

        return request;
    }

    public VisilabsActionRequest requestActionId(String actionId) throws Exception {

        VisilabsActionRequest request = new VisilabsActionRequest(mContext);
        request.setApiVer("Android");
        request.setPath(null);
        request.setHeaders(null);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(mRequestTimeoutSeconds);
        request.setActionId(actionId);
        request.setVisitorData(mVisitorData);
        request.setVisitData(mVisitData);

        return request;
    }

    public VisilabsActionRequest requestAction(String actionType) throws Exception {

        VisilabsActionRequest request = new VisilabsActionRequest(mContext);
        request.setApiVer("Android");
        request.setPath(null);
        request.setHeaders(null);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(mRequestTimeoutSeconds);
        request.setActionType(actionType);

        request.setVisitorData(mVisitorData);
        request.setVisitData(mVisitData);

        return request;
    }

    private VisilabsAction buildAction() throws VisilabsNotReadyException {
        if (!mIsCreated) {
            throw new VisilabsNotReadyException();
        }
        return new VisilabsTargetRequest(mContext);
    }

    public void signUp(String exVisitorID, HashMap<String, String> properties) {
        if (StringUtils.isNullOrWhiteSpace(exVisitorID)) {
            VisilabsLog.w(LOG_TAG, "Attempted to use null or empty exVisitorID. Ignoring.");
        } else {
            if (properties == null) {
                properties = new HashMap<>();
            }
            properties.put(VisilabsConstant.EXVISITORID_KEY, exVisitorID);
            properties.put(VisilabsConstant.SIGN_UP_KEY, exVisitorID);
            properties.put(VisilabsConstant.B_SIGN_UP_KEY, "SignUp");
            customEvent("SignUpPage", properties);
        }
    }

    public void login(String exVisitorID, HashMap<String, String> properties) {
        if (StringUtils.isNullOrWhiteSpace(exVisitorID)) {
            VisilabsLog.w(LOG_TAG, "Attempted to use null or empty exVisitorID. Ignoring.");
        } else {
            if (properties == null) {
                properties = new HashMap<>();
            }
            properties.put(VisilabsConstant.EXVISITORID_KEY, exVisitorID);
            properties.put(VisilabsConstant.LOGIN_KEY, exVisitorID);
            properties.put(VisilabsConstant.B_LOGIN_KEY, "Login");
            customEvent("LoginPage", properties);
        }
    }

    public void login(String exVisitorID) {
        if (exVisitorID == null || exVisitorID.length() == 0) {
            Log.w(LOG_TAG, "Attempted to use null or empty exVisitorID. Ignoring.");
            return;
        }
        exVisitorID = VisilabsEncoder.encode(exVisitorID);
        long timeOfEvent = System.currentTimeMillis() / 1000;

        if (mExVisitorID != null && !mExVisitorID.equals(exVisitorID)) {
            setCookieID(null);
        }

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, "LoginPage");
        queryMap.put(VisilabsConstant.B_LOGIN_KEY, "Login");
        queryMap.put(VisilabsConstant.LOGIN_KEY, exVisitorID);
        queryMap.put(VisilabsConstant.EXVISITORID_KEY, exVisitorID);
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.MAPPL_KEY, "true");

        if (mIdentifierForAdvertising != null) {
            queryMap.put(VisilabsConstant.ADVERTISER_ID_KEY, mIdentifierForAdvertising);
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            queryMap.put(VisilabsConstant.TOKENID_KEY, mSysTokenID);
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            queryMap.put(VisilabsConstant.APPID_KEY, mSysAppID);
        }

        queryMap.put("client", "logger");

        setExVisitorID(exVisitorID);

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }

        send();
    }

    public void signUp(String exVisitorID) {
        if (exVisitorID == null || exVisitorID.length() == 0) {
            VisilabsLog.w(LOG_TAG, "Attempted to use nil or empty exVisitorID. Ignoring.");
            return;
        }
        try {
            exVisitorID = URLEncoder.encode(exVisitorID, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mExVisitorID != null && !mExVisitorID.equals(exVisitorID)) {
            setCookieID(null);
        }


        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, "SignUpPage");
        queryMap.put(VisilabsConstant.B_SIGN_UP_KEY, "SignUp");
        queryMap.put(VisilabsConstant.SIGN_UP_KEY, exVisitorID);
        queryMap.put(VisilabsConstant.EXVISITORID_KEY, exVisitorID);
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.MAPPL_KEY, "true");

        if (mIdentifierForAdvertising != null) {
            queryMap.put(VisilabsConstant.ADVERTISER_ID_KEY, mIdentifierForAdvertising);
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            queryMap.put(VisilabsConstant.TOKENID_KEY, mSysTokenID);
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            queryMap.put(VisilabsConstant.APPID_KEY, mSysAppID);
        }

        queryMap.put("client", "logger");

        setExVisitorID(exVisitorID);

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }

        send();
    }

    public void sendCampaignParameters(HashMap<String, String> properties) {
        if(properties == null || properties.isEmpty()){
            Log.w(LOG_TAG, "properties cannot be null or empty.");
            return;
        }
        sendEvent(properties);
    }

    private void sendEvent(HashMap<String, String> properties){
        Context context = mContext;

        if (properties != null) {
            if (properties.containsKey(VisilabsConstant.COOKIEID_KEY)) {
                mCookieID = properties.get(VisilabsConstant.COOKIEID_KEY);
                setCookieID(properties.get(VisilabsConstant.COOKIEID_KEY));
                properties.remove(VisilabsConstant.COOKIEID_KEY);
            }
            if (properties.containsKey(VisilabsConstant.EXVISITORID_KEY)) {
                if (mExVisitorID != null && !mExVisitorID.equals(properties.get(VisilabsConstant.EXVISITORID_KEY))) {
                    setCookieID(null);
                }

                mExVisitorID = properties.get(VisilabsConstant.EXVISITORID_KEY);
                setExVisitorID(mExVisitorID);
                if (!mExVisitorID.equals("")) {
                    properties.remove(VisilabsConstant.EXVISITORID_KEY);
                }
            }
            if (properties.containsKey(VisilabsConstant.CHANNEL_KEY)) {
                if (properties.get(VisilabsConstant.CHANNEL_KEY) != null) {
                    mChannel = properties.get(VisilabsConstant.CHANNEL_KEY);
                }
                properties.remove(VisilabsConstant.CHANNEL_KEY);
            }

            if (properties.containsKey(VisilabsConstant.TOKENID_KEY)) {
                if (properties.get(VisilabsConstant.TOKENID_KEY) != null) {
                    mSysTokenID = properties.get(VisilabsConstant.TOKENID_KEY);
                    setTokenID(properties.get(VisilabsConstant.TOKENID_KEY));
                }
                properties.remove(VisilabsConstant.TOKENID_KEY);
            }
            if (properties.containsKey(VisilabsConstant.APPID_KEY)) {
                if (properties.get(VisilabsConstant.APPID_KEY) != null) {
                    mSysAppID = properties.get(VisilabsConstant.APPID_KEY);
                    setAppID(properties.get(VisilabsConstant.APPID_KEY));
                }
                properties.remove(VisilabsConstant.APPID_KEY);
            }

            try {
                PersistentTargetManager.with(context).saveParameters(properties);
            } catch (Exception e) {
                VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            }
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.MAPPL_KEY, "true");

        if (mIdentifierForAdvertising != null) {
            queryMap.put(VisilabsConstant.ADVERTISER_ID_KEY, mIdentifierForAdvertising);
        }

        if (properties != null) {
            for (int i = 0; i < properties.keySet().size(); i++) {
                String key = (String) properties.keySet().toArray()[i];
                queryMap.put(key, properties.get(key));
            }
        }

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            queryMap.put(VisilabsConstant.TOKENID_KEY, mSysTokenID);
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            queryMap.put(VisilabsConstant.APPID_KEY, mSysAppID);
        }

        queryMap.put("client", "logger");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }

        send();
    }

    public void customEvent(String pageName, HashMap<String, String> properties) {
        if (pageName == null || pageName.length() == 0) {
            Log.w(LOG_TAG, "Name cannot be null");
            return;
        }
        if (properties == null) {
            properties = new HashMap<>();
        }

        properties.put(VisilabsConstant.URI_KEY, pageName);
        sendEvent(properties);
    }

    public void customEvent(String pageName, HashMap<String, String> properties, Activity parent) {
        if (pageName == null || pageName.length() == 0) {
            Log.w(LOG_TAG, "Name cannot be null");
            return;
        }

        Context context = mContext;

        if (properties != null) {
            if (properties.containsKey(VisilabsConstant.COOKIEID_KEY)) {
                mCookieID = properties.get(VisilabsConstant.COOKIEID_KEY);
                setCookieID(properties.get(VisilabsConstant.COOKIEID_KEY));
                properties.remove(VisilabsConstant.COOKIEID_KEY);
            }
            if (properties.containsKey(VisilabsConstant.EXVISITORID_KEY)) {
                if (mExVisitorID != null && !mExVisitorID.equals(properties.get(VisilabsConstant.EXVISITORID_KEY))) {
                    setCookieID(null);
                }
                mExVisitorID = properties.get(VisilabsConstant.EXVISITORID_KEY);
                setExVisitorID(mExVisitorID);
                properties.remove(VisilabsConstant.EXVISITORID_KEY);
            }
            if (properties.containsKey(VisilabsConstant.CHANNEL_KEY)) {
                if (properties.get(VisilabsConstant.CHANNEL_KEY) != null) {
                    mChannel = properties.get(VisilabsConstant.CHANNEL_KEY);
                }
                properties.remove(VisilabsConstant.CHANNEL_KEY);
            }

            if (properties.containsKey(VisilabsConstant.TOKENID_KEY)) {
                if (properties.get(VisilabsConstant.TOKENID_KEY) != null) {
                    mSysTokenID = properties.get(VisilabsConstant.TOKENID_KEY);
                    setTokenID(properties.get(VisilabsConstant.TOKENID_KEY));
                }
                properties.remove(VisilabsConstant.TOKENID_KEY);
            }
            if (properties.containsKey(VisilabsConstant.APPID_KEY)) {
                if (properties.get(VisilabsConstant.APPID_KEY) != null) {
                    mSysAppID = properties.get(VisilabsConstant.APPID_KEY);
                    setAppID(properties.get(VisilabsConstant.APPID_KEY));
                }
                properties.remove(VisilabsConstant.APPID_KEY);
            }

            try {
                PersistentTargetManager.with(context).saveParameters(properties);
            } catch (Exception e) {
                VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            }
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(VisilabsConstant.ORGANIZATIONID_KEY, mOrganizationID);
        queryMap.put(VisilabsConstant.SITEID_KEY, mSiteID);
        queryMap.put(VisilabsConstant.DATE_KEY, String.valueOf(timeOfEvent));
        queryMap.put(VisilabsConstant.URI_KEY, pageName);
        queryMap.put(VisilabsConstant.COOKIEID_KEY, mCookieID);
        queryMap.put(VisilabsConstant.CHANNEL_KEY, mChannel);
        queryMap.put(VisilabsConstant.MAPPL_KEY, "true");

        if (mIdentifierForAdvertising != null) {
            queryMap.put(VisilabsConstant.ADVERTISER_ID_KEY, mIdentifierForAdvertising);
        }

        if (properties != null) {
            for (int i = 0; i < properties.keySet().size(); i++) {
                String key = (String) properties.keySet().toArray()[i];
                queryMap.put(key, properties.get(key));
            }
        }

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            queryMap.put(VisilabsConstant.EXVISITORID_KEY, mExVisitorID);
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            queryMap.put(VisilabsConstant.TOKENID_KEY, mSysTokenID);
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            queryMap.put(VisilabsConstant.APPID_KEY, mSysAppID);
        }

        queryMap.put("client", "logger");

        synchronized (this) {
            addQueryParametersToQueue(queryMap);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                HashMap<String, String> queryMapRealTime = new HashMap<>();
                for(int i=0; i<queryMap.size(); i++){
                    if(!((String) queryMap.keySet().toArray()[i]).equals("client")) {
                        queryMapRealTime.put((String) queryMap.keySet().toArray()[i], queryMap.get(queryMap.keySet().toArray()[i]));
                    }
                }
                queryMapRealTime.put("client", "realTime");
                addQueryParametersToQueue(queryMapRealTime);
            }
        }

        if (mCheckForNotificationsOnLoggerRequest && mActionURL != null) {
            showNotification(pageName, parent, properties);
            showActions(pageName, parent, properties);
        }

        send();
    }

    public void send() {
        synchronized (this) {
            if (mSendQueue == null || mSendQueue.size() == 0) {
                return;
            }

            HashMap<String, String> nextQueryParameters = new HashMap<>();
            for(int j = 0; j < mSendQueue.get(0).size(); j++){
                Object[] keyArray = mSendQueue.get(0).keySet().toArray();
                nextQueryParameters.put((String)keyArray[j],
                        mSendQueue.get(0).get(keyArray[j]));
            }
            mSendQueue.get(0).clear();
            mSendQueue.remove(0);

            String loadBalanceCookieKey = "";
            String loadBalanceCookieValue = "";
            String OM3rdCookieValue = "";

            HashMap<String, String> headers = new HashMap<>();
            if(mUserAgent!=null) {
                headers.put(VisilabsConstant.USER_AGENT_KEY, mUserAgent);
            }

            switch (nextQueryParameters.get("client")) {
                case "logger": {

                    nextQueryParameters.remove("client");

                    if (mCookie != null) {
                        loadBalanceCookieKey = mCookie.getLoggerCookieKey();
                        loadBalanceCookieValue = mCookie.getLoggerCookieValue();
                        OM3rdCookieValue = mCookie.getLoggerOM3rdCookieValue();

                        String cookieString = "";
                        if (loadBalanceCookieKey != null && loadBalanceCookieValue != null
                                && !loadBalanceCookieKey.equals("") && !loadBalanceCookieValue.equals("")) {
                            cookieString = loadBalanceCookieKey + "=" + loadBalanceCookieValue;
                        }
                        if (OM3rdCookieValue != null && !OM3rdCookieValue.equals("")) {
                            if (!cookieString.equals("")) {
                                cookieString = cookieString + ";";
                            }
                            cookieString = cookieString + VisilabsConstant.OM_3_KEY + "=" + OM3rdCookieValue;
                        }

                        if (!cookieString.equals("")) {
                            headers.put("Cookie", cookieString);
                        }
                    }

                    Call<Void> call = mVisilabsLoggerApiInterface.sendToLogger(mDataSource, headers, nextQueryParameters);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.i(LOG_TAG, "Successful Request : " + response.raw().request().url().toString());
                            parseAndSetResponseHeaders(response.headers(), "logger");
                            send();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i(LOG_TAG, "Fail Request : " + call.request().url().toString());
                            send();
                        }
                    });
                    break;
                }
                case "realTime": {

                    nextQueryParameters.remove("client");

                    if (mCookie != null) {
                        loadBalanceCookieKey = mCookie.getRealTimeCookieKey();
                        loadBalanceCookieValue = mCookie.getRealTimeCookieValue();
                        OM3rdCookieValue = mCookie.getRealOM3rdTimeCookieValue();

                        String cookieString = "";
                        if (loadBalanceCookieKey != null && loadBalanceCookieValue != null
                                && !loadBalanceCookieKey.equals("") && !loadBalanceCookieValue.equals("")) {
                            cookieString = loadBalanceCookieKey + "=" + loadBalanceCookieValue;
                        }
                        if (OM3rdCookieValue != null && !OM3rdCookieValue.equals("")) {
                            if (!cookieString.equals("")) {
                                cookieString = cookieString + ";";
                            }
                            cookieString = cookieString + VisilabsConstant.OM_3_KEY + "=" + OM3rdCookieValue;
                        }

                        if (!cookieString.equals("")) {
                            headers.put("Cookie", cookieString);
                        }
                    }

                    Call<Void> call = mVisilabsRealTimeApiInterface.sendToRealTime(mDataSource, headers, nextQueryParameters);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.i(LOG_TAG, "Successful Request : " + response.raw().request().url().toString());
                            parseAndSetResponseHeaders(response.headers(), "realTime");
                            send();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i(LOG_TAG, "Fail Request : " + call.request().url().toString());
                            send();
                        }
                    });
                    break;
                }
                case "s": {

                    nextQueryParameters.remove("client");

                    Call<Void> call = mVisilabsSApiInterface.sendSubsJsonRequestToS(headers, nextQueryParameters);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.i(LOG_TAG, "Successful Request : " + response.raw().request().url().toString());
                            parseAndSetResponseHeaders(response.headers(), "s");
                            send();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i(LOG_TAG, "Fail Request : " + call.request().url().toString());
                            send();
                        }
                    });
                    break;
                }
            }
        }
    }

    synchronized private void setCookieID(String _cookieID) {
        String cookieID = _cookieID;
        if (cookieID == null || cookieID.equals("")) {
            cookieID = UUID.randomUUID().toString();
        }
        mCookieID = cookieID;

        String previousCookieID = Prefs.getFromPrefs(mContext, VisilabsConstant.COOKIEID_PREF,
                VisilabsConstant.COOKIEID_PREF_KEY, null);
        if (previousCookieID != null && previousCookieID.equals(cookieID)) {
            PersistentTargetManager.with(mContext).clearParameters();
        }

        Prefs.saveToPrefs(mContext, VisilabsConstant.COOKIEID_PREF, VisilabsConstant.COOKIEID_PREF_KEY, mCookieID);
    }

    synchronized private void setExVisitorID(String exVisitorID) {
        mExVisitorID = exVisitorID;

        String previousExVisitorID = Prefs.getFromPrefs(mContext, VisilabsConstant.EXVISITORID_PREF,
                VisilabsConstant.EXVISITORID_PREF_KEY, null);
        if (previousExVisitorID != null && !previousExVisitorID.equals(exVisitorID)) {
            PersistentTargetManager.with(mContext).clearParameters();
        }

        Prefs.saveToPrefs(mContext, VisilabsConstant.EXVISITORID_PREF, VisilabsConstant.EXVISITORID_PREF_KEY, mExVisitorID);
    }

    synchronized private void setTokenID(String tokenID) {
        mSysTokenID = tokenID;
        String previousTokenID = Prefs.getFromPrefs(mContext, VisilabsConstant.TOKENID_PREF,
                VisilabsConstant.TOKENID_PREF_KEY, null);
        if (previousTokenID == null || !previousTokenID.equals(tokenID)) {
            Prefs.saveToPrefs(mContext, VisilabsConstant.TOKENID_PREF, VisilabsConstant.TOKENID_PREF_KEY, mSysTokenID);
        }
    }

    synchronized private void setAppID(String appID) {
        mSysAppID = appID;
        String previousAppID = Prefs.getFromPrefs(mContext, VisilabsConstant.APPID_PREF,
                VisilabsConstant.APPID_PREF_KEY, null);
        if (previousAppID == null || !previousAppID.equals(appID)) {
            Prefs.saveToPrefs(mContext, VisilabsConstant.APPID_PREF, VisilabsConstant.APPID_PREF_KEY, mSysAppID);
        }
    }

    private void addQueryParametersToQueue(HashMap<String, String> queryParameters) {
        if (queryParameters != null && queryParameters.size() > 0) {
            mSendQueue.add(queryParameters);
        }
    }

    List<HashMap<String, String>> getSendQueue() {
        return mSendQueue;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Returns push URL
     *
     * @param source   UTM-source
     * @param campaign UTM-campaign
     * @param medium   UTM-medium
     * @param content  UTM-content
     * @return URL string with query parameters
     */
    public String getPushURL(String source, String campaign, String medium, String content) {
        long timeOfEvent = System.currentTimeMillis() / 1000;
        String pageName = "/Push";
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.vchannel=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode(pageName)
                , VisilabsEncoder.encode(mChannel));

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }
        if (source != null && source.length() > 0) {
            query += String.format("&utm_source=%s", VisilabsEncoder.encode(source));
        }
        if (campaign != null && campaign.length() > 0) {
            query += String.format("&utm_campaign=%s", VisilabsEncoder.encode(campaign));
        }
        if (medium != null && medium.length() > 0) {
            query += String.format("&utm_medium=%s", VisilabsEncoder.encode(medium));
        }
        if (content != null && content.length() > 0) {
            query += String.format("&utm_content=%s", VisilabsEncoder.encode(content));
        }

        return mRESTURL + "/" + mEncryptedDataSource + "/" + mDataSource + "/" + mCookieID + "?" + query;
    }

    /**
     * Sets the log level for the library.
     * The default level is {@link VisilabsConstant#LOG_LEVEL_ERROR}. Please make sure this is set to {@link VisilabsConstant#LOG_LEVEL_ERROR}
     * or {@link VisilabsConstant#LOG_LEVEL_NONE} before releasing the application.
     * The log level can be one of
     * <ul>
     * <li>{@link VisilabsConstant#LOG_LEVEL_VERBOSE}</li>
     * <li>{@link VisilabsConstant#LOG_LEVEL_DEBUG}</li>
     * <li>{@link VisilabsConstant#LOG_LEVEL_INFO}</li>
     * <li>{@link VisilabsConstant#LOG_LEVEL_WARNING}</li>
     * <li>{@link VisilabsConstant#LOG_LEVEL_ERROR}</li>
     * <li>{@link VisilabsConstant#LOG_LEVEL_NONE}</li>
     * </ul>
     *
     * @param logLevel The level of logging for the Visilabs library
     */
    public static void setLogLevel(int logLevel) {
        mLogLevel = logLevel;
    }

    /**
     * Gets the current log level for the Visilabs library.
     *
     * @return The current log level
     */
    public static int getLogLevel() {
        return mLogLevel;
    }


    public static String getTargetURL() {
        return mTargetURL;
    }

    public static String getActionURL() {
        return mActionURL;
    }

    public String getExVisitorID() {
        return mExVisitorID;
    }

    public String getCookieID() {
        return mCookieID;
    }

    public String getSysTokenID() {
        return mSysTokenID;
    }

    public String getSysAppID() {
        return mSysAppID;
    }

    public String getChannelName() {
        return mChannel;
    }


    public String getOrganizationID() {
        return mOrganizationID;
    }

    public String getSiteID() {
        return mSiteID;
    }

    public static boolean isOnline() {
        return NetworkManager.getInstance().isOnline();
    }

    private static void checkNetworkStatus(Context context) {
        NetworkManager networkManager = NetworkManager.init(context);
        networkManager.registerNetworkListener();
        networkManager.checkNetworkStatus();
    }

    /**
     * Gets the customized user-agent string for the SDK.
     *
     * @return customized user-agent string
     */
    public static String getUserAgent() {
        return mUserAgent;
    }

    private void initVisilabsParameters() {
        List<VisilabsParameter> visilabsParameters = new ArrayList<>();
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VOSS_KEY, VisilabsConstant.TARGET_PREF_VOSS_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VCNAME_KEY, VisilabsConstant.TARGET_PREF_VCNAME_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VCMEDIUM_KEY, VisilabsConstant.TARGET_PREF_VCMEDIUM_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VCSOURCE_KEY, VisilabsConstant.TARGET_PREF_VCSOURCE_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VSEG1_KEY, VisilabsConstant.TARGET_PREF_VSEG1_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VSEG2_KEY, VisilabsConstant.TARGET_PREF_VSEG2_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VSEG3_KEY, VisilabsConstant.TARGET_PREF_VSEG3_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VSEG4_KEY, VisilabsConstant.TARGET_PREF_VSEG4_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VSEG5_KEY, VisilabsConstant.TARGET_PREF_VSEG5_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_BD_KEY, VisilabsConstant.TARGET_PREF_BD_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_GN_KEY, VisilabsConstant.TARGET_PREF_GN_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_LOC_KEY, VisilabsConstant.TARGET_PREF_LOC_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VPV_KEY, VisilabsConstant.TARGET_PREF_VPV_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_LPVS_KEY, VisilabsConstant.TARGET_PREF_LPVS_STORE_KEY, 10, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_LPP_KEY, VisilabsConstant.TARGET_PREF_LPP_STORE_KEY,
                1, new ArrayList<String>() {{
            add(VisilabsConstant.TARGET_PREF_PPR_KEY);
        }}));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VQ_KEY, VisilabsConstant.TARGET_PREF_VQ_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConstant.TARGET_PREF_VRDOMAIN_KEY, VisilabsConstant.TARGET_PREF_VRDOMAIN_STORE_KEY, 1, null));
        VisilabsConstant.VISILABS_PARAMETERS = visilabsParameters;
    }

    public void startGpsManager() {
        int per = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (per != PackageManager.PERMISSION_GRANTED) {
            Timer timer = new Timer("startGpsManager", false);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    startGpsManager();
                }
            };
            timer.schedule(task, 5000);
            return;
        }
        gpsManager = GpsFactory.createManager(mContext);
        gpsManager.start();
    }

    public Context getContext() {
        return mContext;
    }


    public String getGeofenceURL() {
        return mGeofenceURL;
    }

    public void setExVisitorIDToNull() {
        mExVisitorID = null;
    }

    public int getRequestTimeoutSeconds(){
        return mRequestTimeoutSeconds;
    }

    private void parseAndSetResponseHeaders(Headers responseHeaders, String type){
        Set<String> names = responseHeaders.names();
        if (names.size() > 0) {
            List<String> cookies = new ArrayList<>();
            for (int i = 0; i < names.size(); i++) {
                String str = responseHeaders.name(i);
                if (str.equals("set-cookie") || str.equals("cookie")){
                    cookies.add(responseHeaders.value(i));
                }
            }

            if (cookies.size() > 0) {
                for (String cookie : cookies) {
                    String[] fields = cookie.split(";");
                    if (fields[0].toLowerCase().contains(VisilabsConstant.LOAD_BALANCE_PREFIX.toLowerCase())) {
                        String[] cookieKeyValue = fields[0].split("=");
                        if (cookieKeyValue.length > 1) {
                            String cookieKey = cookieKeyValue[0];
                            String cookieValue = cookieKeyValue[1];

                            if (type.equals("logger") && Visilabs.CallAPI().getCookie() != null) {
                                mCookie.setLoggerCookieKey(cookieKey);
                                mCookie.setLoggerCookieValue(cookieValue);

                            } else if (type.equals("realTime") && Visilabs.CallAPI().getCookie() != null) {
                                mCookie.setRealTimeCookieKey(cookieKey);
                                mCookie.setRealTimeCookieValue(cookieValue);
                            }
                        }
                    }

                    if (fields[0].toLowerCase().contains(VisilabsConstant.OM_3_KEY.toLowerCase())) {
                        String[] cookieKeyValue = fields[0].split("=");
                        if (cookieKeyValue.length > 1 || Visilabs.CallAPI().getCookie() != null) {
                            String cookieValue = cookieKeyValue[1];

                            if (type.equals("logger") && Visilabs.CallAPI().getCookie() != null) {
                                mCookie.setLoggerOM3rdCookieValue(cookieValue);
                            } else if (type.equals("realTime") && Visilabs.CallAPI().getCookie() != null) {
                                mCookie.setRealOM3rdTimeCookieValue(cookieValue);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method checks if the application
     * is a system application
     * @param applicationInfo : ApplicationInfo
     * @return true if it is a system app
     *         false if it is not a system app
     */
    private boolean isSystemApp(ApplicationInfo applicationInfo){
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     * This method checks if the application
     * is installed from a store like Play Store or Huawei App Gallery
     * @param packageManager : PackageManager
     * @param applicationInfo : ApplicationInfo
     * @return true if it is installed from a store
     *         false if is is not installed from a store
     */
    private boolean isFromStore(PackageManager packageManager, ApplicationInfo applicationInfo){
        ArrayList<String> validInstallers = new ArrayList<>();
        validInstallers.add("com.android.vending"); // Play Store
        validInstallers.add("com.huawei.appmarket"); // Huawei App Gallery
        validInstallers.add("com.amazon.venezia"); // Amazon App Store

        final String installerName = packageManager.getInstallerPackageName(applicationInfo.packageName);

        return installerName != null && validInstallers.contains(installerName);
    }
}