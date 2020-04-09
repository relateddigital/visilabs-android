
package com.visilabs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.visilabs.api.VisilabsAction;
import com.visilabs.api.VisilabsTargetCallback;
import com.visilabs.api.VisilabsTargetFilter;
import com.visilabs.api.VisilabsTargetRequest;
import com.visilabs.exceptions.VisilabsNotReadyException;
import com.visilabs.gps.factory.GpsFactory;
import com.visilabs.gps.factory.GpsFactory2;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.gps.manager.GpsManager2;
import com.visilabs.inApp.InAppMessageManager;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.inApp.VisilabsNotificationRequest;
import com.visilabs.util.Device;
import com.visilabs.util.NetworkManager;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.Prefs;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConfig;
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
    private static int mLogLevel = VisilabsConfig.LOG_LEVEL_VERBOSE;
    private static boolean mIsCreated = false;
    private static boolean mReady = false;
    private static String mDeviceUserAgent;
    private static String mTargetURL;
    private static String mActionURL;
    private List<String> mSendQueue;

    private static Visilabs visilabs = null;
    private String _organizationID;
    private String _siteID;
    private String _segmentURL;
    private String _realTimeURL;

    private String _dataSource;
    private String _exVisitorID;
    private String _cookieID;
    private String _channel;
    private Context _context;
    private String _userAgent;
    private int _requestTimeoutSeconds = 30;
    private String _RESTURL;
    private String _encryptedDataSource;

    private String mVisitData = "";
    private String mVisitorData = "";

    private Cookie cookie;

    private Boolean mCheckForNotificationsOnLoggerRequest;

    private String mIdentifierForAdvertising = null;

    private String mGeofenceURL;
    private boolean mGeofenceEnabled;

    private String mSysAppID;
    private String mSysTokenID;


    private Visilabs(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String RESTURL, String encryptedDataSource, String targetURL, String actionURL, String geofenceURL, boolean geofenceEnabled) {
        if (context == null) {
            return;
        }

        this.mGeofenceURL = geofenceURL;
        this.mGeofenceEnabled = geofenceEnabled;

        this.mCheckForNotificationsOnLoggerRequest = true;
        this._context = context;
        getIdThread(context);

        if (requestTimeoutSeconds > 0) {
            this._requestTimeoutSeconds = requestTimeoutSeconds;
        }
        this._RESTURL = RESTURL;
        this._encryptedDataSource = encryptedDataSource;
        this._organizationID = VisilabsEncoder.encode(organizationID);
        this._siteID = VisilabsEncoder.encode(siteID);
        this._channel = (channel != null) ? channel : "ANDROID";
        this._userAgent = System.getProperty("http.agent");
        mDeviceUserAgent = Device.getUserAgent();

        initVisilabsParameters();

        this._segmentURL = segmentURL;
        this._dataSource = dataSource;
        this._realTimeURL = realTimeURL;
        mTargetURL = targetURL;
        mActionURL = actionURL;

        this._exVisitorID = Prefs.getFromPrefs(this._context, VisilabsConfig.EXVISITORID_PREF, VisilabsConfig.EXVISITORID_PREF_KEY, null);
        this._cookieID = Prefs.getFromPrefs(this._context, VisilabsConfig.COOKIEID_PREF, VisilabsConfig.COOKIEID_PREF_KEY, null);

        this.mSysTokenID = Prefs.getFromPrefs(this._context, VisilabsConfig.TOKENID_PREF, VisilabsConfig.TOKENID_PREF_KEY, null);
        this.mSysAppID = Prefs.getFromPrefs(this._context, VisilabsConfig.APPID_PREF, VisilabsConfig.APPID_PREF_KEY, null);


        this.mVisitorData = Prefs.getFromPrefs(this._context, VisilabsConfig.VISITOR_DATA_PREF, VisilabsConfig.VISITOR_DATA_PREF_KEY, null);

        if (this._cookieID == null) {
            this.setCookieID(null);
        }

        this.mSendQueue = new ArrayList<>();

        if (!mIsCreated) {
            checkNetworkStatus(this._context);
            mIsCreated = true;
            mReady = true;
        }
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, 30
                    , null, null, null, null, null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, requestTimeoutSeconds
                    , null, null, null, null, null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String targetURL) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, requestTimeoutSeconds
                    , null, null, targetURL, null, null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , String targetURL, String actionURL, int requestTimeoutSeconds) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, requestTimeoutSeconds
                    , null, null, targetURL, actionURL, null, false);
        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , String targetURL, String actionURL, int requestTimeoutSeconds, String geofenceURL, boolean geofenceEnabled) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, requestTimeoutSeconds
                    , null, null, targetURL, actionURL, geofenceURL, geofenceEnabled);
            if (geofenceEnabled && !StringUtils.isNullOrWhiteSpace(geofenceURL)) {
                Visilabs.CallAPI().startGpsManager();

            }

        }
        return visilabs;
    }

    public static synchronized Visilabs CreateAPI(String organizationID, String siteID, String segmentURL, String dataSource, String realTimeURL, String channel, Context context
            , int requestTimeoutSeconds, String RESTURL, String encryptedDataSource) {
        if (visilabs == null) {
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, requestTimeoutSeconds
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
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                organizationID = ai.metaData.getString(VisilabsConfig.VISILABS_ORGANIZATION_ID);
                siteID = ai.metaData.getString(VisilabsConfig.VISILABS_SITE_ID);
                segmentURL = ai.metaData.getString(VisilabsConfig.VISILABS_SEGMENT_URL);
                dataSource = ai.metaData.getString(VisilabsConfig.VISILABS_DATA_SOURCE);
                realTimeURL = ai.metaData.getString(VisilabsConfig.VISILABS_REAL_TIME_URL);
                channel = ai.metaData.getString(VisilabsConfig.VISILABS_CHANNEL);

                try {
                    requestTimeoutSeconds = ai.metaData.getInt(VisilabsConfig.VISILABS_REQUEST_TIMEOUT_IN_SECONDS, 30);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    RESTURL = ai.metaData.getString(VisilabsConfig.VISILABS_REST_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    encryptedDataSource = ai.metaData.getString(VisilabsConfig.VISILABS_ENCRYPTED_DATA_SOURCE);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    targetURL = ai.metaData.getString(VisilabsConfig.VISILABS_TARGET_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    actionURL = ai.metaData.getString(VisilabsConfig.VISILABS_ACTION_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    geofenceURL = ai.metaData.getString(VisilabsConfig.VISILABS_GEOFENCE_URL);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }
                try {
                    geofenceEnabled = ai.metaData.getBoolean(VisilabsConfig.VISILABS_GEOFENCE_ENABLED, false);
                } catch (Exception ex) {
                    Log.d("CreateApi", ex.toString());
                }


            } catch (Exception e) {
                Log.d("CreateApi", e.toString());
            }
            visilabs = new Visilabs(organizationID, siteID, segmentURL, dataSource, realTimeURL, channel, context, requestTimeoutSeconds
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

    public void getIdThread(final Context context) {
        new Thread(new Runnable() {
            public void run() {
                Info adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    mIdentifierForAdvertising = adInfo.getId();
                    boolean isLAT = adInfo.isLimitAdTrackingEnabled();
                } catch (Exception e) {
                }

            }
        }).start();
    }

    public void setCheckForNotificationsOnLoggerRequest(Boolean checkForNotificationsOnLoggerRequest) {
        mCheckForNotificationsOnLoggerRequest = checkForNotificationsOnLoggerRequest;
    }


    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void trackInAppMessageClick(InAppMessage inAppMessage) {

        if (inAppMessage == null || inAppMessage.getQueryString() == null || inAppMessage.getQueryString() == "") {
            Log.w(LOG_TAG, "Notification or query string is null or empty.");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=%s&OM.cookieID=%s&OM.vchannel=%s&OM.domain=%s"
                , VisilabsEncoder.encode(this._organizationID)
                , VisilabsEncoder.encode(this._siteID)
                , timeOfEvent
                , VisilabsEncoder.encode("/OM_evt.gif")
                , VisilabsEncoder.encode(this._cookieID)
                , VisilabsEncoder.encode(this._channel)
                , VisilabsEncoder.encode(this._dataSource + "_Android"));


        if (this._exVisitorID != null && this._exVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(this._exVisitorID));
        }

        query += String.format("&%s", inAppMessage.getQueryString());


        String segURL = this._segmentURL + "/" + this._dataSource + "/om.gif?" + query;
        String realURL = "";


        if (this._realTimeURL != null && !this._realTimeURL.equals("")) {
            realURL = this._realTimeURL + "/" + this._dataSource + "/om.gif?" + query;
        }


        if (VisilabsConfig.DEBUG) {
            Log.v(LOG_TAG, String.format("Notification button tapped %s", segURL));
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (this._realTimeURL != null && !this._realTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }

        this.send();

        if (this._realTimeURL != null && !this._realTimeURL.equals("")) {
            this.send();
        }
    }


    public void showNotification(String pageName, Activity parent) {
        if (Build.VERSION.SDK_INT < VisilabsConfig.UI_FEATURES_MIN_API) {
            return;
        }
        showNotification(pageName, null, 0, parent, null);
    }

    public void showNotification(String pageName, Activity parent, HashMap<String, String> properties) {
        if (Build.VERSION.SDK_INT < VisilabsConfig.UI_FEATURES_MIN_API) {
            return;
        }
        showNotification(pageName, null, 0, parent, properties);
    }

    private void showNotification(String pageName, String type, int id, Activity parent, HashMap<String, String> properties) {
        if (mActionURL == null || mActionURL.equals("")) {
            Log.e(LOG_TAG, "Action URL is empty.");
        }

        VisilabsNotificationRequest request = new VisilabsNotificationRequest(_context);
        request.setApiVer("Android");
        request.setPageName(pageName);
        request.setPath(null);
        request.setHeaders(null);
        request.setMethod(VisilabsNotificationRequest.Methods.GET);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(this._requestTimeoutSeconds);

        request.setVisitorData(this.mVisitorData);
        request.setVisitData(this.mVisitData);

        request.setProperties(properties);

        try {
            request.executeAsync(getInAppMessageCallback(type, id, parent));
        } catch (Exception ex) {
            if (VisilabsConfig.DEBUG) {
                Log.e(LOG_TAG, ex.toString());
            }
        }

    }

    private VisilabsTargetCallback getInAppMessageCallback(final String type, final int notificationID, final Activity parent) {


         final String inAppJson = "[{\n" +
                 "\"actid\":129,\n" +
                 "\"title\":\"YENİ In-App-Message, template: image_text_button\",\n" +
                 "\"actiontype\":\"InAppMessage\",\n" +
                 "\"actiondata\":{\n" +
                 "\"msg_title\":\"erhg erhser serhe r\",\n" +
                 "\"msg_body\":\"ty rt jrtsh h ste jdykdytr trtjerst jter jtr krsk srt ktry kxrt kjrtx \",\n" +
                 "\"btn_text\":\"YOLLA\",\n" +
                 "\"ios_lnk\":\"\",\n" +
                 "\"android_lnk\":\"http://relateddigital.com\",\n" +
                 "\"msg_type\":\"image_text_button\",\n" +
                 "\"img\":\"http://localhost/VisilabsManager/img/in-app-message/uploaded_images/112_885_20200406200552257.jpg\",\n" +
                 "\"visitor_data\":\"{\\\"data\\\":{\\\"0\\\":{\\\"ts\\\":\\\"\\\\/Date(1586269619073+0300)\\\\/\\\",\\\"im\\\":1}}}\",\n" +
                 "\"visit_data\":\"\",\n" +
                 "\"qs\":\"OM.zn=acttype-13&OM.zpc=act-129\",\n" +
                 "\"cid\":\"\",\n" +
                 "\"courseofaction\":\"null\",\n" +
                 "\"msg_title_color\":\"#d90eb8\",\n" +
                 "\"close_button_color\":\"black\",\n" +
                 "\"msg_body_textsize\":\"3\",\n" +
                 "\"msg_body_color\":\"#22222a\",\n" +
                 "\"button_text_color\":\"#efefef\",\n" +
                 "\"button_color\":\"#0063cb\",\n" +
                 "\"font_family\":\"default\",\n" +
                 "\"background\":\"#ffffff\"\n" +
                 "}\n" +
                 "}]";


        return new VisilabsTargetCallback() {
            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();

                rawResponse = inAppJson;

                if (VisilabsConfig.DEBUG) {
                    Log.d(LOG_TAG, rawResponse);
                }
                try {

                    JSONArray array = new JSONArray(rawResponse);
                  //  JSONArray array = response.getArray();
                    if (array != null) {
                        ArrayList<InAppMessage> inAppMessageList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            InAppMessage inAppMessage = new InAppMessage(obj, _context);
                            if (inAppMessage != null) {
                                inAppMessageList.add(inAppMessage);
                            }
                        }

                        InAppMessage inAppMessage = null;
                        if (notificationID > 0) {
                            for (int i = 0; i < inAppMessageList.size(); i++) {
                                if (inAppMessageList.get(i) != null && inAppMessageList.get(i).getId() == notificationID) {
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

                        if (inAppMessage == null && inAppMessageList != null && inAppMessageList.size() > 0) {
                            inAppMessage = inAppMessageList.get(0);
                        }

                        if (inAppMessage != null) {

                            new InAppMessageManager(_cookieID, _dataSource).showInAppMessage(inAppMessage, parent);

                            if (inAppMessage.getVisitData() != null && inAppMessage.getVisitData() != "") {
                                Log.v("mVisitData", inAppMessage.getVisitData());
                                mVisitData = inAppMessage.getVisitData();
                            }

                            if (inAppMessage.getVisitorData() != null && inAppMessage.getVisitorData() != "") {
                                Prefs.saveToPrefs(parent, VisilabsConfig.VISITOR_DATA_PREF, VisilabsConfig.VISITOR_DATA_PREF_KEY, inAppMessage.getVisitorData());
                                Log.v("mVisitorData", inAppMessage.getVisitData());
                            }
                        } else {
                            if (VisilabsConfig.DEBUG) {
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
        request.setTimeOutInSeconds(this._requestTimeoutSeconds);
        request.setApiVer("Android");
        return request;
    }

    public VisilabsTargetRequest buildTargetRequest(String zoneID, String productCode, HashMap<String, String> properties, List<VisilabsTargetFilter> filters)
            throws Exception {
        VisilabsTargetRequest request = (VisilabsTargetRequest) buildAction();
        request.setZoneID(zoneID);
        request.setProductCode(productCode);
        request.setPath(null);
        request.setHeaders(null);
        request.setMethod(VisilabsTargetRequest.Methods.GET);
        request.setRequestArgs(null);
        request.setTimeOutInSeconds(this._requestTimeoutSeconds);
        request.setFilters(filters);
        request.setProperties(properties);
        request.setApiVer("Android");
        return request;
    }


    private VisilabsAction buildAction() throws VisilabsNotReadyException {
        if (!mIsCreated) {
            throw new VisilabsNotReadyException();
        }
        return new VisilabsTargetRequest(_context);
    }

    public void signUp(String exVisitorID, HashMap<String, String> properties) {
        if (StringUtils.isNullOrWhiteSpace(exVisitorID)) {
            VisilabsLog.w(LOG_TAG, "Attempted to use null or empty exVisitorID. Ignoring.");
            return;
        } else {
            if (properties == null) {
                properties = new HashMap<String, String>();
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
            return;
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

        if (this._exVisitorID != null && !this._exVisitorID.equals(exVisitorID)) {
            this.setCookieID(null);
        }


        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=LoginPage&OM.b_login=Login&Login=%s&OM.exVisitorID=%s&OM.cookieID=%s&OM.vchannel=%s&OM.mappl=%s"
                , VisilabsEncoder.encode(this._organizationID)
                , VisilabsEncoder.encode(this._siteID)
                , timeOfEvent
                , VisilabsEncoder.encode(exVisitorID)
                , VisilabsEncoder.encode(exVisitorID)
                , VisilabsEncoder.encode(this._cookieID)
                , VisilabsEncoder.encode(this._channel)
                , VisilabsEncoder.encode("true"));

        if (this.mIdentifierForAdvertising != null) {
            query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(this.mIdentifierForAdvertising));
        }

        if (this.mSysTokenID != null && this.mSysTokenID.length() > 0) {
            query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(this.mSysTokenID));
        }

        if (this.mSysAppID != null && this.mSysAppID.length() > 0) {
            query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(this.mSysAppID));
        }

        String segURL = this._segmentURL + "/" + this._dataSource + "/om.gif?" + query;

        String realURL = "";
        if (this._realTimeURL != null && this._realTimeURL != "") {
            realURL = this._realTimeURL + "/" + this._dataSource + "/om.gif?" + query;
        }

        synchronized (this) {
            this.setExVisitorID(exVisitorID);
            addUrlToQueue(segURL);
            if (this._realTimeURL != null && this._realTimeURL != "") {
                addUrlToQueue(realURL);
            }
        }

        this.send();
        if (this._realTimeURL != null && this._realTimeURL != "") {
            this.send();
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

        }

        if (this._exVisitorID != null && !this._exVisitorID.equals(exVisitorID)) {
            this.setCookieID(null);
        }


        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = "";
        try {
            query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.uri=SignUpPage&OM.b_sgnp=SignUp&SignUp=%s&OM.exVisitorID=%s&OM.cookieID=%s&OM.vchannel=%s&OM.mappl=%s"
                    , URLEncoder.encode(this._organizationID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(this._siteID, "UTF-8").replace("+", "%20")
                    , timeOfEvent
                    , URLEncoder.encode(exVisitorID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(exVisitorID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(this._cookieID, "UTF-8").replace("+", "%20")
                    , URLEncoder.encode(this._channel, "UTF-8").replace("+", "%20")
                    , VisilabsEncoder.encode("true"));

            if (this.mIdentifierForAdvertising != null) {
                query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(this.mIdentifierForAdvertising));
            }

            if (this.mSysTokenID != null && this.mSysTokenID.length() > 0) {
                query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(this.mSysTokenID));
            }

            if (this.mSysAppID != null && this.mSysAppID.length() > 0) {
                query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(this.mSysAppID));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        String segURL = this._segmentURL + "/" + this._dataSource + "/om.gif?" + query;

        String realURL = "";
        if (this._realTimeURL != null && this._realTimeURL != "") {
            realURL = this._realTimeURL + "/" + this._dataSource + "/om.gif?" + query;
        }

        synchronized (this) {
            this.setExVisitorID(exVisitorID);
            addUrlToQueue(segURL);
            if (this._realTimeURL != null && this._realTimeURL != "") {
                addUrlToQueue(realURL);
            }
        }
        this.send();
        if (this._realTimeURL != null && this._realTimeURL != "") {
            this.send();
        }
    }

    public void customEvent(String pageName, HashMap<String, String> properties) {
        if (pageName == null || pageName.length() == 0) {
            Log.w(LOG_TAG, "Name cannot be null");
            return;
        }

        Context context = this._context;

        if (properties != null) {
            if (properties.containsKey("OM.cookieID")) {
                this._cookieID = properties.get("OM.cookieID");
                this.setCookieID(properties.get("OM.cookieID"));
                properties.remove("OM.cookieID");
            }
            if (properties.containsKey("OM.exVisitorID")) {
                if (this._exVisitorID != null && !this._exVisitorID.equals(properties.get("OM.exVisitorID"))) {
                    this.setCookieID(null);
                }

                this._exVisitorID = properties.get("OM.exVisitorID");
                this.setExVisitorID(this._exVisitorID);
                properties.remove("OM.exVisitorID");
            }
            if (properties.containsKey("OM.vchannel")) {
                if (properties.get("OM.vchannel") != null) {
                    this._channel = properties.get("OM.vchannel");
                }
                properties.remove("OM.vchannel");
            }

            if (properties.containsKey("OM.sys.TokenID")) {
                if (properties.get("OM.sys.TokenID") != null) {
                    this.mSysTokenID = properties.get("OM.sys.TokenID");
                    this.setTokenID(properties.get("OM.sys.TokenID"));
                }
                properties.remove("OM.sys.TokenID");
            }
            if (properties.containsKey("OM.sys.AppID")) {
                if (properties.get("OM.sys.AppID") != null) {
                    this.mSysAppID = properties.get("OM.sys.AppID");
                    this.setAppID(properties.get("OM.sys.AppID"));
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
                , VisilabsEncoder.encode(this._organizationID)
                , VisilabsEncoder.encode(this._siteID)
                , timeOfEvent
                , VisilabsEncoder.encode(pageName)
                , VisilabsEncoder.encode(this._cookieID)
                , VisilabsEncoder.encode(this._channel)
                , VisilabsEncoder.encode("true"));

        if (this.mIdentifierForAdvertising != null) {
            query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(this.mIdentifierForAdvertising));
        }

        if (properties != null) {
            String additionalURL = "";
            for (int i = 0; i < properties.keySet().size(); i++) {
                String key = (String) properties.keySet().toArray()[i];
                additionalURL += VisilabsEncoder.encode(key) + "=" + VisilabsEncoder.encode(properties.get(key));

                if (i < properties.keySet().size() - 1) {
                    additionalURL += "&";
                }
            }
            if (additionalURL != "" && additionalURL.length() > 0) {
                query += "&" + additionalURL;
            }
        }

        if (this._exVisitorID != null && this._exVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(this._exVisitorID));
        }

        if (this.mSysTokenID != null && this.mSysTokenID.length() > 0) {
            query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(this.mSysTokenID));
        }

        if (this.mSysAppID != null && this.mSysAppID.length() > 0) {
            query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(this.mSysAppID));
        }

        String segURL = this._segmentURL + "/" + this._dataSource + "/om.gif?" + query;
        String realURL = "";

        if (this._realTimeURL != null && this._realTimeURL != "") {
            realURL = this._realTimeURL + "/" + this._dataSource + "/om.gif?" + query;
        }

        synchronized (this) {
            addUrlToQueue(segURL);
            if (this._realTimeURL != null && this._realTimeURL != "") {
                addUrlToQueue(realURL);
            }
        }

        this.send();

        if (this._realTimeURL != null && this._realTimeURL != "") {
            this.send();
        }
    }

    public void customEvent(String pageName, HashMap<String, String> properties, Activity parent) {
        if (pageName == null || pageName.length() == 0) {
            Log.w(LOG_TAG, "Name cannot be null");
            return;
        }

        Context context = this._context;

        if (properties != null) {
            if (properties.containsKey("OM.cookieID")) {
                this._cookieID = properties.get("OM.cookieID");
                this.setCookieID(properties.get("OM.cookieID"));
                properties.remove("OM.cookieID");
            }
            if (properties.containsKey("OM.exVisitorID")) {
                if (this._exVisitorID != null && !this._exVisitorID.equals(properties.get("OM.exVisitorID"))) {
                    this.setCookieID(null);
                }
                this._exVisitorID = properties.get("OM.exVisitorID");
                this.setExVisitorID(this._exVisitorID);
                properties.remove("OM.exVisitorID");
            } // yeniliyelim uygulamayı tamam  he tamamdır burayı salla ya dur bakalım okuyalım nereye koyalım
            if (properties.containsKey("OM.vchannel")) {
                if (properties.get("OM.vchannel") != null) {
                    this._channel = properties.get("OM.vchannel");
                }
                properties.remove("OM.vchannel");
            }

            if (properties.containsKey("OM.sys.TokenID")) {
                if (properties.get("OM.sys.TokenID") != null) {
                    this.mSysTokenID = properties.get("OM.sys.TokenID");
                    this.setTokenID(properties.get("OM.sys.TokenID"));
                }
                properties.remove("OM.sys.TokenID");
            }
            if (properties.containsKey("OM.sys.AppID")) {
                if (properties.get("OM.sys.AppID") != null) {
                    this.mSysAppID = properties.get("OM.sys.AppID");
                    this.setAppID(properties.get("OM.sys.AppID"));
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
                , VisilabsEncoder.encode(this._organizationID)
                , VisilabsEncoder.encode(this._siteID)
                , timeOfEvent
                , VisilabsEncoder.encode(pageName)
                , VisilabsEncoder.encode(this._cookieID)
                , VisilabsEncoder.encode(this._channel)
                , VisilabsEncoder.encode("true"));

        assert this.mIdentifierForAdvertising != null;
        if (this.mIdentifierForAdvertising != null || this.mIdentifierForAdvertising.equals("cca-app-pub-3940256099942544~3347511713")) {
            query = String.format("%s&OM.m_adid=%s", query, VisilabsEncoder.encode(this.mIdentifierForAdvertising));
        }

        if (properties != null) {
            String additionalURL = "";
            for (int i = 0; i < properties.keySet().size(); i++) {
                String key = (String) properties.keySet().toArray()[i];
                additionalURL += VisilabsEncoder.encode(key) + "=" + VisilabsEncoder.encode(properties.get(key));

                if (i < properties.keySet().size() - 1) {
                    additionalURL += "&";
                }
            }
            if (additionalURL != "" && additionalURL.length() > 0) {
                query += "&" + additionalURL;
            }
        }

        if (this._exVisitorID != null && this._exVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(this._exVisitorID));
        }

        if (this.mSysTokenID != null && this.mSysTokenID.length() > 0) {
            query += String.format("&OM.sys.TokenID=%s", VisilabsEncoder.encode(this.mSysTokenID));
        }

        if (this.mSysAppID != null && this.mSysAppID.length() > 0) {
            query += String.format("&OM.sys.AppID=%s", VisilabsEncoder.encode(this.mSysAppID));
        }

        String segURL = this._segmentURL + "/" + this._dataSource + "/om.gif?" + query;
        String realURL = "";

        if (this._realTimeURL != null && !this._realTimeURL.equals("")) {
            realURL = this._realTimeURL + "/" + this._dataSource + "/om.gif?" + query;
        }


        synchronized (this) {
            addUrlToQueue(segURL);
            if (this._realTimeURL != null && !this._realTimeURL.equals("")) {
                addUrlToQueue(realURL);
            }
        }


        if (mCheckForNotificationsOnLoggerRequest && mActionURL != null) {
            this.showNotification(pageName, parent, properties);
        }

        this.send();

        if (this._realTimeURL != null && !this._realTimeURL.equals("")) {
            this.send();
        }
    }

    public void send() {
        synchronized (this) {
            if (this.mSendQueue.size() == 0) {
                return;
            }
            String nextAPICall = this.mSendQueue.get(0);

            if (nextAPICall == null) {
                if (this.mSendQueue != null && this.mSendQueue.size() > 0)
                    this.mSendQueue.remove(0);
                return;
            }

            VisilabsURLConnection connector = VisilabsURLConnection.initializeConnector(this);

            String loadBalanceCookieKey = "";
            String loadBalanceCookieValue = "";
            String OM3rdCookieValue = "";

            if (nextAPICall.contains(VisilabsConfig.LOGGER_URL)) {
                loadBalanceCookieKey = this.cookie.getLoggerCookieKey();
                loadBalanceCookieValue = this.cookie.getLoggerCookieValue();
                OM3rdCookieValue = this.cookie.getLoggerOM3rdCookieValue();
            } else if (nextAPICall.contains(VisilabsConfig.REAL_TIME_URL)) {
                loadBalanceCookieKey = this.cookie.getRealTimeCookieKey();
                loadBalanceCookieValue = this.cookie.getRealTimeCookieValue();
                OM3rdCookieValue = this.cookie.getRealOM3rdTimeCookieValue();
            }

            connector.connectURL(nextAPICall, this._userAgent, this._requestTimeoutSeconds, loadBalanceCookieKey
                    , loadBalanceCookieValue, OM3rdCookieValue);
        }
    }

    synchronized private void setCookieID(String _cookieID) {
        String cookieID = _cookieID;
        if (cookieID == null || cookieID == "") {
            cookieID = UUID.randomUUID().toString();
        }
        this._cookieID = cookieID;

        String previousCookieID = Prefs.getFromPrefs(this._context, VisilabsConfig.COOKIEID_PREF, VisilabsConfig.COOKIEID_PREF_KEY, null);
        if (previousCookieID != null && previousCookieID.equals(cookieID)) {
            PersistentTargetManager.with(this._context).clearParameters();
        }

        Prefs.saveToPrefs(this._context, VisilabsConfig.COOKIEID_PREF, VisilabsConfig.COOKIEID_PREF_KEY, this._cookieID);
    }

    synchronized private void setExVisitorID(String exVisitorID) {
        this._exVisitorID = exVisitorID;

        String previousExVisitorID = Prefs.getFromPrefs(this._context, VisilabsConfig.EXVISITORID_PREF, VisilabsConfig.EXVISITORID_PREF_KEY, null);
        if (previousExVisitorID != null && !previousExVisitorID.equals(exVisitorID)) {
            PersistentTargetManager.with(this._context).clearParameters();
        }

        Prefs.saveToPrefs(this._context, VisilabsConfig.EXVISITORID_PREF, VisilabsConfig.EXVISITORID_PREF_KEY, this._exVisitorID);
    }

    synchronized private void setTokenID(String tokenID) {
        this.mSysTokenID = tokenID;
        String previousTokenID = Prefs.getFromPrefs(this._context, VisilabsConfig.TOKENID_PREF, VisilabsConfig.TOKENID_PREF_KEY, null);
        if (previousTokenID == null || !previousTokenID.equals(tokenID)) {
            Prefs.saveToPrefs(this._context, VisilabsConfig.TOKENID_PREF, VisilabsConfig.TOKENID_PREF_KEY, this.mSysTokenID);
        }
    }

    synchronized private void setAppID(String appID) {
        this.mSysAppID = appID;
        String previousAppID = Prefs.getFromPrefs(this._context, VisilabsConfig.APPID_PREF, VisilabsConfig.APPID_PREF_KEY, null);
        if (previousAppID == null || !previousAppID.equals(appID)) {
            Prefs.saveToPrefs(this._context, VisilabsConfig.APPID_PREF, VisilabsConfig.APPID_PREF_KEY, this.mSysAppID);
        }
    }


    @Deprecated
    public void setProperties(HashMap<String, String> properties) {
        if (properties == null || properties.size() == 0) {
            Log.w(LOG_TAG, "Tried to set properties with no properties in it..");
            return;
        }

        String additionalURL = "";
        for (int i = 0; i < properties.keySet().size(); i++) {
            String key = (String) properties.keySet().toArray()[i];
            additionalURL += key + "=" + properties.get(key).toString();
            if (i < properties.keySet().size() - 1) {
                additionalURL += "&";
            }
        }
        if (additionalURL.length() == 0) {
            Log.w(LOG_TAG, "No valid properties in setProperties:. Ignoring call");
            return;
        }

        long timeOfEvent = System.currentTimeMillis() / 1000;
        String query = String.format("OM.oid=%s&OM.siteID=%s&dat=%d&OM.cookieID=%s", this._organizationID, this._siteID, timeOfEvent, this._cookieID);

        if (this._exVisitorID != null) {
            query = query + String.format("&OM.exVisitorID=%s", this._exVisitorID);
        }

        query += "&" + additionalURL;
        String theURL = null;
        try {
            theURL = new URI(this._segmentURL, null, this._dataSource, query, null).toASCIIString();
        } catch (URISyntaxException e) {
            Log.w(LOG_TAG, "Failed to set properties");
            return;
        }

        synchronized (this) {
            addUrlToQueue(theURL);
        }
        this.send();
    }

    private void addUrlToQueue(String url) {
        if (null != url) {
            this.mSendQueue.add(url);
        }
    }

    public void finished(int statusCode) {
        this.send();
    }

    public List<String> getSendQueue() {
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
                , VisilabsEncoder.encode(this._organizationID)
                , VisilabsEncoder.encode(this._siteID)
                , timeOfEvent
                , VisilabsEncoder.encode(pageName)
                , VisilabsEncoder.encode(this._channel));

        if (this._exVisitorID != null && this._exVisitorID.length() > 0) {
            query += String.format("&OM.exVisitorID=%s", VisilabsEncoder.encode(this._exVisitorID));
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

        return this._RESTURL + "/" + this._encryptedDataSource + "/" + this._dataSource + "/" + this._cookieID + "?" + query;


    }

    /**
     * Sets the log level for the library.
     * The default level is {@link VisilabsConfig#LOG_LEVEL_ERROR}. Please make sure this is set to {@link VisilabsConfig#LOG_LEVEL_ERROR}
     * or {@link VisilabsConfig#LOG_LEVEL_NONE} before releasing the application.
     * The log level can be one of
     * <ul>
     * <li>{@link VisilabsConfig#LOG_LEVEL_VERBOSE}</li>
     * <li>{@link VisilabsConfig#LOG_LEVEL_DEBUG}</li>
     * <li>{@link VisilabsConfig#LOG_LEVEL_INFO}</li>
     * <li>{@link VisilabsConfig#LOG_LEVEL_WARNING}</li>
     * <li>{@link VisilabsConfig#LOG_LEVEL_ERROR}</li>
     * <li>{@link VisilabsConfig#LOG_LEVEL_NONE}</li>
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
        return _exVisitorID;
    }

    public String getCookieID() {
        return _cookieID;
    }

    public String getSysTokenID() {
        return mSysTokenID;
    }

    public String getSysAppID() {
        return mSysAppID;
    }


    public String getOrganizationID() {
        return _organizationID;
    }

    public String getSiteID() {
        return _siteID;
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
        List<VisilabsParameter> visilabsParameters = new ArrayList<VisilabsParameter>();
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VOSS_KEY, VisilabsConfig.TARGET_PREF_VOSS_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VCNAME_KEY, VisilabsConfig.TARGET_PREF_VCNAME_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VCMEDIUM_KEY, VisilabsConfig.TARGET_PREF_VCMEDIUM_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VCSOURCE_KEY, VisilabsConfig.TARGET_PREF_VCSOURCE_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VSEG1_KEY, VisilabsConfig.TARGET_PREF_VSEG1_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VSEG2_KEY, VisilabsConfig.TARGET_PREF_VSEG2_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VSEG3_KEY, VisilabsConfig.TARGET_PREF_VSEG3_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VSEG4_KEY, VisilabsConfig.TARGET_PREF_VSEG4_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VSEG5_KEY, VisilabsConfig.TARGET_PREF_VSEG5_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_BD_KEY, VisilabsConfig.TARGET_PREF_BD_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_GN_KEY, VisilabsConfig.TARGET_PREF_GN_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_LOC_KEY, VisilabsConfig.TARGET_PREF_LOC_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VPV_KEY, VisilabsConfig.TARGET_PREF_VPV_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_LPVS_KEY, VisilabsConfig.TARGET_PREF_LPVS_STORE_KEY, 10, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_LPP_KEY, VisilabsConfig.TARGET_PREF_LPP_STORE_KEY,
                1, new ArrayList<String>() {{
            add(VisilabsConfig.TARGET_PREF_PPR_KEY);
        }}));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VQ_KEY, VisilabsConfig.TARGET_PREF_VQ_STORE_KEY, 1, null));
        visilabsParameters.add(new VisilabsParameter(VisilabsConfig.TARGET_PREF_VRDOMAIN_KEY, VisilabsConfig.TARGET_PREF_VRDOMAIN_STORE_KEY, 1, null));
        VisilabsConfig.VISILABS_PARAMETERS = visilabsParameters;
    }


    private GpsManager gpsManager = null;
    private GpsManager2 gpsManager2 = null;

    public void startGpsManager() {
        //if (ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int per = ContextCompat.checkSelfPermission(this._context, Manifest.permission.ACCESS_FINE_LOCATION);
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
            gpsManager2 = GpsFactory2.createManager(this._context);
            gpsManager2.start();
        } else {
            int per = ContextCompat.checkSelfPermission(this._context, Manifest.permission.ACCESS_FINE_LOCATION);
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
            gpsManager = GpsFactory.createManager(this._context);
            gpsManager.start();
        }

    }

    public Context getContext() {
        return this._context;
    }


    public String getGeofenceURL() {
        return this.mGeofenceURL;
    }

    public void setExVisitorIDToNull() {
        this._exVisitorID = null;
    }
}