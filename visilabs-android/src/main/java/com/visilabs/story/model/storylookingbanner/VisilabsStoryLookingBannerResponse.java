package com.visilabs.story.model.storylookingbanner;

import com.visilabs.story.model.storylookingbanner.Story;

import java.io.Serializable;
import java.util.List;

public class VisilabsStoryLookingBannerResponse implements Serializable {
    private String capping;

    private int VERSION;

    private List<String> FavoriteAttributeAction;

    private List<Story> Story;

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
}

