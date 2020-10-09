package com.visilabs.story.model.storylookingbanner;

import java.io.Serializable;

public class Stories implements Serializable {
    private String title;

    private String smallImg;

    private String link;

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
}
