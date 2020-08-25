package com.visilabs.story;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.visilabs.android.R;
import com.visilabs.story.model.VisilabsStoryResponse;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisilabsStoryAdapter extends RecyclerView.Adapter<VisilabsStoryAdapter.StoryHolder> {

    Context context;

    VisilabsStoryResponse visilabsStoryResponse;

    public VisilabsStoryAdapter(Context context) {
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
    public void onBindViewHolder(StoryHolder storyHolder, int position) {
        String getName = visilabsStoryResponse.getStory()[0].getTitle();
        storyHolder.tvStoryName.setText(getName);
        storyHolder.tvStoryName.setTextColor(Color.parseColor(visilabsStoryResponse.getExtendedProps().getStorylb_label_color()));

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
        public CircleImageView circleImageView;
        public ImageView imageView;

        public StoryHolder(final View itemView) {
            super(itemView);

            tvStoryName = itemView.findViewById(R.id.tv_story_name);
            circleImageView = itemView.findViewById(R.id.civ_story);
            imageView = itemView.findViewById(R.id.iv_story);

            int borderRadius = Integer.parseInt(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderRadius());

            switch (borderRadius) {
                case 0:
                    setCircleView();
                    break;
                case 1:
                    float[] radii = new float[]{15, 15, 15, 15, 15, 15, 15, 15};
                    setImageView(radii);
                    break;

                case 2:
                    float[] radius = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                    setImageView(radius);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "item clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

        private void setImageView(float[] radii) {
            int borderWidth = Integer.parseInt(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderWidth());
            imageView.setVisibility(View.VISIBLE);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(radii);
            shape.setStroke(borderWidth, Color.parseColor(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderColor()));
            imageView.setBackground(shape);
        }

        private void setCircleView() {
            int borderWidth = Integer.parseInt(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderWidth());

            circleImageView.setVisibility(View.VISIBLE);
            circleImageView.setBorderColor(Color.parseColor(visilabsStoryResponse.getExtendedProps().getStorylb_img_borderColor()));
            circleImageView.setBorderWidth(borderWidth);
        }
    }
}
