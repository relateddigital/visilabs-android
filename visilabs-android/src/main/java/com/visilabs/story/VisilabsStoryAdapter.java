package com.visilabs.story;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.visilabs.android.R;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.VisilabsStoryResponse;
import com.visilabs.util.VisilabsConstant;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsStoryAdapter extends RecyclerView.Adapter<VisilabsStoryAdapter.StoryHolder> {

    Context context;

    VisilabsStoryResponse visilabsStoryResponse;

    StoryItemClickListener storyItemClickListener;

    public VisilabsStoryAdapter(Context context, StoryItemClickListener storyItemClickListener) {
        this.context = context;
        this.storyItemClickListener = storyItemClickListener;
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.visilabs_story_item, parent, false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryHolder storyHolder, final int position) {
        String getName = visilabsStoryResponse.getStory()[0].getTitle();
        storyHolder.tvStoryName.setText(getName);
        storyHolder.tvStoryName.setTextColor(Color.parseColor(visilabsStoryResponse.getExtendedProps().getStorylb_label_color()));

        storyHolder.rlStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyItemClickListener.storyItemClicked(visilabsStoryResponse.getStory()[0].getLink() + " test");
            }
        });

        String borderRadius = visilabsStoryResponse.getExtendedProps().getStorylb_img_borderRadius();

        switch (borderRadius) {
            case VisilabsConstant.STORY_CIRCLE:
                storyHolder.setCircleViewProperties();
                break;

            case VisilabsConstant.STORY_ROUNDED_RECTANGLE:
                float[] roundedRectangleBorderRadius = new float[]{15, 15, 15, 15, 15, 15, 15, 15};
                storyHolder.setRectangleViewProperties(roundedRectangleBorderRadius);
                break;

            case VisilabsConstant.STORY_RECTANGLE:
                float[] rectangleBorderRadius = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                storyHolder.setRectangleViewProperties(rectangleBorderRadius);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public void setStoryItem(VisilabsStoryResponse story) {
        this.visilabsStoryResponse = story;
    }

    public class StoryHolder extends RecyclerView.ViewHolder {
        public TextView tvStoryName;
        public CircleImageView civStory;
        public ImageView ivStory;
        public RelativeLayout rlStory;

        public StoryHolder(final View itemView) {
            super(itemView);

            tvStoryName = itemView.findViewById(R.id.tv_story_name);
            civStory = itemView.findViewById(R.id.civ_story);
            ivStory = itemView.findViewById(R.id.iv_story);
            rlStory = itemView.findViewById(R.id.rl_story);
        }

        private void setRectangleViewProperties(float[] borderRadius) {
            ivStory.setVisibility(View.VISIBLE);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(borderRadius);
            shape.setStroke( Integer.parseInt(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderWidth()), Color.parseColor(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderColor()));
            ivStory.setBackground(shape);
        }

        private void setCircleViewProperties() {
            civStory.setVisibility(View.VISIBLE);
            civStory.setBorderColor(Color.parseColor(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderColor()));
            civStory.setBorderWidth(Integer.parseInt(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderWidth()));
        }
    }
}
