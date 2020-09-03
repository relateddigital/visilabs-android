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

    public void setStoryAction(Context context, int storyActionId, StoryItemClickListener storyItemClickListener) {
        this.storyItemClickListener = storyItemClickListener;
     /*   VisilabsActionRequest visilabsActionRequest = null;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestAction(null);
            visilabsActionRequest.executeAsyncAction(getVisilabsStoryCallback(context));

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        String testResponse = "{\n" +
                "    \"capping\":\"{\\\"data\\\":{}}\",\n" +
                "    \"VERSION\":2,\n" +
                "    \"FavoriteAttributeAction\":[],\n" +
                "    \"Story\":[\n" +
                "        {\n" +
                "            \"actid\": \"123\",\n" +
                "            \"title\":\"storytitle\",\n" +
                "            \"actiontype\":\"Story\",\n" +
                "            \"actiondata\":{\n" +
                "\"stories\":[\n" +
                "{\n" +
                "\"title\":\"madonna \",\n" +
                "\"smallImg\":\"https://upload.wikimedia.org/wikipedia/commons/a/a5/Red_Kitten_01.jpg\",\n" +
                "\"link\":\"a.html?OM.zn=acttype-32&OM.zpc=act-167\",\n" +
                "\"linkOriginal\":\"a.html\"\n" +
                "},\n" +
                "{\n" +
                "\"title\":\"rihanna\",\n" +
                "\"smallImg\":\"https://i.pinimg.com/originals/3c/2f/c8/3c2fc8c57233fe5da99ad6c90ab6aea0.jpg\",\n" +
                "\"link\":\"b.html?OM.zn=acttype-32&OM.zpc=act-167\",\n" +
                "\"linkOriginal\":\"b.html\"\n" +
                "},\n" +
                "],\n" +
                "\"taTemplate\":\"story_looking_banners\",\n" +
                "\"ExtendedProps\":\"%7B%22storylb_img_borderWidth%22%3A%223%22%2C%22storylb_img_borderColor%22%3A%22%23ebc70b%22%2C%22storylb_img_borderRadius%22%3A%2250%25%22%2C%22storylb_img_boxShadow%22%3A%22rgba(0%2C0%2C0%2C0.4)%205px%205px%2010px%22%2C%22storylb_label_color%22%3A%22%23ff34ae%22%7D\"\n" +
                "} \n" +
                "}]}";


        VisilabsStoryResponse visilabsStoryResponse = new Gson().fromJson(testResponse, VisilabsStoryResponse.class);
        setStoryItemAdapter(context, visilabsStoryResponse);
    }

    public VisilabsCallback getVisilabsStoryCallback(final Context context) {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {
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
        VisilabsStoryAdapter visilabsStoryAdapter = new VisilabsStoryAdapter(context, storyItemClickListener);
        setHasFixedSize(true);
        visilabsStoryAdapter.setStoryItem(visilabsStoryResponse);
        setAdapter(visilabsStoryAdapter);
    }
}
