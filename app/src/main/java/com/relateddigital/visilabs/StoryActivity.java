package com.relateddigital.visilabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.relateddigital.visilabs.databinding.ActivityStoryDemoBinding;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.util.PersistentTargetManager;

public class StoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = "StoryActivity";

    private ActivityStoryDemoBinding binding;
    private StoryItemClickListener storyItemClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryDemoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        storyItemClickListener = new StoryItemClickListener() {
            @Override
            public void storyItemClicked(String storyLink) {
                Toast.makeText(getApplicationContext(), storyLink, Toast.LENGTH_SHORT).show();
                Log.i("link story", storyLink);
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storyLink));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "The link is not formatted properly!");
                }
            }
        };

        binding.btnShowStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(StoryActivity.this);
                binding.etStoryId.clearFocus();
                showStory();
            }
        });

        binding.btnClearStoryCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(StoryActivity.this);
                binding.etStoryId.clearFocus();
                clearStoryCache();
            }
        });

        binding.sw.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                hideKeyboard(StoryActivity.this);
                binding.etStoryId.clearFocus();
                return false;
            }
        });

    }

    private void showStory() {
        String storyId = binding.etStoryId.getText().toString().trim();
        if(storyId.isEmpty()) {
            binding.vrvStory.setStoryAction(getApplicationContext(), storyItemClickListener);
        } else {
            binding.vrvStory.setStoryActionId(getApplicationContext(), storyId, storyItemClickListener);
        }
    }

    private void clearStoryCache() {
        PersistentTargetManager.with(getApplicationContext()).clearStoryCache();
    }

    private static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
