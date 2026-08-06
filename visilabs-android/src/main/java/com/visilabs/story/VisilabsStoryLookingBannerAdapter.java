package com.visilabs.story;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
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
import com.visilabs.util.AppUtils;
import com.visilabs.util.PersistentTargetManager;
import com.visilabs.util.VisilabsConstant;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsStoryLookingBannerAdapter extends RecyclerView.Adapter<VisilabsStoryLookingBannerAdapter.StoryHolder> {

    // visilabs_story_item.xml icindeki iv_story/civ_story boyutu
    private static final int STORY_IMAGE_SIZE_IN_DP = 72;

    Context mContext;
    RecyclerView mRecyclerView;
    StoryItemClickListener mStoryItemClickListener;
    VisilabsStoryLookingBannerResponse mStoryLookingBanner;
    String mExtendsProps;
    private boolean moveShownToEnd = false;

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

        String labelColor = extendedProps != null ? extendedProps.getStorylb_label_color() : null;
        if (labelColor != null && !labelColor.isEmpty()) {
            try {
                storyHolder.tvStoryName.setTextColor(Color.parseColor(labelColor));
            } catch (IllegalArgumentException e) {
                Log.w("StoryLookingBanner", "Invalid label color : " + labelColor);
            }
        }

        storyHolder.tvStoryName.setTypeface(AppUtils.getFontFamily(mContext,
                extendedProps != null ? extendedProps.getFont_family() : null,
                extendedProps != null ? extendedProps.getCustom_font_family_android() : null));

        String boxShadow = extendedProps != null ? extendedProps.getStorylb_img_boxShadow() : null;
        if (boxShadow == null || boxShadow.isEmpty()){
            storyHolder.flCircleShadow.setVisibility(View.VISIBLE);
        }

        String borderRadius = extendedProps != null ? extendedProps.getStorylb_img_borderRadius() : null;

        boolean isRectangle = extendedProps != null && extendedProps.getShape() != null
                && extendedProps.getShape().equalsIgnoreCase(VisilabsConstant.STORY_SHAPE_RECTANGLE);

        boolean isShown = moveShownToEnd ? shown : isItShown(position);

        if (isRectangle) {
            storyHolder.setShapeRectangle(isShown);
        } else if (borderRadius == null) {
            // Panelden deger gelmediginde eski varsayilan davranis: daire
            storyHolder.setCircleViewProperties(isShown);
        } else {
            float borderRadiusPercentage = AppUtils.parseStoryBorderRadiusPercentage(borderRadius);
            if (borderRadiusPercentage >= 50f) {
                storyHolder.setCircleViewProperties(isShown);
            } else {
                storyHolder.setRectangleViewProperties(AppUtils.getStoryCornerRadii(mContext,
                        borderRadiusPercentage, STORY_IMAGE_SIZE_IN_DP), isShown);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mStoryLookingBanner.getStory().get(0).getActiondata().getStories().size();
    }

    public void setStoryList(VisilabsStoryLookingBannerResponse storyLookingBanner, String extendsProps) {
        try {
            ExtendedProps extendedProps = new Gson().fromJson(new java.net.URI(storyLookingBanner.
                    getStory().get(0).getActiondata().getExtendedProps()).getPath(), ExtendedProps.class);
            moveShownToEnd = extendedProps.getMoveShownToEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (moveShownToEnd) {
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
            int borderColor = shown ? Color.rgb(127, 127, 127) : getBorderColor();
            ivStory.setVisibility(View.VISIBLE);
            civStory.setVisibility(View.GONE); // Ensure circle is hidden

            if (hasNoBoxShadow()){
                flRectangleShadow.setBackground(null);
            }

            // Reset size to default 72dp in case it was changed by setShapeRectangle
            float density = mContext.getResources().getDisplayMetrics().density;
            int size = (int) (STORY_IMAGE_SIZE_IN_DP * density);
            ViewGroup.LayoutParams params = ivStory.getLayoutParams();
            if (params.width != size || params.height != size) {
                params.width = size;
                params.height = size;
                ivStory.setLayoutParams(params);
            }

            // Adjust TextView width to match image width and increase font size
            ViewGroup.LayoutParams tvParams = tvStoryName.getLayoutParams();
            if (tvParams.width != size) {
                tvParams.width = size;
                tvStoryName.setLayoutParams(tvParams);
            }
            tvStoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(borderRadius);
            shape.setStroke(getBorderWidth() * 2, borderColor);
            ivStory.setBackground(shape);
        }

        private void setCircleViewProperties(boolean shown) {
            int borderColor = shown ? Color.rgb(127, 127, 127) : getBorderColor();
            if (hasNoBoxShadow()){
                flCircleShadow.setBackground(null);
            }

            civStory.setVisibility(View.VISIBLE);
            ivStory.setVisibility(View.GONE); // Ensure rectangle is hidden
            civStory.setBorderColor(borderColor);
            civStory.setBorderWidth(getBorderWidth() * 2);
        }

        private void setShapeRectangle(boolean shown) {
            int borderColor = shown ? Color.rgb(127, 127, 127) : getBorderColor();
            ivStory.setVisibility(View.VISIBLE);
            civStory.setVisibility(View.GONE);

            if (hasNoBoxShadow()){
                flRectangleShadow.setBackground(null);
            }

            // 9:16 aspect ratio logic
            // 9:16 aspect ratio logic
            // User requested a larger size similar to the banner in the screenshot.
            // Setting to approx 135dp width and 240dp height.
            float density = mContext.getResources().getDisplayMetrics().density;
            int size = (int) (72 * density); // Default size for resetting
            int width = (int) (135 * density);
            int height = (int) (240 * density);

            ViewGroup.LayoutParams params = ivStory.getLayoutParams();
            params.width = width;
            params.height = height;
            ivStory.setLayoutParams(params);

            ViewGroup.LayoutParams tvParams = tvStoryName.getLayoutParams();
            if (tvParams.width != width) {
                tvParams.width = width;
                tvStoryName.setLayoutParams(tvParams);
            }
            tvStoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            // Also update the container/shadow frame if needed, but ivStory change might be enough if frame wraps content
            // The layout xml says FrameLayout wrap_content, so it should be fine.

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
            shape.setStroke(getBorderWidth() * 2, borderColor);
            ivStory.setBackground(shape);
        }

        // Panelden eksik/bozuk gelen degerler yuzunden onBindViewHolder patlayip
        // gorselin hic cizilmemesine sebep olmasin diye guvenli okuyucular:
        private int getBorderColor() {
            String borderColorString = extendedProps != null ? extendedProps.getStorylb_img_borderColor() : null;
            if (borderColorString == null || borderColorString.isEmpty()) {
                return Color.parseColor("#161616");
            }
            try {
                return Color.parseColor(borderColorString);
            } catch (IllegalArgumentException e) {
                return Color.parseColor("#161616");
            }
        }

        private int getBorderWidth() {
            String borderWidthString = extendedProps != null ? extendedProps.getStorylb_img_borderWidth() : null;
            if (borderWidthString == null || borderWidthString.trim().isEmpty()) {
                return 0;
            }
            try {
                return Integer.parseInt(borderWidthString.trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        private boolean hasNoBoxShadow() {
            String boxShadow = extendedProps != null ? extendedProps.getStorylb_img_boxShadow() : null;
            return boxShadow == null || boxShadow.isEmpty();
        }
    }

    private boolean isItShown(int position) {
        boolean result = false;
        Map<String, List<String>> shownStoriesMap = PersistentTargetManager.with(mContext).getShownStories();

        if (shownStoriesMap.containsKey(mStoryLookingBanner.getStory().get(0).getActid())){
            List<String> shownTitles = shownStoriesMap.get(mStoryLookingBanner.getStory().get(0).getActid());

            if(shownTitles != null && !shownTitles.isEmpty()){
                result = shownTitles.contains(mStoryLookingBanner.getStory().get(0).getActiondata().getStories().get(position).getTitle());
            }
        }
        return result;
    }
}
