package com.visilabs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.graphics.Color;

import com.visilabs.android.R;

public class NpsWithNumbersView extends LinearLayout {

    private RatingGrid mRatingGrid;
    private final int mRateCount;
    private int[] mColors;

    public NpsWithNumbersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.nps_with_numbers, this);
        mRateCount = 10;
        mColors = new int[] {Color.GREEN};
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRatingGrid = (RatingGrid) findViewById(R.id.rating_grid);
    }

    public void setColors(int[] colors) {
        mColors = colors;
        mRatingGrid.init(mRateCount, mColors);
    }

    public int getSelectedRate() {
        return mRatingGrid.getSelectedRate();
    }

}