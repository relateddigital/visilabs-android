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
                HashMap<String, String> parameters= new HashMap<String, String>();
                parameters.put("OM.pv","Product Code");
                parameters.put("OM.pn","Product Name");
                parameters.put("OM.ppr","10.0");
                parameters.put("OM.pv.1","Brand");
                parameters.put("OM.inv","3");
                Visilabs.CallAPI().customEvent("Product View", parameters);
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
                sendInAppRequest("full");
            }
        });

        binding.inApp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("mini");
            }
        });

        binding.inApp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("full_image");
            }
        });

        binding.inApp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("image_button");
            }
        });

        binding.inApp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("image_text_button");
            }
        });

        binding.inApp6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("smile_rating");
            }
        });

        binding.inApp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("nps_with_numbers");
            }
        });

        binding.inApp8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("nps");
            }
        });

        binding.inApp9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("alert_native");
            }
        });

        binding.inApp10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("alert_actionsheet");
            }
        });

        binding.inApp11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("mailsubsform");
            }
        });

        binding.inApp12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("mailsubsform");
            }
        });

      try {
            VisilabsActionRequest visilabsActionRequest = Visilabs.CallAPI().requestAction(VisilabsConstant.FavoriteAttributeAction);
            visilabsActionRequest.executeAsyncAction(getVisilabsCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendInAppRequest(String type) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.inapptype", type);
        Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
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
