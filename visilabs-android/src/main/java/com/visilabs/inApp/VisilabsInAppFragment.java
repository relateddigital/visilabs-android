package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
    private static final int MINI_REMOVE_TIME = 10000;

    private Activity mParent;
    private GestureDetector mDetector;
    private Handler mHandler;
    private int mInAppStateId;
    private InAppNotificationState mInAppNotificationState;
    private InAppMessage mInAppMessage;
    private Runnable mRemover, mDisplayMini;
    private boolean mCleanedUp;
    private FragmentInAppMiniBinding binding;
    private FragmentInAppMiniTopBinding bindingTop;
    private boolean useBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCleanedUp = false;
        useBinding = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(useBinding){

            binding = FragmentInAppMiniBinding.inflate(inflater, container, false);
            View view = binding.getRoot();


        if (mInAppNotificationState != null) {
            if(mInAppMessage == null) {
                remove();
            } else {

                binding.tvInAppTitleMini.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
                binding.tvInAppTitleMini.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
                // TODO when backend ready setCloseButton()
                //setCloseButton()
                if (!mInAppMessage.getActionData().getImg().equals("")) {
                    binding.ivInAppImageMini.setVisibility(View.VISIBLE);
                    Picasso.get().load(mInAppMessage.getActionData().getImg()).into(binding.ivInAppImageMini);
                } else {
                    binding.ivInAppImageMini.setVisibility(View.GONE);
                }

                mHandler.postDelayed(mRemover, MINI_REMOVE_TIME);
            }

        } else {
            cleanUp();
            view = null;
        }

        return view;
    }

    else {

        bindingTop = FragmentInAppMiniTopBinding.inflate(inflater, container, false);
        View viewTop = bindingTop.getRoot();
        if (mInAppNotificationState != null) {
            if(mInAppMessage == null) {
                remove();
            } else {

                bindingTop.tvInAppTitleMini.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
                bindingTop.tvInAppTitleMini.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));

                if (!mInAppMessage.getActionData().getImg().equals("")) {
                    bindingTop.ivInAppImageMini.setVisibility(View.VISIBLE);
                    Picasso.get().load(mInAppMessage.getActionData().getImg()).into(bindingTop.ivInAppImageMini);
                } else {
                    bindingTop.ivInAppImageMini.setVisibility(View.GONE);
                }

                mHandler.postDelayed(mRemover, MINI_REMOVE_TIME);
            }

        } else {
            cleanUp();
            viewTop = null;
        }
        return viewTop;
    }

    }

    private int getCloseIcon() {
        if (mInAppMessage != null && mInAppMessage.getActionData() != null) {
            String closeButtonColor = mInAppMessage.getActionData().getCloseButtonColor();
            if ("white".equals(closeButtonColor)) {
                return R.drawable.ic_close_white_24dp;
            } else if ("black".equals(closeButtonColor)) {
                return R.drawable.ic_close_black_24dp;
            }
        }
        return R.drawable.ic_close_black_24dp;
    }

    public void setCloseButton() {
        // TODO: up or bottom
    /*
    if (mInAppMessage != null && mInAppMessage.getActionData() != null) {
        if (mInAppMessage.getActionData().isUp()) {
            binding.ibClose.setVisibility(View.VISIBLE);
            binding.ibClose.setBackgroundResource(R.drawable.ic_close_white_24dp);
            binding.ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove();
                }
            });
        } else {
            bindingTop.ibClose.setVisibility(View.VISIBLE);
            bindingTop.ibClose.setBackgroundResource(R.drawable.ic_close_white_24dp);
            bindingTop.ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove();
                }
            });
        }
    }
    */
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

        if (mCleanedUp) {
            mParent.getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mInAppMessage != null) {
            // TODO when backend ready make arrangement for delay time here
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
                getView().setVisibility(View.VISIBLE);
                getView().setBackgroundColor(mInAppNotificationState.getHighlightColor());
                getView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return VisilabsInAppFragment.this.mDetector.onTouchEvent(event);
                    }
                });
                if(useBinding){
                getView().startAnimation(AnimationManager.getMiniTranslateAnimation(getActivity()));

                binding.ivInAppImageMini.startAnimation(AnimationManager.getMiniScaleAnimation(getActivity()));


                    }
                else {
                    getView().startAnimation(AnimationManager.getMiniTranslateTopAnimation(getActivity()));

                    bindingTop.ivInAppImageMini.startAnimation(AnimationManager.getMiniScaleAnimation(getActivity()));
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
                // TODO When backend ready arrange close animation here
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

