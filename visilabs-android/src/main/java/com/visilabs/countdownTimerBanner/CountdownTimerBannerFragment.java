package com.visilabs.countdownTimerBanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    // Aynı anda yalnızca tek bir banner gösterilsin; tekrarlı tetiklemelerde
    // (örn. butona arka arkaya tıklama) çoklama yapılmasın.
    public static volatile boolean isShowing = false;

    private FragmentCountdownBannerBinding binding;
    private CountdownTimerBanner bannerModel;
    private CountdownTimerBannerActionData actionData;
    private CountdownTimerBannerExtendedProps extendedProps;
    private CountDownTimer timer;

    // Sürükleme durumu
    private float dragStartRawY = 0f;
    private float dragStartTranslationY = 0f;
    private boolean isDragging = false;

    public static CountdownTimerBannerFragment newInstance(CountdownTimerBanner model) {
        CountdownTimerBannerFragment fragment = new CountdownTimerBannerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Geri sayım hedef tarihi geçmişse banner gösterilmemelidir.
     */
    public static boolean isExpired(CountdownTimerBanner model) {
        if (model == null || model.getActiondata() == null) {
            return false;
        }
        try {
            CountdownTimerBannerActionData data = model.getActiondata();
            String targetDateString = data.getCounter_Date() + " " + data.getCounter_Time();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date targetDate = sdf.parse(targetDateString);
            if (targetDate == null) {
                return false;
            }
            return targetDate.getTime() <= System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
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
        isShowing = true;

        // iOS'taki PassthroughWindow davranışını taklit eden kapsayıcı:
        // dokunuş yalnızca banner kartı içinde ise tüketilir, dışarıdaki
        // dokunuşlar alttaki Activity içeriğine geçer.
        Context ctx = inflater.getContext();
        FrameLayout passthroughRoot = new FrameLayout(ctx) {
            // Dokunuş banner kartı üzerinde başladıysa, sürükleme sırasında parmak
            // kartın dışına çıksa bile tüm hareketi karta iletmeye devam et.
            private boolean touchStartedInCard = false;

            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                if (binding == null) {
                    return super.dispatchTouchEvent(ev);
                }
                View card = binding.bannerCardView;
                int action = ev.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    int[] location = new int[2];
                    // getRawX/Y ekran koordinatı olduğundan ekran konumu ile karşılaştır.
                    card.getLocationOnScreen(location);
                    int left = location[0];
                    int top = location[1];
                    int right = left + card.getWidth();
                    int bottom = top + card.getHeight();
                    int x = (int) ev.getRawX();
                    int y = (int) ev.getRawY();
                    touchStartedInCard = (x >= left && x <= right && y >= top && y <= bottom);
                    return touchStartedInCard ? super.dispatchTouchEvent(ev) : false;
                }

                if (!touchStartedInCard) {
                    return false;
                }

                boolean handled = super.dispatchTouchEvent(ev);
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    touchStartedInCard = false;
                }
                return handled;
            }
        };
        passthroughRoot.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        passthroughRoot.addView(binding.getRoot());

        // Android 15+/16 edge-to-edge: banner status bar ve navigation bar'a
        // yapışmasın diye sistem çubuğu inset'leri kadar padding uygulanır.
        passthroughRoot.setClipToPadding(false);
        ViewCompat.setOnApplyWindowInsetsListener(passthroughRoot, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), bars.top, v.getPaddingRight(), bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(passthroughRoot);

        return passthroughRoot;
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
        // Tıklamalar yalnızca CardView üzerinde dinlenir; root'a click listener
        // bağlanmaz, dış alanın passthrough davranışı dispatchTouchEvent ile sağlanır.
        binding.ibClose.setOnClickListener(v -> endFragment());
        binding.bannerCardView.setOnClickListener(v -> {
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

        setupDragToSnap();
    }

    /**
     * Banner kartını dikey olarak sürüklenebilir yapar. Bırakıldığında karta
     * göre ekranın üst yarısındaysa tam üste, alt yarısındaysa tam alta yapışır.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupDragToSnap() {
        if (binding == null) return;
        final View card = binding.bannerCardView;
        final int touchSlop = ViewConfiguration.get(card.getContext()).getScaledTouchSlop();
        card.setOnTouchListener((v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dragStartRawY = event.getRawY();
                    dragStartTranslationY = v.getTranslationY();
                    isDragging = false;
                    return false;
                case MotionEvent.ACTION_MOVE: {
                    float dy = event.getRawY() - dragStartRawY;
                    if (!isDragging && Math.abs(dy) > touchSlop) {
                        isDragging = true;
                    }
                    if (isDragging) {
                        v.setTranslationY(dragStartTranslationY + dy);
                        return true;
                    }
                    return false;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isDragging) {
                        isDragging = false;
                        snapToNearestEdge(v);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        });
    }

    private void snapToNearestEdge(final View card) {
        if (!(card.getParent() instanceof View)) return;
        View parent = (View) card.getParent();
        if (!(card.getLayoutParams() instanceof ConstraintLayout.LayoutParams)) return;
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) card.getLayoutParams();

        float cardCenterY = card.getY() + card.getHeight() / 2f;
        boolean snapTop = cardCenterY < parent.getHeight() / 2f;
        final float startY = card.getY();

        if (snapTop) {
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
        } else {
            params.topToTop = ConstraintLayout.LayoutParams.UNSET;
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        }
        card.setTranslationY(0f);
        card.setLayoutParams(params);

        // Yeni yerleşim hesaplandıktan sonra (çizimden önce) görsel sıçramayı
        // önlemek için kartı eski konumundan hedefe doğru animasyonla taşı.
        card.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                card.getViewTreeObserver().removeOnPreDrawListener(this);
                float newY = card.getY();
                card.setTranslationY(startY - newY);
                card.animate().translationY(0f).setDuration(200).start();
                return true;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Banner kapandığında yeniden gösterilebilmesi için bayrağı sıfırla.
        isShowing = false;
    }
}