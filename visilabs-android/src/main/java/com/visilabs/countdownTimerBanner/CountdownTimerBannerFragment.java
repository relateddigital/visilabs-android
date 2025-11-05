package com.visilabs.countdownTimerBanner;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.FragmentCountdownBannerBinding;
import com.visilabs.countdownTimerBanner.model.CountdownTimerBanner;
import com.visilabs.countdownTimerBanner.model.CountdownTimerBannerActionData;
import com.visilabs.countdownTimerBanner.model.CountdownTimerBannerExtendedProps;
import com.visilabs.mailSub.Report;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CountdownTimerBannerFragment extends Fragment {

    private static final String LOG_TAG = "CountdownTimerBanner";
    private static final String ARG_PARAM1 = "dataKey"; // Key eşleşmesi için

    private FragmentCountdownBannerBinding binding;
    private CountdownTimerBanner bannerModel;
    private CountdownTimerBannerActionData actionData;
    private CountdownTimerBannerExtendedProps extendedProps;
    private CountDownTimer timer;

    public static CountdownTimerBannerFragment newInstance(CountdownTimerBanner model) {
        CountdownTimerBannerFragment fragment = new CountdownTimerBannerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCountdownBannerBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            bannerModel = (CountdownTimerBanner) getArguments().getSerializable(ARG_PARAM1);
            if (bannerModel != null) {
                actionData = bannerModel.getActiondata();
            }
        }

        if (bannerModel == null || actionData == null) {
            Log.e(LOG_TAG, "CountdownTimerBanner data or actionData is null. Closing fragment.");
            endFragment();
            return binding.getRoot();
        }
        parseExtendedProps();
        positionBanner();

        return binding.getRoot();
    }

    private void positionBanner() {
        if (extendedProps == null) return;

        String position = extendedProps.getPosition_on_page();

        if (position != null && "DownPosition".equalsIgnoreCase(position.trim())) {

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.bannerCardView.getLayoutParams();

            params.topToTop = ConstraintLayout.LayoutParams.UNSET;

            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

            binding.bannerCardView.setLayoutParams(params);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // bannerModel/actionData zaten onCreateView'de kontrol edildi
        if (actionData != null) {

            setupInitialView();
            startCountdown();

            // TODO: Impression Raporu
            if (actionData.getWaiting_time() > 0) {
                Report report = new Report();
                report.impression = actionData.getReport().getImpression();

                Visilabs.CallAPI().trackActionImpression(report);
            }

        } else {
            endFragment();
        }
    }

    private void parseExtendedProps() {
        try {
            if (actionData != null && actionData.getExtendedProps() != null) {
                String decodedString = new URI(actionData.getExtendedProps()).getPath();
                extendedProps = new Gson().fromJson(decodedString, CountdownTimerBannerExtendedProps.class);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error parsing ExtendedProps. Using default values.", e);
            endFragment();
        }
    }

    private void setupInitialView() {
        if (actionData == null) {
            Log.e(LOG_TAG, "ActionData is null, cannot setup view.");
            return;
        }

        binding.tvBannerText.setText(actionData.getContent_body());

        if (isAdded() && getContext() != null) {
            Glide.with(this)
                    .load(actionData.getImg())
                    .into(binding.ivBannerIcon);
        }

        // Renkleri ExtendedProps'tan uygula
        if (extendedProps != null) {

            // 1. Ana CardView arkaplan rengi
            try {
                if (extendedProps.getBackground_color() != null && !extendedProps.getBackground_color().isEmpty()) {
                    binding.bannerCardView.setCardBackgroundColor(Color.parseColor(extendedProps.getBackground_color()));
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Invalid background_color. JSON'da '#RRGGBB' formatı kullanın. Gelen: " + extendedProps.getBackground_color(), e);
            }

            // 2. Kampanya metni rengi
            try {
                if (extendedProps.getContent_body_text_color() != null && !extendedProps.getContent_body_text_color().isEmpty()) {
                    binding.tvBannerText.setTextColor(Color.parseColor(extendedProps.getContent_body_text_color()));
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Invalid content_body_text_color: " + extendedProps.getContent_body_text_color(), e);
            }

            // 3. Kapatma butonu rengi
            try {
                String closeColor = extendedProps.getClose_button_color();
                if (closeColor != null && !closeColor.isEmpty()) {
                    int closeButtonColor = Color.parseColor(closeColor);
                    DrawableCompat.setTint(binding.ibClose.getDrawable(), closeButtonColor);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Invalid close_button_color: " + extendedProps.getClose_button_color(), e);
            }

            // 4. Zamanlayıcı Arka Plan Rengi
            try {
                if (actionData.getScratch_color() != null && !actionData.getScratch_color().isEmpty()) {
                    GradientDrawable timerBackground = (GradientDrawable) binding.layoutTimer.getBackground().mutate();
                    timerBackground.setColor(Color.parseColor(actionData.getScratch_color()));
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to set timer background color (scratch_color). Gelen: " + actionData.getScratch_color(), e);
            }

            // 5. Zamanlayıcı METİN Rengi
            try {
                if (extendedProps.getCounter_color() != null && !extendedProps.getCounter_color().isEmpty()) {
                    int timerTextColor = Color.parseColor(extendedProps.getCounter_color());

                    binding.tvTimerDays.setTextColor(timerTextColor);
                    binding.tvTimerHours.setTextColor(timerTextColor);
                    binding.tvTimerMinutes.setTextColor(timerTextColor);

                    binding.tvTimerSubDays.setTextColor(timerTextColor);
                    binding.tvTimerSubHours.setTextColor(timerTextColor);
                    binding.tvTimerSubMinutes.setTextColor(timerTextColor);
                }
            } catch (Exception e) {
                // LOG DÜZELTMESİ: Hatalı log mesajı düzeltildi.
                Log.e(LOG_TAG, "Invalid timer text color (counter_color). Gelen: " + extendedProps.getCounter_color(), e);
            }
        }

        // --- Click listener'lar ---
        binding.ibClose.setOnClickListener(v -> endFragment());
        binding.getRoot().setOnClickListener(v -> {
            var link = actionData.getAndroid_lnk();

            if (!link.isEmpty()) {
                CountdownTimerBannerClickCallback callback = Visilabs.CallAPI().setCountdownTimerBannerClickCallback();
                if (callback != null) {
                    try {
                        Report report = new Report();
                        report.click = actionData.getReport().getClick();
                        Visilabs.CallAPI().trackActionClick(report);
                        callback.onCountdownTimerBannerClick(link);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error firing CountdownTimerBannerClickCallback", e);
                    }

                    endFragment();
                }
            }
        });
    }

    /**
     * Geri sayım sayacını başlatır.
     */
    private void startCountdown() {
        if (actionData == null) return;

        try {
            String targetDateString = actionData.getCounter_Date() + " " + actionData.getCounter_Time();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

            Date targetDate = sdf.parse(targetDateString);
            long now = System.currentTimeMillis();
            long millisInFuture = targetDate.getTime() - now;

            if (millisInFuture <= 0) {
                showCampaignFinished();
                return;
            }

            timer = new CountDownTimer(millisInFuture, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(binding == null) return;

                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;

                    binding.tvTimerDays.setText(String.format(Locale.getDefault(), "%02d", days));
                    binding.tvTimerHours.setText(String.format(Locale.getDefault(), "%02d", hours));
                    binding.tvTimerMinutes.setText(String.format(Locale.getDefault(), "%02d", minutes));
                }

                @Override
                public void onFinish() {
                    if(binding != null) {
                        showCampaignFinished();
                    }
                }
            }.start();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to parse countdown date.", e);
            showCampaignFinished();
        }
    }

    private void showCampaignFinished() {
        if(binding == null) return;
        binding.tvBannerText.setText("Kampanya sona erdi!");
        binding.layoutTimer.setVisibility(View.GONE);

    }

    private void endFragment() {
        if (getActivity() != null && isAdded()) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel(); // Sayacı durdur
            timer = null;
        }
        binding = null;
    }
}