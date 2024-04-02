package com.visilabs.inApp.bannercarousel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;
import com.squareup.picasso.RequestHandler;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.inApp.appBannerModel.AppBanner;
import com.visilabs.inApp.appBannerModel.BannerResponse;

import java.util.HashMap;

public class BannerRecyclerView extends RecyclerView {
    private static final String LOG_TAG = "BannerRecyclerView";
    private Context mContext;
    private BannerItemClickListener mBannerItemClickListener;
    private BannerRequestListener mBannerRequestListener;

    public BannerRecyclerView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void requestBannerCarouselAction(Context context,
                                            HashMap<String, String> properties,
                                            BannerRequestListener bannerRequestListener,
                                            BannerItemClickListener bannerItemClickListener) {
        if (Visilabs.CallAPI().isBlocked()) {
            Log.e(LOG_TAG, "Too much server load, ignoring the request!");
            if (bannerRequestListener != null) {
                bannerRequestListener.onRequestResult(false);
            }
            return;
        }

        mBannerItemClickListener = bannerItemClickListener;
        mBannerRequestListener = bannerRequestListener;
        VisilabsActionRequest visilabsActionRequest;

        try {
            visilabsActionRequest = Visilabs.CallAPI().requestBannerAction(properties, "AppBanner");
            visilabsActionRequest.executeBannerAsyncAction(getBannerCallback(context));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VisilabsCallback getBannerCallback(final Context context) {
        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                if (mBannerRequestListener != null) {
                    mBannerRequestListener.onRequestResult(true);
                }
                try {
                    BannerResponse mBannerResponse = new Gson().fromJson(response.getRawResponse(), BannerResponse.class);
                    AppBanner mAppBanner = mBannerResponse.getAppBanner().get(0);
                    BannerCarouselAdapter bannerCarouselAdapter = new BannerCarouselAdapter(context, mBannerItemClickListener);
                    setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    bannerCarouselAdapter.setBannerList(BannerRecyclerView.this, mAppBanner);
                    setHasFixedSize(true);
                    setAdapter(bannerCarouselAdapter);
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(BannerRecyclerView.this);
                    bannerCarouselAdapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                    if (mBannerRequestListener != null) {
                        mBannerRequestListener.onRequestResult(false);
                    }
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(LOG_TAG, response.getRawResponse());
                if (mBannerRequestListener != null) {
                    mBannerRequestListener.onRequestResult(false);
                }
            }
        };
    }


}
