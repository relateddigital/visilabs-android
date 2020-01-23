package com.visilabs.android.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.visilabs.android.exceptions.VisilabsNotificationException;
import com.visilabs.android.json.JSONObject;
import com.visilabs.android.json.JSONException;
import com.visilabs.android.util.ImageStore;
import com.visilabs.android.util.RemoteService;

public class VisilabsNotification implements Parcelable  {

    public enum Type {
        UNKNOWN {
            @Override
            public String toString() {
                return "unknown";
            }
        },
        MINI {
            @Override
            public String toString() {
                return "mini";
            }
        },
        FULL {
            @Override
            public String toString() {
                return "full";
            }
        };
    }

    public VisilabsNotification(Parcel in) {
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

    public VisilabsNotification(JSONObject description, Context context) throws VisilabsNotificationException {
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

    private Bitmap getNotificationImage(Context context)
            throws RemoteService.ServiceUnavailableException {
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

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private static int getDisplayWidth(final Display display) {
        if (Build.VERSION.SDK_INT < 13) {
            return display.getWidth();
        } else {
            final Point displaySize = new Point();
            display.getSize(displaySize);
            return displaySize.x;
        }
    }


    String toJSON() {
        return mDescription.toString();
    }




    public int getId() {
        return mId;
    }

    public Type getType() {
        if(mType == null){
            return Type.UNKNOWN;
        }
        if (Type.MINI.toString().equals(mType.toLowerCase())) {
            return Type.MINI;
        }
        if (Type.FULL.toString().equals(mType.toLowerCase())) {
            return Type.FULL;
        }
        return Type.UNKNOWN;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public String getImageUrl() {
        if(getType() == Type.MINI){
            return sizeSuffixUrl(mImageUrl, "@1x");
        }

        return mImageUrl;
    }


    //TODO:bunlara bak bir gerek var mı?
    public String getImage2xUrl() {
        return sizeSuffixUrl(mImageUrl, "@2x");
    }

    public String getImage4xUrl() {
        return sizeSuffixUrl(mImageUrl, "@4x");
    }

    void setImage(final Bitmap image) {
        mImage = image;
    }

    public Bitmap getImage() {
        if(mImage != null){
            return mImage;
        }else{
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
            }catch (Exception ex){
                Log.e(LOG_TAG, "Can not create image from URL.", ex );
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

    public static final Parcelable.Creator<VisilabsNotification> CREATOR = new Parcelable.Creator<VisilabsNotification>() {

        @Override
        public VisilabsNotification createFromParcel(Parcel source) {
            return new VisilabsNotification(source);
        }

        @Override
        public VisilabsNotification[] newArray(int size) {
            return new VisilabsNotification[size];
        }
    };

    static String sizeSuffixUrl(String url, String sizeSuffix) {
        final Matcher matcher = FILE_EXTENSION_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.replaceFirst(sizeSuffix + "$1");
        } else {
            return url;
        }
    }

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
}
