package com.visilabs.story;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.story.model.Actiondata;
import com.visilabs.story.model.VisilabsStory;
import com.visilabs.story.model.VisilabsStory;
import com.visilabs.story.model.skinbased.SkinBased;
import com.visilabs.story.model.storylookingbanner.StoryLookingBanner;
import com.visilabs.util.VisilabsConstant;

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

        String skin_bassed = "{\n" +
                "  \"capping\": \"{\\\"data\\\":{}}\",\n" +
                "  \"VERSION\": 2,\n" +
                "  \"FavoriteAttributeAction\": [\n" +
                "  ],\n" +
                "  \"Story\": [\n" +
                "    {\n" +
                "      \"actid\": \"int action id\",\n" +
                "      \"title\": \"string action name\",\n" +
                "      \"actiontype\": \"Story\",\n" +
                "      \"actiondata\": {\n" +
                "        \"stories\": [\n" +
                "          {\n" +
                "            \"title\": \"Ramon\",\n" +
                "            \"thumbnail\": \"http://testapp.relateddigital.com/cdnpath/img/Story/users1.jpg\",\n" +
                "            \"items\": [\n" +
                "              {\n" +
                "                \"fileSrc\": \"http://testapp.relateddigital.com/cdnpath/img/Story/stories1.jpg\",\n" +
                "                \"filePreview\": \"\",\n" +
                "                \"buttonText\": \"Visit my Portfolio\",\n" +
                "                \"buttonTextColor\": \"#7d1212\",\n" +
                "                \"buttonColor\": \"#998686\",\n" +
                "                \"fileType\": \"photo\",\n" +
                "                \"displayTime\": 3,\n" +
                "                \"targetUrl\": \"http://visilabs.com/?title=Ramon&OM.zn=acttype-32&OM.zpc=act-160\",\n" +
                "                \"targetUrlOriginal\": \"http://visilabs.com/?title=Ramon\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"fileSrc\": \"http://testapp.relateddigital.com/cdnpath/img/Story/stories2.mp4\",\n" +
                "                \"filePreview\": \"\",\n" +
                "                \"buttonText\": \"\",\n" +
                "                \"fileType\": \"video\",\n" +
                "                \"displayTime\": 0,\n" +
                "                \"targetUrl\": \"\",\n" +
                "                \"targetUrlOriginal\": \"\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"title\": \"Rivers Cuomo\",\n" +
                "            \"thumbnail\": \"http://testapp.relateddigital.com/cdnpath/img/Story/users5.jpg\",\n" +
                "            \"items\": [\n" +
                "              {\n" +
                "              }]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"taTemplate\": \"skin_based\",\n" +
                "        \"ExtendedProps\": \"encoding string\"\n" +
                "      }\n" +
                "    }]\n" +
                "}\n" +
                " ";
    }

    public void setStoryActionId(Context context, String actionId,StoryItemClickListener storyItemClickListener) {
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
                    setStoryItemAdapter(context, visilabsStoryResponse.getStory().get(0).getActiondata());

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

    private void setStoryItemAdapter(Context context, Actiondata actiondata) {

        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        VisilabsStoryAdapter visilabsStoryAdapter = new VisilabsStoryAdapter(context, storyItemClickListener);
        setHasFixedSize(true);

       // if (visilabsStoryResponse.getStory().size() > 0) {


            if (actiondata.getTaTemplate().equals(VisilabsConstant.STORY_LOOKING_BANNER)){
            setStoryLookingBannerAdapter(actiondata.getStories(), actiondata.getExtendedProps());

        } else if (actiondata.getTaTemplate().equals(VisilabsConstant.STORY_SKIN_BASED)){
            setSkinBasedAdapter(actiondata.getStories(), actiondata.getExtendedProps());
        }
    }

    private void setSkinBasedAdapter(String storyJson, String extendedProps) {

        VisilabsSkinBasedAdapter visilabsSkinBasedAdapter = new VisilabsSkinBasedAdapter(context);
        SkinBased skinBased = new Gson().fromJson(storyJson, SkinBased.class);
        visilabsSkinBasedAdapter.setStory(skinBased, extendedProps);

        setAdapter(visilabsSkinBasedAdapter);
    }

    private void setStoryLookingBannerAdapter(String storyJson, String extendedProps) {
        VisilabsStoryLookingBannerAdapter visilabsStoryLookingBannerAdapter = new VisilabsStoryLookingBannerAdapter(context, storyItemClickListener);
        StoryLookingBanner storyLookingBanner = new Gson().fromJson(storyJson, StoryLookingBanner.class);
        visilabsStoryLookingBannerAdapter.setStoryList(storyLookingBanner, extendedProps);

        setAdapter(visilabsStoryLookingBannerAdapter);

    }
}
