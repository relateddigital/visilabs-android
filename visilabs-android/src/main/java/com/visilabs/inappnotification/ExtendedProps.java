package com.visilabs.inappnotification;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.visilabs.inApp.FontFamily;
import com.visilabs.util.AppUtils;

import java.io.Serializable;

public class ExtendedProps implements Serializable {

    private String content_minimized_text_size;

    private String content_minimized_text_color;

    private String content_minimized_font_family;

    private String content_minimized_custom_font_family_ios;

    private String content_minimized_custom_font_family_android;

    private String content_minimized_text_orientation;

    private String content_minimized_background_image;

    private String content_minimized_background_color;

    private String content_minimized_arrow_color;

    private String content_maximized_background_image;

    private String content_maximized_background_color;

    String getMiniTextSize() {
        return content_minimized_text_size;
    }

    String getMiniTextColor() {
        return content_minimized_text_color;
    }

    Typeface getMiniFontFamily(Context context) {
        if (content_minimized_font_family == null || content_minimized_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(content_minimized_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(content_minimized_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(content_minimized_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(content_minimized_custom_font_family_android != null && !content_minimized_custom_font_family_android.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, content_minimized_custom_font_family_android)) {
                int id = context.getResources().getIdentifier(content_minimized_custom_font_family_android, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    String getMiniTextOrientation() {
        return content_minimized_text_orientation;
    }

    String getMiniBackgroundImage() {
        return content_minimized_background_image;
    }

    String getMiniBackgroundColor() {
        return content_minimized_background_color;
    }

    String getArrowColor() {
        return content_minimized_arrow_color;
    }

    String getMaxiBackgroundImage() {
        return content_maximized_background_image;
    }

    String getMaxiBackgroundColor() {
        return content_maximized_background_color;
    }
}
