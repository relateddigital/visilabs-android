package com.visilabs;

import android.util.Log;

import com.visilabs.util.VisilabsConstant;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class VisilabsURLConnection {
    private static VisilabsURLConnection mConnector;
    private static Visilabs mApiContext;
    private static VisilabsURLConnectionCallbackInterface mCallback;
    private static boolean mIsConnected = false;

    private VisilabsURLConnection(Visilabs context, VisilabsURLConnectionCallbackInterface callback) {
        mApiContext = context;
        mCallback = callback;
    }

    public static VisilabsURLConnection initializeConnector(Visilabs context) {
        if (mConnector == null)
            mConnector = new VisilabsURLConnection(context, context);
        return mConnector;
    }

    public static VisilabsURLConnection initializeConnector(Visilabs context, VisilabsURLConnectionCallbackInterface callback) {
        if (mConnector == null)
            mConnector = new VisilabsURLConnection(context, callback);
        return mConnector;
    }

    public void connectURL(final String requestURL, final String userAgent, final int timeOutSeconds, final String loadBalanceCookieKey
            , final String loadBalanceCookieValue, final String OM3rdCookieValue) {
        if (mIsConnected || requestURL == null || requestURL.length() == 0)
            return;

        mIsConnected = true;
        new Thread(new Runnable() {
            public void run() {

                Map<String, List<String>> responseHeaders;
                int statusCode = -1;
                try {
                    URL httpURL = new URL(requestURL);
                    HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
                    connection.setConnectTimeout(timeOutSeconds * 1000);
                    connection.setReadTimeout(5000);
                    if (userAgent != null) {
                        connection.setRequestProperty("User-Agent", userAgent);
                    }

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
                        connection.setRequestProperty("Cookie", cookieString);
                    }

                    statusCode = connection.getResponseCode();

                    responseHeaders = connection.getHeaderFields();

                    if (responseHeaders != null && (responseHeaders.containsKey("Set-Cookie")
                            || responseHeaders.containsKey("Cookie"))) {
                        List<String> cookies;
                        if (responseHeaders.containsKey("Set-Cookie")) {
                            cookies = responseHeaders.get("Set-Cookie");
                        } else {
                            cookies = responseHeaders.get("Cookie");
                        }

                        if (cookies != null) {
                            for (String cookie : cookies) {
                                String[] fields = cookie.split(";");
                                if (fields[0].toLowerCase().contains(VisilabsConstant.LOAD_BALANCE_PREFIX.toLowerCase())) {
                                    String[] cookieKeyValue = fields[0].split("=");
                                    if (cookieKeyValue.length > 1) {
                                        String cookieKey = cookieKeyValue[0];
                                        String cookieValue = cookieKeyValue[1];

                                        if (requestURL.contains(VisilabsConstant.LOGGER_URL) && Visilabs.CallAPI().getCookie() != null) {
                                            Visilabs.CallAPI().getCookie().setLoggerCookieKey(cookieKey);
                                            Visilabs.CallAPI().getCookie().setLoggerCookieValue(cookieValue);

                                        } else if (requestURL.contains(VisilabsConstant.REAL_TIME_URL) && Visilabs.CallAPI().getCookie() != null) {
                                            Visilabs.CallAPI().getCookie().setRealTimeCookieKey(cookieKey);
                                            Visilabs.CallAPI().getCookie().setRealTimeCookieValue(cookieValue);
                                        }
                                    }
                                }

                                if (fields[0].toLowerCase().contains(VisilabsConstant.OM_3_KEY.toLowerCase())) {
                                    String[] cookieKeyValue = fields[0].split("=");
                                    if (cookieKeyValue.length > 1 || Visilabs.CallAPI().getCookie() != null) {
                                        String cookieValue = cookieKeyValue[1];

                                        if (requestURL.contains(VisilabsConstant.LOGGER_URL) && Visilabs.CallAPI().getCookie() != null) {
                                            Visilabs.CallAPI().getCookie().setLoggerOM3rdCookieValue(cookieValue);
                                        } else if (requestURL.contains(VisilabsConstant.REAL_TIME_URL) && Visilabs.CallAPI().getCookie() != null) {
                                            Visilabs.CallAPI().getCookie().setRealOM3rdTimeCookieValue(cookieValue);
                                        }
                                    }
                                }
                            }
                        }

                    }
                    connection.connect();
                } catch (Exception e) {
                    if (mApiContext.getSendQueue() != null && mApiContext.getSendQueue().size() > 0)
                        mApiContext.getSendQueue().remove(0);
                    Log.w("VisilabsAPI", requestURL + " " + e.toString());
                } finally {
                    mIsConnected = false;
                }

                if (statusCode == 200 || statusCode == 304) {
                    if (mApiContext.getSendQueue() != null && mApiContext.getSendQueue().size() > 0)
                        mApiContext.getSendQueue().remove(0);
                } else if (statusCode >= 400 && statusCode <= 500) {
                    //String failedURL = mApiContext.getSendQueue().get(0);
                    if (mApiContext.getSendQueue() != null && mApiContext.getSendQueue().size() > 0)
                        mApiContext.getSendQueue().remove(0);
                    Log.e("VL", statusCode + " CODE -" + requestURL);
                }
                if (mCallback != null)
                    mCallback.finished(statusCode);
            }
        }).start();
    }

    public static Visilabs getApiContext() {
        return mApiContext;
    }

    public static void setApiContext(Visilabs apiContext) {
        VisilabsURLConnection.mApiContext = apiContext;
    }
}
