package com.visilabs.inApp.appBannerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppBannerActionData implements Serializable {
    @SerializedName("taTemplate")
    private String taTemplate;

    @SerializedName("app_banners")
    private List<AppBannerItem> appBanners;

    @SerializedName("transition_action")
    private String transitionAction;

    @SerializedName("report")
    private AppBannerReport report;

    @SerializedName("ExtendedProps")
    private String extendedProps;


    // getter and setter methods for each field
    public String getTaTemplate() {
        return taTemplate;
    }

    public void setTaTemplate(String taTemplate) {
        this.taTemplate = taTemplate;
    }

    public List<AppBannerItem> getAppBanners() {
        return appBanners;
    }

    public void setAppBanners(List<AppBannerItem> appBanners) {
        this.appBanners = appBanners;
    }

    public String getTransitionAction() {
        return transitionAction;
    }

    public void setTransitionAction(String transitionAction) {
        this.transitionAction = transitionAction;
    }

    public AppBannerReport getReport() {
        return report;
    }

    public void setReport(AppBannerReport report) {
        this.report = report;
    }

    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }
}
