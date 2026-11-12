package com.visilabs.inApp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentSocialProofBinding;
import com.visilabs.util.AppUtils;
import com.visilabs.util.UtilResultModel;
import java.net.URISyntaxException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialProofFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialProofFragment extends Fragment {

    private static final String LOG_TAG = "SocialProofFragment";
    private static final String ARG_PARAM1 = "dataKey";

    private ProductStatNotifierModel mModel;
    private boolean mIsTop;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mAutoClose = new Runnable() {
        @Override
        public void run() {
            endFragment();
        }
    };
    private FragmentSocialProofBinding binding;
    private ProductStatNotifierExtendedProps mExtendedProps;
    private UtilResultModel utilResultModel;
    private Typeface mFontFamily = Typeface.DEFAULT;

    public SocialProofFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param model Parameter 1.
     * @return A new instance of fragment SocialProofFragment.
     */
    public static SocialProofFragment newInstance(ProductStatNotifierModel model) {
        SocialProofFragment fragment = new SocialProofFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = getArguments() != null ?
                (ProductStatNotifierModel) getArguments().getSerializable(ARG_PARAM1) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSocialProofBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        hideStatusBar();

        if (mModel == null || mModel.getActiondata() == null) {
            Log.e(LOG_TAG, "Invalid Inputs!");
            endFragment();
            return view;
        }

        try {
            if(checkNumber()) {
                setupInitialView();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Social proof could not be rendered properly!", e);
            endFragment();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHandler.removeCallbacks(mAutoClose);
    }

    private void setupInitialView() {
        try {
            mExtendedProps = new Gson().fromJson(new java.net.URI(mModel.
                    getActiondata().getExtendedProps()).getPath(), ProductStatNotifierExtendedProps.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            endFragment();
        } catch (Exception e) {
            e.printStackTrace();
            endFragment();
        }
        if (mExtendedProps == null) {
            endFragment();
            return;
        }
        getFontFamily();
        mIsTop = "top".equals(mModel.getActiondata().getPos());
        if(mIsTop){
            adjustTop();
        } else {
            adjustBottom();
        }

        setTimer();
        setupCloseButton();
    }

    private void adjustTop() {
        InAppSafeUtils.setBackgroundColor(binding.socialProofContainerTop, mModel.getActiondata().getBgcolor());
        String text = utilResultModel.getMessage() != null ? utilResultModel.getMessage() : "";
        binding.textViewTop.setTypeface(mFontFamily);
        binding.textViewTop.setTextSize(InAppSafeUtils.parseFloat(mExtendedProps.getContent_text_size(), 0f) * 2 + 14);
        InAppSafeUtils.setTextColor(binding.textViewTop, mExtendedProps.getContent_text_color());
        binding.socialProofContainerBot.setVisibility(View.GONE);
        if(utilResultModel.getIsTag()) {
            binding.textViewTop.setText(createCountSpans(text));
        } else {
            binding.textViewTop.setText(text.replace("\\n", "\n"));
        }
    }

    private void adjustBottom() {
        InAppSafeUtils.setBackgroundColor(binding.socialProofContainerBot, mModel.getActiondata().getBgcolor());
        String text = utilResultModel.getMessage() != null ? utilResultModel.getMessage() : "";
        binding.textViewBot.setTypeface(mFontFamily);
        binding.textViewBot.setTextSize(InAppSafeUtils.parseFloat(mExtendedProps.getContent_text_size(), 0f) * 2 + 14);
        InAppSafeUtils.setTextColor(binding.textViewBot, mExtendedProps.getContent_text_color());
        binding.socialProofContainerTop.setVisibility(View.GONE);
        if(utilResultModel.getIsTag()) {
            binding.textViewBot.setText(createCountSpans(text));
        } else {
            binding.textViewBot.setText(text.replace("\\n", "\n"));
        }
    }

    private SpannableString createCountSpans(String text) {
        SpannableString spannableString = new SpannableString(text);
        if (utilResultModel.getNumbers() == null || utilResultModel.getStartIdxs() == null ||
                utilResultModel.getEndIdxs() == null) {
            return spannableString;
        }
        int countTextSize = InAppSafeUtils.parseInt(mExtendedProps.getContentcount_text_size(), 0) * 2 + 14;
        int countTextColor = InAppSafeUtils.parseColor(mExtendedProps.getContentcount_text_color(), Color.BLACK);
        boolean isCountColorValid = InAppSafeUtils.isValidColor(mExtendedProps.getContentcount_text_color());
        for(int i = 0 ; i < utilResultModel.getNumbers().size() ; i++) {
            if (i >= utilResultModel.getStartIdxs().size() || i >= utilResultModel.getEndIdxs().size()) {
                break;
            }
            int start = utilResultModel.getStartIdxs().get(i);
            int end = utilResultModel.getEndIdxs().get(i);
            if (start < 0 || end > text.length() || start > end) {
                continue;
            }
            spannableString.setSpan(new AbsoluteSizeSpan(countTextSize, true), start, end, 0);
            if (isCountColorValid) {
                spannableString.setSpan(new ForegroundColorSpan(countTextColor), start, end, 0);
            }
        }
        return spannableString;
    }

    private void setTimer() {
        int timeout = InAppSafeUtils.parseInt(mModel.getActiondata().getTimeout(), 0);
        if (timeout > 0) {
            mHandler.postDelayed(mAutoClose, timeout);

        } else {
            if (mIsTop) {
                binding.socialProofContainerTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endFragment();
                    }
                });
            } else {
                binding.socialProofContainerBot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endFragment();
                    }
                });
            }
        }
    }

    private void setupCloseButton() {
        if(Boolean.TRUE.equals(mModel.getActiondata().getShowclosebtn())) {
            if (mIsTop) {
                binding.closeButtonTop.setBackgroundResource(getCloseIcon());
                binding.closeButtonTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endFragment();
                    }
                });
            } else {
                binding.closeButtonBot.setBackgroundResource(getCloseIcon());
                binding.closeButtonBot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endFragment();
                    }
                });
            }
        } else {
            binding.closeButtonTop.setVisibility(View.GONE);
            binding.closeButtonBot.setVisibility(View.GONE);
        }
    }

    private int getCloseIcon() {
        if ("white".equals(mExtendedProps.getClose_button_color())) {
            return R.drawable.ic_close_white_24dp;
        }
        return R.drawable.ic_close_black_24dp;
    }

    private boolean checkNumber() {
        utilResultModel = AppUtils.getNumberFromText(mModel.getActiondata().getContent());
        if(utilResultModel == null) {
            Log.e(LOG_TAG, "Invalid Inputs!");
            endFragment();
            return false;
        } else {
            return true;
        }
    }

    private void getFontFamily() {
        if (mExtendedProps.getContent_font_family() == null) {
            mFontFamily = Typeface.DEFAULT;
            return;
        }
        if (FontFamily.Monospace.toString().equals(mExtendedProps.getContent_font_family().toLowerCase())) {
            mFontFamily = Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(mExtendedProps.getContent_font_family().toLowerCase())) {
            mFontFamily = Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(mExtendedProps.getContent_font_family().toLowerCase())) {
            mFontFamily = Typeface.SERIF;
        }
        if (FontFamily.Default.toString().equals(mExtendedProps.getContent_font_family().toLowerCase())) {
            mFontFamily = Typeface.DEFAULT;
        }
    }

    private void endFragment() {
        mHandler.removeCallbacks(mAutoClose);
        if (isAdded()) {
            getParentFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        } else if (getActivity() != null && getActivity() instanceof FragmentActivity) {
            ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    private void hideStatusBar() {
        if (getActivity() == null) {
            return;
        }
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

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mAutoClose);
        super.onDestroyView();
        showStatusBar();
    }
}