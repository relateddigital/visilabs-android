package com.visilabs.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.visilabs.android.R;
import com.visilabs.util.ColorUtils;

import java.util.HashMap;

public class RatingGrid extends GridView {

    private final RatingGrid.CellAdapter mAdapter = new CellAdapter();
    private int mRateCount;
    private int[] mColors;
    private int mSelectedRate;
    private HashMap<Integer, int[]> mCells = new HashMap<>();

    public RatingGrid(Context context) {
        super(context);
    }

    public RatingGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(int rateCount, int[] colors) {
        mRateCount = rateCount;
        mColors = colors;
        mCells = ColorUtils.calculateGradientColors(mRateCount, mColors);
        setNumColumns(mCells.size());
        setStretchMode(STRETCH_COLUMN_WIDTH);
        setHorizontalSpacing(8);
        validateAndUpdate();
    }

    public int getSelectedRate(){
        return mSelectedRate;
    }

    private void validateAndUpdate() {
        if (getAdapter() == null) {
            setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    private class CellAdapter extends BaseAdapter {
        private final LayoutInflater inflater;

        private CellAdapter() {
            inflater = LayoutInflater.from(getContext());
        }

        @Override public boolean isEnabled(int position) {
            return true;
        }

        @Override public int getCount() {
            return mCells.size();
        }

        @Override public Object getItem(int position) {
            return position + 1;
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            final RatingCell ratingCell;
            final int cellValue = position + 1;
            final int[] cellColors = mCells.get(cellValue);

            if (convertView == null) {
                ratingCell = RatingCell.create(parent, inflater, cellValue, cellColors);
            } else {
                ratingCell = (RatingCell)convertView;
            }

            GradientDrawable drawable = null;
            if(cellColors.length == 2) {
                drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, cellColors);
            } else {
                drawable = new GradientDrawable();
                drawable.setColor(cellColors[0]);
            }
            drawable.setShape(GradientDrawable.OVAL);

            if(cellValue == mSelectedRate) {
                drawable.setStroke(5, Color.BLACK);
                ratingCell.setBackground(drawable);
            } else {
                ratingCell.setBackground(drawable);
            }


            ratingCell.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedRate = cellValue;
                    validateAndUpdate();
                    invalidateViews();
                }
            });

            return ratingCell;
        }
    }
}
