
package com.visilabs.story;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.visilabs.story.model.skinbased.Items;
import com.visilabs.story.model.skinbased.Stories;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.VisilabsConstant;
import java.util.Objects;


public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private StoriesProgressView mStoriesProgressView;
    private ImageView mIvStory;
    int mStoryItemPosition;
    long mPressTime = 0L;
    long mLimit = 500L;
    Stories mStories;
    Actiondata mActionData;
    String mActionId;
    Button mBtnStory;
    View mReverse, mSkip;
    ImageView mIvClose, mIvCover;
    TextView mTvCover;
    GestureDetector mGestureDetector;
    int mStoryPosition;
    View.OnTouchListener mOnTouchListener;
    static StoryItemClickListener mStoryItemClickListener;
    static RecyclerView mRecyclerView;
    static VisilabsSkinBasedAdapter mVisilabsSkinBasedAdapter;

    public static void setStoryItemClickListener(StoryItemClickListener storyItemClickListener) {
        mStoryItemClickListener = storyItemClickListener;
    }

    public static void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }
    public static void setVisilabsSkinBasedAdapter(VisilabsSkinBasedAdapter visilabsSkinBasedAdapter) {
        mVisilabsSkinBasedAdapter = visilabsSkinBasedAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story);

        hideActionBar();

        mActionData = (Actiondata) getIntent().getSerializableExtra(VisilabsConstant.ACTION_DATA);
        mActionId = (String) getIntent().getSerializableExtra(VisilabsConstant.ACTION_ID);
        mStoryPosition = getIntent().getExtras().getInt(VisilabsConstant.STORY_POSITION);
        mStoryItemPosition = getIntent().getExtras().getInt(VisilabsConstant.STORY_ITEM_POSITION);
        mStories = mActionData.getStories().get(mStoryPosition);

        setTouchEvents();

        setInitialView();
    }

    private void setTouchEvents() {

        mGestureDetector = new GestureDetector(getApplicationContext(), new GestureListener());

        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mPressTime = System.currentTimeMillis();
                        mStoriesProgressView.pause();

                        return false;

                    case MotionEvent.ACTION_UP:
                        long now = System.currentTimeMillis();
                        mStoriesProgressView.resume();

                        return mLimit < now - mPressTime;
                }
                return mGestureDetector.onTouchEvent(event);
            }
        };
    }

    private void setInitialView() {

        mIvStory = findViewById(R.id.iv_story);
        mIvCover = findViewById(R.id.civ_cover);
        mTvCover = findViewById(R.id.tv_cover);
        mIvClose = findViewById(R.id.ivClose);
        mBtnStory = findViewById(R.id.btn_story);
        mStoriesProgressView = findViewById(R.id.stories);
        mReverse = findViewById(R.id.reverse);
        mSkip = findViewById(R.id.skip);

        String title = mStories.getTitle();

        Log.i("StoryActivityShows ", mActionId + " : " + mStories.getTitle());
        PersistentTargetManager.with(getApplicationContext()).saveShownStory(mActionId, mStories.getTitle());
        mVisilabsSkinBasedAdapter.setStoryList(mVisilabsSkinBasedAdapter.mVisilabsSkinBasedResponse, mVisilabsSkinBasedAdapter.mExtendsProps);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        mStoriesProgressView.setStoriesCount(mStories.getItems().size());
        mStoriesProgressView.setStoryDuration(Integer.parseInt(mStories.getItems()
                .get(mStoryItemPosition).getDisplayTime()) * 1000);
        mStoriesProgressView.setStoriesListener(this);
        mStoriesProgressView.startStories(mStoryItemPosition);

        String impressionReport = mActionData.getReport().getImpression();
        Visilabs.CallAPI().impressionStory(impressionReport);

        Picasso.get().load(mStories.getThumbnail()).into(mIvCover);
        mTvCover.setText(mStories.getTitle());

        setStoryItem(mStories.getItems().get(mStoryItemPosition));
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bindReverseView();
        bindSkipView();
    }

    private void bindSkipView() {

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStoriesProgressView.skip();
            }
        });

        mSkip.setOnTouchListener(mOnTouchListener);
    }

    private void bindReverseView() {
        mReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStoriesProgressView.reverse();
            }
        });

        mReverse.setOnTouchListener(mOnTouchListener);
    }

    private void hideActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onNext() {
        Visilabs.CallAPI().impressionStory(mActionData.getReport().getImpression());

        setStoryItem(mStories.getItems().get(++mStoryItemPosition));
    }

    @Override
    public void onPrev() {
        Visilabs.CallAPI().impressionStory(mActionData.getReport().getImpression());

        if ((mStoryItemPosition - 1) < 0) {
            if (mStoryPosition - 1 < mActionData.getStories().size() && mStoryPosition - 1 > -1) {

                mStoryPosition--;

                int previousStoryGroupLatestPosition = mActionData.getStories().get(mStoryPosition)
                        .getItems().size() - 1;

                startStoryGroup(previousStoryGroupLatestPosition);
            } else {
                onBackPressed();
            }
        } else {
            setStoryItem(mStories.getItems().get(--mStoryItemPosition));
        }
    }

    @Override
    public void onComplete() {

        mStoryPosition++;

        if (mStoryPosition < mActionData.getStories().size()) {
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
        intent.putExtra(VisilabsConstant.STORY_POSITION, mStoryPosition);
        intent.putExtra(VisilabsConstant.ACTION_DATA, mActionData);
        intent.putExtra(VisilabsConstant.ACTION_ID, mActionId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mStoriesProgressView.destroy();
        super.onDestroy();
    }

    private void setStoryItem(final Items item) {

        if (!item.getFileSrc().equals("")) {
            Picasso.get().load(item.getFileSrc()).into(mIvStory);
        }

        if (!item.getButtonText().equals("")) {
            mBtnStory.setVisibility(View.VISIBLE);
            mBtnStory.setText(item.getButtonText());
            mBtnStory.setTextColor(Color.parseColor(item.getButtonTextColor()));
            mBtnStory.setBackgroundColor(Color.parseColor(item.getButtonColor()));

        } else {
            mBtnStory.setVisibility(View.GONE);
        }

        mBtnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Visilabs.CallAPI().trackStoryClick(mActionData.getReport().getClick());
                if (mStoryItemClickListener != null) {
                    mStoryItemClickListener.storyItemClicked(item.getTargetUrl());
                }
            }
        });
    }
}