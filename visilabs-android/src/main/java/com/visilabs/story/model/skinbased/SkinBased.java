package com.visilabs.story.model.skinbased;

import java.io.Serializable;
import java.util.List;

public class SkinBased implements Serializable {

    List<Stories> stories;

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }
}

