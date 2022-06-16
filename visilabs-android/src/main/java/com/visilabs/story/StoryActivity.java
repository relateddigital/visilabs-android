
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
        Visilabs.CallAPI().impressionStory(mActionData.getReport().getImpression());

        if(mStories.getItems().size() > mStoryItemPosition + 1) {
            setStoryItem(mStories.getItems().get(++mStoryItemPosition));
        }
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

            // TODO : real control here:
            isCountDownTimer = false;
            if(isCountDownTimer) {
                mCountDownContainer.setVisibility(View.VISIBLE);
                mCountDownTimer.setBackgroundResource(R.drawable.rounded_corners_full);
                GradientDrawable gd = (GradientDrawable) mCountDownTimer.getBackground();
                gd.setColor(getResources().getColor(R.color.white));
                // TODO : real control here
                if(true) { // TODO : text top
                    mCountDownTopText.setVisibility(View.VISIBLE);
                    mCountDownBotText.setVisibility(View.GONE);

                    // TODO : real data here
                    mCountDownTopText.setText(("SAAT 16:00'DA PAYLAŞILACAK HİKAYEDEKİ İNDİRİM KODUNU BUL" +
                            ", ONU KULLANABİLECEK 25 ŞANSLI KİŞİDEN BİRİ OL!").replace("\\n", "\n"));
                    mCountDownTopText.setTextColor(Color.parseColor("#FFFFFF"));
                    mCountDownTopText.setTextSize(Float.parseFloat("9") + 16);
                    mCountDownTopText.setTypeface(Typeface.DEFAULT);
                } else {
                    mCountDownBotText.setVisibility(View.VISIBLE);
                    mCountDownTopText.setVisibility(View.GONE);

                    // TODO : real data here
                    mCountDownBotText.setText(("SAAT 16:00'DA PAYLAŞILACAK HİKAYEDEKİ İNDİRİM KODUNU BUL" +
                            ", ONU KULLANABİLECEK 25 ŞANSLI KİŞİDEN BİRİ OL!").replace("\\n", "\n"));
                    mCountDownBotText.setTextColor(Color.parseColor("#FFFFFF"));
                    mCountDownBotText.setTextSize(Float.parseFloat("9") + 16);
                    mCountDownBotText.setTypeface(Typeface.DEFAULT);
                }

                setTimerValues();
                //TODO check the format here and set the visibilities of the views accordingly
                //TODO: convert bigger part like week to smaller parts like day if necessary according to the format
                mWeekNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                GradientDrawable gdWeek = (GradientDrawable) mWeekNum.getBackground();
                gdWeek.setColor(Color.parseColor("#E5E4E2"));
                mWeekNum.setText(String.valueOf(mWeekNumber));
                mWeekNum.setTextColor(getResources().getColor(R.color.black));

                mDayNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                GradientDrawable gdDay = (GradientDrawable) mDayNum.getBackground();
                gdDay.setColor(Color.parseColor("#E5E4E2"));
                mDayNum.setText(String.valueOf(mDayNumber));
                mDayNum.setTextColor(getResources().getColor(R.color.black));

                mHourNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                GradientDrawable gdHour = (GradientDrawable) mHourNum.getBackground();
                gdHour.setColor(Color.parseColor("#E5E4E2"));
                mHourNum.setText(String.valueOf(mHourNumber));
                mHourNum.setTextColor(getResources().getColor(R.color.black));

                mMinuteNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                GradientDrawable gdMinute = (GradientDrawable) mMinuteNum.getBackground();
                gdMinute.setColor(Color.parseColor("#E5E4E2"));
                mMinuteNum.setText(String.valueOf(mMinuteNumber));
                mMinuteNum.setTextColor(getResources().getColor(R.color.black));

                mSecNum.setBackgroundResource(R.drawable.rounded_corners_full_small_edge);
                GradientDrawable gdSec = (GradientDrawable) mSecNum.getBackground();
                gdSec.setColor(Color.parseColor("#E5E4E2"));
                mSecNum.setText(String.valueOf(mSecondNumber));
                mSecNum.setTextColor(getResources().getColor(R.color.black));

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
        //TODO: When real data came, adjust here accordingly
        mWeekNumber = 3;
        mDayNumber = 5;
        mHourNumber = 17;
        mMinuteNumber = 0;
        mSecondNumber = 6;
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
        //TODO: Adjust the logic here for each format. For example, if there is no week field
        //in the format do not set day to max 6 below.
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.time_is_over), Toast.LENGTH_LONG).show();
            }
        });

        // TODO : real control here
        if(true) { // if it should be removed when it expires
            mCountDownContainer.setVisibility(View.GONE);
        }
    }
}