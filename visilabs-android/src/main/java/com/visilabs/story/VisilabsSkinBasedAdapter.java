package com.visilabs.story;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.skinbased.VisilabsSkinBasedResponse;

import com.visilabs.story.model.skinbased.ExtendedProps;
import com.visilabs.util.VisilabsConstant;

import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsSkinBasedAdapter extends RecyclerView.Adapter<VisilabsSkinBasedAdapter.StoryHolder> {

    Context context;


    VisilabsSkinBasedResponse visilabsSkinBasedResponse;

    String extendsProps;

    public VisilabsSkinBasedAdapter(Context context) {
        this.context = context;
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
        String storyName = visilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().get(position).getTitle();
        String storyImage = visilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().get(position).getThumbnail();
        storyHolder.tvStoryName.setText(storyName);

        Picasso.get().load(storyImage).fit().into(storyHolder.ivStory);

        Picasso.get().load(storyImage).fit().into(storyHolder.civStory);

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
                clickEvent(position);
            }
        });

        storyHolder.ivStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent(position);
            }
        });


        String borderRadius = extendedProps != null ? extendedProps.getStoryz_img_borderRadius() : null;

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

                default:
                    storyHolder.setCircleViewProperties();
                    break;
            }
        }
    }

    private void clickEvent(int position) {

        if (visilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().get(position).getItems().size() != 0) {

            StoryActivity story = new StoryActivity();
            Intent intent = new Intent(context, story.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
            intent.putExtra(VisilabsConstant.STORY_POSITION, position);
            intent.putExtra(VisilabsConstant.STORY_ITEM_POSITION, 0);
            intent.putExtra(VisilabsConstant.ACTION_DATA, visilabsSkinBasedResponse.getStory().get(0).getActiondata());
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return visilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().size();
    }

    public void setStoryList(VisilabsSkinBasedResponse visilabsSkinBasedResponse, String extendsProps) {
        this.extendsProps = extendsProps;
        this.visilabsSkinBasedResponse = visilabsSkinBasedResponse;
    }

    public void setStoryListener(StoryItemClickListener storyItemClickListener) {
        StoryActivity.setStoryItemClickListener(storyItemClickListener);

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
            frameLayout = itemView.findViewById(R.id.fl_circle);

            String extendedPropsEncoded = extendsProps;

            try {
                extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded).getPath(), ExtendedProps.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        private void setRectangleViewProperties(float[] borderRadius) {
            ivStory.setVisibility(View.VISIBLE);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(borderRadius);
            shape.setStroke(3, Color.parseColor(extendedProps.getStoryz_img_borderColor()));
            ivStory.setBackground(shape);
        }

        private void setCircleViewProperties() {
            civStory.setVisibility(View.VISIBLE);
            civStory.setBorderColor(Color.parseColor(extendedProps.getStoryz_img_borderColor()));
            civStory.setBorderWidth(3);
        }
    }
}
