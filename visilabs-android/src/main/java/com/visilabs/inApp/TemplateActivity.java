package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityTemplateBinding;
import com.visilabs.android.databinding.ActivityTemplateCarouselBinding;
import com.visilabs.android.databinding.NpsSecondPopUpBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.inApp.carousel.CarouselItem;
import com.visilabs.inApp.carousel.OnSwipeTouchListener;
import com.visilabs.inApp.inlineNpsWithNumber.InlineNpsWithNumbersView;
import com.visilabs.util.AppUtils;
import com.visilabs.util.StringUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.view.BaseRating;
import com.visilabs.view.SmileRating;

import java.util.List;


public class TemplateActivity extends Activity implements SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {
    enum NpsSecondPopUpType {
        IMAGE_TEXT_BUTTON,
        IMAGE_TEXT_BUTTON_IMAGE,
        FEEDBACK_FORM
    }

    enum NpsType {
        NPS,
        SMILE_RATING,
        NPS_WITH_NUMBERS,
        NPS_WITH_SECOND_POPUP,
        NONE
    }

    private static final String LOG_TAG = "Template Activity";
    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";
    private static final String CAROUSEL_LAST_INDEX_KEY = "carousel_last_index";
    InAppMessage mInAppMessage;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private int mIntentId = -1;
    private ActivityTemplateBinding binding;
    private NpsSecondPopUpBinding bindingSecondPopUp;
    private ActivityTemplateCarouselBinding bindingCarousel;
    private boolean mIsCarousel = false;
    private List<CarouselItem> mCarouselItems;
    private int mCarouselPosition = -1;
    private boolean mIsRotation = false;
    private NpsSecondPopUpType secondPopUpType = NpsSecondPopUpType.IMAGE_TEXT_BUTTON;
    private NpsType npsType = NpsType.NONE;
    private InAppButtonInterface buttonCallback = null;
    private Boolean isNpsSecondPopupButtonClicked = false;
    private Boolean isNpsSecondPopupActivated = false;
    private ExoPlayer player = null;
    private ExoPlayer player2 = null;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mIntentId = savedInstanceState.getInt(INTENT_ID_KEY, Integer.MAX_VALUE);
        } else {
            mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        }

        mInAppMessage = getInAppMessage();

        if (mInAppMessage == null) {
            Log.e(LOG_TAG, "InAppMessage is null! Could not get display state!");
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        } else {


            buttonCallback = Visilabs.CallAPI().getInAppButtonInterface();

            View view;

            if (mInAppMessage.getActionData().getMsgType() == InAppActionType.CAROUSEL) {
                mIsCarousel = true;
                mCarouselItems = mInAppMessage.getActionData().getCarouselItems();
                bindingCarousel = ActivityTemplateCarouselBinding.inflate(getLayoutInflater());
                view = bindingCarousel.getRoot();
                if (savedInstanceState != null) {
                    mCarouselPosition = savedInstanceState.getInt(CAROUSEL_LAST_INDEX_KEY, -1);
                }
            } else {
                mIsCarousel = false;
                binding = ActivityTemplateBinding.inflate(getLayoutInflater());
                view = binding.getRoot();
            }

            cacheResources();

            setContentView(view);
            if (isShowingNpsInApp()) {
                if (isShowingInApp()) {
                    if (mIsCarousel) {
                        if (mCarouselPosition == -1) {
                            mCarouselPosition = 0;
                        }
                        bindingCarousel.carouselContainer.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                            public void onSwipeRight() {
                                if (!isFirstCarousel()) {
                                    releasePlayer();
                                    mCarouselPosition--;
                                    setupViewCarousel();
                                }
                            }

                            public void onSwipeLeft() {
                                if (!isLastCarousel()) {
                                    releasePlayer();
                                    mCarouselPosition++;
                                    setupViewCarousel();
                                }
                            }
                        });
                        setupInitialViewCarousel();
                    } else {
                        setUpView();
                    }
                }
            } else {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }


        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INTENT_ID_KEY, mIntentId);
        if (mIsCarousel) {
            outState.putInt(CAROUSEL_LAST_INDEX_KEY, mCarouselPosition);
        }
        mIsRotation = true;
    }

    private InAppMessage getInAppMessage() {

        InAppNotificationState inAppNotificationState;

        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);

        if (mUpdateDisplayState == null || mUpdateDisplayState.getDisplayState() == null) {
            return null;
        } else {
            inAppNotificationState =
                    (InAppNotificationState) mUpdateDisplayState.getDisplayState();
            if (inAppNotificationState != null) {
                return inAppNotificationState.getInAppMessage();
            } else {
                return null;
            }
        }
    }

    private void setUpView() {

        if (mInAppMessage.getActionData().getImg() != null &&
                !mInAppMessage.getActionData().getImg().equals("") &&
                !mInAppMessage.getActionData().getImg().isEmpty()) {
            binding.ivTemplate.setVisibility(View.VISIBLE);
            binding.videoView.setVisibility(View.GONE);
            if (AppUtils.isAnImage(mInAppMessage.getActionData().getImg())) {
                Picasso.get().load(mInAppMessage.getActionData().getImg()).into(binding.ivTemplate);
            } else {
                Glide.with(this)
                        .load(mInAppMessage.getActionData().getImg())
                        .into(binding.ivTemplate);
            }
        } else {
            binding.ivTemplate.setVisibility(View.GONE);
            if (mInAppMessage.getActionData().getVideoUrl() != null && !mInAppMessage.getActionData().getVideoUrl().equals("")) {
                binding.videoView.setVisibility(View.VISIBLE);
                startPlayer();
            } else {
                binding.videoView.setVisibility(View.GONE);
                releasePlayer();
            }
        }

        binding.smileRating.setOnSmileySelectionListener(this);
        binding.smileRating.setOnRatingSelectedListener(this);

        setCloseButton();

        setTemplate();

    }

    private void setTemplate() {


        if (mInAppMessage.getActionData().getBackground() != null && !mInAppMessage.getActionData().getBackground().equals("")) {
            try {
                binding.llBack.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getBackground()));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for background color\nSetting the default value.");
                e.printStackTrace();
            }
        }

        switch (mInAppMessage.getActionData().getMsgType()) {

            case IMAGE_TEXT_BUTTON:

                setTitle();
                setBody();
                setButton();
                setPromotionCode();
                binding.ratingBar.setVisibility(View.GONE);
                binding.smileRating.setVisibility(View.GONE);

                break;

            case FULL_IMAGE:

                binding.tvBody.setVisibility(View.GONE);
                binding.tvTitle.setVisibility(View.GONE);
                binding.smileRating.setVisibility(View.GONE);
                binding.btnTemplate.setVisibility(View.GONE);

                binding.ivTemplate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                        if (buttonCallback != null) {
                            Visilabs.CallAPI().setInAppButtonInterface(null);
                            buttonCallback.onPress(mInAppMessage.getActionData().getAndroidLnk());
                        } else {
                            if (mInAppMessage.getActionData().getAndroidLnk() != null && mInAppMessage.getActionData().getAndroidLnk().length() > 0) {
                                try {
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getActionData().getAndroidLnk()));
                                    startActivity(viewIntent);

                                } catch (final ActivityNotFoundException e) {
                                    Log.i("Visilabs", "User doesn't have an activity for notification URI");
                                }
                            }
                        }
                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                        finish();
                    }
                });

                break;

            case IMAGE_BUTTON:

                binding.smileRating.setVisibility(View.GONE);
                binding.llTextContainer.setVisibility(View.GONE);
                setButton();

                break;

            case NPS:
                npsType = NpsType.NPS;
                setTitle();
                setBody();
                setButton();
                showNps();

                break;

            case SMILE_RATING:
                npsType = NpsType.SMILE_RATING;
                setBody();
                setTitle();
                setButton();
                showSmileRating();

                break;

            case NPS_WITH_NUMBERS:


                npsType = NpsType.NPS_WITH_NUMBERS;
                binding.smileRating.setVisibility(View.GONE);
                setBody();
                setTitle();
                setButton();
                showNpsWithNumbers();

                break;

            case NPS_AND_SECOND_POP_UP:
                npsType = NpsType.NPS_WITH_SECOND_POPUP;
                setNpsSecondPopUpCloseButton();
                setTitle();
                setBody();
                setNpsSecondPopUpButton();
                showNps();

                break;

        }


    }

    private void setNpsSecondPopUpCloseButton() {
        binding.ibClose.setVisibility(View.GONE);
        switch (mInAppMessage.getActionData().getSecondPopupType()) {
            case "image_text_button": {
                secondPopUpType = NpsSecondPopUpType.IMAGE_TEXT_BUTTON;
                break;
            }

            case "image_text_button_image": {
                secondPopUpType = NpsSecondPopUpType.IMAGE_TEXT_BUTTON_IMAGE;
                break;
            }

            case "feedback_form": {
                secondPopUpType = NpsSecondPopUpType.FEEDBACK_FORM;
                break;
            }
        }
    }

    private void setTitle() {

        if (mInAppMessage.getActionData().getMsgTitle().equals("") ||
                mInAppMessage.getActionData().getMsgTitle() == null) {
            binding.tvTitle.setVisibility(View.GONE);
        } else {
            if (mInAppMessage.getActionData().getMsgTitleBackgroundColor() != null &&
                    !mInAppMessage.getActionData().getMsgTitleBackgroundColor().equals("")) {
                binding.tvTitle.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleBackgroundColor()));
            }
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.tvTitle.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
            binding.tvTitle.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
            if (mInAppMessage.getActionData().getMsgTitleColor() != null && !mInAppMessage.getActionData().getMsgTitleColor().equals("")) {
                try {
                    binding.tvTitle.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleColor()));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for message title color\nSetting the default value.");
                    e.printStackTrace();
                    binding.tvTitle.setTextColor(getResources().getColor(R.color.blue));
                }
            } else {
                binding.tvTitle.setTextColor(getResources().getColor(R.color.blue));
            }
            try {
                binding.tvTitle.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgBodyTextSize()) + 12);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for message body text size\nSetting the default value.");
                e.printStackTrace();
                binding.tvTitle.setTextSize(16);
            }
        }
    }

    private void setBody() {

        if (mInAppMessage.getActionData().getMsgBody().equals("") ||
                mInAppMessage.getActionData().getMsgBody() == null) {
            binding.tvBody.setVisibility(View.GONE);
        } else {
            if (mInAppMessage.getActionData().getMsgBodyBackgroundColor() != null &&
                    !mInAppMessage.getActionData().getMsgBodyBackgroundColor().equals("")) {
                binding.tvBody.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getMsgBodyBackgroundColor()));
            }
            binding.tvBody.setText(mInAppMessage.getActionData().getMsgBody().replace("\\n", "\n"));
            binding.tvBody.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
            binding.tvBody.setVisibility(View.VISIBLE);
            if (mInAppMessage.getActionData().getMsgBodyColor() != null && !mInAppMessage.getActionData().getMsgBodyColor().equals("")) {
                try {
                    binding.tvBody.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgBodyColor()));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for message body color\nSetting the default value.");
                    e.printStackTrace();
                }
            }
            try {
                binding.tvBody.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgBodyTextSize()) + 8);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for message body text size\nSetting the default value.");
                e.printStackTrace();
                binding.tvBody.setTextSize(12);
            }
        }
    }

    private void setButton() {

        if (mInAppMessage.getActionData().getBtnText().equals("") ||
                mInAppMessage.getActionData().getBtnText() == null) {
            binding.btnTemplate.setVisibility(View.GONE);
        } else {
            binding.btnTemplate.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
            binding.btnTemplate.setVisibility(View.VISIBLE);
            binding.btnTemplate.setText(mInAppMessage.getActionData().getBtnText());
            if (mInAppMessage.getActionData().getButtonTextColor() != null && !mInAppMessage.getActionData().getButtonTextColor().equals("")) {
                try {
                    binding.btnTemplate.setTextColor(Color.parseColor(mInAppMessage.getActionData().getButtonTextColor()));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for button text color\nSetting the default value.");
                    e.printStackTrace();
                    binding.btnTemplate.setTextColor(getResources().getColor(R.color.black));
                }
            } else {
                binding.btnTemplate.setTextColor(getResources().getColor(R.color.black));
            }
            if (mInAppMessage.getActionData().getButtonColor() != null && !mInAppMessage.getActionData().getButtonColor().equals("")) {
                try {
                    binding.btnTemplate.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getButtonColor()));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for button color\nSetting the default value.");
                    e.printStackTrace();
                }
            }

            binding.btnTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (npsType == NpsType.NONE) {
                        Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                        if (buttonCallback != null) {
                            Visilabs.CallAPI().setInAppButtonInterface(null);
                            buttonCallback.onPress(mInAppMessage.getActionData().getAndroidLnk());
                        } else {
                            if (mInAppMessage.getActionData().getMsgType() == InAppActionType.IMAGE_TEXT_BUTTON) {
                                if (mInAppMessage.getActionData().getButtonFunction().equals(VisilabsConstant.BUTTON_LINK)) {
                                    if (mInAppMessage.getActionData().getAndroidLnk() != null && mInAppMessage.getActionData().getAndroidLnk().length() > 0) {
                                        try {
                                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getActionData().getAndroidLnk()));
                                            startActivity(viewIntent);

                                        } catch (final ActivityNotFoundException e) {
                                            Log.i("Visilabs", "User doesn't have an activity for notification URI");
                                        }
                                    }
                                } else {
                                    AppUtils.goToNotificationSettings(getApplicationContext());
                                }
                            } else {
                                if (mInAppMessage.getActionData().getAndroidLnk() != null && mInAppMessage.getActionData().getAndroidLnk().length() > 0) {
                                    try {
                                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getActionData().getAndroidLnk()));
                                        startActivity(viewIntent);

                                    } catch (final ActivityNotFoundException e) {
                                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                                    }
                                }
                            }
                        }
                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                        finish();
                    } else {
                        if (isRatingEntered()) {
                            Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                            if (buttonCallback != null) {
                                Visilabs.CallAPI().setInAppButtonInterface(null);
                                buttonCallback.onPress(mInAppMessage.getActionData().getAndroidLnk());
                            } else {
                                if (mInAppMessage.getActionData().getAndroidLnk() != null && mInAppMessage.getActionData().getAndroidLnk().length() > 0) {
                                    try {
                                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getActionData().getAndroidLnk()));
                                        startActivity(viewIntent);

                                    } catch (final ActivityNotFoundException e) {
                                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                                    }
                                }
                            }
                            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                            finish();
                        }
                    }
                }
            });
        }
    }

    private boolean isRatingEntered() {
        boolean result = false;
        switch (npsType) {
            case NPS: {
                if (binding.ratingBar.getRating() != 0) {
                    result = true;
                }
                break;
            }
            case SMILE_RATING: {
                result = true; // Since the default is "GREAT", no need to check if the user chose something.
                break;
            }
            case NPS_WITH_NUMBERS: {
                if (binding.npsWithNumbersView.getSelectedRate() != 0) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    private void setNpsSecondPopUpButton() {
        binding.btnTemplate.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
        binding.btnTemplate.setVisibility(View.VISIBLE);
        binding.btnTemplate.setText(mInAppMessage.getActionData().getBtnText());
        if (mInAppMessage.getActionData().getButtonTextColor() != null && !mInAppMessage.getActionData().getButtonTextColor().equals("")) {
            try {
                binding.btnTemplate.setTextColor(Color.parseColor(mInAppMessage.getActionData().getButtonTextColor()));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for button text color\nSetting the default value.");
                e.printStackTrace();
                binding.btnTemplate.setTextColor(getResources().getColor(R.color.black));
            }
        } else {
            binding.btnTemplate.setTextColor(getResources().getColor(R.color.black));
        }
        if (mInAppMessage.getActionData().getButtonColor() != null && !mInAppMessage.getActionData().getButtonColor().equals("")) {
            try {
                binding.btnTemplate.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getButtonColor()));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for button color\nSetting the default value.");
                e.printStackTrace();
            }
        }

        binding.btnTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlayer();
                if (binding.ratingBar.getRating() != 0) {
                    if (secondPopUpType == NpsSecondPopUpType.FEEDBACK_FORM) {
                        if (isRatingAboveThreshold()) {
                            Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                            finish();
                        } else {
                            setupSecondPopUp();
                        }
                    } else {
                        setupSecondPopUp();
                    }
                }
            }
        });
    }

    private boolean isRatingAboveThreshold() {
        float rating = binding.ratingBar.getRating();
        if(!mInAppMessage.getActionData().getSecondPopupFeecbackFormMinPoint().isEmpty() && !mInAppMessage.getActionData().getSecondPopupFeecbackFormMinPoint().equals("")&& !mInAppMessage.getActionData().getSecondPopupFeecbackFormMinPoint().equals("undefined")  ) {
            return rating >= (Float.parseFloat(mInAppMessage.getActionData().getSecondPopupFeecbackFormMinPoint()));
        }
        else {
            return true;
        }
    }

    private void setupSecondPopUp() {
        isNpsSecondPopupActivated = true;
        bindingSecondPopUp = NpsSecondPopUpBinding.inflate(getLayoutInflater());
        setContentView(bindingSecondPopUp.getRoot());
        if (mInAppMessage.getActionData().getBackground() != null && !mInAppMessage.getActionData().getBackground().equals("")) {
            try {
                bindingSecondPopUp.container.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getBackground()));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for background color\nSetting the default value.");
                e.printStackTrace();
            }
        }
        switch (secondPopUpType) {
            case IMAGE_TEXT_BUTTON: {
                bindingSecondPopUp.commentBox.setVisibility(View.GONE);
                bindingSecondPopUp.imageView2.setVisibility(View.GONE);
                bindingSecondPopUp.secondVideoView2.setVisibility(View.GONE);
                if (mInAppMessage.getActionData().getPromotionCode() != null &&
                        !mInAppMessage.getActionData().getPromotionCode().isEmpty()) {
                    bindingSecondPopUp.couponContainer.setBackgroundColor(Color.parseColor(
                            mInAppMessage.getActionData().getPromoCodeBackgroundColor()
                    ));
                    bindingSecondPopUp.couponCode.setText(mInAppMessage.getActionData().getPromotionCode());
                    bindingSecondPopUp.couponCode.setTextColor(Color.parseColor(
                            mInAppMessage.getActionData().getPromoCodeTextColor()
                    ));
                    bindingSecondPopUp.couponContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), mInAppMessage.getActionData().getPromotionCode());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    bindingSecondPopUp.couponContainer.setVisibility(View.GONE);
                }
                break;
            }
            case IMAGE_TEXT_BUTTON_IMAGE: {
                bindingSecondPopUp.commentBox.setVisibility(View.GONE);
                bindingSecondPopUp.couponContainer.setVisibility(View.GONE);
                if (mInAppMessage.getActionData().getSecondPopupImg2() != null &&
                        !mInAppMessage.getActionData().getSecondPopupImg2().isEmpty()) {
                    bindingSecondPopUp.imageView2.setVisibility(View.VISIBLE);
                    bindingSecondPopUp.secondVideoView2.setVisibility(View.GONE);
                    if (AppUtils.isAnImage(mInAppMessage.getActionData().getSecondPopupImg2())) {
                        Picasso.get().load(mInAppMessage.getActionData().getSecondPopupImg2())
                                .into(bindingSecondPopUp.imageView2);
                    } else {
                        Glide.with(this)
                                .load(mInAppMessage.getActionData().getSecondPopupImg2())
                                .into(bindingSecondPopUp.imageView2);
                    }
                } else {
                    bindingSecondPopUp.imageView2.setVisibility(View.GONE);
                    if (mInAppMessage.getActionData().getSecondPopupVideoUrl2() != null &&
                            !mInAppMessage.getActionData().getSecondPopupVideoUrl2().isEmpty()) {
                        bindingSecondPopUp.secondVideoView2.setVisibility(View.VISIBLE);
                        player2 = new ExoPlayer.Builder(this).build();
                        bindingSecondPopUp.secondVideoView2.setPlayer(player2);
                        MediaItem mediaItem = MediaItem.fromUri(mInAppMessage.getActionData().getSecondPopupVideoUrl2());
                        player2.setMediaItem(mediaItem);
                        player2.prepare();
                        player2.setPlayWhenReady(true);
                    } else {
                        bindingSecondPopUp.secondVideoView2.setVisibility(View.GONE);
                        releasePlayer();
                    }

                }
                break;
            }
            case FEEDBACK_FORM: {
                bindingSecondPopUp.imageView2.setVisibility(View.GONE);
                bindingSecondPopUp.secondVideoView2.setVisibility(View.GONE);
                bindingSecondPopUp.couponContainer.setVisibility(View.GONE);
                break;
            }
            default: {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
                break;
            }
        }
        if (mInAppMessage.getActionData().getCloseEventTrigger().equals("backgroundclick")) {
            bindingSecondPopUp.closeButton.setVisibility(View.GONE);
            this.setFinishOnTouchOutside(true);
        } else {
            this.setFinishOnTouchOutside(!mInAppMessage.getActionData().getCloseEventTrigger().equals("closebutton"));
            bindingSecondPopUp.closeButton.setBackgroundResource(getCloseIcon());
            bindingSecondPopUp.closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                    finish();
                }
            });
        }
        if (mInAppMessage.getActionData().getSecondPopupImg1() != null &&
                !mInAppMessage.getActionData().getSecondPopupImg1().isEmpty()) {
            bindingSecondPopUp.imageView.setVisibility(View.VISIBLE);
            bindingSecondPopUp.secondVideoView.setVisibility(View.GONE);
            if (AppUtils.isAnImage(mInAppMessage.getActionData().getSecondPopupImg1())) {
                Picasso.get().load(mInAppMessage.getActionData().getSecondPopupImg1())
                        .into(bindingSecondPopUp.imageView);
            } else {
                Glide.with(this)
                        .load(mInAppMessage.getActionData().getSecondPopupImg1())
                        .into(bindingSecondPopUp.imageView);
            }
        } else {
            bindingSecondPopUp.imageView.setVisibility(View.GONE);
            if (mInAppMessage.getActionData().getSecondPopupVideoUrl1() != null &&
                    !mInAppMessage.getActionData().getSecondPopupVideoUrl1().isEmpty()) {
                bindingSecondPopUp.secondVideoView.setVisibility(View.VISIBLE);
                player = new ExoPlayer.Builder(this).build();
                bindingSecondPopUp.secondVideoView.setPlayer(player);
                MediaItem mediaItem = MediaItem.fromUri(mInAppMessage.getActionData().getSecondPopupVideoUrl1());
                player.setMediaItem(mediaItem);
                player.prepare();
                startPlayer();
            } else {
                bindingSecondPopUp.secondVideoView.setVisibility(View.GONE);
                releasePlayer();
            }

        }
        bindingSecondPopUp.titleView.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
        bindingSecondPopUp.titleView.setText(mInAppMessage.getActionData().getSecondPopupMsgTitle().replace("\\n", "\n"));
        bindingSecondPopUp.titleView.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleColor()));
        bindingSecondPopUp.bodyTextView.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
        bindingSecondPopUp.bodyTextView.setText(mInAppMessage.getActionData().getSecondPopupMsgBody().replace("\\n", "\n"));
        bindingSecondPopUp.bodyTextView.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgBodyColor()));
        bindingSecondPopUp.bodyTextView.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getSecondPopupMsgBodyTextSize()) + 8);
        bindingSecondPopUp.button.setTypeface(mInAppMessage.getActionData().getFontFamily(this));
        bindingSecondPopUp.button.setText(mInAppMessage.getActionData().getSecondPopupBtnText());
        bindingSecondPopUp.button.setTextColor(Color.parseColor(mInAppMessage.getActionData().getButtonTextColor()));
        bindingSecondPopUp.button.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getButtonColor()));
        bindingSecondPopUp.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getNpsSecondPopupRateReport());
                isNpsSecondPopupButtonClicked = true;
                if (buttonCallback != null) {
                    Visilabs.CallAPI().setInAppButtonInterface(null);
                    buttonCallback.onPress(mInAppMessage.getActionData().getAndroidLnk());
                } else {
                    if (mInAppMessage.getActionData().getAndroidLnk() != null &&
                            !mInAppMessage.getActionData().getAndroidLnk().isEmpty()) {
                        try {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    mInAppMessage.getActionData().getAndroidLnk()));
                            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "The link is not formatted properly!");
                        }
                    } else {
                        Log.e(LOG_TAG, "The link is empty or not in a proper format!");
                    }
                }
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        });
    }

    private void setPromotionCode() {
        if (!StringUtils.isNullOrWhiteSpace(mInAppMessage.getActionData().getPromotionCode())
                && !StringUtils.isNullOrWhiteSpace(mInAppMessage.getActionData().getPromoCodeBackgroundColor())
                && !StringUtils.isNullOrWhiteSpace(mInAppMessage.getActionData().getPromoCodeTextColor())) {
            binding.llCouponContainer.setVisibility(View.VISIBLE);
            binding.llCouponContainer.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getPromoCodeBackgroundColor()));
            binding.tvCouponCode.setText(mInAppMessage.getActionData().getPromotionCode());
            binding.tvCouponCode.setTextColor(Color.parseColor(mInAppMessage.getActionData().getPromoCodeTextColor()));
            binding.llCouponContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), mInAppMessage.getActionData().getPromotionCode());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                    if(!mInAppMessage.getActionData().getmPromoCodeCopyButtonFunction().isEmpty()) {
                        if (mInAppMessage.getActionData().getmPromoCodeCopyButtonFunction().equals("copy_close")) {
                            finish();
                        }
                    }
                }
            });
        } else {
            binding.llCouponContainer.setVisibility(View.GONE);
        }
    }

    private String getRateReport() {
        switch (mInAppMessage.getActionData().getMsgType()) {
            case SMILE_RATING:
                return "OM.s_point=" + binding.smileRating.getRating() + "&OM.s_cat=" + mInAppMessage.getActionData().getMsgType() + "&OM.s_page=act-" + mInAppMessage.getActId();

            case NPS:

            case NPS_AND_SECOND_POP_UP:
                return "OM.s_point=" + binding.ratingBar.getRating() + "&OM.s_cat=" + mInAppMessage.getActionData().getMsgType() + "&OM.s_page=act-" + mInAppMessage.getActId();

            case NPS_WITH_NUMBERS:
                return "OM.s_point=" + binding.npsWithNumbersView.getSelectedRate() + "&OM.s_cat=" + mInAppMessage.getActionData().getMsgType() + "&OM.s_page=act-" + mInAppMessage.getActId();
        }

        return "";
    }

    private String getNpsSecondPopupRateReport() {
        switch (secondPopUpType) {
            case IMAGE_TEXT_BUTTON:

            case IMAGE_TEXT_BUTTON_IMAGE: {
                return "OM.s_point=" + binding.ratingBar.getRating() + "&OM.s_cat=" + mInAppMessage.getActionData().getMsgType()
                        + "&OM.s_page=act-" + mInAppMessage.getActId() +
                        "&OM.btn_title=" + "Android Link Yonlendirme" +
                        "&OM.btn_source=" + "OM.act-" + mInAppMessage.getActId();
            }

            case FEEDBACK_FORM: {
                Toast.makeText(this, getString(R.string.feedback_toast), Toast.LENGTH_SHORT).show();
                return "OM.s_point=" + binding.ratingBar.getRating() + "&OM.s_cat=" + mInAppMessage.getActionData().getMsgType()
                        + "&OM.s_page=act-" + mInAppMessage.getActId() + "&OM.s_feed=" +
                        bindingSecondPopUp.commentBox.getText().toString();
            }
            default: {
                return "OM.s_point=" + binding.ratingBar.getRating() + "&OM.s_cat=" + mInAppMessage.getActionData().getMsgType()
                        + "&OM.s_page=act-" + mInAppMessage.getActId();
            }
        }
    }


    public void setCloseButton() {

        if (mInAppMessage.getActionData().getCloseEventTrigger() != null) {
            if (mInAppMessage.getActionData().getCloseEventTrigger().equals("backgroundclick")) {
                binding.ibClose.setVisibility(View.GONE);
                this.setFinishOnTouchOutside(true);
            } else {
                this.setFinishOnTouchOutside(!mInAppMessage.getActionData().getCloseEventTrigger().equals("closebutton"));
                binding.ibClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);

                        finish();
                    }
                });

                binding.ibClose.setBackgroundResource(getCloseIcon());
            }
        } else {
            this.setFinishOnTouchOutside(true);
            binding.ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);

                    finish();
                }
            });

            binding.ibClose.setBackgroundResource(getCloseIcon());
        }
    }

    void showNps() {

        binding.ratingBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.yellow)));
        }
    }

    void showSmileRating() {
        binding.smileRating.setVisibility(View.VISIBLE);
    }

    void showNpsWithNumbers() {
        binding.npsWithNumbersView.setVisibility(View.VISIBLE);
        int[] colors = new int[mInAppMessage.getActionData().getNumberColors().length];

        for (int i = 0; i < mInAppMessage.getActionData().getNumberColors().length; i++) {
            colors[i] = Color.parseColor(mInAppMessage.getActionData().getNumberColors()[i]);
        }
        binding.npsWithNumbersView.setColors(colors);
    }

    private int getCloseIcon() {

        switch (mInAppMessage.getActionData().getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;
    }

    private boolean isShowingInApp() {

        if (mUpdateDisplayState == null) {
            return false;
        }

        return InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );


    }

    private boolean isShowingNpsInApp() {
        boolean isshow = true;
        Log.e("DENEME ",mInAppMessage.getActionData().getMsgType().toString());
        if(mInAppMessage.getActionData().getMsgType().toString().equals("nps_with_numbers")) {
            if (mInAppMessage.getActionData().getDisplayType().equals("inline")) {
                isshow = false;
            } else if (mInAppMessage.getActionData().getDisplayType().equals("popup") && mInAppMessage.getActionData().getDisplayType() == null && mInAppMessage.getActionData().getDisplayType().isEmpty()) {
                isshow = true;
            }
        }
            return isshow;
    }

    @Override
    public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
        switch (smiley) {
            case SmileRating.BAD:
                Log.i("VL", "Bad");
                break;
            case SmileRating.GOOD:
                Log.i("VL", "Good");
                break;
            case SmileRating.GREAT:
                Log.i("VL", "Great");
                break;
            case SmileRating.OKAY:
                Log.i("VL", "Okay");
                break;
            case SmileRating.TERRIBLE:
                Log.i("VL", "Terrible");
                break;
            case SmileRating.NONE:
                Log.i("VL", "None");
                break;
        }
    }

    @Override
    public void onRatingSelected(int level, boolean reselected) {
        Log.i("VL", "Rated as: " + level + " - " + reselected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mInAppMessage != null) {
            if (mIsRotation) {
                mIsRotation = false;
            } else {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
            if (mInAppMessage.getActionData().getMsgType() == InAppActionType.NPS_AND_SECOND_POP_UP) {
                if (!isNpsSecondPopupButtonClicked && isNpsSecondPopupActivated) {
                    Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                }
            }
        }
    }

    private void setupInitialViewCarousel() {
        if (mInAppMessage.getActionData().getCloseEventTrigger().equals("backgroundclick")) {
            bindingCarousel.carouselCloseButton.setVisibility(View.GONE);
            this.setFinishOnTouchOutside(true);
        } else {
            this.setFinishOnTouchOutside(!mInAppMessage.getActionData().getCloseEventTrigger().equals("closebutton"));
            bindingCarousel.carouselCloseButton.setBackgroundResource(getCloseIcon());
            bindingCarousel.carouselCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                    finish();
                }
            });
        }
        for (int i = 0; i < mCarouselItems.size(); i++) {
            View view = new View(getApplicationContext());
            view.setBackgroundResource(R.drawable.dot_indicator_default);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    40, 40);
            layoutParams.setMargins(10, 0, 10, 0);
            view.setLayoutParams(layoutParams);
            bindingCarousel.dotIndicator.addView(view);
        }
        setupViewCarousel();
    }

    private void setupViewCarousel() {
        bindingCarousel.carouselImage.setVisibility(View.VISIBLE);
        bindingCarousel.carouselTitle.setVisibility(View.VISIBLE);
        bindingCarousel.carouselBodyText.setVisibility(View.VISIBLE);
        bindingCarousel.carouselButton.setVisibility(View.VISIBLE);
        bindingCarousel.background.setVisibility(View.VISIBLE);
        bindingCarousel.couponContainer.setVisibility(View.VISIBLE);

        for (int i = 0; i < mCarouselItems.size(); i++) {
            if (i == mCarouselPosition) {
                bindingCarousel.dotIndicator.getChildAt(i).setBackgroundResource(R.drawable.dot_indicator_selected);
            } else {
                bindingCarousel.dotIndicator.getChildAt(i).setBackgroundResource(R.drawable.dot_indicator_default);
            }
        }

        setupCarouselItem(mCarouselPosition);
    }

    private boolean isLastCarousel() {
        return mCarouselPosition == mCarouselItems.size() - 1;
    }

    private boolean isFirstCarousel() {
        return mCarouselPosition == 0;
    }

    private void cacheResources() {
        if (mIsCarousel) {
            for (int i = 0; i < mCarouselItems.size(); i++) {
                if (mCarouselItems.get(i).getImage() != null && !mCarouselItems.get(i).getImage().isEmpty()) {
                    if (AppUtils.isAnImage(mCarouselItems.get(i).getImage())) {
                        Picasso.get().
                                load(mCarouselItems.get(i).getImage()).fetch();
                    }
                }
                if (mCarouselItems.get(i).getBackgroundImage() != null && !mCarouselItems.get(i).getBackgroundImage().isEmpty()) {
                    if (AppUtils.isAnImage(mCarouselItems.get(i).getBackgroundImage())) {
                        Picasso.get().
                                load(mCarouselItems.get(i).getBackgroundImage()).fetch();
                    }
                }
            }
        } else {
            if (mInAppMessage.getActionData().getImg() != null &&
                    !mInAppMessage.getActionData().getImg().isEmpty()) {
                if (AppUtils.isAnImage(mInAppMessage.getActionData().getImg())) {
                    Picasso.get().load(mInAppMessage.getActionData().getImg()).fetch();
                }
            }
            if (mInAppMessage.getActionData().getMsgType() == InAppActionType.NPS_AND_SECOND_POP_UP) {
                if (mInAppMessage.getActionData().getSecondPopupImg1() != null &&
                        !mInAppMessage.getActionData().getSecondPopupImg1().isEmpty()) {
                    if (AppUtils.isAnImage(mInAppMessage.getActionData().getSecondPopupImg1())) {
                        Picasso.get().load(mInAppMessage.getActionData().getSecondPopupImg1()).fetch();
                    }
                }
                if (mInAppMessage.getActionData().getSecondPopupImg2() != null &&
                        !mInAppMessage.getActionData().getSecondPopupImg2().isEmpty()) {
                    if (AppUtils.isAnImage(mInAppMessage.getActionData().getSecondPopupImg2())) {
                        Picasso.get().load(mInAppMessage.getActionData().getSecondPopupImg2()).fetch();
                    }
                }
            }
        }

        if (mInAppMessage.getActionData().getVideoUrl() != null && !mInAppMessage.getActionData().getVideoUrl().equals("")) {
            initializePlayer();
        }
    }

    private void initializePlayer() {
        if (!mIsCarousel) {
            player = new ExoPlayer.Builder(this).build();
            binding.videoView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(mInAppMessage.getActionData().getVideoUrl());
            player.setMediaItem(mediaItem);
            player.prepare();
        }
    }

    private void startPlayer() {
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
        if (player2 != null) {
            player2.release();
            player2 = null;
        }
    }


    private void setupCarouselItem(int position) {
        if (mCarouselItems.get(position).getBackgroundImage() != null &&
                !mCarouselItems.get(position).getBackgroundImage().isEmpty()) {
            Picasso.get().
                    load(mCarouselItems.get(position).getBackgroundImage())
                    .into(bindingCarousel.background);
        } else {
            bindingCarousel.background.setVisibility(View.GONE);
            if (mCarouselItems.get(position).getBackgroundColor() != null &&
                    !mCarouselItems.get(position).getBackgroundColor().isEmpty()) {
                bindingCarousel.background.setVisibility(View.GONE);
                bindingCarousel.carouselContainer.setBackgroundColor(Color.parseColor(
                        mCarouselItems.get(position).getBackgroundColor()));
            }
        }

        if (mCarouselItems.get(position).getImage() != null && !mCarouselItems.get(position).getImage().isEmpty()) {
            bindingCarousel.carouselImage.setVisibility(View.VISIBLE);
            bindingCarousel.carouselVideo.setVisibility(View.GONE);
            if (AppUtils.isAnImage(mCarouselItems.get(position).getImage())) {
                Picasso.get().
                        load(mCarouselItems.get(position).getImage())
                        .into(bindingCarousel.carouselImage);
            } else {
                Glide.with(this)
                        .load(mCarouselItems.get(position).getImage())
                        .into(bindingCarousel.carouselImage);
            }
        } else {
            bindingCarousel.carouselImage.setVisibility(View.GONE);
            if (mCarouselItems.get(position).getVideoUrl() != null && !mCarouselItems.get(position).getVideoUrl().isEmpty()) {
                bindingCarousel.carouselVideo.setVisibility(View.VISIBLE);
                player = new ExoPlayer.Builder(this).build();
                bindingCarousel.carouselVideo.setPlayer(player);
                MediaItem mediaItem = MediaItem.fromUri(mCarouselItems.get(position).getVideoUrl());
                player.setMediaItem(mediaItem);
                player.prepare();
                startPlayer();
            } else {
                bindingCarousel.carouselVideo.setVisibility(View.GONE);
                releasePlayer();
            }
        }

        if (mCarouselItems.get(position).getTitle() != null &&
                !mCarouselItems.get(position).getTitle().isEmpty()) {
            bindingCarousel.carouselTitle.setText(mCarouselItems.get(position).getTitle().replace("\\n", "\n"));
            bindingCarousel.carouselTitle.setTextColor(Color.parseColor(mCarouselItems.get(position).getTitleColor()));
            bindingCarousel.carouselTitle.setTextSize(
                    Float.parseFloat(mCarouselItems.get(position).getTitleTextsize()) + 12);
            bindingCarousel.carouselTitle.setTypeface(mCarouselItems.get(position).getTitleFontFamily(this));
        } else {
            bindingCarousel.carouselTitle.setVisibility(View.GONE);
        }

        if (mCarouselItems.get(position).getBody() != null &&
                !mCarouselItems.get(position).getBody().isEmpty()) {
            bindingCarousel.carouselBodyText.setText(mCarouselItems.get(position).getBody().replace("\\n", "\n"));
            bindingCarousel.carouselBodyText.setTextColor(Color.parseColor(mCarouselItems.get(position).getBodyColor()));
            bindingCarousel.carouselBodyText.setTextSize(
                    Float.parseFloat(mCarouselItems.get(position).getBodyTextsize()) + 8);
            bindingCarousel.carouselBodyText.setTypeface(mCarouselItems.get(position).getBodyFontFamily(this));
        } else {
            bindingCarousel.carouselBodyText.setVisibility(View.GONE);
        }

        if (mCarouselItems.get(position).getPromotionCode() != null &&
                !mCarouselItems.get(position).getPromotionCode().isEmpty()) {
            bindingCarousel.couponContainer.setBackgroundColor(Color.parseColor(
                    mCarouselItems.get(position).getPromocodeBackgroundColor()));
            bindingCarousel.couponCode.setText(mCarouselItems.get(position).getPromotionCode());
            bindingCarousel.couponCode.setTextColor(Color.parseColor(mCarouselItems.get(position).getPromocodeTextColor()));
            bindingCarousel.couponContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), mCarouselItems.get(position).getPromotionCode());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            bindingCarousel.couponContainer.setVisibility(View.GONE);
        }

        if (mCarouselItems.get(position).getButtonText() != null &&
                !mCarouselItems.get(position).getButtonText().isEmpty()) {
            bindingCarousel.carouselButton.setBackgroundColor(Color.parseColor(mCarouselItems.get(position).getButtonColor()));
            bindingCarousel.carouselButton.setText(mCarouselItems.get(position).getButtonText());
            bindingCarousel.carouselButton.setTextColor(Color.parseColor(mCarouselItems.get(position).getButtonTextColor()));
            bindingCarousel.carouselButton.setTextSize(
                    Float.parseFloat(mCarouselItems.get(position).getButtonTextsize()) + 12);
            bindingCarousel.carouselButton.setTypeface(mCarouselItems.get(position).getButtonFontFamily(this));
            bindingCarousel.carouselButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                    if (buttonCallback != null) {
                        Visilabs.CallAPI().setInAppButtonInterface(null);
                        buttonCallback.onPress(mCarouselItems.get(position).getAndroidLnk());
                    } else {
                        try {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCarouselItems.get(position).getAndroidLnk()));
                            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "The link is not formatted properly!");
                        }
                    }
                    VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                    finish();
                }
            });
        } else {
            bindingCarousel.carouselButton.setVisibility(View.GONE);
        }
    }
}
