package com.visilabs.story.action;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.visilabs.android.R;

final class PausableProgressBar extends FrameLayout {

    private static final int DEFAULT_PROGRESS_DURATION = 2000;

    private final View mFrontProgressView;
    private final View mMaxProgressView;
    private PausableScaleAnimation mAnimation;
    private long mDuration = DEFAULT_PROGRESS_DURATION;
    private Callback mCallback;

    interface Callback {
        void onStartProgress();
        void onFinishProgress();
    }

    public PausableProgressBar(Context context) {
        this(context, null);
        mAnimation = new PausableScaleAnimation(0, 1, 1, 1,
                Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, 0);
    }

    public PausableProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PausableProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.pausable_progress, this);
        mFrontProgressView = findViewById(R.id.front_progress);
        mMaxProgressView = findViewById(R.id.max_progress); // work around
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setCallback(@NonNull Callback callback) {
        mCallback = callback;
    }

    void setMax() {
        finishProgress(true);
    }

    void setMin() {
        finishProgress(false);
    }

    void setMinWithoutCallback() {
        mMaxProgressView.setBackgroundResource(R.color.progress_secondary);

        mMaxProgressView.setVisibility(VISIBLE);
        if (mAnimation != null) {
            mAnimation.setAnimationListener(null);
            mAnimation.cancel();
        }
    }

    void setMaxWithoutCallback() {
        mMaxProgressView.setBackgroundResource(R.color.progress_max_active);

        mMaxProgressView.setVisibility(VISIBLE);
        if (mAnimation != null) {
            mAnimation.setAnimationListener(null);
            mAnimation.cancel();
        }
    }

    private void finishProgress(boolean isMax) {
        if (isMax) mMaxProgressView.setBackgroundResource(R.color.progress_max_active);
        mMaxProgressView.setVisibility(isMax ? VISIBLE : GONE);
        if (mAnimation != null) {
            mAnimation.setAnimationListener(null);
            mAnimation.cancel();
            if (mCallback != null) {
                mCallback.onFinishProgress();
            }
        }
    }

    public void startProgress() {
        mMaxProgressView.setVisibility(GONE);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFrontProgressView.setVisibility(View.VISIBLE);
                if (mCallback != null) mCallback.onStartProgress();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mCallback != null) mCallback.onFinishProgress();
            }
        });
        mAnimation.setFillAfter(true);
        mFrontProgressView.startAnimation(mAnimation);
    }

    public void pauseProgress() {
        if (mAnimation != null) {
            mAnimation.pause();
        }
    }

    public void resumeProgress() {
        if (mAnimation != null) {
            mAnimation.resume();
        }
    }

    void clear() {
        if (mAnimation != null) {
            mAnimation.setAnimationListener(null);
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    private static class PausableScaleAnimation extends ScaleAnimation {

        private long mElapsedAtPause = 0;
        private boolean mPaused = false;

        PausableScaleAnimation(float fromX, float toX, float fromY,
                               float toY, int pivotXType, float pivotXValue, int pivotYType,
                               float pivotYValue) {
            super(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType,
                    pivotYValue);
        }

        @Override
        public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
            if (mPaused && mElapsedAtPause == 0) {
                mElapsedAtPause = currentTime - getStartTime();
            }
            if (mPaused) {
                setStartTime(currentTime - mElapsedAtPause);
            }
            return super.getTransformation(currentTime, outTransformation, scale);
        }

        /***
         * pause animation
         */
        void pause() {
            if (mPaused) return;
            mElapsedAtPause = 0;
            mPaused = true;
        }

        /***
         * resume animation
         */
        void resume() {
            mPaused = false;
        }
    }
}
