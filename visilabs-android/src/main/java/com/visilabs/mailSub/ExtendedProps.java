package com.visilabs.mailSub;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.visilabs.inApp.FontFamily;
import com.visilabs.util.AppUtils;

import java.io.Serializable;

public class ExtendedProps implements Serializable {
    public String getTitle_text_color() {
        return title_text_color;
    }

    public Typeface getTitle_font_family(Context context) {
        if (title_font_family == null || title_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(title_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(title_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(title_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(title_custom_font_family_android != null && !title_custom_font_family_android.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, title_custom_font_family_android)) {
                int id = context.getResources().getIdentifier(title_custom_font_family_android, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public String getTitle_text_size() {
        return title_text_size;
    }

    public String getText_color() {
        return text_color;
    }

    public Typeface getText_font_family(Context context) {
        if (text_font_family == null || text_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(text_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(text_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(text_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(text_custom_font_family_android != null && !text_custom_font_family_android.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, text_custom_font_family_android)) {
                int id = context.getResources().getIdentifier(text_custom_font_family_android, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public String getText_size() {
        return text_size;
    }

    public String getButton_color() {
        return button_color;
    }

    public String getButton_text_color() {
        return button_text_color;
    }

    public Typeface getButton_font_family(Context context) {
        if (button_font_family == null || button_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(button_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(button_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(button_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(button_custom_font_family_android != null && !button_custom_font_family_android.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, button_custom_font_family_android)) {
                int id = context.getResources().getIdentifier(button_custom_font_family_android, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public String getButton_text_size() {
        return button_text_size;
    }

    public String getEmailpermit_text_size() {
        return emailpermit_text_size;
    }

    public String getEmailpermit_text_url() {
        return emailpermit_text_url;
    }

    public String getConsent_text_size() {
        return consent_text_size;
    }

    public String getConsent_text_url() {
        return consent_text_url;
    }

    public String getClose_button_color() {
        return close_button_color;
    }

    public String getBackground_color() {
        return background_color;
    }

    private String title_text_color;

    private String title_font_family;

    private String title_custom_font_family_ios;

    private String title_custom_font_family_android;

    private String title_text_size;

    private String text_color;

    private String text_font_family;

    private String text_custom_font_family_ios;

    private String text_custom_font_family_android;

    private String text_size;

    private String button_color;

    private String button_text_color;

    private String button_font_family;

    private String button_custom_font_family_ios;

    private String button_custom_font_family_android;

    private String button_text_size;

    private String emailpermit_text_size;

    private String emailpermit_text_url;

    private String consent_text_size;

    private String consent_text_url;

    private String close_button_color;

    private String background_color;
}
