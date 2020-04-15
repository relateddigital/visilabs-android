package com.visilabs.inApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.regex.Pattern;

import com.visilabs.exceptions.VisilabsNotificationException;
import com.visilabs.json.JSONObject;
import com.visilabs.json.JSONException;
import com.visilabs.util.ImageStore;
import com.visilabs.util.SizeUtil;

public class InAppMessage implements Parcelable {

    private Bitmap mImage;

    private JSONObject mDescription;
    private int mId;
    private String msg_type;
    private String img;
    private String visitor_data;
    private String visit_data;
    private String qs;

    private String font_family;
    private String background;

    private String btn_text;
    private String mButtonURL;
    private String button_color;
    private String button_text_color;

    private String msg_title;
    private String msg_title_color;

    private String msg_body;
    private String msg_body_color;
    private String msg_body_textsize;
    private String close_button_color;

    private final Context mContext;

    private static final String LOG_TAG = "VisilabsNotification";
    private static final Pattern FILE_EXTENSION_PATTERN = Pattern.compile("(\\.[^./]+$)");

    public InAppMessage(Parcel in) {
        JSONObject temp = new JSONObject();
        try {
            temp = new JSONObject(in.readString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error reading JSON when creating VisilabsNotification from Parcel");
        }
        mDescription = temp;
        mId = in.readInt();
        msg_type = in.readString();
        msg_title = in.readString();
        msg_body = in.readString();
        img = in.readString();
        btn_text = in.readString();
        mButtonURL = in.readString();
        visitor_data = in.readString();
        visit_data = in.readString();
        qs = in.readString();
        mImage = in.readParcelable(Bitmap.class.getClassLoader());

        msg_title_color = in.readString();
        msg_body_color = in.readString();
        msg_body_textsize = in.readString();
        font_family = in.readString();
        background = in.readString();
        button_text_color = in.readString();
        button_color = in.readString();
        close_button_color = in.readString();

        mContext = null;
    }

    public InAppMessage(JSONObject description, Context context) throws VisilabsNotificationException {
        try {
            mDescription = description;
            mId = description.getInt("actid");

            JSONObject actionData = description.getJSONObject("actiondata");

            msg_type = actionData.getString("msg_type");
            msg_title = actionData.getString("msg_title");
            msg_body = actionData.getString("msg_body");
            img = actionData.getString("img");
            btn_text = actionData.getString("btn_text");
            mButtonURL = actionData.getString("android_lnk");
            visitor_data = actionData.getString("visitor_data");
            visit_data = actionData.getString("visit_data");
            qs = actionData.getString("qs");


            msg_title_color = actionData.getString("msg_title_color");
            msg_body_color = actionData.getString("msg_body_color");
            msg_body_textsize = actionData.getString("msg_body_textsize");
            font_family = actionData.getString("font_family");
            background = actionData.getString("background");
            close_button_color = "white";
           // close_button_color = actionData.getString("close_button_color");
            button_text_color = actionData.getString("button_text_color");
            button_color = actionData.getString("button_color");

            mContext = context;


        } catch (final JSONException e) {
            throw new VisilabsNotificationException("Notification JSON was unexpected or bad", e);
        }
    }

    String toJSON() {
        return mDescription.toString();
    }

    public int getId() {
        return mId;
    }

    public void setType(String mType) {
        this.msg_type = mType;
    }

    public InAppActionType getType() {
        if (msg_type == null) {
            return InAppActionType.UNKNOWN;
        }
        if (InAppActionType.MINI.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.MINI;
        }
        if (InAppActionType.FULL.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.FULL;
        }
        if (InAppActionType.IMAGE_TEXT_BUTTON.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.IMAGE_TEXT_BUTTON;
        }

        if (InAppActionType.FULL_IMAGE.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.FULL_IMAGE;
        }

        if (InAppActionType.NPS.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.NPS;
        }

        if (InAppActionType.IMAGE_BUTTON.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.IMAGE_BUTTON;
        }

        if (InAppActionType.SMILE_RATING.toString().equals(msg_type.toLowerCase())) {
            return InAppActionType.SMILE_RATING;
        }
        return InAppActionType.UNKNOWN;
    }

    public String getTitle() {
        return msg_title;
    }

    public String getBody() {
        return msg_body;
    }


    private Bitmap getNotificationImage(Context context) {
        String url = getImageUrl();
        try {
            return createImageStore(context).getImage(url);
        } catch (ImageStore.CantGetImageException e) {
            Log.v(LOG_TAG, "Can't load image " + url + " for a notification", e);
        }
        return null;
    }


    protected ImageStore createImageStore(final Context context) {
        return new ImageStore(context, "DecideChecker");
    }

    public String getImageUrl() {

        if (getType() == InAppActionType.MINI) {
            return SizeUtil.sizeSuffixUrl(img, "@1x");
        }
        return img;
    }

    //TODO:bunlara bak bir gerek var mı?
    public String getImage2xUrl() {
        return SizeUtil.sizeSuffixUrl(img, "@2x");
    }

    public String getImage4xUrl() {
        return SizeUtil.sizeSuffixUrl(img, "@4x");
    }

    void setImage(final Bitmap image) {
        mImage = image;
    }

    public Bitmap getImageBitmap() {
        if (mImage != null) {
            return mImage;
        } else {
            try {
                final Bitmap image = getNotificationImage(mContext);
                if (null == image) {
                    Log.i(LOG_TAG, "Could not retrieve image for notification " + getId() +
                            ", will not show the notification.");
                    return Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
                } else {
                    mImage = image;
                    return mImage;
                }
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Can not create image from URL.", ex);
                return Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            }
        }
    }

    public String getButtonText() {
        return btn_text;
    }

    public String getButtonURL() {
        return mButtonURL;
    }

    public String getVisitorData() {
        return visitor_data;
    }

    public String getVisitData() {
        return visit_data;
    }

    public String getQueryString() {
        return qs;
    }

    //


    public Typeface getFont_family() {
        if (font_family == null) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansaSerif.toString().equals(font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(font_family.toLowerCase())) {
            return Typeface.SERIF;
        }

        if (FontFamily.Default.toString().equals(font_family.toLowerCase())) {
            return Typeface.DEFAULT;
        }

        return Typeface.DEFAULT;
    }

    public String getBackground() {
        return background;
    }

    public String getButton_color() {
        return button_color;
    }

    public String getButton_text_color() {
        return button_text_color;
    }

    public String getMsg_title_color() {
        return msg_title_color;
    }

    public String getMsg_body_color() {
        return msg_body_color;
    }

    public String getMsg_body_textsize() {
        return msg_body_textsize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDescription.toString());
        dest.writeInt(mId);
        dest.writeString(msg_type);
        dest.writeString(msg_title);
        dest.writeString(msg_body);
        dest.writeString(img);
        dest.writeString(btn_text);
        dest.writeString(mButtonURL);
        dest.writeString(visitor_data);
        dest.writeString(visit_data);
        dest.writeString(qs);
        dest.writeParcelable(mImage, flags);
        dest.writeString(msg_body_color);
        dest.writeString(msg_body_textsize);
        dest.writeString(msg_title_color);
        dest.writeString(background);
        dest.writeString(font_family);
        dest.writeString(button_text_color);
        dest.writeString(button_color);
        dest.writeString(close_button_color);

    }

    public static final Parcelable.Creator<InAppMessage> CREATOR = new Parcelable.Creator<InAppMessage>() {

        @Override
        public InAppMessage createFromParcel(Parcel source) {
            return new InAppMessage(source);
        }

        @Override
        public InAppMessage[] newArray(int size) {
            return new InAppMessage[size];
        }
    };

    public String getCloseButton() {
        return close_button_color;
    }
}
