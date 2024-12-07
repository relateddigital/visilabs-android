package com.visilabs.inApp.appBannerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppBannerExtendedProps  implements Serializable {


    @SerializedName("width")
    private Integer width;

    @SerializedName("height")
    private Integer height;


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

}
