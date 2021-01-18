package com.relateddigital.visilabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.relateddigital.visilabs.databinding.ActivityMainBinding;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.favs.FavsResponse;

import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.util.VisilabsConstant;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.sys.AppID", "visilabs-android-sdk");
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
                //Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this); // TODO: ikincisini kaldır sonra
            }
        });

        binding.customEvent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
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

    public VisilabsCallback getVisilabsCallback() {

        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                try {

                    FavsResponse favsResponse = new Gson().fromJson(response.getRawResponse(), FavsResponse.class);

                    //String favBrands = favsResponse.getFavoriteAttributeAction()[0].getActiondata().getFavorites().getBrand()[0];
                    //Log.i("Favs 1.Brand", favBrands);

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
