package com.visilabs.mailSub;

import com.visilabs.story.model.Report;

import java.io.Serializable;

public class ActionData implements Serializable {
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
