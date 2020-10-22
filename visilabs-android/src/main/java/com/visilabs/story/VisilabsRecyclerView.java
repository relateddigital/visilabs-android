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
import com.visilabs.story.model.storylookingbanner.Actiondata;
import com.visilabs.util.VisilabsConstant;

import com.visilabs.api.VisilabsCallback;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.storylookingbanner.VisilabsStoryLookingBannerResponse;

public class VisilabsRecyclerView extends RecyclerView {

    Context context;
    StoryItemClickListener storyItemClickListener;

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
            "            \"thumbnail\": \"https://cdn.jpegmini.com/user/images/slider_puffin_jpegmini_mobile.jpg\",\n" +
            "            \"items\": [\n" +
            "              {\n" +
            "                \"fileSrc\": \"https://nenedir.com.tr/wp-content/uploads/2018/11/reklam.jpg\",\n" +
            "                \"filePreview\": \"\",\n" +
            "                \"buttonText\": \"Visit my Portfolio\",\n" +
            "                \"buttonTextColor\": \"#7d1212\",\n" +
            "                \"buttonColor\": \"#998686\",\n" +
            "                \"fileType\": \"photo\",\n" +
            "                \"displayTime\": 3,\n" +
            "                \"targetUrl\": \"http://1.com/\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"fileSrc\": \"https://digitalage.com.tr/wp-content/uploads/2017/06/Sosyal-medya-reklam-modelleri.jpg\",\n" +
            "                \"filePreview\": \"\",\n" +
            "                \"buttonText\": \"test test Portfolio\",\n" +
            "                \"buttonTextColor\": \"#7d1212\",\n" +
            "                \"buttonColor\": \"#998686\",\n" +
            "                \"fileType\": \"video\",\n" +
            "                \"displayTime\": 3,\n" +
            "                \"targetUrl\": \"http://2.com/\"\n" +
            "              }\n" +
            "            ]\n" +
            "          },\n" +
            "          {\n" +
            "            \"title\": \"Rivers Cuomo\",\n" +
            "            \"thumbnail\": \"https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg\",\n" +
            "            \"items\": [\n" +
            "              {\n" +
            "                \"fileSrc\": \"https://i.pinimg.com/originals/64/20/4e/64204e53a21f9e4cf8b1bf2a929b528b.jpg\",\n" +
            "                \"filePreview\": \"\",\n" +
            "                \"buttonText\": \"Visit my Portfolio\",\n" +
            "                \"buttonTextColor\": \"#7d1212\",\n" +
            "                \"buttonColor\": \"#998686\",\n" +
            "                \"fileType\": \"photo\",\n" +
            "                \"displayTime\": 3,\n" +
            "                \"targetUrl\": \"http://3.com/\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"fileSrc\": \"https://i.pinimg.com/564x/2d/1e/52/2d1e5289f3f5c52c80159fe8c99d07a6.jpg\",\n" +
            "                \"filePreview\": \"\",\n" +
            "                \"buttonText\": \"\",\n" +
            "                \"buttonTextColor\": \"#7d1212\",\n" +
            "                \"buttonColor\": \"#998686\",\n" +
            "                \"fileType\": \"photo\",\n" +
            "                \"displayTime\": 3,\n" +
            "                \"targetUrl\": \"www.4.com\"\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ],\n" +
            "        \"taTemplate\": \"skin_based\",\n" +
            "      \"report\":{\"impression\":\"OM.zdn=acttype-32&OM.zcp=act-305\",\"click\":\"OM.zpc=acttype-32&OM.zn=act-305\"},\"after\":false,\n" +
            "        \"ExtendedProps\": \"%7B%22storyz_img_borderRadius%22%3A%2250%25%22%7D\"\n" +
            "      }\n" +
            "    }]\n" +
            "}\n" +
            " ";



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

                    if (visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getTaTemplate().equals("story_looking_bannersz")) {

                        VisilabsStoryLookingBannerAdapter visilabsStoryLookingBannerAdapter = new VisilabsStoryLookingBannerAdapter(context, storyItemClickListener);


                        visilabsStoryLookingBannerAdapter.setStoryList(visilabsStoryLookingBannerResponse, visilabsStoryLookingBannerResponse.getStory().get(0).getActiondata().getExtendedProps());

                        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        setHasFixedSize(true);

                        setAdapter(visilabsStoryLookingBannerAdapter);

                    } else {
                        VisilabsSkinBasedResponse skinBased = new Gson().fromJson(skin_bassed, VisilabsSkinBasedResponse.class);

                        VisilabsSkinBasedAdapter visilabsSkinBasedAdapter = new VisilabsSkinBasedAdapter(context);

                        visilabsSkinBasedAdapter.setStoryListener(storyItemClickListener);

                        visilabsSkinBasedAdapter.setStoryList(skinBased, skinBased.getStory().get(0).getActiondata().getExtendedProps());

                        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        setHasFixedSize(true);

                        setAdapter(visilabsSkinBasedAdapter);
                    }

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
}
