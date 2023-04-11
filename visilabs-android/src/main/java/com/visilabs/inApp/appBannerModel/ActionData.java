package com.visilabs.inApp.appBannerModel;

import java.util.ArrayList;
import java.util.List;

public class ActionData {

    private String taTemplate;
    private List<AppBanner> appBanners = new ArrayList<AppBanner>();
    private String transitionAction;
    private Report report;

    public String getTaTemplate() {
        return taTemplate;
    }
    public void setTaTemplate(String taTemplate) {
        this.taTemplate = taTemplate;
    }
    public List<AppBanner> getAppBanners() {
        return appBanners;
    }
    public void setAppBanners(List<AppBanner> appBanners) {
        this.appBanners = appBanners;
    }
    public String getTransitionAction() {
        return transitionAction;
    }
    public void setTransitionAction(String transitionAction) {
        this.transitionAction = transitionAction;
    }
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
    }
}
