package com.visilabs.story.model.storylookingbanners;

import java.io.Serializable;

public class Stories implements Serializable {
    private String title;

    private String smallImg;

    private String link;

    private boolean shown;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public String getSmallImg() {
        return this.smallImg;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    public Boolean getShown() {
        return shown;
    }

    public void setShown(Boolean shown) {
        this.shown = shown;
    }
}
