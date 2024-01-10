package com.visilabs.inApp.customactions.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class CustomActionsExtendedProps implements Serializable {

    @SerializedName("position")
    private String position;

    @SerializedName("width")
    private Integer width;

    @SerializedName("height")
    private Integer height;

    @SerializedName("close_button_color")
    private String closeButtonColor;

    @SerializedName("border_radius")
    private Integer borderRadius;

    // Getter ve Setter metotları

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCloseButtonColor() {
        return closeButtonColor;
    }

    public void setCloseButtonColor(String closeButtonColor) {
        this.closeButtonColor = closeButtonColor;
    }

    public Integer getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(Integer borderRadius) {
        this.borderRadius = borderRadius;
    }
}