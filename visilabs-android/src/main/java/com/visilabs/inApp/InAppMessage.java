package com.visilabs.inApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.visilabs.exceptions.VisilabsNotificationException;
import com.visilabs.json.JSONObject;
import com.visilabs.json.JSONException;
import com.visilabs.util.ImageStore;
import com.visilabs.util.SizeUtil;

public class InAppMessage implements Parcelable {

    private Bitmap mImage;

    private final JSONObject mDescription;
    private final int mId;
    private final String mType;
    private final String mTitle;
    private final String mBody;
    private final String mImageUrl;
    private final String mButtonText;
    private final String mButtonURL;
    private final String mVisitorData;
    private final String mVisitData;
    private final String mQueryString;

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
        mType = in.readString();
        mTitle = in.readString();
        mBody = in.readString();
        mImageUrl = in.readString();
        mButtonText = in.readString();
        mButtonURL = in.readString();
        mVisitorData = in.readString();
        mVisitData = in.readString();
        mQueryString = in.readString();
        mImage = in.readParcelable(Bitmap.class.getClassLoader());

        mContext = null;
    }

    public InAppMessage(JSONObject description, Context context) throws VisilabsNotificationException {
        try {
            mDescription = description;
            mId = description.getInt("actid");

            JSONObject actionData = description.getJSONObject("actiondata");

            mType = actionData.getString("msg_type");
            mTitle = actionData.getString("msg_title");
            mBody = actionData.getString("msg_body");
            mImageUrl = actionData.getString("img");
            mButtonText = actionData.getString("btn_text");
            mButtonURL = actionData.getString("android_lnk");
            mVisitorData = actionData.getString("visitor_data");
            mVisitData = actionData.getString("visit_data");
            mQueryString = actionData.getString("qs");

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

    public InAppActionType getType() {
        if (mType == null) {
            return InAppActionType.UNKNOWN;
        }
        if (InAppActionType.MINI.toString().equals(mType.toLowerCase())) {
            return InAppActionType.MINI;
        }
        if (InAppActionType.FULL.toString().equals(mType.toLowerCase())) {
            return InAppActionType.FULL;
        }
        if (InAppActionType.IMAGE_TEXT_BUTTON.toString().equals(mType.toLowerCase())){
            return InAppActionType.IMAGE_TEXT_BUTTON;
        }
        return InAppActionType.UNKNOWN;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
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
            return SizeUtil.sizeSuffixUrl(mImageUrl, "@1x");
        }
        return mImageUrl;
    }

    //TODO:bunlara bak bir gerek var mı?
    public String getImage2xUrl() {
        return SizeUtil.sizeSuffixUrl(mImageUrl, "@2x");
    }

    public String getImage4xUrl() {
        return SizeUtil.sizeSuffixUrl(mImageUrl, "@4x");
    }

    void setImage(final Bitmap image) {
        mImage = image;
    }

    public Bitmap getImage() {
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
        return mButtonText;
    }

    public String getButtonURL() {
        return mButtonURL;
    }

    public String getVisitorData() {
        return mVisitorData;
    }

    public String getVisitData() {
        return mVisitData;
    }

    public String getQueryString() {
        return mQueryString;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDescription.toString());
        dest.writeInt(mId);
        dest.writeString(mType);
        dest.writeString(mTitle);
        dest.writeString(mBody);
        dest.writeString(mImageUrl);
        dest.writeString(mButtonText);
        dest.writeString(mButtonURL);
        dest.writeString(mVisitorData);
        dest.writeString(mVisitData);
        dest.writeString(mQueryString);
        dest.writeParcelable(mImage, flags);
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
}
