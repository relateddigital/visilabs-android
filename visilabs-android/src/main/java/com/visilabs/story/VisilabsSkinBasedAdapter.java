package com.visilabs.story;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.skinbased.SkinBased;

public class VisilabsSkinBasedAdapter extends RecyclerView.Adapter<VisilabsStoryLookingBannerAdapter.StoryHolder>  {

    Context context;
    public VisilabsSkinBasedAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VisilabsStoryLookingBannerAdapter.StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VisilabsStoryLookingBannerAdapter.StoryHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setStory(SkinBased skinBased, String extendedProps) {

    }
}
