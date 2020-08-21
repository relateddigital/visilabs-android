package com.visilabs.story;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VisilabsRecyclerView extends RecyclerView {

    public VisilabsRecyclerView(@NonNull Context context) {
        super(context);
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
    }
}
