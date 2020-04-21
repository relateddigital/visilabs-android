package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AnimationManager;

public class VisilabsInAppFragment extends Fragment {

    private Activity mParent;
    private GestureDetector mDetector;
    private Handler mHandler;
    private int mInAppStateId;
    private InAppNotificationState inAppNotificationState;
    private Runnable mRemover, mDisplayMini;
    private boolean mCleanedUp;

    private static final String LOG_TAG = "VisilabsFragment";
    private static final int MINI_REMOVE_TIME = 10000;

    TextView tvInAppTitleMini;
    ImageView ivInAppImageMini;

    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCleanedUp = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(inAppNotificationState != null) {

             rootView = inflater.inflate(R.layout.fragment_in_app_mini, container, false);

            InAppMessage inApp = inAppNotificationState.getInAppMessage();

            tvInAppTitleMini.setText(inApp.getTitle());

            setInAppImage(inApp);

            mHandler.postDelayed(mRemover, MINI_REMOVE_TIME);

        } else {
            cleanUp();
        }

        return rootView;
    }

    public void setInAppState(int stateId, InAppNotificationState inAppState) {
        this.mInAppStateId = stateId;
        this.inAppNotificationState = inAppState;
    }

    private void setInAppImage(InAppMessage inApp) {
        new RetrieveImageTask(new AsyncResponse() {
            @Override
            public void processFinish(Bitmap output) {
                ivInAppImageMini.setImageBitmap(output);
            }
        }).execute(inApp);
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
        mHandler.postDelayed(mDisplayMini, 500);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        cleanUp();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mParent = activity;
        if (inAppNotificationState == null) {
            cleanUp();
            return;
        }

        mHandler = new Handler();
        mRemover = new Runnable() {
            public void run() {
                VisilabsInAppFragment.this.remove();
            }
        };

        displayMiniInApp();

        setGestureDetector(getActivity());
    }

    private void displayMiniInApp() {
        mDisplayMini = new Runnable() {
            @Override
            public void run() {
                getView().setVisibility(View.VISIBLE);
                getView().setBackgroundColor(inAppNotificationState.getHighlightColor());
                getView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return VisilabsInAppFragment.this.mDetector.onTouchEvent(event);
                    }
                });

                getView().startAnimation(AnimationManager.getMiniTranslateAnimation(getActivity()));

                ivInAppImageMini.startAnimation(AnimationManager.getMiniScaleAnimation(getActivity()));
            }
        };
    }

    private void setGestureDetector(Context activity) {

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
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                final InAppMessage inApp = inAppNotificationState.getInAppMessage();

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
                        Visilabs.CallAPI().trackInAppMessageClick(inApp, null );


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
    public void onPause() {
        super.onPause();
        cleanUp();
    }

    private void cleanUp() {
        if (!mCleanedUp) {
            mHandler.removeCallbacks(mRemover);
            mHandler.removeCallbacks(mDisplayMini);
            VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);

            try {
                if (!mParent.isFinishing()) {
                    final FragmentManager fragmentManager = mParent.getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.remove(this).commitAllowingStateLoss();
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Parent is finishing.", e);
            }
        }

        mCleanedUp = true;
    }

    @SuppressLint("ResourceType")
    private void remove() {
        if (mParent != null && !mCleanedUp) {
            mHandler.removeCallbacks(mRemover);
            mHandler.removeCallbacks(mDisplayMini);

            final FragmentManager fragmentManager = mParent.getFragmentManager();

            try {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(0, R.anim.anim_slide_down).remove(this).commitAllowingStateLoss();
                VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
                mCleanedUp = true;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Fragment can not be removed.", e);
            }
        }
    }
}

