package com.visilabs.mailSub;

import com.visilabs.story.model.storylookingbanners.Story;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisilabsMailSubscriptionFormResponse implements Serializable {
    private String capping;

    private int VERSION;

    private List<String> FavoriteAttributeAction;

    private List<Story> Story;

    private List<MailSubscriptionForm> MailSubscriptionForm;

    public void setCapping(String capping) {
        this.capping = capping;
    }

    public String getCapping() {
        return this.capping;
    }

    public void setVERSION(int VERSION) {
        this.VERSION = VERSION;
    }

    public int getVERSION() {
        return this.VERSION;
    }

    public void setFavoriteAttributeAction(List<String> FavoriteAttributeAction) {
        this.FavoriteAttributeAction = FavoriteAttributeAction;
    }

    public List<String> getFavoriteAttributeAction() {
        return this.FavoriteAttributeAction;
    }

    public void setStory(List<Story> Story) {
        this.Story = Story;
    }

    public List<Story> getStory() {
        return this.Story;
    }

    public void setMailSubscriptionForm(List<MailSubscriptionForm> MailSubscriptionForm) {
        this.MailSubscriptionForm = MailSubscriptionForm;
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
    }

    public List<MailSubscriptionForm> getMailSubscriptionForm() {
        return this.MailSubscriptionForm;
    }


}
