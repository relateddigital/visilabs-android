package com.visilabs.model;

import com.google.gson.annotations.SerializedName;
import com.visilabs.favs.FavoriteAttributeAction;
import com.visilabs.inApp.ProductStatNotifierModel;
import com.visilabs.inApp.customactions.model.CustomActions;
import com.visilabs.inappnotification.DrawerModel;
import com.visilabs.mailSub.MailSubscriptionForm;
import com.visilabs.scratchToWin.model.ScratchToWinModel;
import com.visilabs.spinToWin.model.SpinToWinModel;
import com.visilabs.story.model.storylookingbanners.Story;

import java.io.Serializable;
import java.util.List;

public class VisilabsActionsResponse implements Serializable {
    @SerializedName("FavoriteAttributeAction")
    private List<FavoriteAttributeAction> mFavoriteAttributeAction = null;
    @SerializedName("MailSubscriptionForm")
    private List<MailSubscriptionForm> mMailSubscriptionForm = null;
    @SerializedName("SpinToWin")
    private List<SpinToWinModel> mSpinToWinList = null;
    @SerializedName("ScratchToWin")
    private List<ScratchToWinModel> mScratchToWinList = null;

    @SerializedName("MobileCustomActions")
    private List<CustomActions> mCustomActionList = null;
    @SerializedName("ProductStatNotifier")
    private List<ProductStatNotifierModel> mProductStatNotifier = null;
    @SerializedName("Story")
    private List<Story> mStory = null;
    @SerializedName("Drawer")
    private List<DrawerModel> mDrawer = null;
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

    public List<SpinToWinModel> getSpinToWinList() {
        return mSpinToWinList;
    }

    public void setSpinToWinList(List<SpinToWinModel> spinToWinList) {
        mSpinToWinList = spinToWinList;
    }

    public List<ScratchToWinModel> getScratchToWinList() {
        return mScratchToWinList;
    }

    public List<CustomActions> getCustomActionList() {
        return mCustomActionList;
    }

    public void setCustomActionList(List<CustomActions> customActionList) {
        mCustomActionList = customActionList;
    }

    public void setScratchToWinList(List<ScratchToWinModel> scratchToWinList) {
        mScratchToWinList = scratchToWinList;
    }

    public List<ProductStatNotifierModel> getProductStatNotifierList() {
        return mProductStatNotifier;
    }

    public void setProductStatNotifierList(List<ProductStatNotifierModel> productStatNotifierList) {
        mProductStatNotifier = productStatNotifierList;
    }

    public List<Story> getStory() {
        return mStory;
    }

    public void setStory(List<Story> story) {
        mStory = story;
    }

    public List<DrawerModel> getDrawer() {
        return mDrawer;
    }

    public void setDrawer(List<DrawerModel> drawer) {
        mDrawer = drawer;
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
