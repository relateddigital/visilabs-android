package com.visilabs.spinToWin;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentSpintowinCodeBannerBinding;
import com.visilabs.inApp.FontFamily;
import com.visilabs.spinToWin.model.ExtendedProps;
import com.visilabs.util.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.visilabs.spinToWin.SpinToWinCodeBannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinToWinCodeBannerFragment extends Fragment {

    private static final String LOG_TAG = "SpinToWinBanner";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "banner_data";
    private static final String ARG_PARAM2 = "banner_code";

    private ExtendedProps mExtendedProps;
    private String bannerCode;
    private FragmentSpintowinCodeBannerBinding binding;

    public SpinToWinCodeBannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param extendedProps Parameter 1.
     * @param code Parameter 2.
     * @return A new instance of fragment SpinToWinCodeBannerFragment.
     */
    public static SpinToWinCodeBannerFragment newInstance(ExtendedProps extendedProps, String code) {
        SpinToWinCodeBannerFragment fragment = new SpinToWinCodeBannerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, extendedProps);
        args.putString(ARG_PARAM2, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExtendedProps = (ExtendedProps) getArguments().getSerializable(ARG_PARAM1);
        bannerCode = getArguments().getString(ARG_PARAM2);
        if(bannerCode == null || bannerCode.isEmpty()) {
            endFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpintowinCodeBannerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        hideStatusBar();

        if (mExtendedProps != null) {
            setupUi();

        } else {
            endFragment();
            Log.e(LOG_TAG, "Could not get the data, closing in app");
        }
        return view;
    }

    private void setupUi() {
        if(mExtendedProps.getPromocodeBannerBackgroundColor() != null &&
                !mExtendedProps.getPromocodeBannerBackgroundColor().isEmpty()) {
            binding.container.setBackgroundColor(Color.parseColor(mExtendedProps.getPromocodeBannerBackgroundColor()));
        } else {
            binding.container.setBackgroundColor(getResources().getColor(R.color.black));
        }

        binding.bannerText.setText(mExtendedProps.getPromocodeBannerText());
        binding.bannerLabel.setText(mExtendedProps.getPromocodeBannerButtonLabel());
        binding.bannerCode.setText(bannerCode);

        if(mExtendedProps.getPromocodeBannerTextColor() != null &&
                !mExtendedProps.getPromocodeBannerTextColor().isEmpty()) {
            binding.bannerText.setTextColor(Color.parseColor(mExtendedProps.getPromocodeBannerTextColor()));
            binding.bannerLabel.setTextColor(Color.parseColor(mExtendedProps.getPromocodeBannerTextColor()));
            binding.bannerCode.setTextColor(Color.parseColor(mExtendedProps.getPromocodeBannerTextColor()));
        } else {
            binding.bannerText.setTextColor(getResources().getColor(R.color.white));
            binding.bannerLabel.setTextColor(getResources().getColor(R.color.white));
            binding.bannerCode.setTextColor(getResources().getColor(R.color.white));
        }

        if(mExtendedProps.getTextSize() != null &&
                !mExtendedProps.getTextSize().isEmpty()) {
            binding.bannerText.setTextSize(Float.parseFloat(mExtendedProps.getTextSize()) + 10);
            binding.bannerLabel.setTextSize(Float.parseFloat(mExtendedProps.getTextSize()) + 12);
            binding.bannerCode.setTextSize(Float.parseFloat(mExtendedProps.getTextSize()) + 10);
        } else {
            binding.bannerText.setTextSize(14);
            binding.bannerLabel.setTextSize(16);
            binding.bannerCode.setTextSize(14);
        }

        if (mExtendedProps.getTextFontFamily() == null || mExtendedProps.getTextFontFamily().equals("")) {
            binding.bannerText.setTypeface(Typeface.DEFAULT);
            binding.bannerLabel.setTypeface(Typeface.DEFAULT);
        } else if (FontFamily.Monospace.toString().equals(mExtendedProps.getTextFontFamily().toLowerCase())) {
            binding.bannerText.setTypeface(Typeface.MONOSPACE);
            binding.bannerLabel.setTypeface(Typeface.MONOSPACE);
        } else if (FontFamily.SansSerif.toString().equals(mExtendedProps.getTextFontFamily().toLowerCase())) {
            binding.bannerText.setTypeface(Typeface.SANS_SERIF);
            binding.bannerLabel.setTypeface(Typeface.SANS_SERIF);
        } else if (FontFamily.Serif.toString().equals(mExtendedProps.getTextFontFamily().toLowerCase())) {
            binding.bannerText.setTypeface(Typeface.SERIF);
            binding.bannerLabel.setTypeface(Typeface.SERIF);
        } else if(mExtendedProps.getTextCustomFontFamilyAndroid() != null && !mExtendedProps.getTextCustomFontFamilyAndroid().isEmpty()) {
            if (AppUtils.isResourceAvailable(getActivity(), mExtendedProps.getTextCustomFontFamilyAndroid())) {
                int id = getActivity().getResources().getIdentifier(mExtendedProps.getTextCustomFontFamilyAndroid(), "font", getActivity().getPackageName());
                binding.bannerText.setTypeface(ResourcesCompat.getFont(getActivity(), id));
                binding.bannerLabel.setTypeface(ResourcesCompat.getFont(getActivity(), id));
            }
        } else {
            binding.bannerText.setTypeface(Typeface.DEFAULT);
            binding.bannerLabel.setTypeface(Typeface.DEFAULT);
        }

        binding.closeButton.setBackgroundResource(getCloseIcon());
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFragment();
            }
        });

        binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Coupon Code", bannerCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getCloseIcon() {
        switch (mExtendedProps.getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;
    }

    private void endFragment() {
        if(getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(SpinToWinCodeBannerFragment.this).commit();
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
