package com.visilabs.inApp.carousel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselRecyclerView extends RecyclerView {

    public CarouselRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
    }
}
