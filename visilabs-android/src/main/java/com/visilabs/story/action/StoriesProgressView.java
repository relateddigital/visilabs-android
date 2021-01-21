package com.visilabs.story.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.visilabs.android.R;

import java.util.ArrayList;
import java.util.List;

public class StoriesProgressView extends LinearLayout {

    private static final String TAG = StoriesProgressView.class.getSimpleName();

    private final LayoutParams mProgressBarLayoutParam = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
    private final LayoutParams mSpaceLayoutParam = new LayoutParams(5, LayoutParams.WRAP_CONTENT);
    private final List<PausableProgressBar> mProgressBars = new ArrayList<>();
    private int mStoriesCount = -1;
    /**
     * pointer of running animation
     */
    private int mCurrent = -1;
    private StoriesListener mStoriesListener;
    boolean mIsComplete;
    private boolean mIsSkipStart;
    private boolean mIsReverseStart;

    public interface StoriesListener {
        void onNext();

        void onPrev();

        void onComplete();
    }

    public StoriesProgressView(Context context) {
        this(context, null);
    }

    public StoriesProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StoriesProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StoriesProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoriesProgressView);
        mStoriesCount = typedArray.getInt(R.styleable.StoriesProgressView_progressCount, 0);
        typedArray.recycle();
        bindViews();
    }

    private void bindViews() {
        mProgressBars.clear();
        removeAllViews();

        for (int i = 0; i < mStoriesCount; i++) {
            final PausableProgressBar p = createProgressBar();
            mProgressBars.add(p);
            addView(p);
            if ((i + 1) < mStoriesCount) {
                addView(createSpace());
            }
        }
    }

    private PausableProgressBar createProgressBar() {
        PausableProgressBar p = new PausableProgressBar(getContext());
        p.setLayoutParams(mProgressBarLayoutParam);
        return p;
    }

    private View createSpace() {
        View v = new View(getContext());
        v.setLayoutParams(mSpaceLayoutParam);
        return v;
    }

    /**
     * Set story count and create views
     *
     * @param storiesCount story count
     */
    public void setStoriesCount(int storiesCount) {
        mStoriesCount = storiesCount;
        bindViews();
    }

    /**
     * Set storiesListener
     *
     * @param storiesListener StoriesListener
     */
    public void setStoriesListener(StoriesListener storiesListener) {
        mStoriesListener = storiesListener;
    }

    /**
     * Skip current story
     */
    public void skip() {
        if (mIsSkipStart || mIsReverseStart) return;
        if (mIsComplete) return;
        if (mCurrent < 0) return;
        PausableProgressBar p = mProgressBars.get(mCurrent);
        mIsSkipStart = true;
        p.setMax();
    }

    /**
     * Reverse current story
     */
    public void reverse() {
        if (mIsSkipStart || mIsReverseStart) return;
        if (mIsComplete) return;
        if (mCurrent < 0) return;
        PausableProgressBar p = mProgressBars.get(mCurrent);
        mIsReverseStart = true;
        p.setMin();
    }

    /**
     * Set a story's duration
     *
     * @param duration millisecond
     */
    public void setStoryDuration(long duration) {
        for (int i = 0; i < mProgressBars.size(); i++) {
            mProgressBars.get(i).setDuration(duration);
            mProgressBars.get(i).setCallback(callback(i));
        }
    }

    /**
     * Set stories count and each story duration
     *
     * @param durations milli
     */
    public void setStoriesCountWithDurations(@NonNull long[] durations) {
        mStoriesCount = durations.length;
        bindViews();
        for (int i = 0; i < mProgressBars.size(); i++) {
            mProgressBars.get(i).setDuration(durations[i]);
            mProgressBars.get(i).setCallback(callback(i));
        }
    }

    private PausableProgressBar.Callback callback(final int index) {
        return new PausableProgressBar.Callback() {
            @Override
            public void onStartProgress() {
                mCurrent = index;
            }

            @Override
            public void onFinishProgress() {
                if (mIsReverseStart) {
                    if (mStoriesListener != null) mStoriesListener.onPrev();
                    if (0 <= (mCurrent - 1)) {
                        PausableProgressBar p = mProgressBars.get(mCurrent - 1);
                        p.setMinWithoutCallback();
                        mProgressBars.get(--mCurrent).startProgress();
                    } else {
                        mProgressBars.get(mCurrent).startProgress();
                    }
                    mIsReverseStart = false;
                    return;
                }
                int next = mCurrent + 1;
                if (next <= (mProgressBars.size() - 1)) {
                    if (mStoriesListener != null) mStoriesListener.onNext();
                    mProgressBars.get(next).startProgress();
                } else {
                    mIsComplete = true;
                    if (mStoriesListener != null) mStoriesListener.onComplete();
                }
                mIsSkipStart = false;
            }
        };
    }

    /**
     * Start progress animation
     */
    public void startStories() {
        mProgressBars.get(0).startProgress();
    }

    /**
     * Start progress animation from specific progress
     */
    public void startStories(int from) {
        for (int i = 0; i < from; i++) {
            mProgressBars.get(i).setMaxWithoutCallback();
        }
        mProgressBars.get(from).startProgress();
    }

    /**
     * Need to call when Activity or Fragment destroy
     */
    public void destroy() {
        for (PausableProgressBar p : mProgressBars) {
            p.clear();
        }
    }

    /**
     * Pause story
     */
    public void pause() {
        if (mCurrent < 0) return;
        mProgressBars.get(mCurrent).pauseProgress();
    }

    /**
     * Resume story
     */
    public void resume() {
        if (mCurrent < 0) return;
        mProgressBars.get(mCurrent).resumeProgress();
    }
}
