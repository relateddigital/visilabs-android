package com.visilabs.notificationbell;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentNotificationBellBinding;
import com.visilabs.inApp.FontFamily;
import com.visilabs.mailSub.Report;
import com.visilabs.notificationbell.model.NotificationBell;
import com.visilabs.notificationbell.model.NotificationBellExtendedProps;
import com.visilabs.notificationbell.model.NotificationBellTexts;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * NotificationBell'in tüm görünüm ve etkileşim mantığı. Sadece bir {@link Activity}'ye
 * ihtiyaç duyar; {@code FragmentActivity} gerektirmez. Böylece hem
 * {@link NotificationBellFragment} (FragmentActivity host'lar için) hem de
 * {@link NotificationBellOverlay} (Flutter'ın {@code FlutterActivity}'si gibi
 * FragmentActivity olmayan host'lar için) aynı davranışı paylaşır.
 */
public class NotificationBellController {

    private static final String LOG_TAG = "NotificationBell";

    private final Activity activity;
    private final FragmentNotificationBellBinding binding;
    private final NotificationBell notificationBell;
    private final Runnable onDismiss;
    private NotificationBellExtendedProps extendedProps;

    public NotificationBellController(Activity activity, LayoutInflater inflater, ViewGroup container,
                                      NotificationBell notificationBell, Runnable onDismiss) {
        this.activity = activity;
        this.notificationBell = notificationBell;
        this.onDismiss = onDismiss;
        this.binding = FragmentNotificationBellBinding.inflate(inflater, container, false);

        if (notificationBell == null) {
            Log.e(LOG_TAG, "NotificationBell data is null. Closing.");
            dismiss();
            return;
        }

        parseExtendedProps();
        setupInitialView();
    }

    public View getRoot() {
        return binding.getRoot();
    }

    public void dismiss() {
        if (onDismiss != null) {
            onDismiss.run();
        }
    }

    private void parseExtendedProps() {
        try {
            if (notificationBell.getActiondata() != null && notificationBell.getActiondata().getExtendedProps() != null) {
                String decodedString = new URI(notificationBell.getActiondata().getExtendedProps()).getPath();
                extendedProps = new Gson().fromJson(decodedString, NotificationBellExtendedProps.class);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error parsing ExtendedProps. Using default values.", e);
        }
    }

    private void setupInitialView() {
        loadStaticBellIcon();
        setupDialogContent();
        binding.fabBell.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            private boolean isDragging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Mevcut konumu al
                        int currentX = (int) v.getX();
                        int currentY = (int) v.getY();

                        // Constraintleri sol-üst köseye gore ayarla ki margin ile tasiyabilelim
                        params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
                        params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
                        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                        params.leftMargin = currentX;
                        params.topMargin = currentY;
                        // Marginleri set ettikten sonra view yerinde kalsin diye hemen uygula
                        v.setLayoutParams(params);

                        initialX = params.leftMargin;
                        initialY = params.topMargin;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        isDragging = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - initialTouchX);
                        int dy = (int) (event.getRawY() - initialTouchY);

                        // Hareket eşiği
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                            isDragging = true;
                        }

                        params.leftMargin = initialX + dx;
                        params.topMargin = initialY + dy;
                        v.setLayoutParams(params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        if (activity != null) {
                            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int width = displayMetrics.widthPixels;

                            // Saga mi sola mi yakin?
                            // getX() guncel sol pozisyonu verir
                            int targetX = (v.getX() + v.getWidth() / 2 < width / 2) ? 0 : (width - v.getWidth());

                            ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, targetX);
                            animator.addUpdateListener(animation -> {
                                params.leftMargin = (int) animation.getAnimatedValue();
                                v.setLayoutParams(params);
                            });
                            animator.start();

                            // Eğer sürükleme olmadıysa (tıklama) dialog'u aç/kapa
                            if (!isDragging) {
                                if (binding.dialogContainer.getVisibility() == View.VISIBLE) {
                                    hideDialog();
                                } else {
                                    showDialog();
                                }
                            }
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void loadStaticBellIcon() {
        if (notificationBell.getActiondata() != null) {
            String staticIconUrl = notificationBell.getActiondata().getBell_icon();
            if (staticIconUrl != null && !staticIconUrl.isEmpty()) {
                if (!isActivityGone()) {
                    Glide.with(activity)
                            .load(staticIconUrl)
                            .placeholder(R.drawable.ic_close_black_24dp)
                            .into(binding.fabBell);
                }
            } else {
                binding.fabBell.setImageResource(R.drawable.ic_close_black_24dp);
            }
        }
    }

    private void loadBellAnimation() {
        if (notificationBell.getActiondata() != null) {
            String animationUrl = notificationBell.getActiondata().getBell_animation();
            if (animationUrl != null && !animationUrl.isEmpty()) {
                if (!isActivityGone()) {
                    Glide.with(activity)
                            .asGif()
                            .load(animationUrl)
                            .placeholder(R.drawable.ic_close_black_24dp)
                            .into(binding.fabBell);
                }
            } else {
                loadStaticBellIcon(); // Animasyon yoksa statik ikonu yükle
            }
        }
    }

    private boolean isActivityGone() {
        return activity == null || activity.isFinishing() || activity.isDestroyed();
    }

    private void setupDialogContent() {
        // Renkler, yazılar ve fontlar (null kontrolü ile)
        if (extendedProps != null) {
            try {
                binding.dialogCard.setCardBackgroundColor(Color.parseColor(extendedProps.getBackground_color()));
                binding.ivPointer.setColorFilter(Color.parseColor(extendedProps.getBackground_color()));
                binding.tvTitle.setTextColor(Color.parseColor(extendedProps.getTitle_text_color()));

                float titleTextSize;
                try {
                    titleTextSize = extendedProps.getTitle_text_size() != null ? Float.parseFloat(extendedProps.getTitle_text_size()) : 7f;
                } catch (NumberFormatException e) {
                    titleTextSize = 7f;
                }
                binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize * 2 + 6);
                binding.tvTitle.setTypeface(getFontFamily(extendedProps.getFont_family()));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error applying extended props to dialog.", e);
            }
        }

        if (notificationBell.getActiondata() != null) {
            binding.tvTitle.setText(notificationBell.getActiondata().getTitle());
        }

        binding.ivClose.setOnClickListener(v -> dismiss());
        binding.dialogContainer.setOnClickListener(null); // Arka plan tıklamalarını engelle

        // RecyclerView'ı ayarla
        List<NotificationBellTexts> notificationTexts = (notificationBell.getActiondata() != null && notificationBell.getActiondata().getNotification_texts() != null)
                ? notificationBell.getActiondata().getNotification_texts()
                : Collections.emptyList();

        if (!notificationTexts.isEmpty()) {
            NotificationBellAdapter adapter = new NotificationBellAdapter(activity, notificationTexts, extendedProps, link -> {
                if (link != null) {
                    try {
                        Report report = new Report();
                        report.click = notificationBell.getActiondata().getReport().getClick();
                        Visilabs.CallAPI().trackActionClick(report);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error tracking click", e);
                    }

                    NotificationBellClickCallback callback = Visilabs.CallAPI().getNotificationBellClickCallback();
                    if (callback != null) {
                        try {
                            callback.onNotificationBellClick(link);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error firing NotificationBellClickCallback", e);
                        }
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Could not open the link: " + link, e);
                        }
                    }
                }
                hideDialog();
            });
            binding.rvNotifications.setAdapter(adapter);
        }
    }

    private Typeface getFontFamily(String fontFamilyString) {
        if (fontFamilyString == null) {
            return Typeface.DEFAULT;
        }
        String lowerCaseFontFamily = fontFamilyString.toLowerCase(Locale.ROOT);
        if (lowerCaseFontFamily.equals(FontFamily.Monospace.toString().toLowerCase(Locale.ROOT))) {
            return Typeface.MONOSPACE;
        } else if (lowerCaseFontFamily.equals(FontFamily.SansSerif.toString().toLowerCase(Locale.ROOT))) {
            return Typeface.SANS_SERIF;
        } else if (lowerCaseFontFamily.equals(FontFamily.Serif.toString().toLowerCase(Locale.ROOT))) {
            return Typeface.SERIF;
        } else {
            return Typeface.DEFAULT;
        }
    }

    private void showDialog() {
        loadBellAnimation();
        updateDialogPosition();
        binding.dialogContainer.setVisibility(View.VISIBLE);
    }

    private void updateDialogPosition() {
        float bellY = binding.fabBell.getY();
        if (activity == null) {
            return;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.dialogContainer.getLayoutParams();

        layoutParams.topToBottom = ConstraintLayout.LayoutParams.UNSET;
        layoutParams.bottomToTop = ConstraintLayout.LayoutParams.UNSET;

        if (bellY < screenHeight / 2) {
            layoutParams.topToBottom = binding.fabBell.getId();
            binding.ivPointer.setRotation(180f);
        } else {
            layoutParams.bottomToTop = binding.fabBell.getId();
            binding.ivPointer.setRotation(0f);
        }
        binding.dialogContainer.setLayoutParams(layoutParams);
    }

    private void hideDialog() {
        loadStaticBellIcon();
        binding.dialogContainer.setVisibility(View.GONE);
    }
}
