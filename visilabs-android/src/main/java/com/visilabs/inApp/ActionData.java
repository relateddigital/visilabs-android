package com.visilabs.inApp;

import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.visilabs.util.SizeUtil;

public class ActionData implements Parcelable {
    @SerializedName("alert_type")
    private String mAlertType;
    @SerializedName("android_lnk")
    private String mAndroidLnk;
    @SerializedName("background")
    private String mBackground;
    @SerializedName("btn_text")
    private String mBtnText;
    @SerializedName("button_color")
    private String mButtonColor;
    @SerializedName("button_text_color")
    private String mButtonTextColor;
    @SerializedName("cid")
    private String mCid;
    @SerializedName("close_button_color")
    private String mCloseButtonColor;
    @SerializedName("close_button_text")
    private String mCloseButtonText;
    @SerializedName("courseofaction")
    private String mCourseOfAction;
    @SerializedName("font_family")
    private String mFontFamily;
    @SerializedName("img")
    private String mImg;
    @SerializedName("ios_lnk")
    private String mIosLnk;
    @SerializedName("msg_body")
    private String mMsgBody;
    @SerializedName("msg_body_color")
    private String mMsgBodyColor;
    @SerializedName("msg_body_textsize")
    private String mMsgBodyTextSize;
    @SerializedName("msg_title")
    private String mMsgTitle;
    @SerializedName("msg_title_color")
    private String mMsgTitleColor;
    @SerializedName("msg_type")
    private String mMsgType;
    @SerializedName("promocode_background_color")
    private final String mPromoCodeBackgroundColor;
    @SerializedName("promocode_text_color")
    private final String mPromoCodeTextColor;
    @SerializedName("promotion_code")
    private final String mPromotionCode;
    @SerializedName("number_colors")
    private String[] mNumberColors;
    @SerializedName("qs")
    private final String mQs;
    @SerializedName("visit_data")
    private final String mVisitData;
    @SerializedName("visitor_data")
    private final String mVisitorData;

    protected ActionData(Parcel in) {
        mAlertType = in.readString();
        mAndroidLnk = in.readString();
        mBackground = in.readString();
        mBtnText = in.readString();
        mButtonColor = in.readString();
        mButtonTextColor = in.readString();
        mCid = in.readString();
        mCloseButtonColor = in.readString();
        mCloseButtonText = in.readString();
        mCourseOfAction = in.readString();
        mFontFamily = in.readString();
        mImg = in.readString();
        mIosLnk = in.readString();
        mMsgBody = in.readString();
        mMsgBodyColor = in.readString();
        mMsgBodyTextSize = in.readString();
        mMsgTitle = in.readString();
        mMsgTitleColor = in.readString();
        mMsgType = in.readString();
        mPromoCodeBackgroundColor = in.readString();
        mPromoCodeTextColor = in.readString();
        mPromotionCode = in.readString();
        in.readStringArray(mNumberColors);
        mQs = in.readString();
        mVisitData = in.readString();
        mVisitorData = in.readString();
    }

    public static final Creator<ActionData> CREATOR = new Creator<ActionData>() {
        @Override
        public ActionData createFromParcel(Parcel in) {
            return new ActionData(in);
        }

        @Override
        public ActionData[] newArray(int size) {
            return new ActionData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlertType);
        dest.writeString(mAndroidLnk);
        dest.writeString(mBackground);
        dest.writeString(mBtnText);
        dest.writeString(mButtonColor);
        dest.writeString(mButtonTextColor);
        dest.writeString(mCid);
        dest.writeString(mCloseButtonColor);
        dest.writeString(mCloseButtonText);
        dest.writeString(mCourseOfAction);
        dest.writeString(mFontFamily);
        dest.writeString(mImg);
        dest.writeString(mIosLnk);
        dest.writeString(mMsgBody);
        dest.writeString(mMsgBodyColor);
        dest.writeString(mMsgBodyTextSize);
        dest.writeString(mMsgTitle);
        dest.writeString(mMsgTitleColor);
        dest.writeString(mMsgType);
        dest.writeString(mPromoCodeBackgroundColor);
        dest.writeString(mPromoCodeTextColor);
        dest.writeString(mPromotionCode);
        dest.writeStringArray(mNumberColors);
        dest.writeString(mQs);
        dest.writeString(mVisitData);
        dest.writeString(mVisitorData);
    }

    public String getAlertType() {
        return mAlertType;
    }

    public void setAlertType(String alertType) {
        mAlertType = alertType;
    }

    public String getAndroidLnk() {
        return mAndroidLnk;
    }

    public void setAndroidLnk(String androidLnk) {
        mAndroidLnk = androidLnk;
    }

    public String getBackground() {
        return mBackground;
    }

    public void setBackground(String background) {
        mBackground = background;
    }

    public String getBtnText() {
        return mBtnText;
    }

    public void setBtnText(String btnText) {
        mBtnText = btnText;
    }

    public String getButtonColor() {
        return mButtonColor;
    }

    public void setButtonColor(String buttonColor) {
        mButtonColor = buttonColor;
    }

    public String getButtonTextColor() {
        return mButtonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        mButtonTextColor = buttonTextColor;
    }

    public String getCid() {
        return mCid;
    }

    public void setCid(String cid) {
        mCid = cid;
    }

    public String getCloseButtonColor() {
        return mCloseButtonColor;
    }

    public void setCloseButtonColor(String closeButtonColor) {
        mCloseButtonColor = closeButtonColor;
    }

    public String getCloseButtonText() {
        return mCloseButtonText;
    }

    public void setCloseButtonText(String closeButtonText) {
        mCloseButtonText = closeButtonText;
    }

    public String getCourseOfAction() {
        return mCourseOfAction;
    }

    public void setCourseOfAction(String courseOfAction) {
        mCourseOfAction = courseOfAction;
    }

    public Typeface getFontFamily() {
        if (mFontFamily == null) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(mFontFamily.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(mFontFamily.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(mFontFamily.toLowerCase())) {
            return Typeface.SERIF;
        }

        return Typeface.DEFAULT;
    }

    public void setFontFamily(String fontFamily) {
        mFontFamily = fontFamily;
    }

    public String getImg() {
        if (getMsgType() == InAppActionType.MINI) {
            return SizeUtil.sizeSuffixUrl(mImg, "@1x");
        }
        return mImg;
    }

    public void setImg(String img) {
        mImg = img;
    }

    public String getIosLnk() {
        return mIosLnk;
    }

    public void setIosLnk(String iosLnk) {
        mIosLnk = iosLnk;
    }

    public String getMsgBody() {
        return mMsgBody;
    }

    public void setMsgBody(String msgBody) {
        mMsgBody = msgBody;
    }

    public String getMsgBodyColor() {
        return mMsgBodyColor;
    }

    public void setMsgBodyColor(String msgBodyColor) {
        mMsgBodyColor = msgBodyColor;
    }

    public String getMsgBodyTextSize() {
        if (mMsgBodyTextSize.equals("")) {
            mMsgBodyTextSize = "1";
        }
        return mMsgBodyTextSize;
    }

    public void setMsgBodyTextSize(String msgBodyTextSize) {
        mMsgBodyTextSize = msgBodyTextSize;
    }

    public String getMsgTitle() {
        return mMsgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        mMsgTitle = msgTitle;
    }

    public String getMsgTitleColor() {
        return mMsgTitleColor;
    }

    public void setMsgTitleColor(String msgTitleColor) {
        mMsgTitleColor = msgTitleColor;
    }

    public InAppActionType getMsgType() {
        if (mMsgType == null) {
            return InAppActionType.UNKNOWN;
        }
        if (InAppActionType.MINI.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.MINI;
        }
        if (InAppActionType.FULL.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.FULL;
        }
        if (InAppActionType.IMAGE_TEXT_BUTTON.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.IMAGE_TEXT_BUTTON;
        }
        if (InAppActionType.FULL_IMAGE.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.FULL_IMAGE;
        }
        if (InAppActionType.NPS.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.NPS;
        }
        if (InAppActionType.IMAGE_BUTTON.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.IMAGE_BUTTON;
        }
        if (InAppActionType.SMILE_RATING.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.SMILE_RATING;
        }
        if (InAppActionType.ALERT.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.ALERT;
        }
        if (InAppActionType.NPS_WITH_NUMBERS.toString().equals(mMsgType.toLowerCase())) {
            return InAppActionType.NPS_WITH_NUMBERS;
        }
        return InAppActionType.UNKNOWN;
    }

    public void setMsgType(String msgType) {
        mMsgType = msgType;
    }

    public String getPromoCodeBackgroundColor() {
        return mPromoCodeBackgroundColor;
    }

    public String getPromoCodeTextColor() {
        return mPromoCodeTextColor;
    }

    public String getPromotionCode() {
        return mPromotionCode;
    }

    public String[] getNumberColors() {
        return mNumberColors;
    }

    public String getQs() {
        return mQs;
    }

    public String getVisitData() {
        return mVisitData;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
