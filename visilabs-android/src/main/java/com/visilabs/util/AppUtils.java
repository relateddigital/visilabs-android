package com.visilabs.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.visilabs.model.LocationPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.internal.Util;

public final class AppUtils {
    public static String appVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            Log.w("Visilabs", "Error when getting app version : " + e.toString());

        }
        return null;
    }

    public static LocationPermission getLocationPermissionStatus(Context context) {
        LocationPermission locationPermission = LocationPermission.NONE;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                locationPermission = LocationPermission.ALWAYS;
            } else {
                locationPermission = LocationPermission.NONE;
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationPermission = LocationPermission.ALWAYS;
            } else {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = LocationPermission.APP_OPEN;
                } else {
                    locationPermission = LocationPermission.NONE;
                }
            }
        }
        return locationPermission;
    }

    public static UtilResultModel getNumberFromText(String text) {
        UtilResultModel model = null;

        if (text != null && !text.isEmpty()) {
            try {
                List<String> numbers = new ArrayList<String>();
                final Pattern pattern = Pattern.compile("<COUNT>(.+?)</COUNT>", Pattern.DOTALL);
                final Matcher matcher = pattern.matcher(text);
                while(matcher.find()) {
                    numbers.add(matcher.group(1));
                }
                if (!numbers.isEmpty()) {
                    model = new UtilResultModel();
                    text = text.replaceAll("<COUNT>", "");
                    text = text.replaceAll("</COUNT>", "");
                    model.setIsTag(true);
                    model.setMessage(text);
                    int idxToStart = 0;
                    for(int i = 0 ; i < numbers.size() ; i++) {
                        int number = Integer.parseInt(numbers.get(i));
                        int idx = text.indexOf(numbers.get(i), idxToStart);
                        if(idx != -1) {
                            model.addStartIdx(idx);
                            model.addEndIdx(idx + numbers.get(i).length());
                            model.addNumber(number);
                        }
                        idxToStart = text.indexOf(numbers.get(i)) + 1;
                    }
                } else {
                    if(text.contains("<COUNT>")) {
                        Log.e("SocialProof", "Could not parse the number!");
                    } else {
                        Log.e("SocialProof", "Tag COUNT is not used!");
                        model = new UtilResultModel();
                        model.setMessage(text);
                        model.setIsTag(false);
                    }
                }
            } catch (Exception e) {
                Log.e("SocialProof", "Could not parse the number!");
                model = null;
            }
        }

        return model;
    }

    public static boolean isResourceAvailable(Context context, String name) {
        int res = context.getResources().getIdentifier(name, "font", context.getPackageName());
        return res != 0;
    }
}
