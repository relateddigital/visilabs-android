package com.visilabs.story;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.visilabs.android.R;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsStoryAdapter extends RecyclerView.Adapter<VisilabsStoryAdapter.StoryHolder> {

    ArrayList<String> personNames = new ArrayList<>(Arrays.asList("Nishank", "Prasad", "Vinayak", "Sneha", "Shubham", "Rohan", "Sweety"));
    Context context;

    public VisilabsStoryAdapter(Context context) {
        this.context=context;
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.visilabs_story_item,parent,false);
        return new StoryHolder(view);
    }


    @Override
    public void onBindViewHolder(StoryHolder myViewHolder, int position) {
        String getName= personNames.get(position);
        myViewHolder.tvStoryName.setText(getName);
    }

    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class StoryHolder extends RecyclerView.ViewHolder {
        public TextView tvStoryName;

        public StoryHolder(final View itemView){
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tv_story_name);
            CircleImageView circleImageView = itemView.findViewById(R.id.civ_story);
            ImageView imageView  = itemView.findViewById(R.id.iv_story);
            GradientDrawable backgroundGradient = (GradientDrawable)imageView.getBackground();
            backgroundGradient.setColor(itemView.getContext().getResources().getColor(R.color.yellow));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), VisilabsStory.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
