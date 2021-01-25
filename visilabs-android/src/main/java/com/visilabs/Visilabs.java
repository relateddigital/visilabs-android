
package com.visilabs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.visilabs.api.VisilabsAction;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.api.VisilabsTargetFilter;
import com.visilabs.api.VisilabsTargetRequest;
import com.visilabs.exceptions.VisilabsNotReadyException;
import com.visilabs.gps.factory.GpsFactory;
import com.visilabs.gps.factory.GpsFactory2;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.gps.manager.GpsManagerMoreThanOreo;
import com.visilabs.inApp.InAppMessageManager;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.mailSub.MailSubscriptionForm;
import com.visilabs.mailSub.Report;
import com.visilabs.mailSub.VisilabsMailSubscriptionFormResponse;
import com.visilabs.util.Device;
import com.visilabs.util.NetworkManager;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.Prefs;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.util.VisilabsEncoder;
import com.visilabs.util.VisilabsLog;
import com.visilabs.util.VisilabsParameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Visilabs implements VisilabsURLConnectionCallbackInterface {
    private static final String LOG_TAG = "VisilabsAPI";
    private static int mLogLevel = VisilabsConstant.LOG_LEVEL_VERBOSE;
    private static boolean mIsCreated = false;
    private static String mDeviceUserAgent;
    private static String mTargetURL;
    private static String mActionURL;
    private List<String> mSendQueue;

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
    private String mUserAgent;
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
    private GpsManagerMoreThanOreo gpsManagerMoreThanOreo = null;


    private Visilabs(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String RESTURL, String encryptedDataSource, String targetURL, String actionURL, String geofenceURL, boolean geofenceEnabled) {
        if (context == null) {
            return;
        }

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
        mDeviceUserAgent = Device.getUserAgent();

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


    public Cookie getCookie() {
        return mCookie;
    }

    public void setCookie(Cookie cookie) {
        mCookie = cookie;
    }

    public void trackInAppMessageClick(InAppMessage inAppMessage, String rating) {

        if (inAppMessage == null || inAppMessage.getQueryString() == null || inAppMessage.getQueryString().equals("")) {
            Log.w(LOG_TAG, "Notification or query string is null or empty.");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.cookieID=%s&OM.vchannel=%s&OM.domain=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode("/OM_evt.gif")
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode(mDataSource + "_Android"));


        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }

        query += String.format("&%s", inAppMessage.getQueryString());

        if(rating != null) {
            query += rating;
        }


        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;
        String realURL = "";


        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, String.format("Notification button tapped %s", segURL));
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        send();

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
    }

    public void trackStoryClick(String report) {

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.cookieID=%s&OM.vchannel=%s&OM.domain=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode("/OM_evt.gif")
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode(mDataSource + "_Android"));


        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }

        query += "&"+report;


        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;
        String realURL = "";


        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, String.format("Notification button tapped %s", segURL));
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        send();

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
    }


    public void impressionStory(String report) {

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.cookieID=%s&OM.vchannel=%s&OM.domain=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode("/OM_evt.gif")
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode(mDataSource + "_Android"));


        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }

        query += "&"+report;


        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;
        String realURL = "";


        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, String.format("Notification button tapped %s", segURL));
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        send();

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
    }


    private void showMailSubscriptionForm(String pageName, Activity parent, HashMap<String, String> properties) {
        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            return;
        }
        try {
            VisilabsActionRequest visilabsActionRequest = requestAction("MailSubscriptionForm");
            visilabsActionRequest.setPageName(pageName);
            visilabsActionRequest.setProperties(properties);
            visilabsActionRequest.executeAsyncAction(getVisilabsMailSubscriptionFormCallback(parent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VisilabsCallback getVisilabsMailSubscriptionFormCallback(final Activity parent) {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {
                    VisilabsMailSubscriptionFormResponse visilabsMailSubscriptionFormResponse =
                            new Gson().fromJson(response.getRawResponse(), VisilabsMailSubscriptionFormResponse.class);
                    if(visilabsMailSubscriptionFormResponse != null && !visilabsMailSubscriptionFormResponse.
                            getMailSubscriptionForm().isEmpty()) {
                        MailSubscriptionForm mailSubscriptionForm = visilabsMailSubscriptionFormResponse.
                                getMailSubscriptionForm().get(0);
                        new InAppMessageManager(mCookieID, mDataSource).showMailSubscriptionForm(mailSubscriptionForm, parent);
                    }
                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(LOG_TAG, response.getRawResponse());
            }
        };
    }

    public void trackMailSubscriptionFormClick(Report report) {

        if (report == null || report.getClick() == null || report.getClick().equals("")) {
            Log.w(LOG_TAG, "Mail subs form report click is null or empty.");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.cookieID=%s&OM.vchannel=%s&OM.domain=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode("/OM_evt.gif")
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode(mDataSource + "_Android"));


        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }

        query += String.format("&%s", report.getClick());

        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;
        String realURL = "";


        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }


        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, String.format("Notification button tapped %s", segURL));
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        send();

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
    }

    public void createSubsJsonRequest(String actId, String auth, String email) {
        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&&OM.cookieID=%s&type=%s&actionid=%s&auth=%s&OM.subsemail=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , VisilabsEncoder.encode(mCookieID)
                , "subscription_email"
                , actId
                , auth
                , email);
        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }
        String mailSubsURL = VisilabsConstant.SUBSJSON_ENDPOINT + "?" + query;
        synchronized (this) {
            addUrlToQueue(mailSubsURL);
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
        request.setMethod(VisilabsActionRequest.Methods.GET);
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

    private VisilabsCallback getInAppMessageCallback(final String type, final int actId, final Activity parent) {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();

                if (VisilabsConstant.DEBUG) {
                    Log.d(LOG_TAG, rawResponse);
                }
                try {

                    JSONArray array = response.getArray();
                    if (array != null) {
                        ArrayList<InAppMessage> inAppMessageList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            InAppMessage inAppMessage = new InAppMessage(obj, mContext);
                            if (inAppMessage != null) {
                                inAppMessageList.add(inAppMessage);
                            }
                        }

                        InAppMessage inAppMessage = null;
                        if (actId > 0) {
                            for (int i = 0; i < inAppMessageList.size(); i++) {
                                if (inAppMessageList.get(i) != null && inAppMessageList.get(i).getId() == actId) {
                                    inAppMessage = inAppMessageList.get(i);
                                    break;
                                }
                            }
                        }
                        if (inAppMessage == null && type != null) {
                            for (int i = 0; i < inAppMessageList.size(); i++) {
                                if (inAppMessageList.get(i) != null && inAppMessageList.get(i).getType().toString().equals(type)) {
                                    inAppMessage = inAppMessageList.get(i);
                                    break;
                                }
                            }
                        }

                        if (inAppMessage == null && inAppMessageList.size() != 0) {
                            inAppMessage = inAppMessageList.get(0);
                        }

                        if (inAppMessage != null) {

                            new InAppMessageManager(mCookieID, mDataSource).showInAppMessage(inAppMessage, parent);

                            if (inAppMessage.getVisitData() != null && !inAppMessage.getVisitData().equals("")) {
                                Log.v("mVisitData", inAppMessage.getVisitData());
                                mVisitData = inAppMessage.getVisitData();
                            }

                            if (inAppMessage.getVisitorData() != null && !inAppMessage.getVisitorData().equals("")) {
                                Prefs.saveToPrefs(parent, VisilabsConstant.VISITOR_DATA_PREF,
                                        VisilabsConstant.VISITOR_DATA_PREF_KEY, inAppMessage.getVisitorData());

                                mVisitorData = inAppMessage.getVisitorData();

                                Log.v("mVisitorData", inAppMessage.getVisitorData());
                            }
                        } else {
                            if (VisilabsConstant.DEBUG) {
                                Log.d(LOG_TAG, "There is no notification for your criteria");
                            }
                        }
                    }
                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                Log.e(LOG_TAG, response.getErrorMessage());
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
        request.setMethod(VisilabsTargetRequest.Methods.GET);
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
        request.setMethod(VisilabsTargetRequest.Methods.GET);
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
        request.setMethod(VisilabsActionRequest.Methods.GET);
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
        request.setMethod(VisilabsActionRequest.Methods.GET);
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
        request.setMethod(VisilabsActionRequest.Methods.GET);
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
            properties.put("OM.exVisitorID", exVisitorID);
            properties.put("SignUp", exVisitorID);
            properties.put("OM.b_sgnp", "SignUp");
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
            properties.put("OM.exVisitorID", exVisitorID);
            properties.put("Login", exVisitorID);
            properties.put("OM.b_login", "Login");
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


        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=LoginPage&OM.b_login=Login&Login=%s&OM.exVisitorID=%s&OM.cookieID=%s&OM.vchannel=%s&OM.mappl=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode(exVisitorID)
                , VisilabsEncoder.encode(exVisitorID)
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode("true"));

        if (mIdentifierForAdvertising != null) {
            query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(mIdentifierForAdvertising));
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(mSysTokenID));
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(mSysAppID));
        }

        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;

        String realURL = "";
        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }

        synchronized (this) {
            setExVisitorID(exVisitorID);
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        send();
        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
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
        String query = "";
        try {
            query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=SignUpPage&OM.b_sgnp=SignUp&SignUp=%s&OM.exVisitorID=%s&OM.cookieID=%s&OM.vchannel=%s&OM.mappl=%s"
                    , URLEncoder.encode(mOrganizationID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(mSiteID, "UTF-8").replace("+", "%20")
                    , timeOfEvent
                    , URLEncoder.encode(exVisitorID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(exVisitorID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(mCookieID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(mChannel, "UTF-8").replace("+", "%20")
                    , VisilabsEncoder.encode("true"));

            if (mIdentifierForAdvertising != null) {
                query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(mIdentifierForAdvertising));
            }

            if (mSysTokenID != null && mSysTokenID.length() > 0) {
                query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(mSysTokenID));
            }

            if (mSysAppID != null && mSysAppID.length() > 0) {
                query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(mSysAppID));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;

        String realURL = "";
        if (mRealTimeURL != null && !mRealTimeURL .equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }

        synchronized (this) {
            setExVisitorID(exVisitorID);
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }
        send();
        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
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
            if (properties.containsKey("OM.cookieID")) {
                mCookieID = properties.get("OM.cookieID");
                setCookieID(properties.get("OM.cookieID"));
                properties.remove("OM.cookieID");
            }
            if (properties.containsKey("OM.exVisitorID")) {
                if (mExVisitorID != null && !mExVisitorID.equals(properties.get("OM.exVisitorID"))) {
                    setCookieID(null);
                }

                mExVisitorID = properties.get("OM.exVisitorID");
                setExVisitorID(mExVisitorID);
                if (!mExVisitorID.equals("")) {
                    properties.remove("OM.exVisitorID");
                }
            }
            if (properties.containsKey("OM.vchannel")) {
                if (properties.get("OM.vchannel") != null) {
                    mChannel = properties.get("OM.vchannel");
                }
                properties.remove("OM.vchannel");
            }

            if (properties.containsKey("OM.sys.TokenID")) {
                if (properties.get("OM.sys.TokenID") != null) {
                    mSysTokenID = properties.get("OM.sys.TokenID");
                    setTokenID(properties.get("OM.sys.TokenID"));
                }
                properties.remove("OM.sys.TokenID");
            }
            if (properties.containsKey("OM.sys.AppID")) {
                if (properties.get("OM.sys.AppID") != null) {
                    mSysAppID = properties.get("OM.sys.AppID");
                    setAppID(properties.get("OM.sys.AppID"));
                }
                properties.remove("OM.sys.AppID");
            }

            try {
                PersistentTargetManager.with(context).saveParameters(properties);
            } catch (Exception e) {
                VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            }
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.cookieID=%s&OM.vchannel=%s&OM.mappl=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode("true"));

        if (mIdentifierForAdvertising != null) {
            query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(mIdentifierForAdvertising));
        }

        if (properties != null) {
            StringBuilder additionalURL = new StringBuilder();
            for (int i = 0; i < properties.keySet().size(); i++) {
                String key = (String) properties.keySet().toArray()[i];
                additionalURL.append(VisilabsEncoder.encode(key)).append("=").append(VisilabsEncoder.
                        encode(properties.get(key)));

                if (i < properties.keySet().size() - 1) {
                    additionalURL.append("&");
                }
            }
            if (!additionalURL.toString().equals("") && additionalURL.length() > 0) {
                query += "&" + additionalURL;
            }
        }

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(mSysTokenID));
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(mSysAppID));
        }

        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;
        String realURL = "";

        if (mRealTimeURL != null && mRealTimeURL != "") {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }

        synchronized (this) {
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        send();

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }

    }

    public void customEvent(String pageName, HashMap<String, String> properties) {
        if (pageName == null || pageName.length() == 0) {
            Log.w(LOG_TAG, "Name cannot be null");
            return;
        }
        if (properties == null) {
            properties = new HashMap<>();
            properties.put("OM.uri", pageName);
        }
        sendEvent(properties);
    }

    public void customEvent(String pageName, HashMap<String, String> properties, Activity parent) {
        if (pageName == null || pageName.length() == 0) {
            Log.w(LOG_TAG, "Name cannot be null");
            return;
        }

        Context context = mContext;

        if (properties != null) {
            if (properties.containsKey("OM.cookieID")) {
                mCookieID = properties.get("OM.cookieID");
                setCookieID(properties.get("OM.cookieID"));
                properties.remove("OM.cookieID");
            }
            if (properties.containsKey("OM.exVisitorID")) {
                if (mExVisitorID != null && !mExVisitorID.equals(properties.get("OM.exVisitorID"))) {
                    setCookieID(null);
                }
                mExVisitorID = properties.get("OM.exVisitorID");
                setExVisitorID(mExVisitorID);
                properties.remove("OM.exVisitorID");
            }
            if (properties.containsKey("OM.vchannel")) {
                if (properties.get("OM.vchannel") != null) {
                    mChannel = properties.get("OM.vchannel");
                }
                properties.remove("OM.vchannel");
            }

            if (properties.containsKey("OM.sys.TokenID")) {
                if (properties.get("OM.sys.TokenID") != null) {
                    mSysTokenID = properties.get("OM.sys.TokenID");
                    setTokenID(properties.get("OM.sys.TokenID"));
                }
                properties.remove("OM.sys.TokenID");
            }
            if (properties.containsKey("OM.sys.AppID")) {
                if (properties.get("OM.sys.AppID") != null) {
                    mSysAppID = properties.get("OM.sys.AppID");
                    setAppID(properties.get("OM.sys.AppID"));
                }
                properties.remove("OM.sys.AppID");
            }

            try {
                PersistentTargetManager.with(context).saveParameters(properties);
            } catch (Exception e) {
                VisilabsLog.e(LOG_TAG, e.getMessage(), e);
            }
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.cookieID=%s&OM.vchannel=%s&OM.mappl=%s"
                , VisilabsEncoder.encode(mOrganizationID)
                , VisilabsEncoder.encode(mSiteID)
                , timeOfEvent
                , VisilabsEncoder.encode(pageName)
                , VisilabsEncoder.encode(mCookieID)
                , VisilabsEncoder.encode(mChannel)
                , VisilabsEncoder.encode("true"));

        if (mIdentifierForAdvertising != null) {
            query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(mIdentifierForAdvertising));
        }

        if (properties != null) {
            StringBuilder additionalURL = new StringBuilder();
            for (int i = 0; i < properties.keySet().size(); i++) {
                String key = (String) properties.keySet().toArray()[i];
                additionalURL.append(VisilabsEncoder.encode(key)).append("=").append(VisilabsEncoder.
                        encode(properties.get(key)));

                if (i < properties.keySet().size() - 1) {
                    additionalURL.append("&");
                }
            }
            if (!additionalURL.toString().equals("") && additionalURL.length() > 0) {
                query += "&" + additionalURL;
            }
        }

        if (mExVisitorID != null && mExVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(mExVisitorID));
        }

        if (mSysTokenID != null && mSysTokenID.length() > 0) {
            query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(mSysTokenID));
        }

        if (mSysAppID != null && mSysAppID.length() > 0) {
            query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(mSysAppID));
        }

        String segURL = mSegmentURL + "/" + mDataSource + "/om.gif?" + query;
        String realURL = "";

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            realURL = mRealTimeURL + "/" + mDataSource + "/om.gif?" + query;
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }


        if (mCheckForNotificationsOnLoggerRequest && mActionURL != null) {
            showNotification(pageName, parent, properties);
            showMailSubscriptionForm(pageName, parent, properties);
        }

        send();

        if (mRealTimeURL != null && !mRealTimeURL.equals("")) {
            send();
        }
    }

    public void send() {
        synchronized (this) {
            if (mSendQueue.size() == 0) {
                return;
            }
            String nextAPICall = mSendQueue.get(0);

            if (nextAPICall == null) {
                if (mSendQueue != null && mSendQueue.size() > 0)
                    mSendQueue.remove(0);
                return;
            }

            VisilabsURLConnection connector = VisilabsURLConnection.initializeConnector(this);

            String loadBalanceCookieKey = "";
            String loadBalanceCookieValue = "";
            String OM3rdCookieValue = "";

            if (nextAPICall.contains(VisilabsConstant.LOGGER_URL) && mCookie != null) {
                loadBalanceCookieKey = mCookie.getLoggerCookieKey();
                loadBalanceCookieValue = mCookie.getLoggerCookieValue();
                OM3rdCookieValue = mCookie.getLoggerOM3rdCookieValue();
            } else if (nextAPICall.contains(VisilabsConstant.REAL_TIME_URL) && mCookie != null) {
                loadBalanceCookieKey = mCookie.getRealTimeCookieKey();
                loadBalanceCookieValue = mCookie.getRealTimeCookieValue();
                OM3rdCookieValue = mCookie.getRealOM3rdTimeCookieValue();
            }

            connector.connectURL(nextAPICall, mUserAgent, mRequestTimeoutSeconds, loadBalanceCookieKey
                    , loadBalanceCookieValue, OM3rdCookieValue);
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


    @Deprecated
    public void setProperties(HashMap<String, String> properties) {
        if (properties == null || properties.size() == 0) {
            Log.w(LOG_TAG, "Tried to set properties with no properties in it..");
            return;
        }

        StringBuilder additionalURL = new StringBuilder();
        for (int i = 0; i < properties.keySet().size(); i++) {
            String key = (String) properties.keySet().toArray()[i];
            additionalURL.append(key).append("=").append(properties.get(key));
            if (i < properties.keySet().size() - 1) {
                additionalURL.append("&");
            }
        }
        if (additionalURL.length() == 0) {
            Log.w(LOG_TAG, "No valid properties in setProperties:. Ignoring call");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.cookieID=%s", mOrganizationID,
                mSiteID, timeOfEvent, mCookieID);

        if (mExVisitorID != null) {
            query = query + String.format("&OM.exVisitorID=%s", mExVisitorID);
        }

        query += "&" + additionalURL;
        String theURL = null;
        try {
            theURL = new URI(mSegmentURL, null, mDataSource, query, null).toASCIIString();
        } catch (URISyntaxException e) {
            Log.w(LOG_TAG, "Failed to set properties");
            return;
        }

        synchronized (this) {
            addUrlToQueue(theURL);
        }
        send();
    }

    private void addUrlToQueue(String url) {
        if (null != url) {
            mSendQueue.add(url);
        }
    }

    public void finished(int statusCode) {
        send();
    }

    List<String> getSendQueue() {
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
        return mDeviceUserAgent;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
            gpsManagerMoreThanOreo = GpsFactory2.createManager(mContext);
            gpsManagerMoreThanOreo.start();
        } else {
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
}