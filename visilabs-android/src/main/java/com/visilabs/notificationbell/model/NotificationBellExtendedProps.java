package com.visilabs.notificationbell.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class NotificationBellExtendedProps implements Serializable {

    @SerializedName("background_color")
    private String background_color;

    @SerializedName("font_family")
    private String font_family;

    @SerializedName("title_text_color")
    private String title_text_color;

    @SerializedName("title_text_size")
    private String title_text_size;

    @SerializedName("text_text_color")
    private String text_text_color;

    @SerializedName("text_text_size")
    private String text_text_size;

    public String getBackground_color() {
        return background_color;
    }

    public String getFont_family() {
        return font_family;
    }

    public String getTitle_text_color() {
        return title_text_color;
    }

    public String getTitle_text_size() {
        return title_text_size;
    }

    public String getText_text_color() {
        return text_text_color;
    }

    public String getText_text_size() {
        return text_text_size;
    }
}