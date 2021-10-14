package com.visilabs.story;

import android.app.Activity;
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

    public static final String TAG = "VisilabsRecyclerView";

    Context mContext;
    StoryItemClickListener mStoryItemClickListener;

    public VisilabsRecyclerView(Context context) {
        super(context);
        mContext = context;
    }

    public VisilabsRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VisilabsRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setStoryAction(Context context, StoryItemClickListener storyItemClickListener) {
        if(Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mStoryItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestAction("Story");
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context, null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoryActionWithRequestCallback(Context context, StoryItemClickListener storyItemClickListener,
                                                  StoryRequestListener storyRequestListener) {
        if(Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mStoryItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestAction("Story");
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context, storyRequestListener));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoryActionSync(Context context, Activity activity, StoryItemClickListener storyItemClickListener) {
        if(Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mStoryItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestAction("Story");
            visilabsActionRequest.executeSyncAction(getVisilabsStoryCallbackSync(context, activity));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoryActionId(Context context, String actionId, StoryItemClickListener storyItemClickListener) {
        if(Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mStoryItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestActionId(actionId);
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context, null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoryActionIdWithRequestCallback(Context context, String actionId,
                                                    StoryItemClickListener storyItemClickListener,
                                                    StoryRequestListener storyRequestListener) {
        if(Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mStoryItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestActionId(actionId);
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context, storyRequestListener));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoryActionIdSync(Context context, Activity activity, String actionId, StoryItemClickListener storyItemClickListener) {
        if(Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mStoryItemClickListener = storyItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestActionId(actionId);
            visilabsActionRequest.executeSyncAction(getVisilabsStoryCallbackSync(context, activity));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VisilabsCallback getVisilabsStoryCallback(final Context context,
                                                     final StoryRequestListener storyRequestListener) {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                if(storyRequestListener!=null) {
                    storyRequestListener.onRequestResult(true);
                }
                try {
                    VisilabsStoryLookingBannerResponse visilabsStoryLookingBannerResponse =
                            new Gson().fromJson(response.getRawResponse(), VisilabsStoryLookingBannerResponse.class);

                    if(visilabsStoryLookingBannerResponse.getStory().isEmpty()){
                        Log.i(TAG, "There is no story to show.");
                        return;
                    }

                    if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata()
                            .getTaTemplate().equals(VisilabsConstant.STORY_LOOKING_BANNERS)) {

                        VisilabsStoryLookingBannerAdapter visilabsStoryLookingBannerAdapter =
                                new VisilabsStoryLookingBannerAdapter(context, mStoryItemClickListener);

                        visilabsStoryLookingBannerAdapter.setStoryList(visilabsStoryLookingBannerResponse,
                                visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getExtendedProps());

                        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        setHasFixedSize(true);

                        setAdapter(visilabsStoryLookingBannerAdapter);

                    } else if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata()
                            .getTaTemplate().equals(VisilabsConstant.STORY_SKIN_BASED)) {
                        {
                            VisilabsSkinBasedResponse skinBased = new Gson().fromJson(response
                                    .getRawResponse(), VisilabsSkinBasedResponse.class);

                            VisilabsSkinBasedAdapter visilabsSkinBasedAdapter = new VisilabsSkinBasedAdapter(context);

                            visilabsSkinBasedAdapter.setStoryListener(mStoryItemClickListener);

                            visilabsSkinBasedAdapter.setStoryList(skinBased, skinBased.getStory()
                                    .get(0).getActiondata().getExtendedProps());

                            setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            setHasFixedSize(true);

                            setAdapter(visilabsSkinBasedAdapter);
                        }
                    }

                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    if(storyRequestListener!=null) {
                        storyRequestListener.onRequestResult(false);
                    }
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(TAG, response.getRawResponse());
                if(storyRequestListener!=null) {
                    storyRequestListener.onRequestResult(false);
                }
            }
        };
    }

    public VisilabsCallback getVisilabsStoryCallbackSync(final Context context, final Activity activity) {
        return new VisilabsCallback() {
            @Override
            public void success(final VisilabsResponse response) {
                try {
                    final VisilabsStoryLookingBannerResponse visilabsStoryLookingBannerResponse =
                            new Gson().fromJson(response.getRawResponse(), VisilabsStoryLookingBannerResponse.class);

                    if(visilabsStoryLookingBannerResponse.getStory().isEmpty()){
                        Log.i(TAG, "There is no story to show.");
                        return;
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata()
                                    .getTaTemplate().equals(VisilabsConstant.STORY_LOOKING_BANNERS)) {

                                VisilabsStoryLookingBannerAdapter visilabsStoryLookingBannerAdapter =
                                        new VisilabsStoryLookingBannerAdapter(context, mStoryItemClickListener);

                                visilabsStoryLookingBannerAdapter.setStoryList(visilabsStoryLookingBannerResponse,
                                        visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getExtendedProps());

                                setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                setHasFixedSize(true);

                                setAdapter(visilabsStoryLookingBannerAdapter);

                            } else if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata()
                                    .getTaTemplate().equals(VisilabsConstant.STORY_SKIN_BASED)) {
                                VisilabsSkinBasedResponse skinBased = new Gson().fromJson(response
                                        .getRawResponse(), VisilabsSkinBasedResponse.class);

                                VisilabsSkinBasedAdapter visilabsSkinBasedAdapter = new VisilabsSkinBasedAdapter(context);

                                visilabsSkinBasedAdapter.setStoryListener(mStoryItemClickListener);

                                visilabsSkinBasedAdapter.setStoryList(skinBased, skinBased.getStory()
                                        .get(0).getActiondata().getExtendedProps());

                                setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                setHasFixedSize(true);

                                setAdapter(visilabsSkinBasedAdapter);
                            }
                        }
                    });

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
