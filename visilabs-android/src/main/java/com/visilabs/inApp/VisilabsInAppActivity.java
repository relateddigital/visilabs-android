package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
        if(mInApp.getActionData().getImg() != null && !mInApp.getActionData().getImg().equals("")) {
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
            if(mInApp.getActionData().getVideoUrl() != null && !mInApp.getActionData().getVideoUrl().equals("")) {
                String videoUrl = mInApp.getActionData().getVideoUrl();
                if (videoUrl.toLowerCase().contains("youtube.com") || videoUrl.toLowerCase().contains("youtu.be")) {
                    binding.fullVideoView.setVisibility(View.GONE);
                    binding.fullWebViewInapp.setVisibility(View.VISIBLE);
                    setYoutubeVideo();
                }
                else {
                binding.fullVideoView.setVisibility(View.VISIBLE);
                binding.fullWebViewInapp.setVisibility(View.GONE);
                initializePlayer();
                startPlayer();
                }
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
    private void setYoutubeVideo() {
        WebSettings webViewSettings = binding.fullWebViewInapp.getSettings();
        String backgroundColor = mInApp.getActionData().getBackground();
        if (backgroundColor!= null && !backgroundColor.isEmpty()) {
            binding.fullWebViewInapp.setBackgroundColor(Color.parseColor(mInApp.getActionData().getBackground()));
        }
        else binding.fullWebViewInapp.setBackgroundColor(getResources().getColor(R.color.black));
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDomStorageEnabled(true);
        webViewSettings.setSupportZoom(false);
        webViewSettings.setBuiltInZoomControls(false);
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webViewSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        webViewSettings.setAllowContentAccess(true);
        webViewSettings.setAllowFileAccess(true);
        webViewSettings.setAllowFileAccessFromFileURLs(true);
        webViewSettings.setAllowUniversalAccessFromFileURLs(true);

        binding.fullWebViewInapp.setWebChromeClient(new WebChromeClient());

        String urlString = mInApp.getActionData().getVideoUrl();
        String videoId = extractVideoId(urlString);
        String html =  "<div class=\"iframe-container\">\n" +
                " <div id=\"player\"></div>\n" +
                "</div>\n" +
                "<script>\n" +
                " var tag = document.createElement('script');\n" +
                " tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                " var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                " firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                " var player;\n" +
                " var isPlaying = true;\n" +
                " function onYouTubeIframeAPIReady() {\n" +
                " player = new YT.Player('player', {\n" +
                " width: '100%',\n" +
                " videoId: '" + videoId + "',\n" +
                " playerVars: { 'autoplay': 1, 'playsinline':1, 'rel': 0 },\n" +
                " events: {\n" +
                " 'onReady': function(event) {\n" +
                " event.target.playVideo();\n" +
                " }\n" +
                " }\n" +
                " });\n" +
                " }\n" +
                " function watchPlayingState() {\n" +
                " if (isPlaying) {\n" +
                " player.playVideo();\n" +
                " } else {\n" +
                " player.pauseVideo();\n" +
                " }\n" +
                " }\n" +
                "</script>";

        binding.fullWebViewInapp.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null);
    }

    private String extractVideoId(String videoUrl) {
        String videoId = null;
        if (videoUrl != null && videoUrl.trim().length() > 0) {
            String[] split = videoUrl.split("v=");
            if (split.length > 1) {
                videoId = split[1];
                int ampersandPosition = videoId.indexOf('&');
                if (ampersandPosition != -1) {
                    videoId = videoId.substring(0, ampersandPosition);
                }
            }
        }
        return videoId;
    }


    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        binding.fullVideoView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(mInApp.getActionData().getVideoUrl());
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
