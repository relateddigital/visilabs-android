package com.visilabs.story;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class GestureListener extends GestureDetector.SimpleOnGestureListener  {
    long pressTime = 0L;
    long limit = 500L;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    public interface Yon {

        void onSwipeRight();

        void onSwipeLeft();

        void onSwipeTop();

        void onSwipeBottom();

    }

    Yon yon;

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }


}
