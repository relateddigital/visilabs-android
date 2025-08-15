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
import androidx.fragment.app.Fragment;
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
        loadBellAnimation();
        setupDialogContent();
        binding.fabBell.setOnClickListener(v -> {
            if (binding.dialogContainer.getVisibility() == View.VISIBLE) {
                hideDialog();
            } else {
                showDialog();
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
                    NotificationBellClickCallback callback = Visilabs.CallAPI().setNotificationBellClickCallback();
                    if (callback != null) {
                        try {
                            Report report = new Report();
                            report.click = notificationBell.getActiondata().getReport().getClick();
                            Visilabs.CallAPI().trackActionClick(report);
                            callback.onNotificationBellClick(link);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error firing NotificationBellClickCallback", e);
                        }
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(intent);

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Could not open the link: " + link, e);
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
        loadStaticBellIcon();
        // TODO: Raporlama kodunu buraya ekle (impression report)
        binding.dialogContainer.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        loadBellAnimation();
        binding.dialogContainer.setVisibility(View.GONE);
    }

    private void endFragment() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}