package com.visilabs.story;

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
import com.visilabs.story.model.skinbased.Stories;

import java.util.ArrayList;
import java.util.Objects;


public class PreviewActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView storiesProgressView;
    private ImageView ivStory;

    private int counter = 0;

    private final long[] durations = new long[]{
            500L, 1000L, 1500L, 4000L, 5000L, 1000,
    };

    long pressTime = 0L;
    long limit = 500L;

    Actiondata actiondata;

    GestureDetector gestureDetector;

    View.OnTouchListener onTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actiondata = (Actiondata) getIntent().getSerializableExtra("bundle"); //Obtaining data
        }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }

        ImageView ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setStoryView();
        bindReverseView();
        bindSkipView();

        View exit = findViewById(R.id.exit);

        Button btnStory = findViewById(R.id.btn_story);
        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void setStoryView() {

        storiesProgressView = findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(actiondata.getStories().size());
        storiesProgressView.setStoryDuration(3000L);
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
//        storiesProgressView.startStories();

        storiesProgressView.startStories(counter);
        ivStory = findViewById(R.id.iv_story);
        Picasso.get().load(actiondata.getStories().get(counter).getThumbnail()).into(ivStory);
    }

    private void bindSkipView() {
        View skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        skip.setOnTouchListener(onTouchListener);
    }

    private void bindReverseView() {
        View reverse = findViewById(R.id.reverse);
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
        Picasso.get().load(actiondata.getStories().get(++counter).getThumbnail()).into(ivStory);
      //  ivStory.setImageResource(resources[++counter]);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        Picasso.get().load(actiondata.getStories().get(--counter).getThumbnail()).into(ivStory);
    }

    @Override
    public void onComplete() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }
}