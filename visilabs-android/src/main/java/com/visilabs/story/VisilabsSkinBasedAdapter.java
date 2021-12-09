package com.visilabs.story;

import android.content.Context;
import android.content.Intent;
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
import com.visilabs.android.R;
import com.visilabs.story.model.StoryItemClickListener;
import com.visilabs.story.model.skinbased.Actiondata;
import com.visilabs.story.model.skinbased.Stories;
import com.visilabs.story.model.skinbased.VisilabsSkinBasedResponse;
import com.visilabs.story.model.skinbased.ExtendedProps;
import com.visilabs.util.AppUtils;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.VisilabsConstant;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsSkinBasedAdapter extends RecyclerView.Adapter<VisilabsSkinBasedAdapter.StoryHolder> {

    Context mContext;
    RecyclerView mRecyclerView;
    VisilabsSkinBasedResponse mVisilabsSkinBasedResponse;
    String mExtendsProps;
    private boolean isFirstRun = true;
    private boolean moveShownToEnd = false;

    public VisilabsSkinBasedAdapter(Context context) {
        mContext = context;
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isFirstRun) {
            cacheImagesBeforeDisplaying();
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.visilabs_story_item, parent, false);
        return new StoryHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(StoryHolder storyHolder, final int position) {
        final Stories story = mVisilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().get(position);
        final String storyTitle = story.getTitle();
        final String storyImage = story.getThumbnail();
        final boolean shown = story.getShown();
        storyHolder.tvStoryName.setText(storyTitle);

        if(!storyImage.equals("")) {
            Picasso.get().load(storyImage).fit().into(storyHolder.ivStory);
            Picasso.get().load(storyImage).fit().into(storyHolder.civStory);
        }

        String extendedPropsEncoded = mExtendsProps;

        ExtendedProps extendedProps = null;

        try {
            extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded).getPath(), ExtendedProps.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(extendedProps != null && extendedProps.getStoryz_label_color() != null) {
            storyHolder.tvStoryName.setTextColor(Color.parseColor(extendedProps.getStoryz_label_color()));
        }

        storyHolder.tvStoryName.setTypeface(AppUtils.getFontFamily(mContext,
                extendedProps != null ? extendedProps.getFont_family() : null,
                extendedProps != null ? extendedProps.getCustom_font_family_android() : null));

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
                    if(moveShownToEnd) {
                        storyHolder.setCircleViewProperties(shown);
                    } else {
                        storyHolder.setCircleViewProperties(isItShown(position));
                    }
                    break;

                case VisilabsConstant.STORY_ROUNDED_RECTANGLE:
                    float[] roundedRectangleBorderRadius = new float[]{15, 15, 15, 15, 15, 15, 15, 15};
                    if(moveShownToEnd) {
                        storyHolder.setRectangleViewProperties(roundedRectangleBorderRadius, shown);
                    } else {
                        storyHolder.setRectangleViewProperties(roundedRectangleBorderRadius, isItShown(position));
                    }
                    break;

                case VisilabsConstant.STORY_RECTANGLE:
                    float[] rectangleBorderRadius = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                    if(moveShownToEnd) {
                        storyHolder.setRectangleViewProperties(rectangleBorderRadius, shown);
                    } else {
                        storyHolder.setRectangleViewProperties(rectangleBorderRadius, isItShown(position));
                    }
                    break;

                default:
                    storyHolder.setCircleViewProperties(shown);
                    break;
            }
        }
    }

    private void clickEvent(int position) {

        if (mVisilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().get(position).getItems().size() != 0) {

            StoryActivity story = new StoryActivity();
            StoryActivity.setRecyclerView(mRecyclerView);
            StoryActivity.setVisilabsSkinBasedAdapter(this);
            Intent intent = new Intent(mContext, story.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
            intent.putExtra(VisilabsConstant.STORY_POSITION, position);
            intent.putExtra(VisilabsConstant.STORY_ITEM_POSITION, 0);
            intent.putExtra(VisilabsConstant.ACTION_DATA, mVisilabsSkinBasedResponse.getStory().get(0).getActiondata());
            intent.putExtra(VisilabsConstant.ACTION_ID, mVisilabsSkinBasedResponse.getStory().get(0).getActid());
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mVisilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().size();
    }

    public void setStoryList(VisilabsSkinBasedResponse visilabsSkinBasedResponse, String extendsProps) {
        try {
            ExtendedProps extendedProps = new Gson().fromJson(new java.net.URI(visilabsSkinBasedResponse
                    .getStory().get(0).getActiondata().getExtendedProps()).getPath(), ExtendedProps.class);
            moveShownToEnd = extendedProps.getMoveShownToEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (moveShownToEnd) {
            Map<String, List<String>> shownStoriesMap = PersistentTargetManager.with(mContext).getShownStories();

            if (shownStoriesMap.containsKey(visilabsSkinBasedResponse.getStory().get(0).getActid())){
                List<String> shownTitles = shownStoriesMap.get(visilabsSkinBasedResponse.getStory().get(0).getActid());
                List<Stories> notShownStories = new ArrayList<>();
                List<Stories> shownStories = new ArrayList<>();

                if(shownTitles != null && !shownTitles.isEmpty()){
                    for(Stories s : visilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories()) {
                        if(shownTitles.contains(s.getTitle())) {
                            s.setShown(true);
                            shownStories.add(s);
                        } else {
                            notShownStories.add(s);
                        }
                    }
                    notShownStories.addAll(shownStories);
                    visilabsSkinBasedResponse.getStory().get(0).getActiondata().setStories(notShownStories);
                }
            }
        }

        mExtendsProps = extendsProps;
        mVisilabsSkinBasedResponse = visilabsSkinBasedResponse;
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

            String extendedPropsEncoded = mExtendsProps;

            try {
                extendedProps = new Gson().fromJson(new java.net.URI(extendedPropsEncoded)
                        .getPath(), ExtendedProps.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        private void setRectangleViewProperties(float[] borderRadius, boolean shown) {
            ivStory.setVisibility(View.VISIBLE);

            String borderColorString = extendedProps.getStoryz_img_borderColor();
            if (borderColorString.equals("")){
                borderColorString = "#161616";
            }
            int borderColor = shown ? Color.rgb(127, 127, 127) : Color.parseColor(borderColorString);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(borderRadius);
            shape.setStroke(3, borderColor);
            ivStory.setBackground(shape);
        }

        private void setCircleViewProperties(boolean shown) {
            civStory.setVisibility(View.VISIBLE);

            String borderColorString = extendedProps.getStoryz_img_borderColor();
            if (borderColorString.equals("")){
                borderColorString = "#161616";
            }

            int borderColor = shown ? Color.rgb(127, 127, 127) : Color.parseColor(borderColorString);
            civStory.setBorderColor(borderColor);
            civStory.setBorderWidth(3);
        }
    }

    private void cacheImagesBeforeDisplaying() {
        isFirstRun = false;
        Actiondata actiondata = mVisilabsSkinBasedResponse.getStory().get(0).getActiondata();
        for (int i = 0; i < actiondata.getStories().size(); i++) {
            for (int j = 0; j < actiondata.getStories().get(i).getItems().size(); j++) {
                if (actiondata.getStories().get(i).getItems().get(j).getFileType().equals(VisilabsConstant.STORY_PHOTO_KEY)) {
                    if (!actiondata.getStories().get(i).getItems().get(j).getFileSrc().equals("")) {
                        try {
                            Picasso.get().load(actiondata.getStories().get(i).getItems().get(j).getFileSrc()).fetch();
                        } catch (Exception e) {
                            Log.w("Story Activity", "URL for the image is empty!");
                        }
                    }
                }
            }
        }
    }

    private boolean isItShown(int position) {
        boolean result = false;
        Map<String, List<String>> shownStoriesMap = PersistentTargetManager.with(mContext).getShownStories();

        if (shownStoriesMap.containsKey(mVisilabsSkinBasedResponse.getStory().get(0).getActid())){
            List<String> shownTitles = shownStoriesMap.get(mVisilabsSkinBasedResponse.getStory().get(0).getActid());

            if(shownTitles != null && !shownTitles.isEmpty()){
                result = shownTitles.contains(mVisilabsSkinBasedResponse.getStory().get(0).getActiondata().getStories().get(position).getTitle());
            }
        }
        return result;
    }
}
