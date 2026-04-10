package com.visilabs.inApp.carousel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.inApp.InAppButtonInterface;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.inApp.VisilabsInAppActivity;

import java.util.List;

public class CarouselFullscreenActivity extends Activity {

    private static final String LOG_TAG = "CarouselFullscreen";
    private static final int MAX_ITEMS = 5;
    private static final float HERO_HEIGHT_FRACTION = 0.85f;
    private static final int CARD_OVERLAP_DP = 72;

    private int mIntentId = -1;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private InAppMessage mInAppMessage;
    private List<CarouselItem> mCarouselItems;
    private InAppButtonInterface buttonCallback;
    private int currentPosition = 0;

    private RecyclerView pager;
    private LinearLayout dotsContainer;
    private Button primaryButton;
    private Button secondaryButton;
    private View buttonSpacer;
    private ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_fullscreen);

        if (savedInstanceState != null) {
            mIntentId = savedInstanceState.getInt(VisilabsInAppActivity.INTENT_ID_KEY, Integer.MAX_VALUE);
        } else {
            mIntentId = getIntent().getIntExtra(VisilabsInAppActivity.INTENT_ID_KEY, Integer.MAX_VALUE);
        }

        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);
        if (mUpdateDisplayState == null || mUpdateDisplayState.getDisplayState() == null) {
            Log.e(LOG_TAG, "Could not get display state");
            finish();
            return;
        }

        InAppNotificationState state = (InAppNotificationState) mUpdateDisplayState.getDisplayState();
        if (state == null) {
            Log.e(LOG_TAG, "InAppNotificationState is null");
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
            return;
        }

        mInAppMessage = state.getInAppMessage();
        if (mInAppMessage == null || mInAppMessage.getActionData() == null
                || mInAppMessage.getActionData().getCarouselItems() == null
                || mInAppMessage.getActionData().getCarouselItems().isEmpty()) {
            Log.e(LOG_TAG, "InAppMessage or carousel items missing");
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
            return;
        }

        mCarouselItems = mInAppMessage.getActionData().getCarouselItems();
        if (mCarouselItems.size() > MAX_ITEMS) {
            mCarouselItems = mCarouselItems.subList(0, MAX_ITEMS);
        }

        buttonCallback = Visilabs.CallAPI().getInAppButtonInterface();

        initViews();
        setupPager();
        setupDots();
        setupCloseButton();
        updateFooter(0);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(VisilabsInAppActivity.INTENT_ID_KEY, mIntentId);
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
    }

    private void initViews() {
        pager = findViewById(R.id.carousel_fullscreen_pager);
        dotsContainer = findViewById(R.id.carousel_fullscreen_dots);
        primaryButton = findViewById(R.id.carousel_fullscreen_primary_button);
        secondaryButton = findViewById(R.id.carousel_fullscreen_secondary_button);
        buttonSpacer = findViewById(R.id.carousel_fullscreen_button_spacer);
        closeButton = findViewById(R.id.carousel_fullscreen_close_button);

        primaryButton.setOnClickListener(v -> handleButtonClick(true));
        secondaryButton.setOnClickListener(v -> handleButtonClick(false));
    }

    private void setupPager() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        pager.setLayoutManager(layoutManager);

        final PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(pager);

        pager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snapView = snapHelper.findSnapView(layoutManager);
                    if (snapView != null) {
                        int pos = layoutManager.getPosition(snapView);
                        if (pos != currentPosition && pos >= 0 && pos < mCarouselItems.size()) {
                            updateFooter(pos);
                        }
                    }
                }
            }
        });

        pager.post(new Runnable() {
            @Override
            public void run() {
                pager.setAdapter(new CarouselFullscreenAdapter());
            }
        });
    }

    private void setupDots() {
        dotsContainer.removeAllViews();
        for (int i = 0; i < mCarouselItems.size(); i++) {
            View dot = new View(this);
            dot.setBackgroundResource(R.drawable.dot_indicator_default);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(10), dpToPx(10));
            lp.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            dot.setLayoutParams(lp);
            dotsContainer.addView(dot);
        }
    }

    private void setupCloseButton() {
        String closeColor = mInAppMessage.getActionData().getCloseButtonColor();
        if ("black".equals(closeColor)) {
            closeButton.setBackgroundResource(R.drawable.ic_close_black_24dp);
        } else {
            closeButton.setBackgroundResource(R.drawable.ic_close_white_24dp);
        }
        closeButton.setOnClickListener(v -> {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        });
    }

    private void updateFooter(int position) {
        currentPosition = position;
        CarouselItem item = mCarouselItems.get(position);

        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            dotsContainer.getChildAt(i).setBackgroundResource(
                    i == position ? R.drawable.dot_indicator_selected : R.drawable.dot_indicator_default);
        }

        float borderRadius = 14f;
        String brStr = mInAppMessage.getActionData().getButtonBorderRadius();
        if (brStr != null && !brStr.isEmpty()) {
            try {
                borderRadius = Float.parseFloat(brStr);
            } catch (NumberFormatException ignored) {}
        }
        float radiusPx = dpToPx(borderRadius);

        setupPrimaryButton(item, radiusPx);
        setupSecondaryButton(item, radiusPx);
    }

    private void setupPrimaryButton(CarouselItem item, float radiusPx) {
        if (item.getButtonText() == null || item.getButtonText().isEmpty()) {
            primaryButton.setVisibility(View.GONE);
            return;
        }
        primaryButton.setVisibility(View.VISIBLE);
        primaryButton.setText(item.getButtonText().replace("\\n", "\n"));

        int fillColor = Color.parseColor("#00897B");
        if (item.getButtonColor() != null && !item.getButtonColor().isEmpty()) {
            try { fillColor = parseColor(item.getButtonColor()); } catch (Exception ignored) {}
        }

        int textColor = Color.WHITE;
        if (item.getButtonTextColor() != null && !item.getButtonTextColor().isEmpty()) {
            try { textColor = parseColor(item.getButtonTextColor()); } catch (Exception ignored) {}
        }

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(fillColor);
        bg.setCornerRadius(radiusPx);
        primaryButton.setBackground(bg);
        primaryButton.setTextColor(textColor);
        primaryButton.setTypeface(item.getButtonFontFamily(this));

        if (item.getButtonTextsize() != null && !item.getButtonTextsize().isEmpty()) {
            try {
                primaryButton.setTextSize(mapTextSize(Float.parseFloat(item.getButtonTextsize()), true));
            } catch (NumberFormatException ignored) {}
        }
    }

    private void setupSecondaryButton(CarouselItem item, float radiusPx) {
        boolean hasSecondary = item.getSecondButtonText() != null && !item.getSecondButtonText().trim().isEmpty();
        secondaryButton.setVisibility(hasSecondary ? View.VISIBLE : View.GONE);
        buttonSpacer.setVisibility(hasSecondary ? View.VISIBLE : View.GONE);

        if (!hasSecondary) return;

        secondaryButton.setText(item.getSecondButtonText().replace("\\n", "\n"));

        int fillColor = Color.parseColor("#00897B");
        if (item.getSecondButtonColor() != null && !item.getSecondButtonColor().isEmpty()) {
            try { fillColor = parseColor(item.getSecondButtonColor()); } catch (Exception ignored) {}
        } else if (item.getButtonColor() != null && !item.getButtonColor().isEmpty()) {
            try { fillColor = parseColor(item.getButtonColor()); } catch (Exception ignored) {}
        }

        int secTextColor = Color.WHITE;
        if (item.getSecondButtonTextColor() != null && !item.getSecondButtonTextColor().isEmpty()) {
            try { secTextColor = parseColor(item.getSecondButtonTextColor()); } catch (Exception ignored) {}
        }

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(fillColor);
        bg.setCornerRadius(radiusPx);
        secondaryButton.setBackground(bg);
        secondaryButton.setTextColor(secTextColor);
        secondaryButton.setTypeface(item.getSecondButtonFontFamily(this));

        if (item.getButtonTextsize() != null && !item.getButtonTextsize().isEmpty()) {
            try {
                secondaryButton.setTextSize(mapTextSize(Float.parseFloat(item.getButtonTextsize()), true));
            } catch (NumberFormatException ignored) {}
        }
    }

    private static float mapTextSize(float value, boolean isTitle) {
        int[] titleSizes = {12, 15, 17, 20, 24};
        int[] bodySizes  = {10, 12, 14, 16, 18};
        int idx = Math.max(0, Math.min(4, (int) value - 1));
        return isTitle ? titleSizes[idx] : bodySizes[idx];
    }

    private void handleButtonClick(boolean isPrimary) {
        CarouselItem item = mCarouselItems.get(currentPosition);
        String link = isPrimary ? item.getAndroidLnk() : item.getSecondAndroidLnk();
        String function = isPrimary ? item.getButtonFunction() : item.getSecondButtonFunction();

        Log.d(LOG_TAG, "handleButtonClick isPrimary=" + isPrimary
                + " link=" + link + " function=" + function
                + " callbackSet=" + (buttonCallback != null));

        Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, "");

        if ("redirect".equals(function)) {
            try {
                Intent settingsIntent = new Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(settingsIntent);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Could not open app settings: " + e.getMessage());
            }
        } else {
            if (buttonCallback != null) {
                buttonCallback.onPress(link);
            } else {
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not open link: " + e.getMessage());
                }
            }
        }

        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        finish();
    }

    private int dpToPx(float dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private static int parseColor(String colorStr) {
        if (colorStr == null || colorStr.isEmpty()) throw new IllegalArgumentException();
        if (!colorStr.startsWith("#")) {
            colorStr = "#" + colorStr;
        }
        return Color.parseColor(colorStr);
    }

    // -- Adapter --

    private class CarouselFullscreenAdapter extends RecyclerView.Adapter<CarouselFullscreenViewHolder> {

        @NonNull
        @Override
        public CarouselFullscreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_carousel_fullscreen, parent, false);
            return new CarouselFullscreenViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarouselFullscreenViewHolder holder, int position) {
            holder.bind(mCarouselItems.get(position), pager.getHeight());
        }

        @Override
        public int getItemCount() {
            return mCarouselItems.size();
        }
    }

    private class CarouselFullscreenViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout heroContainer;
        private final ImageView heroImage;
        private final FrameLayout cardView;
        private final ImageView iconImage;
        private final TextView cardTitle;
        private final TextView cardBody;

        CarouselFullscreenViewHolder(@NonNull View itemView) {
            super(itemView);
            heroContainer = itemView.findViewById(R.id.hero_container);
            heroImage = itemView.findViewById(R.id.hero_image);
            cardView = itemView.findViewById(R.id.card_view);
            iconImage = itemView.findViewById(R.id.icon_image);
            cardTitle = itemView.findViewById(R.id.card_title);
            cardBody = itemView.findViewById(R.id.card_body);

            applyCardBorder();
        }

        private void applyCardBorder() {
            GradientDrawable cardBg = new GradientDrawable();
            cardBg.setColor(Color.WHITE);
            cardBg.setCornerRadius(dpToPx(22));
            cardBg.setStroke(dpToPx(1), Color.parseColor("#DDDDDD"));
            cardView.setBackground(cardBg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setClipToOutline(true);
            }
        }

        private void applyLayout(int totalHeight) {
            if (totalHeight <= 0) return;

            int baseHeroHeight = (int) (totalHeight * HERO_HEIGHT_FRACTION);
            int cardOverlapPx = dpToPx(CARD_OVERLAP_DP);
            int baseTopMargin = baseHeroHeight - cardOverlapPx;

            int widthPixels = cardView.getContext().getResources().getDisplayMetrics().widthPixels;
            int widthSpec = View.MeasureSpec.makeMeasureSpec(widthPixels - dpToPx(40), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            cardView.measure(widthSpec, heightSpec);
            int wantedHeight = cardView.getMeasuredHeight();

            int maxAllowedCardHeight = totalHeight - baseTopMargin - dpToPx(16);

            int finalTopMargin = baseTopMargin;
            int finalHeroHeight = baseHeroHeight;

            if (wantedHeight > maxAllowedCardHeight) {
                finalTopMargin = totalHeight - wantedHeight - dpToPx(16);
                finalHeroHeight = finalTopMargin + cardOverlapPx;
                if (finalTopMargin < dpToPx(16)) {
                    finalTopMargin = dpToPx(16);
                    finalHeroHeight = finalTopMargin + cardOverlapPx;
                }
            }

            ViewGroup.LayoutParams heroParams = heroContainer.getLayoutParams();
            heroParams.height = finalHeroHeight;
            heroContainer.setLayoutParams(heroParams);

            FrameLayout.LayoutParams cardParams = (FrameLayout.LayoutParams) cardView.getLayoutParams();
            cardParams.topMargin = finalTopMargin;
            cardParams.bottomMargin = dpToPx(16);
            cardView.setLayoutParams(cardParams);
        }

        void bind(CarouselItem item, int pagerHeight) {

            if (item.getBackgroundImage() != null && !item.getBackgroundImage().isEmpty()) {
                heroImage.setVisibility(View.VISIBLE);
                Picasso.get().load(item.getBackgroundImage()).into(heroImage);
                heroContainer.setBackgroundColor(Color.WHITE);
            } else {
                heroImage.setVisibility(View.GONE);
                int bgColor = Color.WHITE;
                if (item.getBackgroundColor() != null && !item.getBackgroundColor().isEmpty()) {
                    try {
                        bgColor = parseColor(item.getBackgroundColor());
                    } catch (Exception ignored) {}
                }
                heroContainer.setBackgroundColor(bgColor);
            }

            if (item.getImage() != null && !item.getImage().isEmpty()) {
                iconImage.setVisibility(View.VISIBLE);
                Picasso.get().load(item.getImage()).into(iconImage);
            } else {
                iconImage.setVisibility(View.GONE);
            }

            if (item.getTitle() != null && !item.getTitle().isEmpty()) {
                cardTitle.setVisibility(View.VISIBLE);
                cardTitle.setText(item.getTitle().replace("\\n", "\n"));
                if (item.getTitleColor() != null && !item.getTitleColor().isEmpty()) {
                    try { cardTitle.setTextColor(parseColor(item.getTitleColor())); } catch (Exception ignored) {}
                }
                cardTitle.setTypeface(item.getTitleFontFamily(CarouselFullscreenActivity.this));
                if (item.getTitleTextsize() != null && !item.getTitleTextsize().isEmpty()) {
                    try {
                        cardTitle.setTextSize(mapTextSize(Float.parseFloat(item.getTitleTextsize()), true));
                    } catch (NumberFormatException ignored) {}
                }
            } else {
                cardTitle.setVisibility(View.GONE);
            }

            if (item.getBody() != null && !item.getBody().isEmpty()) {
                cardBody.setVisibility(View.VISIBLE);
                cardBody.setText(item.getBody().replace("\\n", "\n"));
                if (item.getBodyColor() != null && !item.getBodyColor().isEmpty()) {
                    try { cardBody.setTextColor(parseColor(item.getBodyColor())); } catch (Exception ignored) {}
                }
                cardBody.setTypeface(item.getBodyFontFamily(CarouselFullscreenActivity.this));
                if (item.getBodyTextsize() != null && !item.getBodyTextsize().isEmpty()) {
                    try {
                        cardBody.setTextSize(mapTextSize(Float.parseFloat(item.getBodyTextsize()), false));
                    } catch (NumberFormatException ignored) {}
                }
            } else {
                cardBody.setVisibility(View.GONE);
            }

            applyLayout(pagerHeight);
        }
    }
}
