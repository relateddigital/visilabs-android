package com.visilabs.notificationbell.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotificationBellActionData {

    @SerializedName("title")
    private String title;

    @SerializedName("bell_icon")
    private String bell_icon;

    @SerializedName("bell_animation")
    private String bell_animation;

    @SerializedName("notification_texts")
    private List<NotificationBellTexts> notification_texts;

    @SerializedName("report")
    private NotificationBellReport report;

    @SerializedName("ExtendedProps")
    private String extendedProps;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBell_icon() {
        return bell_icon;
    }

    public void setBell_icon(String bell_icon) {
        this.bell_icon = bell_icon;
    }

    public String getBell_animation() {
        return bell_animation;
    }

    public void setBell_animation(String bell_animation) {
        this.bell_animation = bell_animation;
    }

    public List<NotificationBellTexts> getNotification_texts() {
        return notification_texts;
    }

    public void setNotification_texts(List<NotificationBellTexts> notification_texts) {
        this.notification_texts = notification_texts;
    }

    public NotificationBellReport getReport() {
        return report;
    }

    public void setReport(NotificationBellReport report) {
        this.report = report;
    }

    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }
}