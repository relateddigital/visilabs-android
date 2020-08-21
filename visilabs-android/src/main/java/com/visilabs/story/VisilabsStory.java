package com.visilabs.story;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.visilabs.android.R;

public class VisilabsStory extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.visilabs_story);

        VisilabsRecyclerView visilabsRecyclerView = findViewById(R.id.rv_stories);
        VisilabsStoryAdapter visilabsStoryAdapter = new VisilabsStoryAdapter(getApplicationContext());
        visilabsRecyclerView.setAdapter(visilabsStoryAdapter);
    }
}
