package com.relateddigital.visilabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.favs.FavsResponse;

import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.story.VisilabsRecyclerView;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.util.VisilabsConstant;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnGoToLogin;

    VisilabsRecyclerView visilabsRecyclerView;

    StoryItemClickListener storyItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToLogin = findViewById(R.id.btn_go_to_login);

        visilabsRecyclerView = findViewById(R.id.vrv_story);

        storyItemClickListener = new StoryItemClickListener() {
            @Override
            public void storyItemClicked(String storyLink) {

                Log.i("link story", storyLink);
            }
        };

        visilabsRecyclerView.setStoryAction(getApplicationContext(), storyItemClickListener);

        //   visilabsRecyclerView.setStoryActionId(getApplicationContext(), "250", null);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.sys.AppID", "visilabs-android-sdk");
        parameters.put("OM.exVisitorID", "ogun.ozturk@euromsg.com");
        Visilabs.CallAPI().customEvent("android-visilab", parameters);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

      try {
            VisilabsActionRequest visilabsActionRequest = Visilabs.CallAPI().requestAction(VisilabsConstant.FavoriteAttributeAction);
            visilabsActionRequest.executeAsyncAction(getVisilabsCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VisilabsCallback getVisilabsCallback() {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {

                    FavsResponse favsResponse = new Gson().fromJson(response.getRawResponse(), FavsResponse.class);

//                    String favBrands = favsResponse.getFavoriteAttributeAction()[0].getActiondata().getFavorites().getBrand()[0];
                    //   Log.i("Favs 1.Brand", favBrands);

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
