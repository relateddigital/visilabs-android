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

    Button btnGoToLogin, btnGoToInApp;
    Button customEvent1, customEvent2, customEvent3, inApp1, inApp2, inApp3;

    VisilabsRecyclerView visilabsRecyclerView;

    StoryItemClickListener storyItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToLogin = findViewById(R.id.btn_go_to_login);
        btnGoToInApp = findViewById(R.id.btn_go_to_inapp);
        customEvent1 = findViewById(R.id.customEvent1);
        customEvent2 = findViewById(R.id.customEvent2);
        customEvent3 = findViewById(R.id.customEvent3);
        inApp1 = findViewById(R.id.inApp1);
        inApp2 = findViewById(R.id.inApp2);
        inApp3 = findViewById(R.id.inApp3);

        visilabsRecyclerView = findViewById(R.id.vrv_story);

        storyItemClickListener = new StoryItemClickListener() {
            @Override
            public void storyItemClicked(String storyLink) {
                Toast.makeText(getApplicationContext(), storyLink, Toast.LENGTH_SHORT).show();
                Log.i("link story", storyLink);
            }
        };

       visilabsRecyclerView.setStoryAction(getApplicationContext(), storyItemClickListener);
           //visilabsRecyclerView.setStoryActionId(getApplicationContext(), "305", null);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.sys.AppID", "visilabs-android-sdk");
        //parameters.put("OM.exVisitorID", "ogun.ozturk@euromsg.com");
        Visilabs.CallAPI().customEvent("android-visilab", parameters);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnGoToInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InAppActivity.class);
                startActivity(intent);
            }
        });

        customEvent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "image_text_button");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
                //Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this); // TODO: ikincisini kaldır sonra
            }
        });

        customEvent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                Visilabs.CallAPI().customEvent("test", parameters, MainActivity.this);
            }
        });

        customEvent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> properties = new HashMap<>();
                properties.put("utm_campaign","android-test-campaign");
                properties.put("utm_source","euromsg");
                properties.put("utm_medium","push");
                Visilabs.CallAPI().sendCampaignParameters(properties);
            }
        });

        inApp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "image_text_button");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
            }
        });

        inApp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "mini");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
            }
        });

        inApp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "full");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
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
