package com.visilabs.story;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
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
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.story.model.storylookingbanners.ExtendedProps;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.storylookingbanners.Stories;
import com.visilabs.story.model.storylookingbanners.VisilabsStoryLookingBannerResponse;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.VisilabsConstant;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsStoryLookingBannerAdapter extends RecyclerView.Adapter<VisilabsStoryLookingBannerAdapter.StoryHolder> {

    Context mContext;
    RecyclerView mRecyclerView;
    StoryItemClickListener mStoryItemClickListener;
    VisilabsStoryLookingBannerResponse mStoryLookingBanner;
    String mExtendsProps;

    public VisilabsStoryLookingBannerAdapter(Context context, StoryItemClickListener storyItemClickListener) {
        mContext = context;
        mStoryItemClickListener = storyItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
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
        final String actionid = mStoryLookingBanner.getStory().get(0).getActid();
        final Stories story = mStoryLookingBanner.getStory().get(0).getActiondata().getStories().get(position);
        final String storyTitle = story.getTitle();
        final String storyImage = story.getSmallImg();
        final String storyLink = story.getLink();
        final boolean shown = story.getShown();
        storyHolder.tvStoryName.setText(storyTitle);

        if(!storyImage.equals("")) {
            Picasso.get().load(storyImage).fit().into(storyHolder.ivStory);
            Picasso.get().load(storyImage).fit().into(storyHolder.civStory);
        }

        String extendedPropsEncoded = mExtendsProps;

        storyHolder.llStoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storyLink = mStoryLookingBanner.getStory().get(0).getActiondata().getStories().get(position).getLink();
                Visilabs.CallAPI().trackStoryClick(mStoryLookingBanner.getStory().get(0).getActiondata().getReport().getClick());

                Log.i("StoryActivityShows ", actionid + " : " + storyTitle);
                PersistentTargetManager.with(mContext).saveShownStory(actionid, storyTitle);
                setStoryList(mStoryLookingBanner, mExtendsProps);
                mRecyclerView.getAdapter().notifyDataSetChanged();
              
                if (mStoryItemClickListener != null) {
                    mStoryItemClickListener.storyItemClicked(storyLink);
                }
            }
        });

        ExtendedProps extendedProps = null;

        try {
             extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded).getPath(), ExtendedProps.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        storyHolder.tvStoryName.setTextColor(Color.parseColor(extendedProps !=
                null ? extendedProps.getStorylb_label_color() : null));

        assert extendedProps != null;
        if (extendedProps.getStorylb_img_boxShadow().equals("")){
            storyHolder.flCircleShadow.setVisibility(View.VISIBLE);
        }
        storyHolder.tvStoryName.setTextColor(Color.parseColor(extendedProps.getStorylb_label_color()));

        String borderRadius = extendedProps.getStorylb_img_borderRadius();

        if (borderRadius != null) {
            switch (borderRadius) {
                case VisilabsConstant.STORY_CIRCLE:
                    storyHolder.setCircleViewProperties(shown);
                    break;

                case VisilabsConstant.STORY_ROUNDED_RECTANGLE:
                    float[] roundedRectangleBorderRadius = new float[]{15, 15, 15, 15, 15, 15, 15, 15};
                    storyHolder.setRectangleViewProperties(roundedRectangleBorderRadius, shown);
                    break;

                case VisilabsConstant.STORY_RECTANGLE:
                    float[] rectangleBorderRadius = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                    storyHolder.setRectangleViewProperties(rectangleBorderRadius, shown);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mStoryLookingBanner.getStory().get(0).getActiondata().getStories().size();
    }

    public void setStoryList(VisilabsStoryLookingBannerResponse storyLookingBanner, String extendsProps) {
        Map<String, List<String>> shownStoriesMap = PersistentTargetManager.with(mContext).getShownStories();

        if (shownStoriesMap.containsKey(storyLookingBanner.getStory().get(0).getActid())){
            List<String> shownTitles = shownStoriesMap.get(storyLookingBanner.getStory().get(0).getActid());
            List<Stories> notShownStories = new ArrayList<>();
            List<Stories> shownStories = new ArrayList<>();

            if(shownTitles != null && !shownTitles.isEmpty()){
                for(Stories s : storyLookingBanner.getStory().get(0).getActiondata().getStories()) {
                    if(shownTitles.contains(s.getTitle())) {
                        s.setShown(true);
                        shownStories.add(s);
                    } else {
                        notShownStories.add(s);
                    }
                }
                notShownStories.addAll(shownStories);
                storyLookingBanner.getStory().get(0).getActiondata().setStories(notShownStories);
            }
        }

        mExtendsProps = extendsProps;
        mStoryLookingBanner = storyLookingBanner;
    }

    public class StoryHolder extends RecyclerView.ViewHolder {
        public TextView tvStoryName;
        public CircleImageView civStory;
        public ImageView ivStory;
        public LinearLayout llStoryContainer;
        ExtendedProps extendedProps;
        FrameLayout flCircleShadow, flRectangleShadow;

        public StoryHolder(final View itemView) {
            super(itemView);

            tvStoryName = itemView.findViewById(R.id.tv_story_name);
            civStory = itemView.findViewById(R.id.civ_story);
            ivStory = itemView.findViewById(R.id.iv_story);
            llStoryContainer = itemView.findViewById(R.id.ll_story);
            flCircleShadow = itemView.findViewById(R.id.fl_circle);
            flRectangleShadow = itemView.findViewById(R.id.fl_rect);

            String extendedPropsEncoded = mExtendsProps;

            try {
                extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded).getPath(),
                        ExtendedProps.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        private void setRectangleViewProperties(float[] borderRadius, boolean shown) {
            int borderColor = shown ? Color.rgb(127, 127, 127) : Color.parseColor(extendedProps.getStorylb_img_borderColor());
            ivStory.setVisibility(View.VISIBLE);

            if (extendedProps.getStorylb_img_boxShadow().equals("")){
                flRectangleShadow.setBackground(null);
            }
            ivStory.setVisibility(View.VISIBLE);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(borderRadius);
            shape.setStroke( Integer.parseInt(extendedProps.getStorylb_img_borderWidth()) * 2, borderColor);
            ivStory.setBackground(shape);
        }

        private void setCircleViewProperties(boolean shown) {
            int borderColor = shown ? Color.rgb(127, 127, 127) : Color.parseColor(extendedProps.getStorylb_img_borderColor());
            if (extendedProps.getStorylb_img_boxShadow().equals("")){
                flCircleShadow.setBackground(null);
            }

            civStory.setVisibility(View.VISIBLE);
            civStory.setBorderColor(borderColor);
            civStory.setBorderWidth(Integer.parseInt(extendedProps.getStorylb_img_borderWidth()) * 2);
        }
    }
}
