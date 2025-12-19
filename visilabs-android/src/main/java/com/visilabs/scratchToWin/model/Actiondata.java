package com.visilabs.scratchToWin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Actiondata implements Serializable {
    @SerializedName("cid")
    private String cid;
    @SerializedName("mail_subscription")
    private Boolean mailSubscription;
    @SerializedName("scratch_color")
    private String scratchColor;
    @SerializedName("auth")
    private String auth;
    @SerializedName("type")
    private String type;
    @SerializedName("waiting_time")
    private Integer waitingTime;
    @SerializedName("code")
    private String code;
    @SerializedName("sendemail")
    private Boolean sendemail;
    @SerializedName("mail_subscription_form")
    private MailSubscriptionForm mailSubscriptionForm;
    @SerializedName("ExtendedProps")
    private String extendedProps;
    @SerializedName("report")
    private Report report;
    @SerializedName("copybutton_label")
    private String copybuttonLabel;
    @SerializedName("promotion_code")
    private String promotionCode;
    @SerializedName("img")
    private String img;
    @SerializedName("content_title")
    private String contentTitle;
    @SerializedName("content_body")
    private String contentBody;
    @SerializedName("down_content_body")
    private String downContentBody;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Boolean getMailSubscription() {
        return mailSubscription;
    }

    public void setMailSubscription(Boolean mailSubscription) {
        this.mailSubscription = mailSubscription;
    }

    public String getScratchColor() {
        return scratchColor;
    }

    public void setScratchColor(String scratchColor) {
        this.scratchColor = scratchColor;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSendemail() {
        return sendemail;
    }

    public void setSendemail(Boolean sendemail) {
        this.sendemail = sendemail;
    }

    public MailSubscriptionForm getMailSubscriptionForm() {
        return mailSubscriptionForm;
    }

    public void setMailSubscriptionForm(MailSubscriptionForm mailSubscriptionForm) {
        this.mailSubscriptionForm = mailSubscriptionForm;
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

    public String getCopybuttonLabel() {
        return copybuttonLabel;
    }

    public void setCopybuttonLabel(String copybuttonLabel) {
        this.copybuttonLabel = copybuttonLabel;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getDownContentBody() {
        return downContentBody;
    }

    public void setDownContentBody(String downContentBody) {
        this.downContentBody = downContentBody;
    }
}
