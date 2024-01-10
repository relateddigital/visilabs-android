package com.visilabs.inApp.customactions.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomActionsActionData implements Serializable {

    @SerializedName("cid")
    private String cid;

    @SerializedName("courseofaction")
    private String courseofaction;

    @SerializedName("javascript")
    private String javascript;

    @SerializedName("content")
    private String content;

    @SerializedName("report")
    private CustomActionsReport report;

    @SerializedName("ExtendedProps")
    private String extendedProps;

    // Getter ve Setter metotları

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCourseofaction() {
        return courseofaction;
    }

    public void setCourseofaction(String courseofaction) {
        this.courseofaction = courseofaction;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CustomActionsReport getReport() {
        return report;
    }

    public void setReport(CustomActionsReport report) {
        this.report = report;
    }

    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }
}