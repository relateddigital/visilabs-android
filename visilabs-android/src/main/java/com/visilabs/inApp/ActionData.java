package com.visilabs.inApp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.content.res.ResourcesCompat;

import com.google.gson.annotations.SerializedName;
import com.visilabs.inApp.carousel.CarouselItem;
import com.visilabs.util.AppUtils;
import com.visilabs.util.SizeUtil;

import java.util.List;

public class ActionData implements Parcelable {
    @SerializedName("display_type")
    private String mDisplayType;
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
    @SerializedName("waiting_time")
    private final String mWaitingTime;
    @SerializedName("secondPopup_type")
    private final String mSecondPopupType;
    @SerializedName("secondPopup_msg_title")
    private final String mSecondPopupMsgTitle;
    @SerializedName("secondPopup_msg_body")
    private final String mSecondPopupMsgBody;
    @SerializedName("secondPopup_btn_text")
    private final String mSecondPopupBtnText;
    @SerializedName("secondPopup_msg_body_textsize")
    private final String mSecondPopupMsgBodyTextSize;
    @SerializedName("secondPopup_feedbackform_minpoint")
    private final String mSecondPopupFeecbackFormMinPoint;
    @SerializedName("secondPopup_image1")
    private final String mSecondPopupImg1;
    @SerializedName("secondPopup_image2")
    private final String mSecondPopupImg2;
    @SerializedName("pos")
    private final String mPos;
    @SerializedName("msg_title_textsize")
    private final String mMsgTitleTextSize;
    @SerializedName("close_event_trigger")
    private final String mCloseEventTrigger;
    @SerializedName("custom_font_family_ios")
    private final String mCustomFontFamilyIos;
    @SerializedName("custom_font_family_android")
    private final String mCustomFontFamilyAndroid;
    @SerializedName("carousel_items")
    private List<CarouselItem> carouselItems = null;
    @SerializedName("msg_title_backgroundcolor")
    private final String mMsgTitleBackgroundColor;
    @SerializedName("msg_body_backgroundcolor")
    private final String mMsgBodyBackgroundColor;
    @SerializedName("button_function")
    private final String mButtonFunction;
    @SerializedName("videourl")
    private final String mVideoUrl;
    @SerializedName("secondPopup_videourl1")
    private final String mSecondPopupVideoUrl1;
    @SerializedName("secondPopup_videourl2")
    private final String mSecondPopupVideoUrl2;
    @SerializedName("promocode_copybutton_function")
    private final String mPromoCodeCopyButtonFunction;

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
        mWaitingTime = in.readString();
        mSecondPopupType = in.readString();
        mSecondPopupMsgTitle = in.readString();
        mSecondPopupMsgBody = in.readString();
        mSecondPopupBtnText = in.readString();
        mSecondPopupMsgBodyTextSize = in.readString();
        mSecondPopupFeecbackFormMinPoint = in.readString();
        mSecondPopupImg1 = in.readString();
        mSecondPopupImg2 = in.readString();
        mPos = in.readString();
        mMsgTitleTextSize = in.readString();
        mCloseEventTrigger = in.readString();
        mCustomFontFamilyIos = in.readString();
        mCustomFontFamilyAndroid = in.readString();
        carouselItems = in.readParcelable(CarouselItem.class.getClassLoader());
        mMsgTitleBackgroundColor = in.readString();
        mMsgBodyBackgroundColor = in.readString();
        mButtonFunction = in.readString();
        mPromoCodeCopyButtonFunction = in.readString();
        mVideoUrl = in.readString();
        mSecondPopupVideoUrl1 = in.readString();
        mSecondPopupVideoUrl2 = in.readString();
        mDisplayType = in.readString();
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
        dest.writeString(mWaitingTime);
        dest.writeString(mSecondPopupType);
        dest.writeString(mSecondPopupMsgTitle);
        dest.writeString(mSecondPopupMsgBody);
        dest.writeString(mSecondPopupBtnText);
        dest.writeString(mSecondPopupMsgBodyTextSize);
        dest.writeString(mSecondPopupFeecbackFormMinPoint);
        dest.writeString(mSecondPopupImg1);
        dest.writeString(mSecondPopupImg2);
        dest.writeString(mPos);
        dest.writeString(mMsgTitleTextSize);
        dest.writeString(mCloseEventTrigger);
        dest.writeString(mCustomFontFamilyIos);
        dest.writeString(mCustomFontFamilyAndroid);
        dest.writeParcelable((Parcelable) carouselItems, 0);
        dest.writeString(mMsgTitleBackgroundColor);
        dest.writeString(mMsgBodyBackgroundColor);
        dest.writeString(mButtonFunction);
        dest.writeString(mPromoCodeCopyButtonFunction);
        dest.writeString(mVideoUrl);
        dest.writeString(mSecondPopupVideoUrl1);
        dest.writeString(mSecondPopupVideoUrl2);
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

    public Typeface getFontFamily(Context context) {
        if (mFontFamily == null || mFontFamily.equals("")) {
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
        if(mCustomFontFamilyAndroid != null && !mCustomFontFamilyAndroid.isEmpty()) {
            if (AppUtils.isResourceAvailable(context, mCustomFontFamilyAndroid)) {
                int id = context.getResources().getIdentifier(mCustomFontFamilyAndroid, "font", context.getPackageName());
                return ResourcesCompat.getFont(context, id);
            }
        }

        return Typeface.DEFAULT;
    }

    public List<CarouselItem> getCarouselItems() {
        return carouselItems;
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
        InAppActionType result;

        if (mMsgType == null) {
            result = InAppActionType.UNKNOWN;
        } else if (InAppActionType.MINI.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.MINI;
        } else if (InAppActionType.FULL.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.FULL;
        } else if (InAppActionType.IMAGE_TEXT_BUTTON.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.IMAGE_TEXT_BUTTON;
        } else if (InAppActionType.FULL_IMAGE.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.FULL_IMAGE;
        } else if (InAppActionType.NPS.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.NPS;
        } else if (InAppActionType.IMAGE_BUTTON.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.IMAGE_BUTTON;
        } else if (InAppActionType.SMILE_RATING.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.SMILE_RATING;
        } else if (InAppActionType.ALERT.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.ALERT;
        } else if (InAppActionType.NPS_WITH_NUMBERS.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.NPS_WITH_NUMBERS;
        } else if(InAppActionType.CAROUSEL.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.CAROUSEL;
        } else if(InAppActionType.NPS_AND_SECOND_POP_UP.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.NPS_AND_SECOND_POP_UP;
        } else if(InAppActionType.HALF_SCREEN.toString().equals(mMsgType.toLowerCase())) {
            result = InAppActionType.HALF_SCREEN;
        } else {
            result = InAppActionType.UNKNOWN;
        }
        return result;
    }

    public void setMsgType(String msgType) {
        mMsgType = msgType;
    }

    public String getDisplayType()  {
        return mDisplayType;

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

    public String getWaitingTime() {
        return mWaitingTime;
    }

    public String getSecondPopupType() {
        return mSecondPopupType;
    }

    public String getSecondPopupMsgTitle() {
        return mSecondPopupMsgTitle;
    }

    public String getSecondPopupMsgBody() {
        return mSecondPopupMsgBody;
    }

    public String getSecondPopupBtnText() {
        return mSecondPopupBtnText;
    }

    public String getSecondPopupMsgBodyTextSize() {
        return mSecondPopupMsgBodyTextSize;
    }

    public String getSecondPopupFeecbackFormMinPoint() {
        return mSecondPopupFeecbackFormMinPoint;
    }

    public String getSecondPopupImg1() {
        return mSecondPopupImg1;
    }

    public String getSecondPopupImg2() {
        return mSecondPopupImg2;
    }

    public String getPos() {
        return mPos;
    }

    public String getMsgTitleTextSize() {
        return mMsgTitleTextSize;
    }

    public String getCloseEventTrigger() {
        return mCloseEventTrigger;
    }

    public java.lang.String getCustomFontFamilyIos() {
        return mCustomFontFamilyIos;
    }

    public String getCustomFontFamilyAndroid() {
        return mCustomFontFamilyAndroid;
    }

    public String getMsgTitleBackgroundColor() {
        return mMsgTitleBackgroundColor;
    }

    public String getMsgBodyBackgroundColor() {
        return mMsgBodyBackgroundColor;
    }

    public String getButtonFunction() {
        return mButtonFunction;
    }

    public String getmPromoCodeCopyButtonFunction() {
        return mPromoCodeCopyButtonFunction;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getSecondPopupVideoUrl1() {
        return mSecondPopupVideoUrl1;
    }

    public String getSecondPopupVideoUrl2() {
        return mSecondPopupVideoUrl2;
    }
}
