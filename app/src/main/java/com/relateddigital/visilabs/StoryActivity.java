package com.relateddigital.visilabs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.relateddigital.visilabs.databinding.ActivityStoryDemoBinding;
import com.visilabs.story.model.StoryItemClickListener;

public class StoryActivity extends AppCompatActivity {

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
            }
        };

        binding.btnShowStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStory();
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

}
