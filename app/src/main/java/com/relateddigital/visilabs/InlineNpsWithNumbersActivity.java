package com.relateddigital.visilabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.visilabs.android.databinding.ActivityDenemeBinding;
import com.visilabs.inApp.inlineNpsWithNumber.NpsItemClickListener;

import java.util.HashMap;

public class InlineNpsWithNumbersActivity extends AppCompatActivity {
    private static final String LOG_TAG = "NpsActivity";

    private ActivityDenemeBinding binding;
    private NpsItemClickListener npsItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDenemeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        npsItemClickListener = new NpsItemClickListener() {
            @Override
            public void npsItemClicked(String npsLink) {
                Toast.makeText(getApplicationContext(), npsLink, Toast.LENGTH_SHORT).show();
                Log.i("link nps", npsLink);
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(npsLink));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "The link is not formatted properly!");
                }
            }

        };

        HashMap<String, String> properties = new HashMap<>();
        properties.put("OM.inapptype", "nps_with_numbers");

        binding.inlineNps.setNpsWithNumberAction(getApplicationContext(), properties, npsItemClickListener, InlineNpsWithNumbersActivity.this);


    }

}