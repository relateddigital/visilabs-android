package com.visilabs.inApp.carousel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.inApp.InAppMessage;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private static final String LOG_TAG = "InAppCarouselAdapter";
    private Context mContext;
    private InAppMessage mData;
    private CarouselFinishInterface mCarouselFinishInterface;

    public CarouselAdapter(Context context, InAppMessage data, CarouselFinishInterface carouselFinishInterface) {
        mContext = context;
        mData = data;
        mCarouselFinishInterface = carouselFinishInterface;
    }

    @NonNull
    @Override
    public CarouselAdapter.CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.in_app_carousel_item, parent, false);
        return new CarouselViewHolder(view, mCarouselFinishInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        //TODO: change this dummy data with the real one.
        //TODO: görsel, başlık, body text ve button hepsi optional.
        //TODO: datada varsa göster yoksa invisible yap.

        switch (position) {
            case 0: {
                Picasso.get().
                        load("https://img.visilabs.net/in-app-message/uploaded_images/163_1100_154_20200603160304969.jpg")
                        .into(holder.itemImage);

                holder.itemTitle.setText("Carousel Item1 Title");
                holder.itemTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                holder.itemTitle.setTextSize(32);
                holder.itemBodyText.setText("Carousel Item1 Text");
                holder.itemBodyText.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.itemBodyText.setTextSize(16);
                holder.itemButton.setText("Carousel Item1 Button");
                holder.itemButton.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.itemButton.setTextSize(24);
                holder.itemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
                            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "The link is not formatted properly!");
                        }
                    }
                });
                break;
            }
            case 1: {
                holder.itemImage.setVisibility(View.GONE);
                holder.itemTitle.setText("Carousel Item2 Title");
                holder.itemTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.itemTitle.setTextSize(32);
                holder.itemBodyText.setText("Carousel Item2 Text");
                holder.itemBodyText.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.itemBodyText.setTextSize(16);
                holder.itemButton.setText("Carousel Item2 Button");
                holder.itemButton.setTextColor(mContext.getResources().getColor(R.color.blue));
                holder.itemButton.setTextSize(24);
                holder.itemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
                            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "The link is not formatted properly!");
                        }
                    }
                });
                break;
            }
            case 2: {
                Picasso.get().
                        load("https://img.visilabs.net/in-app-message/uploaded_images/163_1100_411_20210121113801841.jpg")
                        .into(holder.itemImage);
                holder.itemTitle.setText("Carousel Item3 Title");
                holder.itemTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                holder.itemTitle.setTextSize(32);
                holder.itemBodyText.setText("Carousel Item3 Text");
                holder.itemBodyText.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.itemBodyText.setTextSize(16);
                holder.itemButton.setVisibility(View.GONE);
                break;
            }
            case 3: {
                holder.itemImage.setVisibility(View.GONE);
                holder.itemTitle.setText("Carousel Item4 Title");
                holder.itemTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.itemTitle.setTextSize(32);
                holder.itemBodyText.setVisibility(View.GONE);
                holder.itemButton.setText("Carousel Item4 Button");
                holder.itemButton.setTextColor(mContext.getResources().getColor(R.color.blue));
                holder.itemButton.setTextSize(24);
                holder.itemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
                            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "The link is not formatted properly!");
                        }
                    }
                });
                break;
            }
            case 4: {
                Picasso.get().
                        load("https://e7.pngegg.com/pngimages/994/882/png-clipart-new-super-mario-bros-2-new-super-mario-bros-2-mario-luigi-superstar-saga-mario-heroes-super-mario-bros.png")
                        .into(holder.itemImage);
                holder.itemTitle.setVisibility(View.GONE);
                holder.itemBodyText.setText("Carousel Item5 Text");
                holder.itemBodyText.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.itemBodyText.setTextSize(16);
                holder.itemButton.setVisibility(View.GONE);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        //TODO: return the real item count here
        return 5;
    }

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemTitle;
        private final TextView itemBodyText;
        private final Button itemButton;
        private final ImageButton itemCloseButton;
        private final CarouselFinishInterface mCarouselFinishInterface;
        CarouselViewHolder(View view, CarouselFinishInterface carouselFinishInterface) {
            super(view);
            mCarouselFinishInterface = carouselFinishInterface;
            itemImage = view.findViewById(R.id.carousel_image);
            itemTitle = view.findViewById(R.id.carousel_title);
            itemBodyText = view.findViewById(R.id.carousel_body_text);
            itemButton = view.findViewById(R.id.carousel_button);
            itemCloseButton = view.findViewById(R.id.carousel_close_button);

            itemImage.setVisibility(View.VISIBLE);
            itemTitle.setVisibility(View.VISIBLE);
            itemBodyText.setVisibility(View.VISIBLE);
            itemButton.setVisibility(View.VISIBLE);
            itemCloseButton.setVisibility(View.VISIBLE);

            itemCloseButton.setBackgroundResource(getCloseIcon());
            itemCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCarouselFinishInterface.onFinish();
                }
            });
        }

        private int getCloseIcon() {
            //TODO: change this when the real data comes
        /*switch (mData.getActionData().getCloseButtonColor()) {
            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }*/
            return R.drawable.ic_close_black_24dp;
        }
    }
}
