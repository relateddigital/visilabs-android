package com.visilabs.story;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.story.model.skinbased.VisilabsSkinBased;

import com.visilabs.story.model.ExtendedProps;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.storylookingbanner.VisilabsStoryLookingBanner;
import com.visilabs.util.VisilabsConstant;

import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsSkinBasedAdapter extends RecyclerView.Adapter<VisilabsSkinBasedAdapter.StoryHolder> {

    Context context;

    StoryItemClickListener storyItemClickListener;

    VisilabsSkinBased visilabsSkinBased;

    String extendsProps;

    public VisilabsSkinBasedAdapter(Context context, StoryItemClickListener storyItemClickListener) {
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
        String storyName = visilabsSkinBased.getStory().get(0).getActiondata().getStories().get(position).getTitle();
        String storyImage = visilabsSkinBased.getStory().get(0).getActiondata().getStories().get(position).getThumbnail();
        storyHolder.tvStoryName.setText(storyName);

        Picasso.get().load(storyImage).into(storyHolder.ivStory);
  //      Picasso.get().load(storyImage).into(storyHolder.civStory);



        String extendedPropsEncoded = extendsProps;

        ExtendedProps extendedProps = null;

        try {
            extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded).getPath(), ExtendedProps.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        storyHolder.civStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyItemClickListener.storyItemClicked("");
                Intent intent = new Intent(context, PreviewActivity.class);
                context.startActivity(intent);
            }
        });

        storyHolder.ivStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyItemClickListener.storyItemClicked("");
                Intent intent = new Intent(context, PreviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", visilabsSkinBased.getStory().get(0).getActiondata().getStories().get(position).getItems());
                context.startActivity(intent);
            }
        });

        storyHolder.tvStoryName.setTextColor(Color.parseColor(extendedProps != null ? extendedProps.getStorylb_label_color() : null));

        String borderRadius = extendedProps != null ? extendedProps.getStorylb_img_borderRadius() : null;

        if (borderRadius != null) {
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
    }

    @Override
    public int getItemCount() {
        return visilabsSkinBased.getStory().get(0).getActiondata().getStories().size();
    }

    public void setStoryList(VisilabsSkinBased visilabsSkinBased, String extendsProps) {
        this.extendsProps = extendsProps;
        this.visilabsSkinBased = visilabsSkinBased;
    }

    public class StoryHolder extends RecyclerView.ViewHolder {

        public TextView tvStoryName;
        public CircleImageView civStory;
        public ImageView ivStory;
        public LinearLayout llStoryContainer;
        ExtendedProps extendedProps;
        FrameLayout frameLayout;

        public StoryHolder(final View itemView) {
            super(itemView);

            tvStoryName = itemView.findViewById(R.id.tv_story_name);
            civStory = itemView.findViewById(R.id.civ_story);
            ivStory = itemView.findViewById(R.id.iv_story);
            llStoryContainer = itemView.findViewById(R.id.ll_story);
            frameLayout = itemView.findViewById(R.id.fl);

            String extendedPropsEncoded = extendsProps;

            try {
                extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded).getPath(), ExtendedProps.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        private void setRectangleViewProperties(float[] borderRadius) {
            ivStory.setVisibility(View.VISIBLE);

            if (extendedProps.getStorylb_img_boxShadow().equals("")){
                frameLayout.setBackground(null);
            }
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(borderRadius);
            shape.setStroke( Integer.parseInt(extendedProps.getStorylb_img_borderWidth()) * 5, Color.parseColor(extendedProps.getStorylb_img_borderColor()));
            ivStory.setBackground(shape);
        }

        private void setCircleViewProperties() {
            if (extendedProps.getStorylb_img_boxShadow().equals("")){
                frameLayout.setBackground(null);
            }

            civStory.setVisibility(View.VISIBLE);
            civStory.setBorderColor(Color.parseColor(extendedProps.getStorylb_img_borderColor()));
            civStory.setBorderWidth(Integer.parseInt(extendedProps.getStorylb_img_borderWidth()));
        }
    }
}
