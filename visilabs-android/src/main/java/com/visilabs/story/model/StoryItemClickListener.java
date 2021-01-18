package com.visilabs.story.model;

import java.io.Serializable;

public interface StoryItemClickListener extends Serializable {

    void storyItemClicked(String storyLink);
}
