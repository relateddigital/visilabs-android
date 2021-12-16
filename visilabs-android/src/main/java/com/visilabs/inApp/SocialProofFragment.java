package com.visilabs.inApp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
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

import com.google.gson.Gson;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentSocialProofBinding;
import com.visilabs.util.AppUtils;
import com.visilabs.util.UtilResultModel;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer mTimer;
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
        mModel = (ProductStatNotifierModel) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSocialProofBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        hideStatusBar();

        if(checkNumber()) {
            setupInitialView();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mTimer!=null){
            mTimer.cancel();
        }
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
        getFontFamily();
        mIsTop = mModel.getActiondata().getPos().equals("top");
        if(mIsTop){
            adjustTop();
        } else {
            adjustBottom();
        }

        setTimer();
        setupCloseButton();
    }

    private void adjustTop() {
        binding.socialProofContainerTop.setBackgroundColor(Color.parseColor(mModel.getActiondata().getBgcolor()));
        String text = utilResultModel.getMessage();
        binding.textViewTop.setTypeface(mFontFamily);
        binding.textViewTop.setTextSize(Float.parseFloat(mExtendedProps.getContent_text_size()) * 2 + 14);
        binding.textViewTop.setTextColor(Color.parseColor(mExtendedProps.getContent_text_color()));
        binding.socialProofContainerBot.setVisibility(View.GONE);
        if(utilResultModel.getIsTag()) {
            SpannableString spannableString = new SpannableString(text);
            for(int i = 0 ; i < utilResultModel.getNumbers().size() ; i++) {
                spannableString.setSpan(new AbsoluteSizeSpan(Integer.parseInt(mExtendedProps.getContentcount_text_size()) * 2 + 14, true),
                        utilResultModel.getStartIdxs().get(i), utilResultModel.getEndIdxs().get(i), 0);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(mExtendedProps.getContentcount_text_color())),
                        utilResultModel.getStartIdxs().get(i), utilResultModel.getEndIdxs().get(i), 0);
            }
            binding.textViewTop.setText(spannableString);
        } else {
            binding.textViewTop.setText(text);
        }
    }

    private void adjustBottom() {
        binding.socialProofContainerBot.setBackgroundColor(Color.parseColor(mModel.getActiondata().getBgcolor()));
        String text = utilResultModel.getMessage();
        binding.textViewBot.setTypeface(mFontFamily);
        binding.textViewBot.setTextSize(Float.parseFloat(mExtendedProps.getContent_text_size()) * 2 + 14);
        binding.textViewBot.setTextColor(Color.parseColor(mExtendedProps.getContent_text_color()));
        binding.socialProofContainerTop.setVisibility(View.GONE);
        if(utilResultModel.getIsTag()) {
            SpannableString spannableString = new SpannableString(text);
            for(int i = 0 ; i < utilResultModel.getNumbers().size() ; i++) {
                spannableString.setSpan(new AbsoluteSizeSpan(Integer.parseInt(mExtendedProps.getContentcount_text_size()) * 2 + 14, true),
                        utilResultModel.getStartIdxs().get(i), utilResultModel.getEndIdxs().get(i), 0);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(mExtendedProps.getContentcount_text_color())),
                        utilResultModel.getStartIdxs().get(i), utilResultModel.getEndIdxs().get(i), 0);
            }
            binding.textViewBot.setText(spannableString);
        } else {
            binding.textViewBot.setText(text);
        }
    }

    private void setTimer() {
        if (!mModel.getActiondata().getTimeout().equals("0")) {
            mTimer = new Timer("SocialProofTimer", false);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    endFragment();
                }
            };
            mTimer.schedule(task, Integer.parseInt(mModel.getActiondata().getTimeout()));

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
        if(mModel.getActiondata().getShowclosebtn()) {
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
        switch (mExtendedProps.getClose_button_color()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
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
        if(mTimer!=null){
            mTimer.cancel();
        }
        if(getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(SocialProofFragment.this).commit();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showStatusBar();
    }
}