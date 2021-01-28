package com.visilabs.mailSub;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class VisilabsMailSubscriptionFormResponse implements Serializable {
    @SerializedName("FavoriteAttributeAction")
    private List<Object> mFavoriteAttributeAction = null;
    @SerializedName("MailSubscriptionForm")
    private List<Object> mMailSubscriptionForm = null;
    @SerializedName("Story")
    private List<Object> mStory = null;
    @SerializedName("VERSION")
    private Integer mVersion;
    @SerializedName("capping")
    private String mCapping;

    public List<Object> getFavoriteAttributeAction() {
        return mFavoriteAttributeAction;
    }

    public void setFavoriteAttributeAction(List<Object> favoriteAttributeAction) {
        mFavoriteAttributeAction = favoriteAttributeAction;
    }

    public List<Object> getMailSubscriptionForm() {
        return mMailSubscriptionForm;
    }

    public void setMailSubscriptionForm(List<Object> mailSubscriptionForm) {
        mMailSubscriptionForm = mailSubscriptionForm;
    }

    public List<Object> getStory() {
        return mStory;
    }

    public void setStory(List<Object> story) {
        mStory = story;
    }

    public Integer getVersion() {
        return mVersion;
    }

    public void setVersion(Integer version) {
        mVersion = version;
    }

    public String getCapping() {
        return mCapping;
    }

    public void setCapping(String capping) {
        mCapping = capping;
    }
}
