package com.visilabs.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.visilabs.android.R;

public class RatingCell extends FrameLayout {

    public RatingCell(Context context) {
        super(context);
    }

    public RatingCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static RatingCell create(ViewGroup parent, LayoutInflater inflater, int rating, int[] cellColors) {
        int layoutId = R.layout.nps_with_numbers_cell;
        final RatingCell view = (RatingCell) inflater.inflate(layoutId, parent, false);
        TextView rateTextView = (TextView) view.findViewById(R.id.rate_text);
        rateTextView.setText(String.valueOf(rating));
        return view;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int size = Math.max(width, height);
        int widthSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
        setMeasuredDimension(widthSpec, heightSpec);
    }

}