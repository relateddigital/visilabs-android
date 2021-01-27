package com.relateddigital.visilabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.relateddigital.visilabs.databinding.ActivityMainBinding;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;

import com.visilabs.api.VisilabsFavsRequestCallback;
import com.visilabs.favs.FavsResponse;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.util.VisilabsConstant;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.sys.AppID", "visilabs-android-test");
        //parameters.put("OM.exVisitorID", "ogun.ozturk@euromsg.com");
        Visilabs.CallAPI().customEvent("android-visilab", parameters);

        binding.btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoToInapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InAppActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoToStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StoryActivity.class);
                startActivity(intent);
            }
        });

        binding.customEvent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "image_text_button");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
            }
        });

        binding.customEvent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                //Add the parameters here
                Visilabs.CallAPI().customEvent("test", parameters, MainActivity.this);
            }
        });

        binding.customEvent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> properties = new HashMap<>();
                properties.put("utm_campaign","android-test-campaign");
                properties.put("utm_source","euromsg");
                properties.put("utm_medium","push");
                Visilabs.CallAPI().sendCampaignParameters(properties);
                Toast.makeText(getApplicationContext(), "The campaign parameters are sent.", Toast.LENGTH_LONG).show();
            }
        });

        binding.inApp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "image_text_button");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
            }
        });

        binding.inApp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "mini");
                Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
            }
        });

        binding.inApp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.inapptype", "alert");
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

    public VisilabsFavsRequestCallback getVisilabsCallback() {

        return new VisilabsFavsRequestCallback() {
            @Override
            public void success(FavsResponse message, String url) {
                Log.i(LOG_TAG, "Success Request : " + url);
                //Do your work here by using message
            }

            @Override
            public void fail(Throwable t, String url) {
                Log.e(LOG_TAG, "Fail Request : " + url);
                Log.e(LOG_TAG, "Fail Request Message : " + t.getMessage());
            }
        };
    }
}
