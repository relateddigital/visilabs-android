package com.visilabs.story.model;

import com.visilabs.story.model.ExtendedProps;
import com.visilabs.story.model.Story;

public class VisilabsStoryResponse {
    private com.visilabs.story.model.ExtendedProps ExtendedProps;

    private Story[] story;

    public ExtendedProps getExtendedProps() {
        return ExtendedProps;
    }

    public void setExtendedProps(ExtendedProps ExtendedProps) {
        this.ExtendedProps = ExtendedProps;
    }

    public Story[] getStory() {
        return story;
    }

    public void setStory(Story[] story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return "VisilabsStoryResponse [ExtendedProps = " + ExtendedProps + ", story = " + story + "]";
    }
}
