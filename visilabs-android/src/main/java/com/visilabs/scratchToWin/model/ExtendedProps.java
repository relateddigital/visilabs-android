package com.visilabs.scratchToWin.model;

import android.graphics.Typeface;

import com.google.gson.annotations.SerializedName;
import com.visilabs.inApp.FontFamily;

import java.io.Serializable;

public class ExtendedProps implements Serializable {
    @SerializedName("content_title_text_color")
    private String content_title_text_color;
    @SerializedName("content_title_font_family")
    private String content_title_font_family;
    @SerializedName("content_title_text_size")
    private String content_title_text_size;
    @SerializedName("content_body_text_color")
    private String content_body_text_color;
    @SerializedName("content_body_text_font_family")
    private String content_body_text_font_family;
    @SerializedName("content_body_text_size")
    private String content_body_text_size;
    @SerializedName("button_color")
    private String button_color;
    @SerializedName("button_text_color")
    private String button_text_color;
    @SerializedName("button_font_family")
    private String button_font_family;
    @SerializedName("button_text_size")
    private String button_text_size;
    @SerializedName("promocode_text_color")
    private String promocode_text_color;
    @SerializedName("promocode_font_family")
    private String promocode_font_family;
    @SerializedName("promocode_text_size")
    private String promocode_text_size;
    @SerializedName("copybutton_color")
    private String copybutton_color;
    @SerializedName("copybutton_text_color")
    private String copybutton_text_color;
    @SerializedName("copybutton_font_family")
    private String copybutton_font_family;
    @SerializedName("copybutton_text_size")
    private String copybutton_text_size;
    @SerializedName("emailpermit_text_size")
    private String emailpermit_text_size;
    @SerializedName("emailpermit_text_url")
    private String emailpermit_text_url;
    @SerializedName("consent_text_size")
    private String consent_text_size;
    @SerializedName("consent_text_url")
    private String consent_text_url;
    @SerializedName("close_button_color")
    private String close_button_color;
    @SerializedName("background_color")
    private String background_color;

    public void setContentTitleTextColor(String contentTitleTextColor) {
        content_title_text_color = contentTitleTextColor;
    }

    public String getContentTitleTextColor() {
        return content_title_text_color;
    }

    public void setContentTitleFontFamily(String contentTitleFontFamily) {
        content_title_font_family = contentTitleFontFamily;
    }

    public Typeface getContentTitleFontFamily() {
        if (content_title_font_family == null || content_title_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(content_title_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(content_title_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(content_title_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }

        return Typeface.DEFAULT;
    }

    public void setContentTitleTextSize(String contentTitleTextSize) {
        content_title_text_size = contentTitleTextSize;
    }

    public String getContentTitleTextSize() {
        return content_title_text_size;
    }

    public void setContentBodyTextColor(String contentBodyTextColor) {
        content_body_text_color = contentBodyTextColor;
    }

    public String getContentBodyTextColor() {
        return content_body_text_color;
    }

    public void setContentBodyTextFontFamily(String contentBodyTextFontFamily) {
        content_body_text_font_family = contentBodyTextFontFamily;
    }

    public Typeface getContentBodyTextFontFamily() {
        if (content_body_text_font_family == null || content_body_text_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(content_body_text_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(content_body_text_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(content_body_text_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }

        return Typeface.DEFAULT;
    }

    public void setContentBodyTextSize(String contentBodyTextSize) {
        content_body_text_size = contentBodyTextSize;
    }

    public String getContentBodyTextSize() {
        return content_body_text_size;
    }

    public void setButtonColor(String buttonColor) {
        button_color = buttonColor;
    }

    public String getButtonColor() {
        return button_color;
    }

    public void setButtonTextColor(String buttonTextColor) {
        button_text_color = buttonTextColor;
    }

    public String getButtonTextColor() {
        return button_text_color;
    }

    public void setButtonFontFamily(String buttonFontFamily) {
        button_font_family = buttonFontFamily;
    }

    public Typeface getButtonFontFamily() {
        if (button_font_family == null || button_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(button_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(button_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(button_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }

        return Typeface.DEFAULT;
    }

    public void setButtonTextSize(String buttonTextSize) {
        button_text_size = buttonTextSize;
    }

    public String getButtonTextSize() {
        return button_text_size;
    }

    public void setPromoCodeTextColor(String promoCodeTextColor) {
        promocode_text_color = promoCodeTextColor;
    }

    public String getPromoCodeTextColor() {
        return promocode_text_color;
    }

    public void setPromoCodeFontFamily(String promoCodeFontFamily) {
        promocode_font_family = promoCodeFontFamily;
    }

    public Typeface getPromoCodeFontFamily() {
        if (promocode_font_family == null || promocode_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(promocode_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(promocode_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(promocode_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }

        return Typeface.DEFAULT;
    }

    public void setPromoCodeTextSize(String promoCodeTextSize) {
        promocode_text_size = promoCodeTextSize;
    }

    public String getPromoCodeTextSize() {
        return promocode_text_size;
    }

    public void setCopyButtonColor(String copyButtonColor) {
        copybutton_color = copyButtonColor;
    }

    public String getCopyButtonColor() {
        return copybutton_color;
    }

    public void setCopyButtonTextColor(String copyButtonTextColor) {
        copybutton_text_color = copyButtonTextColor;
    }

    public String getCopyButtonTextColor() {
        return copybutton_text_color;
    }

    public void setCopyButtonFontFamily(String copyButtonFontFamily) {
        copybutton_font_family = copyButtonFontFamily;
    }

    public Typeface getCopyButtonFontFamily() {
        if (copybutton_font_family == null || copybutton_font_family.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(copybutton_font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(copybutton_font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(copybutton_font_family.toLowerCase())) {
            return Typeface.SERIF;
        }

        return Typeface.DEFAULT;
    }

    public void setCopyButtonTextSize(String copyButtonTextSize) {
        copybutton_text_size = copyButtonTextSize;
    }

    public String getCopyButtonTextSize() {
        return copybutton_text_size;
    }

    public void setEmailPermitTextSize(String emailPermitTextSize) {
        emailpermit_text_size = emailPermitTextSize;
    }

    public String getEmailPermitTextSize() {
        return emailpermit_text_size;
    }

    public void setEmailPermitTextUrl(String emailPermitTextUrl) {
        emailpermit_text_url = emailPermitTextUrl;
    }

    public String getEmailPermitTextUrl() {
        return emailpermit_text_url;
    }

    public void setConsentTextSize(String consentTextSize) {
        consent_text_size = consentTextSize;
    }

    public String getConsentTextSize() {
        return consent_text_size;
    }

    public void setConsentTextUrl(String consentTextUrl) {
        consent_text_url = consentTextUrl;
    }

    public String getConsentTextUrl() {
        return consent_text_url;
    }

    public void setCloseButtonColor(String closeButtonColor) {
        close_button_color = closeButtonColor;
    }

    public String getCloseButtonColor() {
        return close_button_color;
    }

    public void setBackgroundColor(String backgroundColor) {
        background_color = backgroundColor;
    }

    public String getBackgroundColor() {
        return background_color;
    }
}
