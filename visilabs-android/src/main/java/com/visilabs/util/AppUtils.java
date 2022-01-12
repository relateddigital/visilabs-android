package com.visilabs.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.visilabs.inApp.FontFamily;
import com.visilabs.model.LocationPermission;
import com.visilabs.spinToWin.model.ExtendedProps;
import com.visilabs.spinToWin.model.SpinToWinModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String getNotificationPermissionStatus(Context context) {
        if(NotificationManagerCompat.from(context).areNotificationsEnabled()){
            return "granted";
        } else {
            return "denied";
        }
    }

    public static Typeface getFontFamily(Context context, String fontFamily, String fontName) {
        if (fontFamily == null || fontFamily.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(fontFamily.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(fontFamily.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(fontFamily.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(fontName != null && !fontName.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, fontName)) {
                int id = context.getResources().getIdentifier(fontName, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public static boolean isAnImage(String url) {
        boolean result = true;
        String[] splitArr;
        String extension;

        try {
            if (url != null && !url.isEmpty()) {
                splitArr = url.split("\\.");
                extension = splitArr[splitArr.length - 1];
                if (!extension.toLowerCase(Locale.ROOT).equals("jpg") &&
                        !extension.toLowerCase(Locale.ROOT).equals("png")) {
                    result = false;
                }
            }
        } catch (Exception e) {
            Log.w("isAnImage", "Could not get the extension from url string!");
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static ArrayList<String> createSpinToWinCustomFontFiles(Context context, String jsonStr) {
        ArrayList<String> result = null;
        SpinToWinModel spinToWinModel = null;
        ExtendedProps extendedProps = null;
        String baseUrlPath = "file://" + context.getFilesDir().getAbsolutePath() + "/";
        String htmlStr = "";
        try {
            spinToWinModel = new Gson().fromJson(jsonStr, SpinToWinModel.class);
            extendedProps = new Gson().fromJson(new java.net.URI(spinToWinModel.getActiondata().
                    getExtendedProps()).getPath(), ExtendedProps.class);
        } catch (Exception e) {
            Log.e("SpinToWin", "Extended properties could not be parsed properly!");
            return null;
        }

        if(spinToWinModel == null || extendedProps == null) {
            return null;
        }

        String displayNameFontFamily = extendedProps.getDisplayNameFontFamily();
        String titleFontFamily = extendedProps.getTitleFontFamily();
        String textFontFamily = extendedProps.getTextFontFamily();
        String buttonFontFamily = extendedProps.getButtonFontFamily();
        String promoCodeTitleFontFamily = extendedProps.getPromocodeTitleFontFamily();
        String copyButtonFontFamily = extendedProps.getCopyButtonFontFamily();
        String promoCodesSoldOutMessageFontFamily = extendedProps.getPromocodesSoldOutMessageFontFamily();

        if(displayNameFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getDisplayNameCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getDisplayNameCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(titleFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getTitleCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getTitleCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(textFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getTextCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getTextCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(buttonFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getButtonCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getButtonCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(promoCodeTitleFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getPromocodeTitleCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getPromocodeTitleCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(copyButtonFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getCopyButtonCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getCopyButtonCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(promoCodesSoldOutMessageFontFamily.equals("custom")) {
            String fontExtension = getFontNameWithExtension(context, extendedProps.getPromocodesSoldOutMessageCustomFontFamilyAndroid());
            if(!fontExtension.isEmpty()) {
                htmlStr = writeToFile(context, extendedProps.getPromocodesSoldOutMessageCustomFontFamilyAndroid(), fontExtension);
                spinToWinModel.fontFiles.add(fontExtension);
            }
        }

        if(!htmlStr.isEmpty()) {
            result = new ArrayList<>();
            result.add(baseUrlPath);
            result.add(htmlStr);
            result.add(new Gson().toJson(spinToWinModel, SpinToWinModel.class));
        }

        return result;
    }

    private static String getFontNameWithExtension(Context context, String font) {
        TypedValue value = new TypedValue();
        if (isResourceAvailable(context, font)) {
            int id = context.getResources().getIdentifier(font, "font", context.getPackageName());
            context.getResources().getValue(id, value, true);
            String[] res = value.string.toString().split("/");
            return res[res.length-1];
        } else {
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String writeToFile(Context context, String fontName, String fontNameWithExtension) {
        String spinToWinFileName = "spintowin";
        String baseUrlPath = "file://" + context.getFilesDir().getAbsolutePath() + "/";
        String htmlString = "";

        File spintowinRelatedDigitalCacheDir = context.getFilesDir();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File htmlFile = new File(spintowinRelatedDigitalCacheDir + "/" + spinToWinFileName + ".html");
            File jsFile = new File(spintowinRelatedDigitalCacheDir + "/" + spinToWinFileName + ".js");
            File fontFile = new File(spintowinRelatedDigitalCacheDir + "/" + fontNameWithExtension);

            htmlFile.createNewFile();
            jsFile.createNewFile();
            fontFile.createNewFile();

            is = context.getAssets().open(spinToWinFileName + ".html");

            byte[] bytes = getBytesFromInputStream(is);
            is.close();
            htmlString = new String(bytes, StandardCharsets.UTF_8);

            fos = new FileOutputStream(htmlFile, false);
            fos.write(bytes);
            fos.close();

            is = context.getAssets().open(spinToWinFileName + ".js");
            bytes = getBytesFromInputStream(is);
            is.close();

            fos = new FileOutputStream(jsFile);
            fos.write(bytes);
            fos.close();


            int fontId = context.getResources().getIdentifier(fontName, "font", context.getPackageName());
            is = context.getResources().openRawResource(fontId);
            bytes = getBytesFromInputStream(is);
            is.close();

            fos = new FileOutputStream(fontFile);
            fos.write(bytes);
            fos.close();

        } catch (Exception e) {
            Log.e("SpinToWin", "Could not create spintowin cache files properly!");
            e.printStackTrace();
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    Log.e("SpinToWin", "Could not close spintowin is stream properly!");
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    Log.e("SpinToWin", "Could not close spintowin fos stream properly!");
                    e.printStackTrace();
                }
            }

        }

        return htmlString;
    }

    private static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
}
