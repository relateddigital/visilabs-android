package com.visilabs.mailSub;

import java.io.Serializable;

public class Actiondata implements Serializable {

    public String getPlaceholder() {
        return placeholder;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getButton_label() {
        return button_label;
    }

    public int getWaiting_time() {
        return waiting_time;
    }

    public Boolean getSendemail() {
        return sendemail;
    }

    public String getAuth() {
        return auth;
    }

    public String getType() {
        return type;
    }

    public Boolean getSendEventsToMyFriend() {
        return SendEventsToMyFriend;
    }

    public String getConsent_text() {
        return consent_text;
    }

    public String getSuccess_message() {
        return success_message;
    }

    public String getInvalid_email_message() {
        return invalid_email_message;
    }

    public String getEmailpermit_text() {
        return emailpermit_text;
    }

    public String getExtendedProps() {
        return ExtendedProps;
    }

    public String getLanguage() {
        return language;
    }

    public String getCheck_consent_message() {
        return check_consent_message;
    }

    public Report getReport() {
        return report;
    }

    private String placeholder;

    private String title;

    private String message;

    private String button_label;

    private int waiting_time;

    private Boolean sendemail;

    private String auth;

    private String type;

    private Boolean SendEventsToMyFriend;

    private String consent_text;

    private String success_message;

    private String invalid_email_message;

    private String emailpermit_text;

    private String ExtendedProps;

    private String language;

    private String check_consent_message;

    private Report report;

}
