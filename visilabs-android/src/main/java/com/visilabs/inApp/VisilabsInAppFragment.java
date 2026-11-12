package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.FragmentInAppMiniBinding;
import com.visilabs.android.databinding.FragmentInAppMiniTopBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AnimationManager;

public class VisilabsInAppFragment extends Fragment {

    private static final String LOG_TAG = "VisilabsFragment";

    private Activity mParent;
    private GestureDetector mDetector;
    private Handler mHandler;
    private int mInAppStateId = 0;
    private InAppNotificationState mInAppNotificationState;
    private InAppMessage mInAppMessage;
    private Runnable mRemover;
    private Runnable mDisplayMini;
    private boolean mCleanedUp = false;
    private FragmentInAppMiniBinding binding;
    private FragmentInAppMiniTopBinding bindingTop;
    private int MINI_REMOVE_TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCleanedUp = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mInAppNotificationState == null || mInAppMessage == null || mInAppMessage.getActionData() == null) {
            Log.e(LOG_TAG, "InAppMessage is null! Closing the mini in app.");
            cleanUp();
            return null;
        }
        try {
            if (isTop()) {
                bindingTop = FragmentInAppMiniTopBinding.inflate(inflater, container, false);
                View viewTop = bindingTop.getRoot();
                bindingTop.tvInAppTitleMini.setText(InAppSafeUtils.replaceNewLines(mInAppMessage.getActionData().getMsgTitle()));
                bindingTop.tvInAppTitleMini.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
                if(mInAppMessage.getActionData().getMsgTitleColor() != null) {
                    bindingTop.tvInAppTitleMini.setTextColor(InAppSafeUtils.parseColor(mInAppMessage.getActionData().getMsgTitleColor(), bindingTop.tvInAppTitleMini.getCurrentTextColor()));
                }
                setCloseButton();
                String img = mInAppMessage.getActionData().getImg();
                if (!InAppSafeUtils.isBlank(img)) {
                    bindingTop.ivInAppImageMini.setVisibility(View.VISIBLE);
                    Picasso.get().load(img).into(bindingTop.ivInAppImageMini);
                } else {
                    bindingTop.ivInAppImageMini.setVisibility(View.GONE);
                }
                delay();
                mHandler.postDelayed(mRemover, MINI_REMOVE_TIME);
                return viewTop;
            } else {
                binding = FragmentInAppMiniBinding.inflate(inflater, container, false);
                View view = binding.getRoot();
                binding.tvInAppTitleMini.setText(InAppSafeUtils.replaceNewLines(mInAppMessage.getActionData().getMsgTitle()));
                binding.tvInAppTitleMini.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
                if(mInAppMessage.getActionData().getMsgTitleColor() != null) {
                    binding.tvInAppTitleMini.setTextColor(InAppSafeUtils.parseColor(mInAppMessage.getActionData().getMsgTitleColor(), binding.tvInAppTitleMini.getCurrentTextColor()));
                }
                setCloseButton();
                String img = mInAppMessage.getActionData().getImg();
                if (!InAppSafeUtils.isBlank(img)) {
                    binding.ivInAppImageMini.setVisibility(View.VISIBLE);
                    Picasso.get().load(img).into(binding.ivInAppImageMini);
                } else {
                    binding.ivInAppImageMini.setVisibility(View.GONE);
                }
                if (mInAppMessage.getActionData().getDuration() != null) {
                    delay();
                }
                mHandler.postDelayed(mRemover, MINI_REMOVE_TIME);
                return view;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not render the mini in app, closing.", e);
            cleanUp();
            return null;
        }
    }

    private boolean isTop() {
        return mInAppMessage != null && mInAppMessage.getActionData() != null &&
                "top".equals(mInAppMessage.getActionData().getPos());
    }

    public void setCloseButton() {
        String closeButtonColor = mInAppMessage.getActionData().getCloseButtonColor();
        if (!InAppSafeUtils.isBlank(closeButtonColor)) {
            if (isTop()) {
                bindingTop.ibClose.setVisibility(View.VISIBLE);
                bindingTop.ibClose.setColorFilter(InAppSafeUtils.parseColor(closeButtonColor, Color.WHITE));
                bindingTop.ibClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove();
                    }
                });
            } else {
                binding.ibClose.setVisibility(View.VISIBLE);
                binding.ibClose.setColorFilter(InAppSafeUtils.parseColor(closeButtonColor, Color.WHITE));
                binding.ibClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove();
                    }
                });
            }
        }
    }



    public void setInAppState(int stateId, InAppNotificationState inAppState) {
        mInAppStateId = stateId;
        mInAppNotificationState = inAppState;
        if(mInAppNotificationState != null) {
            mInAppMessage = mInAppNotificationState.getInAppMessage();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mCleanedUp && mParent != null && isAdded()) {
            mParent.getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mInAppMessage != null && mDisplayMini != null) {
            mHandler.postDelayed(mDisplayMini, 500);
        }
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
        mHandler = new Handler();
        mRemover = new Runnable() {
            public void run() {
                VisilabsInAppFragment.this.remove();
            }
        };
        if(mInAppMessage == null || mInAppNotificationState == null) {
            Log.e(LOG_TAG, "InAppMessage is null! Could not get display state!");
            cleanUp();
        } else {
            displayMiniInApp();

            setGestureDetector(getActivity());
        }
    }

    private void displayMiniInApp() {
        mDisplayMini = new Runnable() {
            @Override
            public void run() {
                View view = getView();
                if (view == null || mCleanedUp) {
                    return;
                }
                try {
                    view.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(InAppSafeUtils.parseColor(mInAppMessage.getActionData().getBackground(),
                            mInAppNotificationState.getHighlightColor()));
                    view.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            return mDetector != null && VisilabsInAppFragment.this.mDetector.onTouchEvent(event);
                        }
                    });
                    if (isTop() && bindingTop != null) {
                        view.startAnimation(AnimationManager.getMiniTranslateTopAnimation(getActivity()));
                        bindingTop.ivInAppImageMini.startAnimation(AnimationManager.getMiniScaleAnimation(getActivity()));
                    } else if (binding != null) {
                        view.startAnimation(AnimationManager.getMiniTranslateAnimation(getActivity()));
                        binding.ivInAppImageMini.startAnimation(AnimationManager.getMiniScaleAnimation(getActivity()));
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not display the mini in app.", e);
                    remove();
                }
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

                final String uriString = mInAppMessage.getActionData().getAndroidLnk();
                InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
                Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, null);
                if(buttonInterface != null) {
                    Visilabs.CallAPI().setInAppButtonInterface(null);
                    buttonInterface.onPress(uriString);
                } else {
                    if (uriString != null && uriString.length() > 0) {
                        Uri uri;
                        try {
                            uri = Uri.parse(uriString);
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                            mParent.startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                            return true;
                        }
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
            if (mHandler != null) {
                mHandler.removeCallbacks(mRemover);
                if (mDisplayMini != null) {
                    mHandler.removeCallbacks(mDisplayMini);
                }
            }
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
            if (mDisplayMini != null) {
                mHandler.removeCallbacks(mDisplayMini);
            }

            final FragmentManager fragmentManager = mParent.getFragmentManager();

            try {
                if (isTop()) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(0, R.anim.slide_up).remove(this).commitAllowingStateLoss();
                    VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
                    mCleanedUp = true;
                } else {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(0, R.anim.anim_slide_down).remove(this).commitAllowingStateLoss();
                    VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
                    mCleanedUp = true;
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Fragment can not be removed.", e);
            }
        }
    }
    private void delay() {
        if (mInAppMessage != null) {
            if (mInAppMessage.getActionData().getDuration() != null) {
                MINI_REMOVE_TIME = (mInAppMessage.getActionData().getDuration() * 1000) + 1000;
            } else {
                MINI_REMOVE_TIME = 5000;
            }
        }
    }
}

