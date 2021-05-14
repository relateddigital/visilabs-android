package com.relateddigital.visilabs;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.relateddigital.visilabs.databinding.ActivityMainBinding;
import com.visilabs.Visilabs;
import com.visilabs.api.VisilabsFavsRequestCallback;
import com.visilabs.favs.FavsResponse;
import com.visilabs.inApp.CountdownTimerFragment;
import com.visilabs.inApp.SocialProofFragment;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.scratchToWin.ScratchToWinActivity;
import com.visilabs.shakeToWin.ShakeToWinActivity;
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
        Visilabs.CallAPI().customEvent("android-visilab", parameters);

        binding.btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put("OM.pv", "Product Code");
                parameters.put("OM.pn", "Product Name");
                parameters.put("OM.ppr", "10.0");
                parameters.put("OM.pv.1", "Brand");
                parameters.put("OM.inv", "3");
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
                properties.put("utm_campaign", "android-test-campaign");
                properties.put("utm_source", "euromsg");
                properties.put("utm_medium", "push");
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
                Intent intent = new Intent(MainActivity.this, ScratchToWinActivity.class);
                startActivity(intent);
                //TODO when backend side gets ready, use below
                //sendInAppRequest("scratchtowin");
            }
        });

        binding.inApp13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("spintowin");
            }
        });

        binding.inApp14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialProofFragment socialProofFragment = SocialProofFragment.newInstance(0, null);

                socialProofFragment.setRetainInstance(true);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(android.R.id.content, socialProofFragment);
                transaction.commit();
                //TODO when backend side gets ready, check below
                //sendInAppRequest("socialproof");
            }
        });

        binding.inApp15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountdownTimerFragment countdownTimerFragment = CountdownTimerFragment.newInstance(0, null);

                countdownTimerFragment.setRetainInstance(true);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(android.R.id.content, countdownTimerFragment);
                transaction.commit();
                //TODO when backend side gets ready, check below
                //sendInAppRequest("countdowntimer");
            }
        });

        binding.inApp16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: change this to "carousel" when BE gets ready
                sendInAppRequest("image_text_button");
            }
        });

        binding.inApp17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShakeToWinActivity.class);
                startActivity(intent);
                //TODO when backend side gets ready, use below
                //sendInAppRequest("shaketowin");
            }
        });

        binding.inApp18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Visilabs.CallAPI().sendTheListOfAppsInstalled();
                Toast.makeText(getApplicationContext(), "The list of apps installed is sent", Toast.LENGTH_LONG).show();
            }
        });

        binding.inApp19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("nps-image-test-button");
            }
        });

        binding.inApp20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("nps-image-test-button-image");
            }
        });

        binding.inApp21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInAppRequest("nps-feedback");
            }
        });

        try {
            VisilabsActionRequest visilabsActionRequest = Visilabs.CallAPI().requestAction(VisilabsConstant.FavoriteAttributeAction);
            visilabsActionRequest.executeAsyncAction(getVisilabsCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Optional
         * The method below is used to send the list of the
         * applications installed from a store in the device to the server.
         */
        // Visilabs.CallAPI().sendTheListOfAppsInstalled();
    }

    private void sendInAppRequest(String type) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("OM.inapptype", type);
        Visilabs.CallAPI().customEvent("in-app", parameters, MainActivity.this);
    }

    private VisilabsFavsRequestCallback getVisilabsCallback() {

        return new VisilabsFavsRequestCallback() {
            @Override
            public void success(FavsResponse message, String url) {
                Log.i(LOG_TAG, "Success Request : " + url);
                //Do your work here by using message, e.g.

                /*
                List<FavoriteAttributeAction> favsActions = message.getFavoriteAttributeAction();
                for (int i = 0; i < favsActions.size() ; i++) {
                    String actionType = favsActions.get(i).getActiontype();
                    Log.i(LOG_TAG, "action type " + i+1 + " : " + actionType);
                    Actiondata  actionData = favsActions.get(i).getActiondata();
                    for (int j = 0; j < favsActions.size() ; j++) {
                        String[] attributes = actionData.getAttributes();
                        Favorites favorites = actionData.getFavorites();
                        //goes on...
                    }
                }
                */
            }

            @Override
            public void fail(Throwable t, String url) {
                Log.e(LOG_TAG, "Fail Request : " + url);
                Log.e(LOG_TAG, "Fail Request Message : " + t.getMessage());
            }
        };
    }
}
