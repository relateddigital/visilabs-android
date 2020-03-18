package com.visilabs.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.android.databinding.ActivityVisilabsNotificationBinding;
import com.visilabs.util.StringUtils;


public class VisilabsNotificationActivity extends Activity implements IVisilabs {

    ActivityVisilabsNotificationBinding mainBinding;

    VisilabsNotification inApp;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    private static final int SHADOW_SIZE_THRESHOLD_PX = 100;

    public static final String INTENT_ID_KEY = "com.visilabs.notifications.VisilabsNotificationActivity.INTENT_ID_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);

        if (null == mUpdateDisplayState) {

            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");
            finish();
            return;
        }

        if (isShowingInApp()) {
            setUpView();
        } else {
            finish();
        }
    }

    @Override
    public void setUpView() {

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_visilabs_notification);

   //     AnimationManager.setBackgroundGradient(mainBinding.ivNotDisplay, AnimationManager.getGradient(mainBinding.llClose, this));

        mainBinding.fivInAppImage.setBackgroundResource(R.drawable.bg_square_dropshadow);

        setAnimationToViews();

        setInAppData();

        clickEvents();
    }


    class RetrieveImageTask extends AsyncTask<VisilabsNotification, Void, Bitmap> {

        protected Bitmap doInBackground(VisilabsNotification... notifications) {
            try {
                if (notifications != null && notifications.length > 0) {
                    VisilabsNotification notification = notifications[0];
                    return notification.getImage();
                }
                return null;

            } catch (Exception e) {
                Log.e("Visilabs", "Can not get image.", e);
                return null;
            }
        }

        protected void onPostExecute(Bitmap inAppImage) {
            if (inAppImage == null) {
                return;
            }

            setInAppImageBackground(inAppImage);
        }
    }

    private void setInAppData() {

        VisilabsUpdateDisplayState.DisplayState.InAppNotificationState notificationState =
                (VisilabsUpdateDisplayState.DisplayState.InAppNotificationState) mUpdateDisplayState.getDisplayState();

        inApp = notificationState.getInAppNotification();

        mainBinding.tvInAppTitle.setText(inApp.getTitle());
        mainBinding.tvInAppSubtitle.setText(inApp.getBody());

        if (inApp.getButtonText() != null && inApp.getButtonText().length() > 0) {
            mainBinding.btnInApp.setText(inApp.getButtonText());
        }

        new RetrieveImageTask().execute(inApp);
    }

    private void setInAppImageBackground(Bitmap inAppImage) {
        if (inAppImage.getWidth() < SHADOW_SIZE_THRESHOLD_PX || inAppImage.getHeight() < SHADOW_SIZE_THRESHOLD_PX) { mainBinding.fivInAppImage.setBackgroundResource(R.drawable.bg_square_nodropshadow);
        } else {
           AnimationManager.setNoDropShadowBackgroundToView(mainBinding.fivInAppImage, inAppImage);

            ImageView iv = (ImageView) mainBinding.fivInAppImage;
            iv.setImageBitmap(inAppImage);
        }


    }

    private void setAnimationToViews() {

        mainBinding.fivInAppImage.startAnimation(AnimationManager.getScaleAnimation());
        mainBinding.tvInAppTitle.startAnimation(AnimationManager.getTranslateAnimation());
        mainBinding.tvInAppSubtitle.startAnimation(AnimationManager.getTranslateAnimation());
        mainBinding.btnInApp.startAnimation(AnimationManager.getTranslateAnimation());
        mainBinding.llClose.startAnimation(AnimationManager.getFadeInAnimation(this));

    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {

        mainBinding.btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inApp.getButtonURL() != null && inApp.getButtonURL().length() > 0) {

                    try {
                        Visilabs.CallAPI().trackNotificationClick(inApp);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(inApp.getButtonURL()));
                        VisilabsNotificationActivity.this.startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                }
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });


        mainBinding.btnInApp.setOnTouchListener(new View.OnTouchListener() {
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

        mainBinding.llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isShowingInApp()) {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        }
        super.onBackPressed();
    }


    private VisilabsUpdateDisplayState.DisplayState.InAppNotificationState getSurveyState() {
        return (VisilabsUpdateDisplayState.DisplayState.InAppNotificationState) mUpdateDisplayState.getDisplayState();
    }

    private boolean isShowingSurvey() {
        if (null == mUpdateDisplayState) {
            return false;
        }
        return VisilabsUpdateDisplayState.DisplayState.InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }

    private boolean isShowingInApp() {
        if (null == mUpdateDisplayState) {
            return false;
        }
        return VisilabsUpdateDisplayState.DisplayState.InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }
}
