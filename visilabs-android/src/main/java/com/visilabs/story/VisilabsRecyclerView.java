package com.visilabs.story;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.VisilabsStoryResponse;

public class VisilabsRecyclerView extends RecyclerView {

    Context context;
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
                    VisilabsStoryResponse visilabsStoryResponse = new Gson().fromJson(response.getRawResponse(), VisilabsStoryResponse.class);
                    setStoryItemAdapter(context, visilabsStoryResponse);

                } catch (Exception ex) {
                    Log.e("Error", ex.getMessage(), ex);
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.d("Error", response.getRawResponse());
            }
        };
    }

    private void setStoryItemAdapter(Context context, VisilabsStoryResponse visilabsStoryResponse) {
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        VisilabsStoryLookingBannerAdapter visilabsStoryLookingBannerAdapter = new VisilabsStoryLookingBannerAdapter(context, storyItemClickListener);
        setHasFixedSize(true);
        if (visilabsStoryResponse.getStory().size() > 0) {
            visilabsStoryLookingBannerAdapter.setStoryItem(visilabsStoryResponse);
            setAdapter(visilabsStoryLookingBannerAdapter);
        }
    }
}
