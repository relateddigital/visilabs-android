package com.visilabs.inApp.bannercarousel;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.inApp.appBannerModel.AppBanner;
import com.visilabs.mailSub.Report;
import com.visilabs.util.VisilabsConstant;

import java.util.HashMap;
import java.util.Objects;



public class BannerCarouselAdapter extends RecyclerView.Adapter<BannerCarouselAdapter.BannerHolder> {
    private final Context mContext;
    private final BannerItemClickListener mBannerItemClickListener;
    private RecyclerView mRecyclerView;
    private boolean isSwipe = true;
    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private int mPosition = 0;
    private boolean isScrolling = false;
    private AppBanner mAppBanner;
    String clickedUrl = "" ;

    public BannerCarouselAdapter(Context context, BannerItemClickListener bannerItemClickListener) {
        mContext = context;
        mBannerItemClickListener = bannerItemClickListener;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if (isSwipe) {
            view = inflater.inflate(R.layout.banner_carousel_swipe_list_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.banner_carousel_slide_list_item, parent, false);
        }
        return new BannerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder bannerHolder, int position) {
        if (isSwipe) {
            Glide.with(mContext)
                    .asBitmap()
                    .transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(0f, 0f, 0f, 0f)))
                    .load(mAppBanner.getActionData().getAppBanners().get(position).getImage())
                    .into(bannerHolder.swipeImageView);

            bannerHolder.dotIndicator.removeAllViews();

            for (int i = 0; i < getItemCount(); i++) {
                View view = new View(mContext);
                view.setBackgroundResource(R.drawable.dot_indicator_banner_default);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                layoutParams.setMargins(10, 0, 10, 0);
                view.setLayoutParams(layoutParams);
                bannerHolder.dotIndicator.addView(view);
            }

            for (int i = 0; i < getItemCount(); i++) {
                if (i == position) {
                    bannerHolder.dotIndicator.getChildAt(i)
                            .setBackgroundResource(R.drawable.dot_indicator_banner_selected);
                } else {
                    bannerHolder.dotIndicator.getChildAt(i)
                            .setBackgroundResource(R.drawable.dot_indicator_banner_default);
                }
            }

            bannerHolder.swipeImageView.setOnClickListener(v -> {
                clickedUrl =mAppBanner.getActionData().getAppBanners().get(position).getAndroidLink();
                mBannerItemClickListener.bannerItemClicked(clickedUrl);

                    Report report = new Report();
                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put(VisilabsConstant.APP_BANNER_PARAMETER_KEY,clickedUrl);

                Visilabs.CallAPI().customEvent("BannerClick", parameters);
            report.impression = mAppBanner.getActionData().getReport().getImpression();
            report.click = mAppBanner.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report); });
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(0f, 0f, 0f, 0f)))
                    .load(mAppBanner.getActionData().getAppBanners().get(position).getImage())
                    .into(bannerHolder.slideImageView);

            String numStr = (position + 1) + "/" + getItemCount();
            bannerHolder.numberIndicator.setText(numStr);

            bannerHolder.slideImageView.setOnClickListener(v -> {
                clickedUrl =mAppBanner.getActionData().getAppBanners().get(position).getAndroidLink();
                mBannerItemClickListener.bannerItemClicked(clickedUrl);
                Report report = new Report();
                report.impression = mAppBanner.getActionData().getReport().getImpression();
                report.click = mAppBanner.getActionData().getReport().getClick();
                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put(VisilabsConstant.APP_BANNER_PARAMETER_KEY,clickedUrl);


                Visilabs.CallAPI().customEvent("BannerClick", parameters);
                Visilabs.CallAPI().trackActionClick(report);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mAppBanner != null && mAppBanner.getActionData() != null && mAppBanner.getActionData().getAppBanners() != null) {
            return mAppBanner.getActionData().getAppBanners().size();
        } else {
            return 0;
        }
    }

    public void setBannerList(RecyclerView recyclerView, AppBanner appBanner) {
        mAppBanner = appBanner;
        isSwipe = mAppBanner.getActionData().getTransitionAction().equals("swipe");
        if (mAppBanner != null && mAppBanner.getActionData() != null) {
            String transitionAction = mAppBanner.getActionData().getTransitionAction();
            isSwipe = "swipe".equals(transitionAction);
        }
        int slidePeriod = 3;

        mRecyclerView = recyclerView;
        if (!isSwipe) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    if(isScrolling) {
                        return true;
                    }
                    return false;
                }
            });

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING){
                        isScrolling = false;
                    }
                }
            });
            mHandler = new Handler(Looper.getMainLooper());
            mRunnable = new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    isScrolling = true;
                    if (mPosition == getItemCount() - 1) {
                        mPosition = 0;
                    } else {
                        mPosition++;
                    }
                    mRecyclerView.smoothScrollToPosition(mPosition);
                    ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                    ActivityManager.getMyMemoryState(myProcess);
                    if (myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE) {
                        mHandler.postDelayed(this, (slidePeriod * 1000));
                    } else {
                        mHandler.removeCallbacks(mRunnable);
                    }
                }
            };
            mHandler.postDelayed(mRunnable, (slidePeriod * 1000));
        }

        notifyDataSetChanged();
    }
    public class BannerHolder extends RecyclerView.ViewHolder {
        public ImageView  swipeImageView;
        public ImageView slideImageView;
        public LinearLayout dotIndicator;
        public TextView numberIndicator;

    public BannerHolder(View itemView) {
        super(itemView);
        if (isSwipe) {
            swipeImageView = itemView.findViewById(R.id.banner_swipe_image_item);
            dotIndicator = itemView.findViewById(R.id.banner_dot_indicator_item);
        } else {
            slideImageView = itemView.findViewById(R.id.banner_slide_image_item);
            numberIndicator = itemView.findViewById(R.id.banner_number_indicator_item);
        }
    }
    }
}



