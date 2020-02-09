package com.visilabs.android.notifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visilabs.android.R;
import com.visilabs.android.Visilabs;
import com.visilabs.android.api.VisilabsUpdateDisplayState;
import com.visilabs.android.util.VisilabsConfig;

import java.net.URL;


/**
 * Activity used internally by Visilabs to display surveys and inapp full screen notifications.
 */
@TargetApi(VisilabsConfig.UI_FEATURES_MIN_API)
@SuppressLint("ClickableViewAccessibility")
public class VisilabsNotificationActivity_2 extends Activity {

    private FadingImageView inAppImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);
        if (null == mUpdateDisplayState) {
            Log.e(LOG_TAG, "VisilabsNotificationActivity_2 intent received, but nothing was found to show.");
            finish();
            return;
        }

        if (isShowingInApp()) {
            onCreateInAppNotification(savedInstanceState);
        }
        else {
            finish();
        }
    }

    class RetrieveImageTask extends AsyncTask<VisilabsNotification, Void, Bitmap> {

        protected Bitmap doInBackground(VisilabsNotification... notifications) {
            try {
                if(notifications != null && notifications.length > 0){
                    VisilabsNotification notification = notifications[0];
                    return notification.getImage();
                }
                return null;

            } catch (Exception e) {
                Log.e(LOG_TAG, "Can not get image.", e);
                return null;
            }
        }

        protected void onPostExecute(Bitmap inAppImage) {
            if(inAppImage == null){
                return;
            }

            if (inAppImage.getWidth() < SHADOW_SIZE_THRESHOLD_PX || inAppImage.getHeight() < SHADOW_SIZE_THRESHOLD_PX) {
                inAppImageView.setBackgroundResource(R.drawable.com_visilabs_android_square_nodropshadow);
            } else {
                int h = inAppImage.getHeight() / 100;
                int w = inAppImage.getWidth() / 100;
                final Bitmap scaledImage = Bitmap.createScaledBitmap(inAppImage, w, h, false);
                int averageColor;
                int averageAlpha;
                outerloop:
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        averageColor = scaledImage.getPixel(x, y);
                        averageAlpha = Color.alpha(averageColor);
                        if (averageAlpha < 0xFF) {
                            inAppImageView.setBackgroundResource(R.drawable.com_visilabs_android_square_nodropshadow);
                            break outerloop;
                        }
                    }
                }
            }
            inAppImageView.setImageBitmap(inAppImage);
        }
    }


    private void onCreateInAppNotification(Bundle savedInstanceState) {

        setContentView(R.layout.com_visilabs_android_activity_notification_full_2);

        final ImageView backgroundImage = (ImageView) findViewById(R.id.com_mixpanel_android_notification_gradient);
        inAppImageView = (FadingImageView) findViewById(R.id.com_mixpanel_android_notification_image);
        final TextView titleView = (TextView) findViewById(R.id.com_mixpanel_android_notification_title);
        final TextView subtextView = (TextView) findViewById(R.id.com_mixpanel_android_notification_subtext);
        //final Button ctaButton = (Button) findViewById(R.id.com_mixpanel_android_notification_button);
        //final Button secondButton = (Button) findViewById(R.id.com_mixpanel_android_notification_second_button);
        final LinearLayout closeButtonWrapper = (LinearLayout) findViewById(R.id.com_mixpanel_android_button_exit_wrapper);





        final VisilabsUpdateDisplayState.DisplayState.InAppNotificationState notificationState =
                (VisilabsUpdateDisplayState.DisplayState.InAppNotificationState) mUpdateDisplayState.getDisplayState();
        final VisilabsNotification inApp = notificationState.getInAppNotification();

        // Layout
        final Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) closeButtonWrapper.getLayoutParams();
            params.setMargins(0, 0, 0, (int) (size.y * 0.06f)); // make bottom margin 6% of screen height
            closeButtonWrapper.setLayoutParams(params);
        }

        final GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, // Ignored in radial gradients
                new int[]{ 0xE560607C, 0xE548485D, 0xE518181F, 0xE518181F }
        );
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gd.setGradientCenter(0.25f, 0.5f);
            gd.setGradientRadius(Math.min(size.x, size.y) * 0.8f);
        } else {
            gd.setGradientCenter(0.5f, 0.33f);
            gd.setGradientRadius(Math.min(size.x, size.y) * 0.7f);
        }

        setViewBackground(backgroundImage, gd);

        titleView.setText(inApp.getTitle());
        subtextView.setText(inApp.getBody());


        new RetrieveImageTask().execute(inApp);

        inAppImageView.setBackgroundResource(R.drawable.com_visilabs_android_square_dropshadow);


        /*
        if (inApp.getButtonText() != null && inApp.getButtonText().length() > 0) {
            ctaButton.setText(inApp.getButtonText());
        }
        */

        // Listeners
/*        ctaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uriString = inApp.getButtonURL();
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                    } catch (final IllegalArgumentException e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                        return;
                    }

                    try {
                        Visilabs.CallAPI().trackNotificationClick(inApp);


                        //final Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        //VisilabsNotificationActivity_2.this.startActivity(viewIntent);


                    } catch (final ActivityNotFoundException e) {
                        Log.i(LOG_TAG, "User doesn't have an activity for notification URI");
                    }
                }
                //finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });
        ctaButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.com_visilabs_android_cta_button_highlight);
                } else {
                    v.setBackgroundResource(R.drawable.com_visilabs_android_cta_button);
                }
                return false;
            }
        });*/
        closeButtonWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });


/*
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uriString = inApp.getButtonURL();
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                    } catch (final IllegalArgumentException e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                        return;
                    }

                    try {
                        Visilabs.CallAPI().trackNotificationClick(inApp);


                        //final Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        //VisilabsNotificationActivity_2.this.startActivity(viewIntent);


                    } catch (final ActivityNotFoundException e) {
                        Log.i(LOG_TAG, "User doesn't have an activity for notification URI");
                    }
                }
                //finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });
        secondButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.com_visilabs_android_cta_button_highlight);
                } else {
                    v.setBackgroundResource(R.drawable.com_visilabs_android_cta_button);
                }
                return false;
            }
        });
*/



        // Animations
        final ScaleAnimation scale = new ScaleAnimation(
                .95f, 1.0f, .95f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        scale.setDuration(200);
        inAppImageView.startAnimation(scale);

        final TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        translate.setInterpolator(new DecelerateInterpolator());
        translate.setDuration(200);
        titleView.startAnimation(translate);
        subtextView.startAnimation(translate);
        //ctaButton.startAnimation(translate);
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.com_visilabs_android_fade_in);
        closeButtonWrapper.startAnimation(fadeIn);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        if (isShowingInApp()) {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        }
        super.onBackPressed();
    }


    private VisilabsUpdateDisplayState.DisplayState.InAppNotificationState getSurveyState() {
        // Throws if this is showing an InApp
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

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setViewBackground(View v, Drawable d) {
        if (Build.VERSION.SDK_INT < 16) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    @SuppressLint("NewApi")
    private void requestOrientationLock() {
        if (Build.VERSION.SDK_INT >= 18) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        } else {
            final int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    private AlertDialog mDialog;


    private TextView mProgressTextView;

    private VisilabsUpdateDisplayState mUpdateDisplayState;


    private int mIntentId = -1;


    private static final int GRAY_30PERCENT = Color.argb(255, 90, 90, 90);
    private static final int SHADOW_SIZE_THRESHOLD_PX = 100;

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "VslbsNtfctnActivity_2";

    public static final String INTENT_ID_KEY = "com.visilabs.android.notifications.VisilabsNotificationActivity_2.INTENT_ID_KEY";
}
