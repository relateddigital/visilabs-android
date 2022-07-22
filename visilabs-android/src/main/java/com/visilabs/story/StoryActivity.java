
package com.visilabs.story;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.story.action.StoriesProgressView;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.skinbased.Actiondata;
import com.visilabs.story.model.skinbased.ExtendedProps;
import com.visilabs.story.model.skinbased.Items;
import com.visilabs.story.model.skinbased.Stories;
import com.visilabs.util.AppUtils;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.VisilabsConstant;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoryActivity extends Activity implements StoriesProgressView.StoriesListener {
    private static final String LOG_TAG = "Story Activity";

    private static int VIDEO_DURATION_OFFSET = 1000;
    private StoriesProgressView mStoriesProgressView;
    private ImageView mIvStory;
    private VideoView mVideoView;
    int mStoryItemPosition;
    long mPressTime = 0L;
    long mLimit = 500L;
    Stories mStories;
    Actiondata mActionData;
    private ExtendedProps mExtendedProps;
    String mActionId;
    Button mBtnStory;
    View mReverse, mSkip;
    ImageView mIvClose, mIvCover;
    TextView mTvCover;
    private ImageView mCountdownEndGifView;
    GestureDetector mGestureDetector;
    int mStoryPosition;
    View.OnTouchListener mOnTouchListener;
    static StoryItemClickListener mStoryItemClickListener;
    static RecyclerView mRecyclerView;
    static VisilabsSkinBasedAdapter mVisilabsSkinBasedAdapter;
    private int mVideoLastPosition;
    private MediaMetadataRetriever mRetriever;
    private Activity mActivity;
    private boolean isCountDownTimer = false;
    private LinearLayout mCountDownContainer;
    private TextView mCountDownTopText, mCountDownBotText;
    private RelativeLayout mCountDownTimer;
    private TextView mWeekNum, mDivider1, mDayNum, mDivider2, mHourNum, mDivider3,
    mMinuteNum, mDivider4, mSecNum, mWeekStr, mDayStr, mHourStr, mMinuteStr, mSecStr;
    private short mWeekNumber;
    private short mDayNumber;
    private short mHourNumber;
    private short mMinuteNumber;
    private short mSecondNumber;
    private Timer mTimerCountDown;
    private static final short TIMER_SCHEDULE = 1000;
    private static final short TIMER_PERIOD = 1000;

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
        setContentView(R.layout.activity_story);

        mActivity = this;

        if(getIntent() != null) {
            try {
                mActionData = (Actiondata) getIntent().getSerializableExtra(VisilabsConstant.ACTION_DATA);
                mExtendedProps = new Gson().fromJson(new java.net.URI(mActionData.getExtendedProps()).getPath(), ExtendedProps.class);
                mActionId = (String) getIntent().getSerializableExtra(VisilabsConstant.ACTION_ID);
                mStoryPosition = getIntent().getExtras().getInt(VisilabsConstant.STORY_POSITION);
                mStoryItemPosition = getIntent().getExtras().getInt(VisilabsConstant.STORY_ITEM_POSITION);
                mStories = mActionData.getStories().get(mStoryPosition);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Could not get the story data properly, finishing...");
                e.printStackTrace();
                finish();
            }
        } else {
            Log.e(LOG_TAG, "Could not get the story data properly, finishing...");
            finish();
        }

        mRetriever = new MediaMetadataRetriever();
        calculateDisplayTimeVideo();

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
                        if(mVideoView.getVisibility() == View.VISIBLE) {
                            mVideoLastPosition = mVideoView.getCurrentPosition();
                            if (mVideoView.isPlaying()) {
                                mVideoView.pause();
                            }
                        }

                        return false;

                    case MotionEvent.ACTION_UP:
                        long now = System.currentTimeMillis();
                        mStoriesProgressView.resume();
                        if(mVideoView.getVisibility() == View.VISIBLE) {
                            mVideoView.seekTo(mVideoLastPosition);
                            mVideoView.start();
                        }

                        return mLimit < now - mPressTime;
                }
                return mGestureDetector.onTouchEvent(event);
            }
        };
    }

    private void setInitialView() {

        mIvStory = findViewById(R.id.iv_story);
        mVideoView = findViewById(R.id.video_story_view);
        mIvCover = findViewById(R.id.civ_cover);
        mTvCover = findViewById(R.id.tv_cover);
        mIvClose = findViewById(R.id.ivClose);
        mBtnStory = findViewById(R.id.btn_story);
        mStoriesProgressView = findViewById(R.id.stories);
        mReverse = findViewById(R.id.reverse);
        mSkip = findViewById(R.id.skip);
        mCountDownContainer = findViewById(R.id.count_down_container);
        mCountDownTopText = findViewById(R.id.count_down_top_text);
        mCountDownBotText = findViewById(R.id.count_down_bot_text);
        mCountDownTimer = findViewById(R.id.countdown_timer);
        mWeekNum = findViewById(R.id.week_num);
        mDivider1 = findViewById(R.id.divider1);
        mDayNum = findViewById(R.id.day_num);
        mDivider2 = findViewById(R.id.divider2);
        mHourNum = findViewById(R.id.hour_num);
        mDivider3 = findViewById(R.id.divider3);
        mMinuteNum = findViewById(R.id.minute_num);
        mDivider4 = findViewById(R.id.divider4);
        mSecNum = findViewById(R.id.sec_num);
        mWeekStr = findViewById(R.id.week_str);
        mDayStr = findViewById(R.id.day_str);
        mHourStr = findViewById(R.id.hour_str);
        mMinuteStr = findViewById(R.id.minute_str);
        mSecStr = findViewById(R.id.sec_str);
        mCountdownEndGifView = findViewById(R.id.countdown_end_gif);

        mCountdownEndGifView.setVisibility(View.GONE);

        String title = mStories.getTitle();

        Log.i("StoryActivityShows ", mActionId + " : " + mStories.getTitle());
        PersistentTargetManager.with(getApplicationContext()).saveShownStory(mActionId, mStories.getTitle());
        mVisilabsSkinBasedAdapter.setStoryList(mVisilabsSkinBasedAdapter.mVisilabsSkinBasedResponse, mVisilabsSkinBasedAdapter.mExtendsProps);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        mStoriesProgressView.setStoriesCount(mStories.getItems().size());
        mStoriesProgressView.setStoriesListener(this);
        if(mStories.getItems().get(0).getFileType().equals(VisilabsConstant.STORY_PHOTO_KEY)){
            mStoriesProgressView.setStoryDuration(Integer.parseInt(mStories.getItems()
                    .get(mStoryItemPosition).getDisplayTime()) * 1000);
        } else {
            mStoriesProgressView.setStoryDuration(Integer.parseInt(mStories.getItems()
                    .get(mStoryItemPosition).getDisplayTime()));
        }

        String impressionReport = mActionData.getReport().getImpression();
        Visilabs.CallAPI().impressionStory(impressionReport);

        if(!mStories.getThumbnail().equals("")) {
            Picasso.get().load(mStories.getThumbnail()).into(mIvCover);
        }
        mTvCover.setText(mStories.getTitle());

        if(mExtendedProps != null && mExtendedProps.getStoryz_label_color() != null && !mExtendedProps.getStoryz_label_color().isEmpty()) {
            mTvCover.setTextColor(Color.parseColor(mExtendedProps.getStoryz_label_color()));
        }

        mTvCover.setTypeface(AppUtils.getFontFamily(this,
                mExtendedProps != null ? mExtendedProps.getFont_family() : null,
                mExtendedProps != null ? mExtendedProps.getCustom_font_family_android() : null));

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

    @Override
    public void onNext() {
        mCountdownEndGifView.setVisibility(View.GONE);
        Visilabs.CallAPI().impressionStory(mActionData.getReport().getImpression());

        if(mStories.getItems().size() > mStoryItemPosition + 1) {
            setStoryItem(mStories.getItems().get(++mStoryItemPosition));
        }
    }

    @Override
    public void onPrev() {
        mCountdownEndGifView.setVisibility(View.GONE);
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
        mCountdownEndGifView.setVisibility(View.GONE);
        mStoryPosition++;

        if (mStoryPosition < mActionData.getStories().size()) {
            int nextStoryGroupFirstPosition = 0;
            startStoryGroup(nextStoryGroupFirstPosition);
        } else {
            onBackPressed();
        }
    }

    private void startStoryGroup(int itemPosition) {
        mCountdownEndGifView.setVisibility(View.GONE);
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
        mRetriever.release();
        if(mTimerCountDown!=null){
            mTimerCountDown.cancel();
        }
        super.onDestroy();
    }

    private void setStoryItem(final Items item) {
        if(mTimerCountDown!=null){
            mTimerCountDown.cancel();
        }

        if(item.getFileType().equals(VisilabsConstant.STORY_PHOTO_KEY)){
            mStoriesProgressView.setStoryDuration(Integer.parseInt(mStories.getItems()
                    .get(mStoryItemPosition).getDisplayTime()) * 1000);
        } else {
            mStoriesProgressView.setStoryDuration(Integer.parseInt(mStories.getItems()
                    .get(mStoryItemPosition).getDisplayTime()));
        }

        if(item.getFileType().equals(VisilabsConstant.STORY_PHOTO_KEY)){
            mVideoView.setVisibility(View.INVISIBLE);
            mIvStory.setVisibility(View.VISIBLE);
            if (!item.getFileSrc().equals("")) {
                Picasso.get().load(item.getFileSrc()).into(mIvStory);
            }

            isCountDownTimer = mStories.getItems().get(mStoryItemPosition).getCountdown() != null;

            if(isCountDownTimer) {
                mCountDownContainer.setVisibility(View.VISIBLE);
                mCountDownTimer.setBackgroundResource(R.drawable.rounded_corners_full);
                GradientDrawable gd = (GradientDrawable) mCountDownTimer.getBackground();
                gd.setColor(getResources().getColor(R.color.white));

                if(mStories.getItems().get(mStoryItemPosition).getCountdown().getPagePosition().equals("top")) {
                    mCountDownTopText.setVisibility(View.VISIBLE);
                    mCountDownBotText.setVisibility(View.GONE);

                    mCountDownTopText.setText((mStories.getItems().get(mStoryItemPosition).
                            getCountdown().getMessageText()).replace("\\n", "\n"));
                    mCountDownTopText.setTextColor(Color.parseColor(mStories.getItems()
                            .get(mStoryItemPosition).getCountdown().getMessageTextColor()));
                    mCountDownTopText.setTextSize(Float.parseFloat(mStories.getItems().
                            get(mStoryItemPosition).getCountdown().getMessageTextSize()) + 16);
                    mCountDownTopText.setTypeface(Typeface.DEFAULT);
                } else {
                    mCountDownBotText.setVisibility(View.VISIBLE);
                    mCountDownTopText.setVisibility(View.GONE);

                    mCountDownBotText.setText((mStories.getItems().get(mStoryItemPosition).
                            getCountdown().getMessageText()).replace("\\n", "\n"));
                    mCountDownBotText.setTextColor(Color.parseColor(mStories.getItems()
                            .get(mStoryItemPosition).getCountdown().getMessageTextColor()));
                    mCountDownBotText.setTextSize(Float.parseFloat(mStories.getItems().
                            get(mStoryItemPosition).getCountdown().getMessageTextSize()) + 16);
                    mCountDownBotText.setTypeface(Typeface.DEFAULT);
                }

                setTimerValues();

                if (mWeekNum.getVisibility() != View.GONE) {
                    mWeekNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdWeek = (GradientDrawable) mWeekNum.getBackground();
                    gdWeek.setColor(Color.parseColor("#E5E4E2"));
                    mWeekNum.setText(String.valueOf(mWeekNumber));
                    mWeekNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mDayNum.getVisibility() != View.GONE) {
                    mDayNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdDay = (GradientDrawable) mDayNum.getBackground();
                    gdDay.setColor(Color.parseColor("#E5E4E2"));
                    mDayNum.setText(String.valueOf(mDayNumber));
                    mDayNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mHourNum.getVisibility() != View.GONE) {
                    mHourNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdHour = (GradientDrawable) mHourNum.getBackground();
                    gdHour.setColor(Color.parseColor("#E5E4E2"));
                    mHourNum.setText(String.valueOf(mHourNumber));
                    mHourNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mMinuteNum.getVisibility() != View.GONE) {
                    mMinuteNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdMinute = (GradientDrawable) mMinuteNum.getBackground();
                    gdMinute.setColor(Color.parseColor("#E5E4E2"));
                    mMinuteNum.setText(String.valueOf(mMinuteNumber));
                    mMinuteNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mSecNum.getVisibility() != View.GONE) {
                    mSecNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdSec = (GradientDrawable) mSecNum.getBackground();
                    gdSec.setColor(Color.parseColor("#E5E4E2"));
                    mSecNum.setText(String.valueOf(mSecondNumber));
                    mSecNum.setTextColor(getResources().getColor(R.color.black));
                }

                startTimer();
            } else {
                mCountDownContainer.setVisibility(View.GONE);
            }
            mStoriesProgressView.startStories(mStoryItemPosition);
        } else if(item.getFileType().equals(VisilabsConstant.STORY_VIDEO_KEY)) {
            mVideoView.setVisibility(View.VISIBLE);
            mIvStory.setVisibility(View.INVISIBLE);
            mCountDownContainer.setVisibility(View.GONE);
            if (!item.getFileSrc().equals("")) {
                mVideoView.setVideoURI(Uri.parse(item.getFileSrc()));
            }
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mStoriesProgressView.startStories(mStoryItemPosition);
                }
            });
            mVideoView.requestFocus();
            mVideoView.start();
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
                mActivity.finish();
                if (mStoryItemClickListener != null) {
                    mStoryItemClickListener.storyItemClicked(item.getTargetUrl());
                }
            }
        });
    }

    private void calculateDisplayTimeVideo() {
        List<Items> items = mStories.getItems();
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getFileType().equals(VisilabsConstant.STORY_VIDEO_KEY)){
                mRetriever.setDataSource(items.get(i).getFileSrc(), new HashMap<String, String>());
                String time = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilliSec = Long.parseLong(time);
                mStories.getItems().get(i).setDisplayTime(String.valueOf(timeInMilliSec + VIDEO_DURATION_OFFSET));
            }
        }
    }

    private void setTimerValues() {
        int timeDifInSec = AppUtils.calculateTimeDifferenceInSec(mStories.getItems().get(mStoryItemPosition)
                .getCountdown().getEndDateTime());

        if(timeDifInSec == 0) {
            mCountDownContainer.setVisibility(View.GONE);
            Log.e(LOG_TAG, "Something went wrong when calculating the time difference!!");
            return;
        } else if(timeDifInSec < 0) {
            mWeekNumber = 0;
            mDayNumber = 0;
            mHourNumber = 0;
            mMinuteNumber = 0;
            mSecondNumber = 0;

            if(true) { // TODO : real control here
                startCountdownEndAnimation();
            }
        }

        switch(mStories.getItems().get(mStoryItemPosition).getCountdown().getDisplayType()) {
            case "dhms": {
                mWeekNum.setVisibility(View.GONE);
                mDayNum.setVisibility(View.VISIBLE);
                mHourNum.setVisibility(View.VISIBLE);
                mMinuteNum.setVisibility(View.VISIBLE);
                mSecNum.setVisibility(View.VISIBLE);
                mWeekStr.setVisibility(View.GONE);
                mDayStr.setVisibility(View.VISIBLE);
                mHourStr.setVisibility(View.VISIBLE);
                mMinuteStr.setVisibility(View.VISIBLE);
                mSecStr.setVisibility(View.VISIBLE);
                mDivider1.setVisibility(View.GONE);
                mDivider2.setVisibility(View.VISIBLE);
                mDivider3.setVisibility(View.VISIBLE);
                mDivider4.setVisibility(View.VISIBLE);

                if(timeDifInSec > 0) {
                    mDayNumber = (short) (timeDifInSec / (60*60*24));
                    timeDifInSec = timeDifInSec - mDayNumber*60*60*24;
                    mHourNumber = (short) (timeDifInSec / (60*60));
                    timeDifInSec = timeDifInSec - mHourNumber*60*60;
                    mMinuteNumber = (short) (timeDifInSec / (60));
                    timeDifInSec = timeDifInSec - mMinuteNumber*60;
                    mSecondNumber = (short) timeDifInSec;
                }
                break;
            }
            case "dhm": {
                mWeekNum.setVisibility(View.GONE);
                mDayNum.setVisibility(View.VISIBLE);
                mHourNum.setVisibility(View.VISIBLE);
                mMinuteNum.setVisibility(View.VISIBLE);
                mSecNum.setVisibility(View.GONE);
                mWeekStr.setVisibility(View.GONE);
                mDayStr.setVisibility(View.VISIBLE);
                mHourStr.setVisibility(View.VISIBLE);
                mMinuteStr.setVisibility(View.VISIBLE);
                mSecStr.setVisibility(View.GONE);
                mDivider1.setVisibility(View.GONE);
                mDivider2.setVisibility(View.VISIBLE);
                mDivider3.setVisibility(View.VISIBLE);
                mDivider4.setVisibility(View.GONE);

                if(timeDifInSec > 0) {
                    mDayNumber = (short) (timeDifInSec / (60*60*24));
                    timeDifInSec = timeDifInSec - mDayNumber*60*60*24;
                    mHourNumber = (short) (timeDifInSec / (60*60));
                    timeDifInSec = timeDifInSec - mHourNumber*60*60;
                    mMinuteNumber = (short) (timeDifInSec / (60));
                }
                break;
            }
            case "d": {
                mWeekNum.setVisibility(View.GONE);
                mDayNum.setVisibility(View.VISIBLE);
                mHourNum.setVisibility(View.GONE);
                mMinuteNum.setVisibility(View.GONE);
                mSecNum.setVisibility(View.GONE);
                mWeekStr.setVisibility(View.GONE);
                mDayStr.setVisibility(View.VISIBLE);
                mHourStr.setVisibility(View.GONE);
                mMinuteStr.setVisibility(View.GONE);
                mSecStr.setVisibility(View.GONE);
                mDivider1.setVisibility(View.GONE);
                mDivider2.setVisibility(View.GONE);
                mDivider3.setVisibility(View.GONE);
                mDivider4.setVisibility(View.GONE);

                if(timeDifInSec > 0) {
                    mDayNumber = (short) (timeDifInSec / (60*60*24));
                }
                break;
            }
            default: {
                mWeekNum.setVisibility(View.VISIBLE);
                mDayNum.setVisibility(View.VISIBLE);
                mHourNum.setVisibility(View.VISIBLE);
                mMinuteNum.setVisibility(View.VISIBLE);
                mSecNum.setVisibility(View.VISIBLE);
                mWeekStr.setVisibility(View.VISIBLE);
                mDayStr.setVisibility(View.VISIBLE);
                mHourStr.setVisibility(View.VISIBLE);
                mMinuteStr.setVisibility(View.VISIBLE);
                mSecStr.setVisibility(View.VISIBLE);
                mDivider1.setVisibility(View.VISIBLE);
                mDivider2.setVisibility(View.VISIBLE);
                mDivider3.setVisibility(View.VISIBLE);
                mDivider4.setVisibility(View.VISIBLE);

                if(timeDifInSec > 0) {
                    mWeekNumber = (short) (timeDifInSec / (60*60*24*7));
                    timeDifInSec = timeDifInSec - mWeekNumber*60*60*24*7;
                    mDayNumber = (short) (timeDifInSec / (60*60*24));
                    timeDifInSec = timeDifInSec - mDayNumber*60*60*24;
                    mHourNumber = (short) (timeDifInSec / (60*60));
                    timeDifInSec = timeDifInSec - mHourNumber*60*60;
                    mMinuteNumber = (short) (timeDifInSec / (60));
                    timeDifInSec = timeDifInSec - mMinuteNumber*60;
                    mSecondNumber = (short) timeDifInSec;
                }
                break;
            }
        }
    }

    private void startTimer() {
        mTimerCountDown = new Timer("CountDownTimer", false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                reAdjustTimerViews();
            }
        };
        mTimerCountDown.schedule(task, TIMER_SCHEDULE, TIMER_PERIOD);
    }

    private void reAdjustTimerViews(){
        calculateTimeFields();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mWeekNum.getVisibility() != View.GONE) {
                        mWeekNum.setText(String.valueOf(mWeekNumber));
                    }

                    if (mDayNum.getVisibility() != View.GONE) {
                        mDayNum.setText(String.valueOf(mDayNumber));
                    }

                    if (mHourNum.getVisibility() != View.GONE) {
                        mHourNum.setText(String.valueOf(mHourNumber));
                    }

                    if (mMinuteNum.getVisibility() != View.GONE) {
                        mMinuteNum.setText(String.valueOf(mMinuteNumber));
                    }

                    if (mSecNum.getVisibility() != View.GONE) {
                        mSecNum.setText(String.valueOf(mSecondNumber));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Log.e(LOG_TAG, "The fields for countdown timer could not be set!");
                }
            }
        });
    }

    private void calculateTimeFields() {
        switch (mStories.getItems().get(mStoryItemPosition).getCountdown().getDisplayType()) {

            case "dhms":

            case "dhm":

            case "d": {
                if(mSecondNumber > 0) {
                    mSecondNumber--;
                } else {
                    mSecondNumber = 59;
                    if(mMinuteNumber > 0){
                        mMinuteNumber--;
                    } else {
                        mMinuteNumber = 59;
                        if(mHourNumber > 0) {
                            mHourNumber--;
                        } else {
                            mHourNumber = 23;
                            if(mDayNumber > 0) {
                                mDayNumber--;
                            } else {
                                expireTime();
                            }
                        }
                    }
                }
                break;
            }

            default: {
                if(mSecondNumber > 0) {
                    mSecondNumber--;
                } else {
                    mSecondNumber = 59;
                    if(mMinuteNumber > 0){
                        mMinuteNumber--;
                    } else {
                        mMinuteNumber = 59;
                        if(mHourNumber > 0) {
                            mHourNumber--;
                        } else {
                            mHourNumber = 23;
                            if(mDayNumber > 0) {
                                mDayNumber--;
                            } else {
                                mDayNumber = 6;
                                if(mWeekNumber > 0) {
                                    mWeekNumber--;
                                } else {
                                    expireTime();
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    private void expireTime() {
        mSecondNumber = 0;
        mMinuteNumber = 0;
        mHourNumber = 0;
        mDayNumber = 0;
        mWeekNumber = 0;
        if(mTimerCountDown!=null){
            mTimerCountDown.cancel();
        }

        if(true) { // TODO : real control here
            startCountdownEndAnimation();
        }
    }

    private void startCountdownEndAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCountdownEndGifView.setVisibility(View.VISIBLE);
                mCountdownEndGifView.setAlpha(0.5f);
                Glide.with(getApplicationContext())
                        .load("https://c.tenor.com/Rdz9M0h2BoQAAAAC/confetti.gif") // TODO : real url here
                        .into(mCountdownEndGifView);
            }
        });
    }
}