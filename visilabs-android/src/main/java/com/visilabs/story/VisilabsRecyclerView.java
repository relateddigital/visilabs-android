package com.visilabs.story;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.story.model.skinbased.VisilabsSkinBasedResponse;
import com.visilabs.util.VisilabsConstant;

import com.visilabs.api.VisilabsCallback;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.storylookingbanners.VisilabsStoryLookingBannerResponse;

public class VisilabsRecyclerView extends RecyclerView {

    Context context;

    public static final String TAG = "VisilabsRecyclerView";

    StoryItemClickListener storyItemClickListener;

    public VisilabsRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public VisilabsRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VisilabsRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setStoryAction(Context context, StoryItemClickListener storyItemClickListener) {
        this.storyItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestAction("Story");
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoryActionId(Context context, String actionId, StoryItemClickListener storyItemClickListener) {
        this.storyItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestActionId(actionId);
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VisilabsCallback getVisilabsStoryCallback(final Context context) {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {
                    VisilabsStoryLookingBannerResponse visilabsStoryLookingBannerResponse = new Gson().fromJson(response.getRawResponse(), VisilabsStoryLookingBannerResponse.class);

                    if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getTaTemplate().equals(VisilabsConstant.STORY_LOOKING_BANNERS)) {

                        VisilabsStoryLookingBannerAdapter visilabsStoryLookingBannerAdapter = new VisilabsStoryLookingBannerAdapter(context, storyItemClickListener);

                        visilabsStoryLookingBannerAdapter.setStoryList(visilabsStoryLookingBannerResponse, visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getExtendedProps());

                        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        setHasFixedSize(true);

                        setAdapter(visilabsStoryLookingBannerAdapter);

                    } else if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getTaTemplate().equals(VisilabsConstant.STORY_SKIN_BASED)) {
                        {
                            VisilabsSkinBasedResponse skinBased = new Gson().fromJson(response.getRawResponse(), VisilabsSkinBasedResponse.class);

                            VisilabsSkinBasedAdapter visilabsSkinBasedAdapter = new VisilabsSkinBasedAdapter(context);

                            visilabsSkinBasedAdapter.setStoryListener(storyItemClickListener);

                            visilabsSkinBasedAdapter.setStoryList(skinBased, skinBased.getStory().get(0).getActiondata().getExtendedProps());

                            setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            setHasFixedSize(true);

                            setAdapter(visilabsSkinBasedAdapter);
                        }
                    }

                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(TAG, response.getRawResponse());
            }
        };
    }
}
