package com.visilabs.android.notifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.visilabs.android.R;
import com.visilabs.android.Visilabs;
import com.visilabs.android.api.VisilabsUpdateDisplayState;
import com.visilabs.android.util.VisilabsConfig;

@TargetApi(VisilabsConfig.UI_FEATURES_MIN_API)
@SuppressLint("ClickableViewAccessibility")
public class VisilabsNotificationFragment extends Fragment {


    private ImageView inAppImageView;


    class RetrieveImageTask extends AsyncTask<VisilabsNotification, Void, Bitmap> {

        protected Bitmap doInBackground(VisilabsNotification... notifications) {
            try {
                if (notifications != null && notifications.length > 0) {
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
            if (inAppImage == null) {
                return;
            }
            inAppImageView.setImageBitmap(inAppImage);
        }
    }


    public void setDisplayState(final int stateId, final VisilabsUpdateDisplayState.DisplayState.InAppNotificationState displayState) {
        mDisplayStateId = stateId;
        mDisplayState = displayState;
    }

    // It's safe to use onAttach(Activity) in API 23 as its implementation has not been changed.
    // Bypass the Lint check for now.
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mParent = activity;
        if (null == mDisplayState) {
            cleanUp();
            return;
        }

        // We have to manually clear these Runnables in onStop in case they exist, since they
        // do illegal operations when onSaveInstanceState has been called already.

        mHandler = new Handler();
        mRemover = new Runnable() {
            public void run() {
                VisilabsNotificationFragment.this.remove();
            }
        };
        mDisplayMini = new Runnable() {
            @Override
            public void run() {
                mInAppView.setVisibility(View.VISIBLE);
                mInAppView.setBackgroundColor(mDisplayState.getHighlightColor());
                mInAppView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return VisilabsNotificationFragment.this.mDetector.onTouchEvent(event);
                    }
                });
                //inAppImageView.setImageBitmap(mDisplayState.getInAppNotification().getImage());
                final ImageView notifImage = (ImageView) mInAppView.findViewById(R.id.com_visilabs_android_notification_image);




                final float heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, mParent.getResources().getDisplayMetrics());
                final TranslateAnimation translate = new TranslateAnimation(0, 0, heightPx, 0);
                translate.setInterpolator(new DecelerateInterpolator());
                translate.setDuration(200);
                mInAppView.startAnimation(translate);

                final ScaleAnimation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, heightPx / 2, heightPx / 2);
                scale.setInterpolator(new SineBounceInterpolator());
                scale.setDuration(400);
                scale.setStartOffset(200);
                notifImage.startAnimation(scale);
            }
        };

        mDetector = new GestureDetector(activity, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                if (velocityY > 0) {
                    remove();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) { }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) { }

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                final VisilabsNotification inApp = mDisplayState.getInAppNotification();

                final String uriString = inApp.getButtonURL();
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                    } catch (IllegalArgumentException e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                        return true;
                    }

                    try {
                        Visilabs.CallAPI().trackNotificationClick(inApp);


                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        mParent.startActivity(viewIntent);



                    } catch (ActivityNotFoundException e) {
                        Log.i(LOG_TAG, "User doesn't have an activity for notification URI " + uri);
                    }
                }

                remove();
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCleanedUp = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (null == mDisplayState) {
            cleanUp();
        } else {
            mInAppView = inflater.inflate(R.layout.com_visilabs_android_activity_notification_mini, container, false);
            final TextView titleView = (TextView) mInAppView.findViewById(R.id.com_visilabs_android_notification_title);
            inAppImageView = (ImageView) mInAppView.findViewById(R.id.com_visilabs_android_notification_image);

            VisilabsNotification inApp = mDisplayState.getInAppNotification();

            titleView.setText(inApp.getTitle());
            new RetrieveImageTask().execute(inApp);
            //inAppImageView.setImageBitmap(inApp.getImage());

            mHandler.postDelayed(mRemover, MINI_REMOVE_TIME);
        }

        return mInAppView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mCleanedUp) {
            mParent.getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // getHighlightColorFromBackground doesn't seem to work on onResume because the view
        // has not been fully rendered, so try and delay a little bit. This is also a bit better UX
        // by giving the user some time to process the new Activity before displaying the notification.
        mHandler.postDelayed(mDisplayMini, 500);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        cleanUp();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanUp();
    }

    private void cleanUp() {
        if (!mCleanedUp) {
            mHandler.removeCallbacks(mRemover);
            mHandler.removeCallbacks(mDisplayMini);
            VisilabsUpdateDisplayState.releaseDisplayState(mDisplayStateId);

            try {
                if(!mParent.isFinishing()) {
                    final FragmentManager fragmentManager = mParent.getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.remove(this).commitAllowingStateLoss();
                }
            }catch (Exception e) {
                Log.e(LOG_TAG, "Parent is finishing.", e);
            }
        }

        mCleanedUp = true;
    }

    private void remove() {
        if (mParent != null && !mCleanedUp) {
            mHandler.removeCallbacks(mRemover);
            mHandler.removeCallbacks(mDisplayMini);

            final FragmentManager fragmentManager = mParent.getFragmentManager();

            // setCustomAnimations works on a per transaction level, so the animations set
            // when this fragment was created do not apply
            try {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(0, R.anim.com_visilabs_android_slide_down).remove(this).commitAllowingStateLoss();
                VisilabsUpdateDisplayState.releaseDisplayState(mDisplayStateId);
                mCleanedUp = true;
            }catch (Exception e) {
                Log.e(LOG_TAG, "Fragment can not be removed.", e);
            }
        }
    }

    private class SineBounceInterpolator implements Interpolator {
        public SineBounceInterpolator() { }
        public float getInterpolation(float t) {
            return (float) -(Math.pow(Math.E, -8*t) * Math.cos(12*t)) + 1;
        }
    }

    private Activity mParent;
    private GestureDetector mDetector;
    private Handler mHandler;
    private int mDisplayStateId;
    private VisilabsUpdateDisplayState.DisplayState.InAppNotificationState mDisplayState;
    private Runnable mRemover, mDisplayMini;
    private View mInAppView;

    private boolean mCleanedUp;

    private static final int SHADOW_SIZE_THRESHOLD_PX = 100;

    private static final String LOG_TAG = "VisilabsFragment";
    private static final int MINI_REMOVE_TIME = 10000;
}

