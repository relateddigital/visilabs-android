package com.visilabs.story.model;

import android.os.Parcelable;

import java.io.Serializable;

public interface StoryItemClickListener extends Serializable {

    void storyItemClicked(String storyLink);
}
