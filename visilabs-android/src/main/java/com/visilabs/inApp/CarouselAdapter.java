package com.visilabs.inApp;

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

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private static final String LOG_TAG = "InAppCarouselAdapter";
    private Context mContext;
    private InAppMessage mData;
    private CarouselFinishInterface mCarouselFinishInterface;

    CarouselAdapter(Context context, InAppMessage data, CarouselFinishInterface carouselFinishInterface) {
        mContext = context;
        mData = data;
        mCarouselFinishInterface = carouselFinishInterface;
    }

    @NonNull
    @Override
    public CarouselAdapter.CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.in_app_carousel_item, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        //TODO: change this dummy data with the real one.
        //TODO: görsel, başlık, body text ve button hepsi optional.
        //TODO: datada varsa göster yoksa invisible yap.
        ImageView itemImage = holder.itemView.findViewById(R.id.carousel_image);
        TextView itemTitle = holder.itemView.findViewById(R.id.carousel_title);
        TextView itemBodyText = holder.itemView.findViewById(R.id.carousel_body_text);
        Button itemButton = holder.itemView.findViewById(R.id.carousel_button);
        ImageButton itemCloseButton = holder.itemView.findViewById(R.id.carousel_close_button);

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

        switch (position) {
            case 0: {
                Picasso.get().
                        load("https://img.visilabs.net/in-app-message/uploaded_images/163_1100_154_20200603160304969.jpg")
                        .into(itemImage);

                itemTitle.setText("Carousel Item1 Title");
                itemTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                itemTitle.setTextSize(32);
                itemBodyText.setText("Carousel Item1 Text");
                itemBodyText.setTextColor(mContext.getResources().getColor(R.color.black));
                itemBodyText.setTextSize(16);
                itemButton.setText("Carousel Item1 Button");
                itemButton.setTextColor(mContext.getResources().getColor(R.color.yellow));
                itemButton.setTextSize(24);
                itemButton.setOnClickListener(new View.OnClickListener() {
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
                itemImage.setVisibility(View.GONE);
                itemTitle.setText("Carousel Item2 Title");
                itemTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                itemTitle.setTextSize(32);
                itemBodyText.setText("Carousel Item2 Text");
                itemBodyText.setTextColor(mContext.getResources().getColor(R.color.yellow));
                itemBodyText.setTextSize(16);
                itemButton.setText("Carousel Item2 Button");
                itemButton.setTextColor(mContext.getResources().getColor(R.color.blue));
                itemButton.setTextSize(24);
                itemButton.setOnClickListener(new View.OnClickListener() {
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
                        .into(itemImage);
                itemTitle.setText("Carousel Item3 Title");
                itemTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                itemTitle.setTextSize(32);
                itemBodyText.setText("Carousel Item3 Text");
                itemBodyText.setTextColor(mContext.getResources().getColor(R.color.yellow));
                itemBodyText.setTextSize(16);
                itemButton.setVisibility(View.GONE);
                break;
            }
            case 3: {
                itemImage.setVisibility(View.GONE);
                itemTitle.setText("Carousel Item4 Title");
                itemTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                itemTitle.setTextSize(32);
                itemBodyText.setVisibility(View.GONE);
                itemButton.setText("Carousel Item4 Button");
                itemButton.setTextColor(mContext.getResources().getColor(R.color.blue));
                itemButton.setTextSize(24);
                itemButton.setOnClickListener(new View.OnClickListener() {
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
                        .into(itemImage);
                itemTitle.setVisibility(View.GONE);
                itemBodyText.setText("Carousel Item5 Text");
                itemBodyText.setTextColor(mContext.getResources().getColor(R.color.yellow));
                itemBodyText.setTextSize(16);
                itemButton.setVisibility(View.GONE);
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

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        CarouselViewHolder(View view) {
            super(view);
        }
    }
}
