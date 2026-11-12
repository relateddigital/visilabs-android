
package com.visilabs.story;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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
import com.visilabs.inApp.InAppSafeUtils;
import com.visilabs.story.action.StoriesProgressView;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.skinbased.Actiondata;
import com.visilabs.story.model.skinbased.ExtendedProps;
import com.visilabs.story.model.skinbased.Items;
import com.visilabs.story.model.skinbased.Stories;
import com.visilabs.util.AppUtils;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.VisilabsConstant;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class StoryActivity extends Activity implements StoriesProgressView.StoriesListener {
    private static final String LOG_TAG = "Story Activity";

    private static int VIDEO_DURATION_OFFSET = 1000;
    private static final long DEFAULT_STORY_DURATION = 5000L;
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
    private static WeakReference<RecyclerView> mRecyclerView = new WeakReference<>(null);
    private static WeakReference<VisilabsSkinBasedAdapter> mVisilabsSkinBasedAdapter = new WeakReference<>(null);
    private int mVideoLastPosition;
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
        mRecyclerView = new WeakReference<>(recyclerView);
    }
    public static void setVisilabsSkinBasedAdapter(VisilabsSkinBasedAdapter visilabsSkinBasedAdapter) {
        mVisilabsSkinBasedAdapter = new WeakReference<>(visilabsSkinBasedAdapter);
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
                return;
            }
        } else {
            Log.e(LOG_TAG, "Could not get the story data properly, finishing...");
            finish();
            return;
        }

        if (mStories == null || mStories.getItems() == null || mStories.getItems().isEmpty()) {
            Log.e(LOG_TAG, "There is no story item to show, finishing...");
            finish();
            return;
        }
        if (mStoryItemPosition < 0 || mStoryItemPosition >= mStories.getItems().size()) {
            mStoryItemPosition = 0;
        }

        setTouchEvents();

        try {
            setInitialView();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not show the story properly, finishing...", e);
            finish();
        }
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
        VisilabsSkinBasedAdapter skinBasedAdapter = mVisilabsSkinBasedAdapter.get();
        if (skinBasedAdapter != null) {
            skinBasedAdapter.setStoryList(skinBasedAdapter.mVisilabsSkinBasedResponse, skinBasedAdapter.mExtendsProps);
        }
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        mStoriesProgressView.setStoriesCount(mStories.getItems().size());
        mStoriesProgressView.setStoriesListener(this);
        mStoriesProgressView.setStoryDuration(getDisplayDuration(mStories.getItems().get(0).getFileType(),
                mStories.getItems().get(mStoryItemPosition).getDisplayTime()));

        impressionStory();

        if(!InAppSafeUtils.isBlank(mStories.getThumbnail())) {
            Picasso.get().load(mStories.getThumbnail()).into(mIvCover);
        }
        mTvCover.setText(mStories.getTitle());

        if(mExtendedProps != null && mExtendedProps.getStoryz_label_color() != null && !mExtendedProps.getStoryz_label_color().isEmpty()) {
            mTvCover.setTextColor(InAppSafeUtils.parseColor(mExtendedProps.getStoryz_label_color(), mTvCover.getCurrentTextColor()));
        }

            mIvClose.setBackgroundResource(getCloseIcon());


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

    private void impressionStory() {
        if (mActionData.getReport() != null && Visilabs.CallAPI() != null) {
            Visilabs.CallAPI().impressionStory(mActionData.getReport().getImpression());
        }
    }

    private long getDisplayDuration(String fileType, String displayTime) {
        double duration = InAppSafeUtils.parseFloat(displayTime, -1f);
        if (VisilabsConstant.STORY_PHOTO_KEY.equals(fileType)) {
            return duration > 0 ? (long) (duration * 1000) : DEFAULT_STORY_DURATION;
        }
        return duration > 0 ? (long) duration : DEFAULT_STORY_DURATION;
    }

    private void showStoryItemSafely(int position) {
        try {
            setStoryItem(mStories.getItems().get(position));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not show the story item properly, finishing...", e);
            finish();
        }
    }

    @Override
    public void onNext() {
        mCountdownEndGifView.setVisibility(View.GONE);
        impressionStory();

        if(mStories.getItems().size() > mStoryItemPosition + 1) {
            showStoryItemSafely(++mStoryItemPosition);
        }
    }

    @Override
    public void onPrev() {
        mCountdownEndGifView.setVisibility(View.GONE);
        impressionStory();

        if ((mStoryItemPosition - 1) < 0) {
            if (mStoryPosition - 1 < mActionData.getStories().size() && mStoryPosition - 1 > -1) {

                mStoryPosition--;

                Stories previousStories = mActionData.getStories().get(mStoryPosition);
                int previousStoryGroupLatestPosition = previousStories != null && previousStories.getItems() != null ?
                        previousStories.getItems().size() - 1 : 0;

                startStoryGroup(Math.max(previousStoryGroupLatestPosition, 0));
            } else {
                onBackPressed();
            }
        } else {
            showStoryItemSafely(--mStoryItemPosition);
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
        if (mStoriesProgressView != null) {
            mStoriesProgressView.destroy();
        }
        if(mTimerCountDown!=null){
            mTimerCountDown.cancel();
        }
        super.onDestroy();
    }

    private void setStoryItem(final Items item) {
        if(mTimerCountDown!=null){
            mTimerCountDown.cancel();
        }

        mStoriesProgressView.setStoryDuration(getDisplayDuration(item.getFileType(), item.getDisplayTime()));

        if(VisilabsConstant.STORY_PHOTO_KEY.equals(item.getFileType())){
            mVideoView.setVisibility(View.INVISIBLE);
            mIvStory.setVisibility(View.VISIBLE);
            if (!InAppSafeUtils.isBlank(item.getFileSrc())) {
                Picasso.get().load(item.getFileSrc()).into(mIvStory);
            }

            isCountDownTimer = mStories.getItems().get(mStoryItemPosition).getCountdown() != null;

            if(isCountDownTimer) {
                mCountDownContainer.setVisibility(View.VISIBLE);
                mCountDownTimer.setBackgroundResource(R.drawable.rounded_corners_full);
                GradientDrawable gd = (GradientDrawable) mCountDownTimer.getBackground();
                gd.setColor(getResources().getColor(R.color.white));

                if("top".equals(mStories.getItems().get(mStoryItemPosition).getCountdown().getPagePosition())) {
                    mCountDownTopText.setVisibility(View.VISIBLE);
                    mCountDownBotText.setVisibility(View.GONE);

                    mCountDownTopText.setText(InAppSafeUtils.replaceNewLines(mStories.getItems().get(mStoryItemPosition).
                            getCountdown().getMessageText()));
                    mCountDownTopText.setTextColor(InAppSafeUtils.parseColor(mStories.getItems()
                            .get(mStoryItemPosition).getCountdown().getMessageTextColor(), mCountDownTopText.getCurrentTextColor()));
                    mCountDownTopText.setTextSize(InAppSafeUtils.parseFloat(mStories.getItems().
                            get(mStoryItemPosition).getCountdown().getMessageTextSize(), 0f) + 16);
                    mCountDownTopText.setTypeface(Typeface.DEFAULT);
                } else {
                    mCountDownBotText.setVisibility(View.VISIBLE);
                    mCountDownTopText.setVisibility(View.GONE);

                    mCountDownBotText.setText(InAppSafeUtils.replaceNewLines(mStories.getItems().get(mStoryItemPosition).
                            getCountdown().getMessageText()));
                    mCountDownBotText.setTextColor(InAppSafeUtils.parseColor(mStories.getItems()
                            .get(mStoryItemPosition).getCountdown().getMessageTextColor(), mCountDownBotText.getCurrentTextColor()));
                    mCountDownBotText.setTextSize(InAppSafeUtils.parseFloat(mStories.getItems().
                            get(mStoryItemPosition).getCountdown().getMessageTextSize(), 0f) + 16);
                    mCountDownBotText.setTypeface(Typeface.DEFAULT);
                }

                setTimerValues();

                if (mWeekNum.getVisibility() != View.GONE) {
                    mWeekNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdWeek = (GradientDrawable) mWeekNum.getBackground();
                    gdWeek.setColor(InAppSafeUtils.parseColor("#E5E4E2", android.graphics.Color.BLACK));
                    mWeekNum.setText(String.valueOf(mWeekNumber));
                    mWeekNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mDayNum.getVisibility() != View.GONE) {
                    mDayNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdDay = (GradientDrawable) mDayNum.getBackground();
                    gdDay.setColor(InAppSafeUtils.parseColor("#E5E4E2", android.graphics.Color.BLACK));
                    mDayNum.setText(String.valueOf(mDayNumber));
                    mDayNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mHourNum.getVisibility() != View.GONE) {
                    mHourNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdHour = (GradientDrawable) mHourNum.getBackground();
                    gdHour.setColor(InAppSafeUtils.parseColor("#E5E4E2", android.graphics.Color.BLACK));
                    mHourNum.setText(String.valueOf(mHourNumber));
                    mHourNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mMinuteNum.getVisibility() != View.GONE) {
                    mMinuteNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdMinute = (GradientDrawable) mMinuteNum.getBackground();
                    gdMinute.setColor(InAppSafeUtils.parseColor("#E5E4E2", android.graphics.Color.BLACK));
                    mMinuteNum.setText(String.valueOf(mMinuteNumber));
                    mMinuteNum.setTextColor(getResources().getColor(R.color.black));
                }

                if (mSecNum.getVisibility() != View.GONE) {
                    mSecNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                    GradientDrawable gdSec = (GradientDrawable) mSecNum.getBackground();
                    gdSec.setColor(InAppSafeUtils.parseColor("#E5E4E2", android.graphics.Color.BLACK));
                    mSecNum.setText(String.valueOf(mSecondNumber));
                    mSecNum.setTextColor(getResources().getColor(R.color.black));
                }

                startTimer();
            } else {
                mCountDownContainer.setVisibility(View.GONE);
            }
            mStoriesProgressView.startStories(mStoryItemPosition);
        } else if(VisilabsConstant.STORY_VIDEO_KEY.equals(item.getFileType())) {
            mVideoView.setVisibility(View.VISIBLE);
            mIvStory.setVisibility(View.INVISIBLE);
            mCountDownContainer.setVisibility(View.GONE);
            if (!InAppSafeUtils.isBlank(item.getFileSrc())) {
                mVideoView.setVideoURI(Uri.parse(item.getFileSrc()));
            }
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        int videoDuration = mp.getDuration();
                        if (videoDuration > 0) {
                            item.setDisplayTime(String.valueOf(videoDuration + VIDEO_DURATION_OFFSET));
                            mStoriesProgressView.setStoryDuration(videoDuration + VIDEO_DURATION_OFFSET);
                        }
                    } catch (Exception e) {
                        Log.w(LOG_TAG, "Could not get the video duration", e);
                    }
                    mStoriesProgressView.startStories(mStoryItemPosition);
                }
            });
            mVideoView.requestFocus();
            mVideoView.start();
        }

        if (!InAppSafeUtils.isBlank(item.getButtonText())) {
            mBtnStory.setVisibility(View.VISIBLE);
            mBtnStory.setText(item.getButtonText());
            mBtnStory.setTextColor(InAppSafeUtils.parseColor(item.getButtonTextColor(), mBtnStory.getCurrentTextColor()));
            if (InAppSafeUtils.isValidColor(item.getButtonColor())) {
                mBtnStory.setBackgroundColor(InAppSafeUtils.parseColor(item.getButtonColor(), Color.TRANSPARENT));
            }

        } else {
            mBtnStory.setVisibility(View.GONE);
        }

        mBtnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionData.getReport() != null && Visilabs.CallAPI() != null) {
                    Visilabs.CallAPI().trackStoryClick(mActionData.getReport().getClick());
                }
                mActivity.finish();
                if (mStoryItemClickListener != null) {
                    mStoryItemClickListener.storyItemClicked(item.getTargetUrl());
                }
            }
        });
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

            if(mStories.getItems().get(mStoryItemPosition).getCountdown()
                    .getEndAnimationImageUrl() != null && !mStories.getItems()
                    .get(mStoryItemPosition).getCountdown().getEndAnimationImageUrl().isEmpty()) {
                startCountdownEndAnimation();
            }
        }

        String displayType = mStories.getItems().get(mStoryItemPosition).getCountdown().getDisplayType();
        switch(displayType != null ? displayType : "") {
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
        if (isFinishing() || isDestroyed()) {
            if (mTimerCountDown != null) {
                mTimerCountDown.cancel();
            }
            return;
        }
        try {
            calculateTimeFields();
        } catch (Exception e) {
            Log.e(LOG_TAG, "The fields for countdown timer could not be calculated!", e);
            return;
        }

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
        String displayType = mStories.getItems().get(mStoryItemPosition).getCountdown().getDisplayType();
        switch (displayType != null ? displayType : "") {

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

        if(mStories.getItems().get(mStoryItemPosition).getCountdown()
                .getEndAnimationImageUrl() != null && !mStories.getItems()
                .get(mStoryItemPosition).getCountdown().getEndAnimationImageUrl().isEmpty()) {
            startCountdownEndAnimation();
        }
    }

    private void startCountdownEndAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                try {
                    mCountdownEndGifView.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(mStories.getItems().get(mStoryItemPosition).getCountdown().getEndAnimationImageUrl())
                            .into(mCountdownEndGifView);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Countdown end animation could not be shown!", e);
                }
            }
        });
    }
    private int getCloseIcon() {
            return R.drawable.ic_close_white_24dp;
    }
}