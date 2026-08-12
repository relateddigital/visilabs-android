package com.visilabs.inappnotification;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.visilabs.inApp.FontFamily;
import com.visilabs.util.AppUtils;

import java.io.Serializable;
import java.util.List;

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

    /**
     * The items of a multi item drawer. Each item carries the same styling fields as the extended
     * props themselves plus its own minimized and maximized content. Null or empty for the legacy
     * payload that describes a single item.
     */
    private List<ExtendedProps> content_minimized_items;

    /**
     * For a multi item payload these come from the item itself. For the legacy payload they are
     * filled in from the action data, where the single item's content lives.
     */
    private String content_minimized_image;

    private String content_minimized_text;

    private String content_maximized_image;

    List<ExtendedProps> getItems() {
        return content_minimized_items;
    }

    String getMiniImage() {
        return content_minimized_image;
    }

    String getMiniText() {
        return content_minimized_text;
    }

    String getMaxiImage() {
        return content_maximized_image;
    }

    /**
     * Fills the content fields from the action data. Used for the legacy single item payload and
     * for multi item payloads where an item leaves a content field out.
     */
    void fillMissingContentFrom(Actiondata actionData) {
        if (actionData == null) {
            return;
        }
        if (content_minimized_image == null || content_minimized_image.isEmpty()) {
            content_minimized_image = actionData.getContentMinimizedImage();
        }
        if (content_minimized_text == null || content_minimized_text.isEmpty()) {
            content_minimized_text = actionData.getContentMinimizedText();
        }
        if (content_maximized_image == null || content_maximized_image.isEmpty()) {
            content_maximized_image = actionData.getContentMaximizedImage();
        }
    }

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
