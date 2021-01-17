package com.relateddigital.visilabs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.visilabs.story.VisilabsRecyclerView;
import com.visilabs.story.model.StoryItemClickListener;

public class StoryActivity extends AppCompatActivity {

    EditText etStoryId;
    Button btnShowStory;
    VisilabsRecyclerView visilabsRecyclerView;
    StoryItemClickListener storyItemClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_demo);
        etStoryId = findViewById(R.id.et_story_id);
        btnShowStory = findViewById(R.id.btn_show_story);
        visilabsRecyclerView = findViewById(R.id.vrv_story);

        storyItemClickListener = new StoryItemClickListener() {
            @Override
            public void storyItemClicked(String storyLink) {
                Toast.makeText(getApplicationContext(), storyLink, Toast.LENGTH_SHORT).show();
                Log.i("link story", storyLink);
            }
        };

        btnShowStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStory();
            }
        });
    }

    private void showStory() {
        String storyId = etStoryId.getText().toString().trim();
        if(storyId.isEmpty()) {
            visilabsRecyclerView.setStoryAction(getApplicationContext(), storyItemClickListener);
        } else {
            visilabsRecyclerView.setStoryActionId(getApplicationContext(), storyId, storyItemClickListener);
        }
    }

}
