package com.visilabs.scratchToWin;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

public class ContainerScrollView extends ScrollView {

    private boolean mIsEnabled = true;

    public ContainerScrollView(Context context) {
        super(context);
    }

    public ContainerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContainerScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollingState(boolean state) {
        mIsEnabled = state;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsEnabled) {
            return super.onInterceptTouchEvent(ev);
        } else {
            mIsEnabled = true;
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
