package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.ActivityInAppFullBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AppUtils;
import com.visilabs.util.StringUtils;


public class VisilabsInAppActivity extends Activity implements IVisilabs {

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";
    public static final String LOG_TAG = "InAppActivityFull";

    InAppMessage mInApp;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private int mIntentId = -1;
    private ActivityInAppFullBinding binding;
    private boolean mIsRotation = false;
    private ExoPlayer player = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInAppFullBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        if(savedInstanceState != null) {
            mIntentId = savedInstanceState.getInt(INTENT_ID_KEY, Integer.MAX_VALUE);
        } else {
            mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        }

        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);


        if (mUpdateDisplayState == null) {

            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
            return;
        }

        if (isShowingInApp()) {
            setUpView();
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INTENT_ID_KEY, mIntentId);
        mIsRotation = true;
    }

    @Override
    public void setUpView() {

        InAppNotificationState inAppNotificationState =
                (InAppNotificationState) mUpdateDisplayState.getDisplayState();

        if(inAppNotificationState != null) {
            mInApp = inAppNotificationState.getInAppMessage();
            if(mInApp != null) {
                setInAppData();
                setPromotionCode();
                clickEvents();
            } else {
                Log.e(LOG_TAG, "InAppMessage is null! Could not get display state!");
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        } else {
            Log.e(LOG_TAG, "InAppMessage is null! Could not get display state!");
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        }
    }

    private void setInAppData() {


        binding.tvInAppTitle.setText(mInApp.getActionData().getMsgTitle().replace("\\n","\n"));
        binding.tvInAppSubtitle.setText(mInApp.getActionData().getMsgBody().replace("\\n","\n"));

        if (mInApp.getActionData().getBtnText() != null && mInApp.getActionData().getBtnText().length() > 0) {
            binding.btnInApp.setText(mInApp.getActionData().getBtnText());
        }
        if(!mInApp.getActionData().getImg().equals("")) {
            binding.fivInAppImage.setVisibility(View.VISIBLE);
            binding.fullVideoView.setVisibility(View.GONE);

            if (AppUtils.isAnImage(mInApp.getActionData().getImg())) {
                Picasso.get().load(mInApp.getActionData().getImg()).into(binding.fivInAppImage);
            } else {
                Glide.with(this)
                        .load(mInApp.getActionData().getImg())
                        .into(binding.fivInAppImage);
            }
        } else {
            binding.fivInAppImage.setVisibility(View.GONE);
            if(false) { // TODO : if !video.isNullOrEmpty():
                binding.fullVideoView.setVisibility(View.VISIBLE);
                initializePlayer();
                startPlayer();
            } else {
                binding.fullVideoView.setVisibility(View.GONE);
                releasePlayer();
            }

        }
    }

    private void setPromotionCode() {
        if(!StringUtils.isNullOrWhiteSpace(mInApp.getActionData().getPromotionCode())
                && !StringUtils.isNullOrWhiteSpace(mInApp.getActionData().getPromoCodeBackgroundColor())
                && !StringUtils.isNullOrWhiteSpace(mInApp.getActionData().getPromoCodeTextColor())){
            binding.llCouponContainer.setVisibility(View.VISIBLE);
            binding.llCouponContainer.setBackgroundColor(Color.parseColor(mInApp.getActionData().getPromoCodeBackgroundColor()));
            binding.tvCouponCode.setText(mInApp.getActionData().getPromotionCode());
            binding.tvCouponCode.setTextColor(Color.parseColor(mInApp.getActionData().getPromoCodeTextColor()));
            binding.llCouponContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), mInApp.getActionData().getPromotionCode());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            binding.llCouponContainer.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {

        binding.btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
                Visilabs.CallAPI().trackInAppMessageClick(mInApp, null);
                if(buttonInterface != null) {
                    Visilabs.CallAPI().setInAppButtonInterface(null);
                    buttonInterface.onPress(mInApp.getActionData().getAndroidLnk());
                } else {
                    if (mInApp.getActionData().getAndroidLnk() != null && mInApp.getActionData().getAndroidLnk().length() > 0) {
                        try {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInApp.getActionData().getAndroidLnk()));
                            VisilabsInAppActivity.this.startActivity(viewIntent);

                        } catch (final ActivityNotFoundException e) {
                            Log.i("Visilabs", "User doesn't have an activity for notification URI");
                        }
                    }
                }
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });


        binding.btnInApp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.cta_button_highlight);
                } else {
                    v.setBackgroundResource(R.drawable.cta_button);
                }
                return false;
            }
        });

        binding.llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });

    }

    private InAppNotificationState getSurveyState() {
        return (InAppNotificationState) mUpdateDisplayState.getDisplayState();
    }

    private boolean isShowingSurvey() {
        if (null == mUpdateDisplayState) {
            return false;
        }
        return InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }

    private boolean isShowingInApp() {
        if (null == mUpdateDisplayState) {
            return false;
        }
        return InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }

    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        binding.fullVideoView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"); //TODO : real url here
        player.setMediaItem(mediaItem);
        player.prepare();
    }

    private void startPlayer() {
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    @Override
    public void onBackPressed() {
        if (isShowingInApp()) {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if(mIsRotation) {
            mIsRotation = false;
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        }
    }

}
