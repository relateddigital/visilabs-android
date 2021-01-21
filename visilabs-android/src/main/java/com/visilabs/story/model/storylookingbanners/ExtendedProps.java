package com.visilabs.story.model.storylookingbanners;

import java.io.Serializable;

public class ExtendedProps implements Serializable {

    private String storylb_img_borderWidth;

    private String storylb_img_borderRadius;

    private String storylb_img_boxShadow;

    private String storylb_img_borderColor;

    private String storylb_label_color;

    public String getStorylb_img_borderWidth() {
        return storylb_img_borderWidth;
    }

    public void setStorylb_img_borderWidth(String storylb_img_borderWidth) {
        this.storylb_img_borderWidth = storylb_img_borderWidth;
    }

    public String getStorylb_img_borderRadius() {
        return storylb_img_borderRadius;
    }

    public void setStorylb_img_borderRadius(String storylb_img_borderRadius) {
        this.storylb_img_borderRadius = storylb_img_borderRadius;
    }

    public String getStorylb_img_boxShadow() {
        return storylb_img_boxShadow;
    }

    public void setStorylb_img_boxShadow(String storylb_img_boxShadow) {
        this.storylb_img_boxShadow = storylb_img_boxShadow;
    }

    public String getStorylb_img_borderColor() {
        return storylb_img_borderColor;
    }

    public void setStorylb_img_borderColor(String storylb_img_borderColor) {
        this.storylb_img_borderColor = storylb_img_borderColor;
    }

    public String getStorylb_label_color() {
        return storylb_label_color;
    }

    public void setStorylb_label_color(String storylb_label_color) {
        this.storylb_label_color = storylb_label_color;
    }

    @Override
    public String toString() {
        return "ExtendedProps [storylb_img_borderWidth = " + storylb_img_borderWidth +
                ", storylb_img_borderRadius = " + storylb_img_borderRadius +
                ", storylb_img_boxShadow = " + storylb_img_boxShadow +
                ", storylb_img_borderColor = " + storylb_img_borderColor +
                ", storylb_label_color = " + storylb_label_color + "]";
    }
}