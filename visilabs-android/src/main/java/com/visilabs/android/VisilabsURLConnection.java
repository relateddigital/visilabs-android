package com.visilabs.android;

import android.util.Log;

import com.visilabs.android.util.VisilabsConfig;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class VisilabsURLConnection
{
    private static VisilabsURLConnection _connector;
    private static Visilabs _apiContext;
    private static VisilabsURLConnectionCallbackInterface _callback;

    private static boolean _isConnected = false;

    private VisilabsURLConnection(Visilabs context, VisilabsURLConnectionCallbackInterface callback)
    {
        _apiContext = context;
        _callback = callback;
    }

    public static VisilabsURLConnection initializeConnector(Visilabs context)
    {
        if (_connector == null)
            _connector = new VisilabsURLConnection(context, context);
        return _connector;
    }

    public static VisilabsURLConnection initializeConnector(Visilabs context, VisilabsURLConnectionCallbackInterface callback)
    {
        if (_connector == null)
            _connector = new VisilabsURLConnection(context, callback);
        return _connector;
    }

    public void connectURL(final String requestURL, final String userAgent, final int timeOutSeconds, final String loadBalanceCookieKey
            , final String loadBalanceCookieValue, final String OM3rdCookieValue)
    {
        if (_isConnected || requestURL == null || requestURL.length() == 0) return;

        _isConnected = true;
        new Thread(new Runnable()
        {
            public void run()
            {

                Map<String, List<String>> responseHeaders;
                int statusCode = -1;
                try
                {
                    URL httpURL = new URL(requestURL);
                    HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
                    connection.setConnectTimeout(timeOutSeconds * 1000);
                    connection.setReadTimeout(5000);
                    if(userAgent != null)
                    {
                        connection.setRequestProperty("User-Agent", userAgent);
                    }

                    String cookieString = "";

                    if(loadBalanceCookieKey != null && loadBalanceCookieValue != null
                            && !loadBalanceCookieKey.equals("") && !loadBalanceCookieValue.equals("")){
                        cookieString = loadBalanceCookieKey + "=" + loadBalanceCookieValue;
                    }
                    if(OM3rdCookieValue != null && !OM3rdCookieValue.equals("")){
                        if(cookieString != null && !cookieString.equals("")){
                            cookieString = cookieString + ";";
                        }
                        cookieString = cookieString + VisilabsConfig.OM_3_KEY + "=" + OM3rdCookieValue;
                    }


                    if(cookieString != null && !cookieString.equals("")){
                        connection.setRequestProperty("Cookie", cookieString);
                    }

                    statusCode = connection.getResponseCode();

                    responseHeaders = connection.getHeaderFields();

                    if(responseHeaders != null && (responseHeaders.containsKey("Set-Cookie")
                    ||responseHeaders.containsKey("Cookie"))){
                        List<String> cookies;
                        if(responseHeaders.containsKey("Set-Cookie")){
                            cookies = responseHeaders.get("Set-Cookie");
                        }else{
                            cookies = responseHeaders.get("Cookie");
                        }

                        if(cookies != null){
                            for (String cookie : cookies)
                            {
                                String[] fields = cookie.split(";");
                                if(fields[0].toLowerCase().contains(VisilabsConfig.LOAD_BALANCE_PREFIX.toLowerCase())){
                                    String[] cookieKeyValue = fields[0].split("=");
                                    if(cookieKeyValue.length > 1){
                                        String cookieKey = cookieKeyValue[0];
                                        String cookieValue = cookieKeyValue[1];

                                        if(requestURL.contains(VisilabsConfig.LOGGER_URL)){
                                            Visilabs.CallAPI().getCookie().setLoggerCookieKey(cookieKey);
                                            Visilabs.CallAPI().getCookie().setLoggerCookieValue(cookieValue);

                                        }else if(requestURL.contains(VisilabsConfig.REAL_TIME_URL)){
                                            Visilabs.CallAPI().getCookie().setRealTimeCookieKey(cookieKey);
                                            Visilabs.CallAPI().getCookie().setRealTimeCookieValue(cookieValue);
                                        }
                                    }
                                }


                                if(fields[0].toLowerCase().contains(VisilabsConfig.OM_3_KEY.toLowerCase())){
                                    String[] cookieKeyValue = fields[0].split("=");
                                    if(cookieKeyValue.length > 1){
                                        String cookieKey = cookieKeyValue[0];
                                        String cookieValue = cookieKeyValue[1];

                                        if(requestURL.contains(VisilabsConfig.LOGGER_URL)){
                                            Visilabs.CallAPI().getCookie().setLoggerOM3rdCookieValue(cookieValue);
                                        }else if(requestURL.contains(VisilabsConfig.REAL_TIME_URL)){
                                            Visilabs.CallAPI().getCookie().setRealOM3rdTimeCookieValue(cookieValue);
                                        }
                                    }
                                }

                            }
                        }

                    }
                    connection.connect();
                }
                catch (Exception e)
                {
                    if(_apiContext.getSendQueue() != null && _apiContext.getSendQueue().size() >0)
                        _apiContext.getSendQueue().remove(0);
                    Log.w("VisilabsAPI", "Failed to connect URL: " + requestURL);
                }
                finally
                {
                    _isConnected = false;
                }

                if (statusCode == 200 || statusCode == 304)
                {
                    if(_apiContext.getSendQueue() != null && _apiContext.getSendQueue().size() >0)
                        _apiContext.getSendQueue().remove(0);
                }
                else if (statusCode >= 400 && statusCode <= 500)
                {
                    //String failedURL = _apiContext.getSendQueue().get(0);
                    if(_apiContext.getSendQueue() != null && _apiContext.getSendQueue().size() >0)
                        _apiContext.getSendQueue().remove(0);
                }
                if (_callback != null)
                    _callback.finished(statusCode);
            }
        }).start();
    }

    public static Visilabs getApiContext()
    {
        return _apiContext;
    }

    public static void setApiContext(Visilabs apiContext) {
        VisilabsURLConnection._apiContext = apiContext;
    }
}
