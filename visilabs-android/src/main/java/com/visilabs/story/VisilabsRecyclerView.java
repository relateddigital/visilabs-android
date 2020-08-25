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
import com.visilabs.story.model.VisilabsStoryResponse;

public class VisilabsRecyclerView extends RecyclerView {

    Context context;

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

    public void setStoryAction(Context context, int storyActionId) {
        VisilabsActionRequest visilabsActionRequest = null;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestAction(null);
            visilabsActionRequest.executeAsyncAction(getVisilabsCallback(context));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public VisilabsCallback getVisilabsCallback(final Context context) {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {

                  String testResponse = "{story : [{ \"title\":\"ccccc\", \"smallImg\":\"c.jpg\", \"link\":\"c.html?OM.zn=acttype-32&OM.zpc=act-167\", \"linkOriginal\":\"c.html\" }] , ExtendedProps : {\"storylb_img_borderWidth\":\"3\",\"storylb_img_borderColor\":\"#ebc70b\",\"storylb_img_borderRadius\":\"1\",\"storylb_img_boxShadow\":\"rgba(0,0,0,0.4) 5px 5px 10px\",\"storylb_label_color\":\"#ff34ae\"}}";
                    VisilabsStoryResponse visilabsStoryResponse = new Gson().fromJson(testResponse, VisilabsStoryResponse.class);

                    setStoryUI(context, visilabsStoryResponse);

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

    private void setStoryUI(Context context, VisilabsStoryResponse visilabsStoryResponse) {

        setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        VisilabsStoryAdapter visilabsStoryAdapter = new VisilabsStoryAdapter(context);
        setHasFixedSize(true);
        visilabsStoryAdapter.setStoryItem(visilabsStoryResponse);
        setAdapter(visilabsStoryAdapter);
    }
}
