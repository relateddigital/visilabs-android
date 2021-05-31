package com.visilabs.scratchToWin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MailSubscriptionForm implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("placeholder")
    private String placeholder;
    @SerializedName("button_label")
    private String buttonLabel;
    @SerializedName("consent_text")
    private String consentText;
    @SerializedName("invalid_email_message")
    private String invalidEmailMessage;
    @SerializedName("success_message")
    private String successMessage;
    @SerializedName("emailpermit_text")
    private String emailpermitText;
    @SerializedName("check_consent_message")
    private String checkConsentMessage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public String getConsentText() {
        return consentText;
    }

    public void setConsentText(String consentText) {
        this.consentText = consentText;
    }

    public String getInvalidEmailMessage() {
        return invalidEmailMessage;
    }

    public void setInvalidEmailMessage(String invalidEmailMessage) {
        this.invalidEmailMessage = invalidEmailMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getEmailpermitText() {
        return emailpermitText;
    }

    public void setEmailpermitText(String emailpermitText) {
        this.emailpermitText = emailpermitText;
    }

    public String getCheckConsentMessage() {
        return checkConsentMessage;
    }

    public void setCheckConsentMessage(String checkConsentMessage) {
        this.checkConsentMessage = checkConsentMessage;
    }
}
