package com.visilabs.countdownTimerBanner.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

// Bu sınıf, ActionData içerisindeki URL-encode edilmiş JSON string'inin
// deserialize edilmesiyle elde edilecek modeli temsil eder.
// Örnek modelinizdeki gibi, bu sınıfın da sadece getter metotları bulunmaktadır.
public class CountdownTimerBannerExtendedProps implements Serializable {

    @SerializedName("content_title_text_color")
    private String content_title_text_color;

    @SerializedName("content_title_custom_font_family_android")
    private String content_title_custom_font_family_android;

    @SerializedName("content_body_text_color")
    private String content_body_text_color;

    @SerializedName("content_body_font_family")
    private String content_body_font_family;

    @SerializedName("content_body_custom_font_family_android")
    private String content_body_custom_font_family_android;

    @SerializedName("button_color")
    private String button_color;

    @SerializedName("counter_color")
    private String counter_color;

    @SerializedName("position_on_page")
    private String position_on_page;

    @SerializedName("close_button_color")
    private String close_button_color;

    @SerializedName("button_text_color")
    private String button_text_color;

    @SerializedName("button_custom_font_family_android")
    private String button_custom_font_family_android;

    @SerializedName("background_color")
    private String background_color;

    public String getContent_title_text_color() {
        return content_title_text_color;
    }

    public String getContent_title_custom_font_family_android() {
        return content_title_custom_font_family_android;
    }

    public String getContent_body_text_color() {
        return content_body_text_color;
    }

    public String getContent_body_font_family() {
        return content_body_font_family;
    }

    public String getContent_body_custom_font_family_android() {
        return content_body_custom_font_family_android;
    }

    public String getCounter_color() {
        return counter_color;
    }

    public void setCounter_color(String counter_color) {
        this.counter_color = counter_color;
    }

    public String getPosition_on_page() {
        return position_on_page;
    }

    public void setPosition_on_page(String position_on_page) {
        this.position_on_page = position_on_page;
    }

    public String getClose_button_color() {
        return close_button_color;
    }

    public void setClose_button_color(String close_button_color) {
        this.close_button_color = close_button_color;
    }

    public String getButton_color() {
        return button_color;
    }

    public String getButton_text_color() {
        return button_text_color;
    }

    public String getButton_custom_font_family_android() {
        return button_custom_font_family_android;
    }

    public String getBackground_color() {
        return background_color;
    }

}