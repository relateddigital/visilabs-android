package com.visilabs.inApp;

import java.io.Serializable;

public class ProductStatNotifierExtendedProps implements Serializable {
    public String getContent_text_color() {
        return content_text_color;
    }

    public String getContent_font_family() {
        return content_font_family;
    }

    public String getContent_text_size() {
        return content_text_size;
    }

    public String getContentcount_text_color() {
        return contentcount_text_color;
    }

    public String getContentcount_text_size() {
        return contentcount_text_size;
    }

    public String getClose_button_color() {
        return close_button_color;
    }

    private String content_text_color;

    private String content_font_family;

    private String content_text_size;

    private String contentcount_text_color;

    private String contentcount_text_size;

    private String close_button_color;
}
