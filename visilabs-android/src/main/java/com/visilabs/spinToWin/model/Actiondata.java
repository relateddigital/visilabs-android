package com.visilabs.spinToWin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Actiondata implements Serializable {

    @SerializedName("slices")
    private List<Slice> slices = null;
    @SerializedName("mail_subscription")
    private Boolean mailSubscription;
    @SerializedName("spin_to_win_content")
    private SpinToWinContent spinToWinContent;
    @SerializedName("font_size")
    private Integer fontSize;
    @SerializedName("circle_R")
    private Integer circleR;
    @SerializedName("auth")
    private String auth;
    @SerializedName("type")
    private String type;
    @SerializedName("waiting_time")
    private Integer waitingTime;
    @SerializedName("promoAuth")
    private String promoAuth;
    @SerializedName("slice_count")
    private String sliceCount;
    @SerializedName("sendemail")
    private Boolean sendemail;
    @SerializedName("courseofaction")
    private String courseofaction;
    @SerializedName("ExtendedProps")
    private String extendedProps;
    @SerializedName("report")
    private Report report;

    public List<Slice> getSlices() {
        return slices;
    }

    public void setSlices(List<Slice> slices) {
        this.slices = slices;
    }

    public Boolean getMailSubscription() {
        return mailSubscription;
    }

    public void setMailSubscription(Boolean mailSubscription) {
        this.mailSubscription = mailSubscription;
    }

    public SpinToWinContent getSpinToWinContent() {
        return spinToWinContent;
    }

    public void setSpinToWinContent(SpinToWinContent spinToWinContent) {
        this.spinToWinContent = spinToWinContent;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getCircleR() {
        return circleR;
    }

    public void setCircleR(Integer circleR) {
        this.circleR = circleR;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getPromoAuth() {
        return promoAuth;
    }

    public void setPromoAuth(String promoAuth) {
        this.promoAuth = promoAuth;
    }

    public String getSliceCount() {
        return sliceCount;
    }

    public void setSliceCount(String sliceCount) {
        this.sliceCount = sliceCount;
    }

    public Boolean getSendemail() {
        return sendemail;
    }

    public void setSendemail(Boolean sendemail) {
        this.sendemail = sendemail;
    }

    public String getCourseofaction() {
        return courseofaction;
    }

    public void setCourseofaction(String courseofaction) {
        this.courseofaction = courseofaction;
    }

    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

}
