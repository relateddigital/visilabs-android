package com.visilabs.inApp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AppUtils;
import com.visilabs.util.StringUtils;

public class FullscreenPopupActivity extends Activity {
    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";
    private static final String LOG_TAG = "FullscreenPopup";

    private InAppMessage inAppMessage;
    private int intentId = -1;
    private InAppButtonInterface buttonCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        intentId = savedInstanceState != null
                ? savedInstanceState.getInt(INTENT_ID_KEY, Integer.MAX_VALUE)
                : getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        inAppMessage = getInAppMessage();

        if (inAppMessage == null) {
            Log.e(LOG_TAG, "InAppMessage is null! Could not get display state!");
            VisilabsUpdateDisplayState.releaseDisplayState(intentId);
            finish();
            return;
        }

        buttonCallback = Visilabs.CallAPI().getInAppButtonInterface();
        setContentView(createContentView());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INTENT_ID_KEY, intentId);
    }

    private InAppMessage getInAppMessage() {
        VisilabsUpdateDisplayState updateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(intentId);
        if (updateDisplayState == null || updateDisplayState.getDisplayState() == null) {
            return null;
        }
        InAppNotificationState state = (InAppNotificationState) updateDisplayState.getDisplayState();
        return state != null ? state.getInAppMessage() : null;
    }

    private View createContentView() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(false);
        root.addView(imageView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        String imageUrl = inAppMessage.getActionData().getImg();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (AppUtils.isAnImage(imageUrl)) {
                Picasso.get().load(imageUrl).into(imageView);
            } else {
                Glide.with(this).load(imageUrl).into(imageView);
            }
        }

        imageView.setOnClickListener(v -> handleImageClick());
        root.addView(createCloseButton(), closeButtonLayoutParams());
        return root;
    }

    private TextView createCloseButton() {
        TextView closeButton = new TextView(this);
        closeButton.setText("×");
        closeButton.setGravity(Gravity.CENTER);
        closeButton.setTextSize(24);
        closeButton.setTextColor(parseCloseButtonColor(inAppMessage.getActionData().getCloseButtonColor()));
        closeButton.setBackgroundColor(Color.TRANSPARENT);
        closeButton.setOnClickListener(v -> {
            VisilabsUpdateDisplayState.releaseDisplayState(intentId);
            finish();
        });
        return closeButton;
    }

    private FrameLayout.LayoutParams closeButtonLayoutParams() {
        int size = dpToPx(44);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.TOP | Gravity.END;
        params.setMargins(0, dpToPx(24), dpToPx(12), 0);
        return params;
    }

    private int parseCloseButtonColor(String rawColor) {
        if (rawColor == null || rawColor.trim().isEmpty()) {
            return Color.WHITE;
        }
        String color = rawColor.trim();
        try {
            if ("black".equalsIgnoreCase(color)) return Color.BLACK;
            if ("white".equalsIgnoreCase(color)) return Color.WHITE;
            return Color.parseColor(color);
        } catch (IllegalArgumentException ignored) {
            return Color.WHITE;
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void handleImageClick() {
        Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, null);
        String link = inAppMessage.getActionData().getAndroidLnk();
        if (buttonCallback != null) {
            Visilabs.CallAPI().setInAppButtonInterface(null);
            buttonCallback.onPress(link);
        } else if (link != null && !link.isEmpty()) {
            try {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(link));
                startActivity(viewIntent);
            } catch (ActivityNotFoundException e) {
                Log.i("Visilabs", "User doesn't have an activity for notification URI");
            }
        }
        VisilabsUpdateDisplayState.releaseDisplayState(intentId);
        finish();
    }
}
