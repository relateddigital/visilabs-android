package com.visilabs.inApp.carousel;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.content.res.ResourcesCompat;

import com.google.gson.annotations.SerializedName;
import com.visilabs.inApp.FontFamily;
import com.visilabs.util.AppUtils;

public class CarouselItem implements Parcelable {

    @SerializedName("image")
    private String image;
    @SerializedName("title")
    private String title;
    @SerializedName("title_color")
    private String titleColor;
    @SerializedName("title_font_family")
    private String titleFontFamily;
    @SerializedName("title_custom_font_family_ios")
    private String titleCustomFontFamilyIos;
    @SerializedName("title_custom_font_family_android")
    private String titleCustomFontFamilyAndroid;
    @SerializedName("title_textsize")
    private String titleTextsize;
    @SerializedName("body")
    private String body;
    @SerializedName("body_color")
    private String bodyColor;
    @SerializedName("body_font_family")
    private String bodyFontFamily;
    @SerializedName("body_custom_font_family_ios")
    private String bodyCustomFontFamilyIos;
    @SerializedName("body_custom_font_family_android")
    private String bodyCustomFontFamilyAndroid;
    @SerializedName("body_textsize")
    private String bodyTextsize;
    @SerializedName("promocode_type")
    private String promocodeType;
    @SerializedName("cid")
    private String cid;
    @SerializedName("promotion_code")
    private String promotionCode;
    @SerializedName("promocode_background_color")
    private String promocodeBackgroundColor;
    @SerializedName("promocode_text_color")
    private String promocodeTextColor;
    @SerializedName("button_text")
    private String buttonText;
    @SerializedName("button_text_color")
    private String buttonTextColor;
    @SerializedName("button_color")
    private String buttonColor;
    @SerializedName("button_font_family")
    private String buttonFontFamily;
    @SerializedName("button_custom_font_family_ios")
    private String buttonCustomFontFamilyIos;
    @SerializedName("button_custom_font_family_android")
    private String buttonCustomFontFamilyAndroid;
    @SerializedName("button_textsize")
    private String buttonTextsize;
    @SerializedName("background_image")
    private String backgroundImage;
    @SerializedName("background_color")
    private String backgroundColor;
    @SerializedName("ios_lnk")
    private String iosLnk;
    @SerializedName("android_lnk")
    private String androidLnk;
    @SerializedName("videourl")
    private String videoUrl;

    protected CarouselItem(Parcel in) {
        image = in.readString();
        title = in.readString();
        titleColor = in.readString();
        titleFontFamily = in.readString();
        titleCustomFontFamilyIos = in.readString();
        titleCustomFontFamilyAndroid = in.readString();
        titleTextsize = in.readString();
        body = in.readString();
        bodyColor = in.readString();
        bodyFontFamily = in.readString();
        bodyCustomFontFamilyIos = in.readString();
        bodyCustomFontFamilyAndroid = in.readString();
        bodyTextsize = in.readString();
        promocodeType = in.readString();
        cid = in.readString();
        promotionCode = in.readString();
        promocodeBackgroundColor = in.readString();
        promocodeTextColor = in.readString();
        buttonText = in.readString();
        buttonTextColor = in.readString();
        buttonColor = in.readString();
        buttonFontFamily = in.readString();
        buttonCustomFontFamilyIos = in.readString();
        buttonCustomFontFamilyAndroid = in.readString();
        buttonTextsize = in.readString();
        backgroundImage = in.readString();
        backgroundColor = in.readString();
        iosLnk = in.readString();
        androidLnk = in.readString();
        videoUrl = in.readString();
    }

    public static final Creator<CarouselItem> CREATOR = new Creator<CarouselItem>() {
        @Override
        public CarouselItem createFromParcel(Parcel in) {
            return new CarouselItem(in);
        }

        @Override
        public CarouselItem[] newArray(int size) {
            return new CarouselItem[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public Typeface getTitleFontFamily(Context context) {
        if (titleFontFamily == null || titleFontFamily.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(titleFontFamily.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(titleFontFamily.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(titleFontFamily.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(titleCustomFontFamilyAndroid != null && !titleCustomFontFamilyAndroid.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, titleCustomFontFamilyAndroid)) {
                int id = context.getResources().getIdentifier(titleCustomFontFamilyAndroid, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public void setTitleFontFamily(String titleFontFamily) {
        this.titleFontFamily = titleFontFamily;
    }

    public String getTitleCustomFontFamilyIos() {
        return titleCustomFontFamilyIos;
    }

    public void setTitleCustomFontFamilyIos(String titleCustomFontFamilyIos) {
        this.titleCustomFontFamilyIos = titleCustomFontFamilyIos;
    }

    public String getTitleCustomFontFamilyAndroid() {
        return titleCustomFontFamilyAndroid;
    }

    public void setTitleCustomFontFamilyAndroid(String titleCustomFontFamilyAndroid) {
        this.titleCustomFontFamilyAndroid = titleCustomFontFamilyAndroid;
    }

    public String getTitleTextsize() {
        return titleTextsize;
    }

    public void setTitleTextsize(String titleTextsize) {
        this.titleTextsize = titleTextsize;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public Typeface getBodyFontFamily(Context context) {
        if (bodyFontFamily == null || bodyFontFamily.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(bodyFontFamily.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(bodyFontFamily.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(bodyFontFamily.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(bodyCustomFontFamilyAndroid != null && !bodyCustomFontFamilyAndroid.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, bodyCustomFontFamilyAndroid)) {
                int id = context.getResources().getIdentifier(bodyCustomFontFamilyAndroid, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public void setBodyFontFamily(String bodyFontFamily) {
        this.bodyFontFamily = bodyFontFamily;
    }

    public String getBodyCustomFontFamilyIos() {
        return bodyCustomFontFamilyIos;
    }

    public void setBodyCustomFontFamilyIos(String bodyCustomFontFamilyIos) {
        this.bodyCustomFontFamilyIos = bodyCustomFontFamilyIos;
    }

    public String getBodyCustomFontFamilyAndroid() {
        return bodyCustomFontFamilyAndroid;
    }

    public void setBodyCustomFontFamilyAndroid(String bodyCustomFontFamilyAndroid) {
        this.bodyCustomFontFamilyAndroid = bodyCustomFontFamilyAndroid;
    }

    public String getBodyTextsize() {
        return bodyTextsize;
    }

    public void setBodyTextsize(String bodyTextsize) {
        this.bodyTextsize = bodyTextsize;
    }

    public String getPromocodeType() {
        return promocodeType;
    }

    public void setPromocodeType(String promocodeType) {
        this.promocodeType = promocodeType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPromocodeBackgroundColor() {
        return promocodeBackgroundColor;
    }

    public void setPromocodeBackgroundColor(String promocodeBackgroundColor) {
        this.promocodeBackgroundColor = promocodeBackgroundColor;
    }

    public String getPromocodeTextColor() {
        return promocodeTextColor;
    }

    public void setPromocodeTextColor(String promocodeTextColor) {
        this.promocodeTextColor = promocodeTextColor;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public Typeface getButtonFontFamily(Context context) {
        if (buttonFontFamily == null || buttonFontFamily.equals("")) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(buttonFontFamily.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(buttonFontFamily.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(buttonFontFamily.toLowerCase())) {
            return Typeface.SERIF;
        }
        if(buttonCustomFontFamilyAndroid != null && !buttonCustomFontFamilyAndroid.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, buttonCustomFontFamilyAndroid)) {
                int id = context.getResources().getIdentifier(buttonCustomFontFamilyAndroid, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public void setButtonFontFamily(String buttonFontFamily) {
        this.buttonFontFamily = buttonFontFamily;
    }

    public String getButtonCustomFontFamilyIos() {
        return buttonCustomFontFamilyIos;
    }

    public void setButtonCustomFontFamilyIos(String buttonCustomFontFamilyIos) {
        this.buttonCustomFontFamilyIos = buttonCustomFontFamilyIos;
    }

    public String getButtonCustomFontFamilyAndroid() {
        return buttonCustomFontFamilyAndroid;
    }

    public void setButtonCustomFontFamilyAndroid(String buttonCustomFontFamilyAndroid) {
        this.buttonCustomFontFamilyAndroid = buttonCustomFontFamilyAndroid;
    }

    public String getButtonTextsize() {
        return buttonTextsize;
    }

    public void setButtonTextsize(String buttonTextsize) {
        this.buttonTextsize = buttonTextsize;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getIosLnk() {
        return iosLnk;
    }

    public void setIosLnk(String iosLnk) {
        this.iosLnk = iosLnk;
    }

    public String getAndroidLnk() {
        return androidLnk;
    }

    public void setAndroidLnk(String androidLnk) {
        this.androidLnk = androidLnk;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(titleColor);
        dest.writeString(titleFontFamily);
        dest.writeString(titleCustomFontFamilyIos);
        dest.writeString(titleCustomFontFamilyAndroid);
        dest.writeString(titleTextsize);
        dest.writeString(body);
        dest.writeString(bodyColor);
        dest.writeString(bodyFontFamily);
        dest.writeString(bodyCustomFontFamilyIos);
        dest.writeString(bodyCustomFontFamilyAndroid);
        dest.writeString(bodyTextsize);
        dest.writeString(promocodeType);
        dest.writeString(cid);
        dest.writeString(promotionCode);
        dest.writeString(promocodeBackgroundColor);
        dest.writeString(promocodeTextColor);
        dest.writeString(buttonText);
        dest.writeString(buttonTextColor);
        dest.writeString(buttonColor);
        dest.writeString(buttonFontFamily);
        dest.writeString(buttonCustomFontFamilyIos);
        dest.writeString(buttonCustomFontFamilyAndroid);
        dest.writeString(buttonTextsize);
        dest.writeString(backgroundImage);
        dest.writeString(backgroundColor);
        dest.writeString(iosLnk);
        dest.writeString(androidLnk);
        dest.writeString(videoUrl);
    }
}
