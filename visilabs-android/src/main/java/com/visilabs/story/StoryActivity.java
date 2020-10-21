package com.visilabs.story;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
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
import com.visilabs.story.model.skinbased.Stories;
import com.visilabs.util.VisilabsConstant;

import java.util.Objects;


public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView storiesProgressView;
    private ImageView ivStory;

    int counter;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    long pressTime = 0L;
    long limit = 500L;

    Stories stories;
    Actiondata actiondata;

    Button btnStory;

    View reverse, skip;

    ImageView ivClose, ivCover;

    TextView tvCover;

    GestureDetector gestureDetector;
    int position;
    View.OnTouchListener onTouchListener;

    static StoryItemClickListener storyItemClickListener;


    public static void setStoryItemClickListener(StoryItemClickListener mStoryItemClickListener){
        storyItemClickListener = mStoryItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);

        stories = (Stories) getIntent().getSerializableExtra("story");

        actiondata = (Actiondata) getIntent().getSerializableExtra("action");

        position = getIntent().getExtras().getInt("position");

        counter = getIntent().getExtras().getInt("counter");

   //     storyItemClickListener = (StoryItemClickListener) getIntent().getSerializableExtra("listener");


        setTouchEvents();

        setInitialView();
    }

    private void setTouchEvents() {

        gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {

                                Log.e("flipping", "right");
                                position = position + 1;

                                if (position < actiondata.getStories().size()) {

                                    Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(VisilabsConstant.POSITION, position);
                                    intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
                                    intent.putExtra("counter", 0);
                                    intent.putExtra(VisilabsConstant.STORY, actiondata.getStories().get(position));
                                    startActivity(intent);
                                } else {
                                    onBackPressed();
                                }

                            } else {
                                Log.e("flipping", "left");

                                position = position - 1;

                                if (position < actiondata.getStories().size()) {

                                    Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(VisilabsConstant.POSITION, position);
                                    intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
                                    intent.putExtra("counter", 0);
                                    intent.putExtra(VisilabsConstant.STORY, actiondata.getStories().get(position));
                                    startActivity(intent);
                                } else {
                                    onBackPressed();
                                }
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            position = position + 1;

                            Log.e("flipping", "bottom");


                            if (position < actiondata.getStories().size()) {

                                Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(VisilabsConstant.POSITION, position);
                                intent.putExtra("counter", 0);
                                intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
                                intent.putExtra(VisilabsConstant.STORY, actiondata.getStories().get(position));
                                startActivity(intent);
                            } else {
                                onBackPressed();
                            }
                        } else {
                            position = position - 1;

                            Log.e("flipping", "top");

                            if (position < actiondata.getStories().size()) {

                                Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("counter", 0);
                                intent.putExtra(VisilabsConstant.POSITION, position);
                                intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
                                intent.putExtra(VisilabsConstant.STORY, actiondata.getStories().get(position));
                                startActivity(intent);
                            } else {
                                onBackPressed();
                            }
                        }


                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        });

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

        Visilabs.CallAPI().impressionStory(actiondata.getReport().getImpression());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }

        ivCover = findViewById(R.id.civ_cover);
        tvCover = findViewById(R.id.tv_cover);
        ivClose = findViewById(R.id.ivClose);
        btnStory = findViewById(R.id.btn_story);
        ivStory = findViewById(R.id.iv_story);
        storiesProgressView = findViewById(R.id.stories);
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);


        storiesProgressView.setStoriesCount(stories.getItems().size());
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);

        Picasso.get().load(stories.getThumbnail()).into(ivCover);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        counter = getIntent().getExtras().getInt("counter");

        storiesProgressView.startStories(counter);

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
        Visilabs.CallAPI().impressionStory(actiondata.getReport().getImpression());

        setStoryItem(++counter, stories);

    }

    @Override
    public void onPrev() {
        Visilabs.CallAPI().impressionStory(actiondata.getReport().getImpression());

        if ((counter - 1) < 0) {
            if (position - 1 < actiondata.getStories().size() && position - 1 > -1) {

                Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("counter", actiondata.getStories().get(position - 1).getItems().size() - 1);
                intent.putExtra(VisilabsConstant.POSITION, position - 1);
                intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
                intent.putExtra(VisilabsConstant.STORY, actiondata.getStories().get(position - 1));
                startActivity(intent);

                position = position - 1;
            } else {
                onBackPressed();
            }
        } else {

            setStoryItem(--counter, stories);
        }
    }

    @Override
    public void onComplete() {

        position = position + 1;

        if (position < actiondata.getStories().size()) {

            Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("counter", 0);
            intent.putExtra(VisilabsConstant.POSITION, position);
            intent.putExtra(VisilabsConstant.ACTION_DATA, actiondata);
            intent.putExtra(VisilabsConstant.STORY, actiondata.getStories().get(position));
            startActivity(intent);
        } else {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private void setStoryItem(final int i, final Stories stories) {

        Picasso.get().load(stories.getItems().get(i).getFileSrc()).into(ivStory);
        tvCover.setText(stories.getTitle());

        if (!stories.getItems().get(i).getButtonText().equals("")) {
            btnStory.setVisibility(View.VISIBLE);
            btnStory.setText(stories.getItems().get(i).getButtonText());
            btnStory.setTextColor(Color.parseColor(stories.getItems().get(i).getButtonTextColor()));
            btnStory.setBackgroundColor(Color.parseColor(stories.getItems().get(i).getButtonColor()));

        } else {
            btnStory.setVisibility(View.GONE);
        }

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String storyLink = stories.getItems().get(i).getTargetUrl();
                Visilabs.CallAPI().trackStoryClick(storyLink);
                if (storyItemClickListener != null) {
                    storyItemClickListener.storyItemClicked(storyLink);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent;
        try {
            intent = new Intent(this,
                    Class.forName("com.relateddigital.visilabs.MainActivity"));
            startActivity(intent);
        } catch (ClassNotFoundException e) {

        }
    }
}