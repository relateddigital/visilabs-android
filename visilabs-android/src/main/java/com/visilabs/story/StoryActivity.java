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
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.story.action.StoriesProgressView;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.skinbased.Actiondata;
import com.visilabs.story.model.skinbased.Items;
import com.visilabs.story.model.skinbased.Stories;
import com.visilabs.util.VisilabsConstant;

import java.util.Objects;


public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView storiesProgressView;
    private ImageView ivStory;

    int storyItemPosition;

    long pressTime = 0L;
    long limit = 500L;

    Stories stories;

    Actiondata actiondata;

    Button btnStory;

    View reverse, skip;

    ImageView ivClose, ivCover;

    TextView tvCover;

    GestureDetector gestureDetector;
    int storyPosition;
    View.OnTouchListener onTouchListener;

    static StoryItemClickListener storyItemClickListener;

    public static void setStoryItemClickListener(StoryItemClickListener mStoryItemClickListener) {
        storyItemClickListener = mStoryItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story);

        hideActionBar();

        actiondata = (Actiondata) getIntent().getSerializableExtra(VisilabsConstant.ACTION_DATA);
        storyPosition = getIntent().getExtras().getInt(VisilabsConstant.STORY_POSITION);
        storyItemPosition = getIntent().getExtras().getInt(VisilabsConstant.STORY_ITEM_POSITION);
        stories = actiondata.getStories().get(storyPosition);

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

        ivStory = findViewById(R.id.iv_story);
        ivCover = findViewById(R.id.civ_cover);
        tvCover = findViewById(R.id.tv_cover);
        ivClose = findViewById(R.id.ivClose);
        btnStory = findViewById(R.id.btn_story);
        storiesProgressView = findViewById(R.id.stories);
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);

        storiesProgressView.setStoriesCount(stories.getItems().size());
        storiesProgressView.setStoryDuration(Integer.parseInt(stories.getItems().get(storyItemPosition).getDisplayTime()) * 1000);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(storyItemPosition);

        String impressionReport = actiondata.getReport().getImpression();
        Visilabs.CallAPI().impressionStory(impressionReport);

        Picasso.get().load(stories.getThumbnail()).into(ivCover);
        tvCover.setText(stories.getTitle());

        setStoryItem(stories.getItems().get(storyItemPosition));
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    private void hideActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onNext() {
        Visilabs.CallAPI().impressionStory(actiondata.getReport().getImpression());

        setStoryItem(stories.getItems().get(++storyItemPosition));
    }

    @Override
    public void onPrev() {
        Visilabs.CallAPI().impressionStory(actiondata.getReport().getImpression());

        if ((storyItemPosition - 1) < 0) {
            if (storyPosition - 1 < actiondata.getStories().size() && storyPosition - 1 > -1) {

                storyPosition--;

                int previousStoryGroupLatestPosition = actiondata.getStories().get(storyPosition).getItems().size() - 1;

                startStoryGroup(previousStoryGroupLatestPosition);
            } else {
                onBackPressed();
            }
        } else {
            setStoryItem(stories.getItems().get(--storyItemPosition));
        }
    }

    @Override
    public void onComplete() {

        storyPosition++;

        if (storyPosition < actiondata.getStories().size()) {
            int nextStoryGroupFirstPosition = 0;
            startStoryGroup(nextStoryGroupFirstPosition);
        } else {
            onBackPressed();
        }
    }

    private void startStoryGroup(int itemPosition) {
        Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
        intent.putExtra(VisilabsConstant.STORY_ITEM_POSITION, itemPosition);
        intent.putExtra(VisilabsConstant.STORY_POSITION, storyPosition);
        intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private void setStoryItem(final Items item) {

        if (!item.getFileSrc().equals("")) {
            Picasso.get().load(item.getFileSrc()).into(ivStory);
        }

        if (!item.getButtonText().equals("")) {
            btnStory.setVisibility(View.VISIBLE);
            btnStory.setText(item.getButtonText());
            btnStory.setTextColor(Color.parseColor(item.getButtonTextColor()));
            btnStory.setBackgroundColor(Color.parseColor(item.getButtonColor()));

        } else {
            btnStory.setVisibility(View.GONE);
        }

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Visilabs.CallAPI().trackStoryClick(item.getTargetUrl());
                if (storyItemClickListener != null) {
                    storyItemClickListener.storyItemClicked(item.getTargetUrl());
                }
            }
        });
    }
}