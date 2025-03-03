package com.visilabs.inApp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;

import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentHalfScreenBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HalfScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HalfScreenFragment extends Fragment {

    private static final String LOG_TAG = "HalfScreenFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "stateIdKey";
    private static final String ARG_PARAM2 = "inAppStateKey";

    private int mStateId;
    private InAppNotificationState mInAppState;
    private InAppMessage mInAppMessage;
    private boolean mIsTop;
    private FragmentHalfScreenBinding binding;
    private ExoPlayer player = null;


    public HalfScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stateId Parameter 1.
     * @param inAppState Parameter 2.
     * @return A new instance of fragment HalfScreenFragment.
     */
    public static HalfScreenFragment newInstance(int stateId, InAppNotificationState inAppState) {
        HalfScreenFragment fragment = new HalfScreenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, stateId);
        args.putParcelable(ARG_PARAM2, inAppState);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStateId = getArguments().getInt(ARG_PARAM1);
        mInAppState = getArguments().getParcelable(ARG_PARAM2);
        if(mInAppState != null) {
            mInAppMessage = mInAppState.getInAppMessage();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHalfScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        hideStatusBar();

        if (mInAppState != null) {
            if(mInAppMessage == null) {
                endFragment();
                Log.e(LOG_TAG, "Could not get the data, closing in app");
            } else {
                setupInitialView();
            }

        } else {
            endFragment();
            Log.e(LOG_TAG, "Could not get the data, closing in app");
        }
        return view;
    }

    private void setupInitialView() {
        mIsTop = mInAppMessage.getActionData().getPos().equals("top");
        if(mIsTop){
            adjustTop();
        } else {
            adjustBottom();
        }
        setupCloseButton();
    }

    private void adjustTop() {
        binding.halfScreenContainerBot.setVisibility(View.GONE);
        binding.halfScreenContainerTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            getActivity().startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                        }
                    }
                }
                endFragment();
            }
        });

        if(mInAppMessage.getActionData().getMsgTitle() != null && !mInAppMessage.getActionData().getMsgTitle().isEmpty()) {
            binding.halfScreenContainerTop.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getBackground()));
            binding.topTitleView.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
            binding.topTitleView.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleColor()));
            binding.topTitleView.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgTitleTextSize()) * 2 + 14);
            binding.topTitleView.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
        } else {
            binding.topTitleView.setVisibility(View.GONE);
        }

        if(mInAppMessage.getActionData().getImg() != null &&
                !mInAppMessage.getActionData().getImg().equals("") &&
                !mInAppMessage.getActionData().getImg().isEmpty()) {
            binding.topImageView.setVisibility(View.VISIBLE);
            binding.topVideoView.setVisibility(View.GONE);
            if (AppUtils.isAnImage(mInAppMessage.getActionData().getImg())) {
                Picasso.get().
                        load(mInAppMessage.getActionData().getImg())
                        .into(binding.topImageView);
            } else {
                Glide.with(getActivity())
                        .load(mInAppMessage.getActionData().getImg())
                        .into(binding.topImageView);
            }
        } else {
            binding.topImageView.setVisibility(View.GONE);
            if(mInAppMessage.getActionData().getVideoUrl() != null && !mInAppMessage.getActionData().getVideoUrl().equals("")) {
                binding.topVideoView.setVisibility(View.VISIBLE);
                initializePlayer();
                startPlayer();
            } else {
                binding.topVideoView.setVisibility(View.GONE);
                releasePlayer();
            }
        }
    }

    private void adjustBottom() {
        binding.halfScreenContainerTop.setVisibility(View.GONE);

        binding.halfScreenContainerBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            getActivity().startActivity(viewIntent);
                        } catch (Exception e) {
                            Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                        }
                    }
                }
                endFragment();
            }
        });

        if(mInAppMessage.getActionData().getMsgTitle() != null && !mInAppMessage.getActionData().getMsgTitle().isEmpty()) {
            binding.halfScreenContainerBot.setBackgroundColor(Color.parseColor(mInAppMessage.getActionData().getBackground()));
            binding.botTitleView.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
            binding.botTitleView.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleColor()));
            binding.botTitleView.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgTitleTextSize()) * 2 + 14);
            binding.botTitleView.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
        } else {
            binding.botTitleView.setVisibility(View.GONE);
        }

        if(mInAppMessage.getActionData().getImg() != null &&
                !mInAppMessage.getActionData().getImg().equals("") &&
                !mInAppMessage.getActionData().getImg().isEmpty()) {
            binding.botImageView.setVisibility(View.VISIBLE);
            binding.botVideoView.setVisibility(View.GONE);
            if (AppUtils.isAnImage(mInAppMessage.getActionData().getImg())) {
                Picasso.get().
                        load(mInAppMessage.getActionData().getImg())
                        .into(binding.botImageView);
            } else {
                Glide.with(getActivity())
                        .load(mInAppMessage.getActionData().getImg())
                        .into(binding.botImageView);
            }
        } else {
            binding.botImageView.setVisibility(View.GONE);
            if(mInAppMessage.getActionData().getVideoUrl() != null && !mInAppMessage.getActionData().getVideoUrl().equals("")) {
                binding.botVideoView.setVisibility(View.VISIBLE);
                initializePlayer();
                startPlayer();
            } else {
                binding.botVideoView.setVisibility(View.GONE);
                releasePlayer();
            }
        }
    }

    private void setupCloseButton() {
        if(mIsTop){
            binding.topCloseButton.setBackgroundResource(getCloseIcon());
            binding.topCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        } else {
            binding.botCloseButton.setBackgroundResource(getCloseIcon());
            binding.botCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        }
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

    private void endFragment() {
        if(getActivity() != null) {
            VisilabsUpdateDisplayState.releaseDisplayState(mStateId);
            releasePlayer();
            getActivity().getFragmentManager().beginTransaction().remove(HalfScreenFragment.this).commit();
        }
    }

    private void hideStatusBar() {
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
    }

    private void showStatusBar() {
        if(getActivity() != null) {
            WindowInsetsControllerCompat windowInsetsController =
                    ViewCompat.getWindowInsetsController(getActivity().getWindow().getDecorView());
            if (windowInsetsController != null) {
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
            }
        }
    }

    private void initializePlayer() {
        player = new androidx.media3.exoplayer.ExoPlayer.Builder(getActivity()).build();
        if (mIsTop) {
            binding.topVideoView.setPlayer(player);
        } else {
            binding.botVideoView.setPlayer(player);
        }
        MediaItem mediaItem = MediaItem.fromUri(mInAppMessage.getActionData().getVideoUrl());
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
    public void onDestroyView() {
        super.onDestroyView();
        showStatusBar();
        releasePlayer();
    }
}