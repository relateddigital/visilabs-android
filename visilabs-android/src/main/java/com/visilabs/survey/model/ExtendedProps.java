package com.visilabs.survey.model;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ExtendedProps implements Serializable {
    @SerializedName("background_color")
    private String background_color;
    @SerializedName("font_family")
    private String font_family;
    @SerializedName("title_text_color")
    private String title_text_color;
    @SerializedName("title_text_size")
    private String title_text_size;
    @SerializedName("navigationbar_color")
    private String navigationbar_color;

    public void setBackgroundColor(String backgroundColor) {
        background_color = backgroundColor;
    }

    public String getBackgroundColor() {
        return background_color;
    }

    public void setFontFamily(String fontFamily) {
        font_family = fontFamily;
    }

    public String getFontFamily() { return font_family; }

    public void setTitleTextColor ( String titleTextColor ) {
        title_text_color = titleTextColor;
    }

    public String getTitleTextColor() {
        return title_text_color;
    }

    public void setTitleTextSize ( String titleTextSize ) {
        title_text_size = titleTextSize;
    }

    public String getTitleTextSize() {
        return title_text_size;
    }

    public void setNavigationBarColor ( String navigationBarColor ) {
        navigationbar_color = navigationBarColor;
    }

    public String getNavigationBarColor() {
        return navigationbar_color;
    }

}

