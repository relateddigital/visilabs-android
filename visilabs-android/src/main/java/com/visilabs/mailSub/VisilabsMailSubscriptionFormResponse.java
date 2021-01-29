package com.visilabs.mailSub;

import com.google.gson.annotations.SerializedName;
import com.visilabs.favs.FavoriteAttributeAction;
import com.visilabs.story.model.storylookingbanners.Story;

import java.io.Serializable;
import java.util.List;

public class VisilabsMailSubscriptionFormResponse implements Serializable {
    @SerializedName("FavoriteAttributeAction")
    private List<FavoriteAttributeAction> mFavoriteAttributeAction = null;
    @SerializedName("MailSubscriptionForm")
    private List<MailSubscriptionForm> mMailSubscriptionForm = null;
    @SerializedName("Story")
    private List<Story> mStory = null;
    @SerializedName("VERSION")
    private Integer mVersion;
    @SerializedName("capping")
    private String mCapping;

    public List<FavoriteAttributeAction> getFavoriteAttributeAction() {
        return mFavoriteAttributeAction;
    }

    public void setFavoriteAttributeAction(List<FavoriteAttributeAction> favoriteAttributeAction) {
        mFavoriteAttributeAction = favoriteAttributeAction;
    }

    public List<MailSubscriptionForm> getMailSubscriptionForm() {
        return mMailSubscriptionForm;
    }

    public void setMailSubscriptionForm(List<MailSubscriptionForm> mailSubscriptionForm) {
        mMailSubscriptionForm = mailSubscriptionForm;
    }

    public List<Story> getStory() {
        return mStory;
    }

    public void setStory(List<Story> story) {
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
