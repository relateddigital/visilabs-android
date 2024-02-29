package com.visilabs.inApp.appratingmodel;

import com.google.gson.annotations.SerializedName;

public class AppRatingActionData {

    @SerializedName("content")
    private String content;

    @SerializedName("report")
    private AppRatingReport report;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AppRatingReport getReport() {
        return report;
    }

    public void setReport(AppRatingReport report) {
        this.report = report;
    }
}
