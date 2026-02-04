package com.visilabs.notificationbell;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.Display;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.content.Context;
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

public class NotificationBellFragment extends Fragment {

    private static final String LOG_TAG = "NotificationBell";
    private static final String ARG_PARAM1 = "dataKey";

    private FragmentNotificationBellBinding binding;
    private NotificationBell notificationBell;
    private NotificationBellExtendedProps extendedProps;

    // Kotlin'deki companion object'in Java karşılığı
    public static NotificationBellFragment newInstance(NotificationBell model) {
        NotificationBellFragment fragment = new NotificationBellFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBellBinding.inflate(inflater, container, false);

        // Gelen veriyi al
        if (getArguments() != null) {
            notificationBell = (NotificationBell) getArguments().getSerializable(ARG_PARAM1);
        }

        if (notificationBell == null) {
            Log.e(LOG_TAG, "NotificationBell data is null. Closing fragment.");
            endFragment();
            return binding.getRoot();
        }

        parseExtendedProps();
        setupInitialView();

        return binding.getRoot();
    }

    private void parseExtendedProps() {
        try {
            // Null kontrolü
            if (notificationBell.getActiondata() != null && notificationBell.getActiondata().getExtendedProps() != null) {
                String decodedString = new URI(notificationBell.getActiondata().getExtendedProps()).getPath();
                extendedProps = new Gson().fromJson(decodedString, NotificationBellExtendedProps.class);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error parsing ExtendedProps. Using default values.", e);
            endFragment();
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
                        if(getActivity() != null) {
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int height = displayMetrics.heightPixels;
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
                if (isAdded()) { // Fragment'in activity'e bağlı olup olmadığını kontrol et
                    Glide.with(this)
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
                if (isAdded()) {
                    Glide.with(this)
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

        binding.ivClose.setOnClickListener(v -> endFragment());
        binding.dialogContainer.setOnClickListener(null); // Arka plan tıklamalarını engelle

        // RecyclerView'ı ayarla
        List<NotificationBellTexts> notificationTexts = (notificationBell.getActiondata() != null && notificationBell.getActiondata().getNotification_texts() != null)
                ? notificationBell.getActiondata().getNotification_texts()
                : Collections.emptyList();

        if (!notificationTexts.isEmpty()) {
            NotificationBellAdapter adapter = new NotificationBellAdapter(requireContext(), notificationTexts, extendedProps, link -> {
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
                            startActivity(intent);
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
        // TODO: Raporlama kodunu buraya ekle (impression report)
        binding.dialogContainer.setVisibility(View.VISIBLE);
    }
    
    // ... (updateDialogPosition remains changed, omitted for brevity if possible, but replace_file_content needs contiguous block. 
    // Since updateDialogPosition is between show and hide, I should probably use multi_replace or just two replace calls if they are far apart.
    // Wait, updateDialogPosition is between them in my previous view_file output.
    // showDialog is at 289, updateDialogPosition at 296, hideDialog at 319.
    // They are close enough I can just replace showDialog and hideDialog separately.
    // actually they are not that close, 20 lines apart. using multi_replace is better.
    
    private void updateDialogPosition() {
        float bellY = binding.fabBell.getY();
        if(getActivity() == null) {
            return;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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

    private void endFragment() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}