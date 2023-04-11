package com.relateddigital.visilabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.relateddigital.visilabs.databinding.ActivityBannerCarouselDemoBinding;
import com.visilabs.inApp.bannercarousel.BannerItemClickListener;
import com.visilabs.inApp.bannercarousel.BannerRequestListener;

import java.util.HashMap;


public class BannerCarouselDemoActivity extends AppCompatActivity {

    private ActivityBannerCarouselDemoBinding binding;
    private BannerItemClickListener bannerItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBannerCarouselDemoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bannerItemClickListener = new BannerItemClickListener() {
            @Override
            public void bannerItemClicked(String bannerLink) {
                Toast.makeText(getApplicationContext(), bannerLink, Toast.LENGTH_SHORT).show();
                Log.i("link banner", bannerLink);
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerLink));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "The link is not formatted properly!");
                }
            }
        };

        binding.btnShowBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBanner();
            }
        });
    }

    private void showBanner() {
        BannerRequestListener bannerRequestListener = new BannerRequestListener() {
            @Override
            public void onRequestResult(boolean isAvailable) {
                if (!isAvailable) {
                    binding.bannerListView.setVisibility(View.GONE);
                }
            }
        };

        HashMap<String, String> properties = new HashMap<>();
        properties.put("OM.inapptype", "AppBanner");

        binding.bannerListView.requestBannerCarouselAction(
                getApplicationContext(),
                properties,
                bannerRequestListener,
                bannerItemClickListener);
    }

    private static final String LOG_TAG = "BannerDemoActivity";
}