package com.visilabs.inApp;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        binding = FragmentHalfScreenBinding.inflate(AppUtils.ensureCompatInflater(inflater), container, false);
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
        setupPromotionCode();
    }

    private void setupPromotionCode() {
        String promoCode = mInAppMessage.getActionData().getPromotionCode();
        if (promoCode != null && !promoCode.isEmpty()) {
            String bgColor = mInAppMessage.getActionData().getPromotionBackgroundColor();
            String textColor = mInAppMessage.getActionData().getPromotionTextColor();
            
            if (mIsTop) {
                binding.topPromotionContainer.setVisibility(View.VISIBLE);
                try {
                    if (bgColor != null && !bgColor.isEmpty()) {
                        binding.topPromotionContainer.setBackgroundColor(Color.parseColor(bgColor));
                    }
                } catch (Exception e) {}
                binding.topPromotionCodeText.setText(promoCode);
                try {
                    if (textColor != null && !textColor.isEmpty()) {
                        binding.topPromotionCodeText.setTextColor(Color.parseColor(textColor));
                    }
                } catch (Exception e) {}
                binding.topPromotionCodeText.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgTitleTextSize()) * 2 + 8);
                binding.topPromotionCodeText.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
                binding.topPromotionCopyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyToClipboard(promoCode, binding.topPromotionCopyButton);
                    }
                });
            } else {
                binding.botPromotionContainer.setVisibility(View.VISIBLE);
                try {
                    if (bgColor != null && !bgColor.isEmpty()) {
                        binding.botPromotionContainer.setBackgroundColor(Color.parseColor(bgColor));
                    }
                } catch (Exception e) {}
                binding.botPromotionCodeText.setText(promoCode);
                try {
                    if (textColor != null && !textColor.isEmpty()) {
                        binding.botPromotionCodeText.setTextColor(Color.parseColor(textColor));
                    }
                } catch (Exception e) {}
                binding.botPromotionCodeText.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgTitleTextSize()) * 2 + 8);
                binding.botPromotionCodeText.setTypeface(mInAppMessage.getActionData().getFontFamily(getActivity()));
                binding.botPromotionCopyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyToClipboard(promoCode, binding.botPromotionCopyButton);
                    }
                });
            }
        }
    }

    private void copyToClipboard(String text, final android.widget.ImageButton button) {
        if (getActivity() != null) {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Promotion Code", text);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();

                button.setImageResource(R.drawable.checked_mark);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setImageResource(R.drawable.content_copy_24);
                    }
                }, 2000);
            }
        }
    }

    private void adjustTop() {
        binding.halfScreenContainerBot.setVisibility(View.GONE);
        binding.topContentArea.setOnClickListener(new View.OnClickListener() {
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
            String topBg = mInAppMessage.getActionData().getBackground();
        if (topBg != null && !topBg.isEmpty()) {
            try {
                binding.topContentArea.setBackgroundColor(Color.parseColor(topBg));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse background color", e);
            }
        }
            binding.topTitleView.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
            binding.topTitleView.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleColor()));
            binding.topTitleView.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgTitleTextSize()) * 2 + 8);
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

        binding.botContentArea.setOnClickListener(new View.OnClickListener() {
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
            String botBg = mInAppMessage.getActionData().getBackground();
        if (botBg != null && !botBg.isEmpty()) {
            try {
                binding.botContentArea.setBackgroundColor(Color.parseColor(botBg));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse background color", e);
            }
        }
            binding.botTitleView.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n", "\n"));
            binding.botTitleView.setTextColor(Color.parseColor(mInAppMessage.getActionData().getMsgTitleColor()));
            binding.botTitleView.setTextSize(Float.parseFloat(mInAppMessage.getActionData().getMsgTitleTextSize()) * 2 + 8);
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
        ImageButton closeButton = mIsTop ? binding.topCloseButton : binding.botCloseButton;
        String colorStr = mInAppMessage.getActionData().getCloseButtonColor();
        boolean isWhite = isWhiteColor(colorStr);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        if (isWhite) {
            bg.setColor(Color.BLACK);
            bg.setStroke(dpToPx(0.5f), Color.parseColor("#33FFFFFF"));
            closeButton.setBackground(bg);
            closeButton.setImageResource(R.drawable.ic_close_white_24dp);
            closeButton.clearColorFilter();
        } else {
            bg.setColor(Color.WHITE);
            bg.setStroke(dpToPx(0.5f), Color.parseColor("#26000000"));
            closeButton.setBackground(bg);
            closeButton.setImageResource(R.drawable.ic_close_black_24dp);
            if (colorStr != null && !colorStr.trim().isEmpty() && !colorStr.equalsIgnoreCase("black")) {
                try {
                    closeButton.setColorFilter(Color.parseColor(colorStr));
                } catch (Exception ignored) {
                    closeButton.clearColorFilter();
                }
            } else {
                closeButton.clearColorFilter();
            }
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFragment();
            }
        });
    }

    private boolean isWhiteColor(String colorStr) {
        if (colorStr == null || colorStr.trim().isEmpty()) {
            return false;
        }
        String c = colorStr.trim().toLowerCase();
        if (c.equals("white") || c.equals("#ffffff") || c.equals("#fff")) {
            return true;
        }
        try {
            int color = Color.parseColor(colorStr);
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            return r >= 240 && g >= 240 && b >= 240;
        } catch (Exception e) {
            return false;
        }
    }

    private int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
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
        try {
            player = new androidx.media3.exoplayer.ExoPlayer.Builder(getActivity()).build();
            if (mIsTop) {
                binding.topVideoView.setPlayer(player);
            } else {
                binding.botVideoView.setPlayer(player);
            }
            MediaItem mediaItem = MediaItem.fromUri(mInAppMessage.getActionData().getVideoUrl());
            player.setMediaItem(mediaItem);
            player.prepare();
        } catch (Throwable throwable) {
            player = null;
            String videoUrl = mInAppMessage.getActionData().getVideoUrl();
            if (mIsTop) {
                binding.topVideoView.playWithPlatformPlayer(videoUrl);
            } else {
                binding.botVideoView.playWithPlatformPlayer(videoUrl);
            }
            Log.w(LOG_TAG, "Could not initialize the media3 player, playing the video with the platform player.", throwable);
        }
    }

    private void startPlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
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