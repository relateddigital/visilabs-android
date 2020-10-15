package com.visilabs.story;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.story.action.StoriesProgressView;
import com.visilabs.story.model.skinbased.Actiondata;
import com.visilabs.story.model.skinbased.Items;
import com.visilabs.story.model.skinbased.Stories;

import java.util.Objects;


public class PreviewActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView storiesProgressView;
    private ImageView ivStory;

    private int counter = 0;

    long pressTime = 0L;
    long limit = 500L;

    Stories stories;
    Actiondata actiondata;

    Button btnStory;

    View reverse, skip;

    ImageView ivClose, ivCover;
    GestureDetector gestureDetector;
    int position;
    View.OnTouchListener onTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);

        stories = (Stories) getIntent().getSerializableExtra("story");

        actiondata = (Actiondata) getIntent().getSerializableExtra("action");

        position = getIntent().getExtras().getInt("position");
        setTouchEvents();

        setInitialView();
    }

    private void setTouchEvents() {
        gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener());

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        pressTime = System.currentTimeMillis();
                        storiesProgressView.pause();

                        return false;

                    case MotionEvent.ACTION_UP:
                        long now = System.currentTimeMillis();
                        storiesProgressView.resume();

                        return limit < now - pressTime;
                }
                return gestureDetector.onTouchEvent(event);
            }
        };
    }

    private void setInitialView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }

        ivCover = findViewById(R.id.civ_cover);
        ivClose = findViewById(R.id.ivClose);
        btnStory = findViewById(R.id.btn_story);
        ivStory = findViewById(R.id.iv_story);
        storiesProgressView = findViewById(R.id.stories);
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);

        storiesProgressView.setStoriesCount(stories.getItems().size());
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(counter);

        Picasso.get().load(stories.getThumbnail()).into(ivCover);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        setStoryItem(counter, stories);
        bindReverseView();
        bindSkipView();

    }

    private void bindSkipView() {

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        skip.setOnTouchListener(onTouchListener);
    }

    private void bindReverseView() {
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });

        reverse.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {
        setStoryItem(++counter, stories);

    }

    @Override
    public void onPrev() {

        if ((counter - 1) < 0) return;

        setStoryItem(--counter, stories);
    }

    @Override
    public void onComplete() {

        position = position + 1;


        if (position < actiondata.getStories().size()) {

            Intent intent = new Intent(getApplicationContext(), PreviewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("position", position);
            intent.putExtra("action", actiondata);
            intent.putExtra("story", actiondata.getStories().get(position));
            startActivity(intent);
        } else {
            onBackPressed();
        }

        /*
        position = position+1;


        if(position<actiondata.getStories().size()) {

            counter = 0;
            storiesProgressView.setStoriesCount(actiondata.getStories().get(position).getItems().size());
            storiesProgressView.setStoryDuration(3000L);
            storiesProgressView.startStories(counter);

            setStoryItem(counter, actiondata.getStories().get(position));
            stories = actiondata.getStories().get(position);

        } else {
          onBackPressed();
        }*/
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private void setStoryItem(int i, Stories stories) {
        Picasso.get().load(stories.getItems().get(i).getFileSrc()).into(ivStory);
        btnStory.setText(stories.getItems().get(i).getButtonText());
        btnStory.setTextColor(Color.parseColor(stories.getItems().get(i).getButtonTextColor()));
        btnStory.setBackgroundColor(Color.parseColor(stories.getItems().get(i).getButtonColor()));
    }

    @Override
    public void onBackPressed() {

        Intent intent = null;
        try {
            intent = new Intent(this,
                    Class.forName("com.relateddigital.visilabs.MainActivity"));
            startActivity(intent);
        } catch (ClassNotFoundException e) {

        }
    }
}