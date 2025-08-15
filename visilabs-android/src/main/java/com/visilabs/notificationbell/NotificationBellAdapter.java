package com.visilabs.notificationbell;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visilabs.android.databinding.ItemNotificationBellBinding;
import com.visilabs.inApp.FontFamily;
import com.visilabs.notificationbell.model.NotificationBellExtendedProps;
import com.visilabs.notificationbell.model.NotificationBellTexts;

import java.util.List;
import java.util.Locale;

public class NotificationBellAdapter extends RecyclerView.Adapter<NotificationBellAdapter.NotificationViewHolder> {

    private final Context context;
    private final List<NotificationBellTexts> notificationList;
    private final NotificationBellExtendedProps extendedProps;
    private final OnItemClickListener itemClickListener;

    // Tıklama olaylarını yönetmek için bir arayüz (interface)
    public interface OnItemClickListener {
        void onItemClick(String link);
    }

    public NotificationBellAdapter(
            Context context,
            List<NotificationBellTexts> notificationList,
            NotificationBellExtendedProps extendedProps,
            OnItemClickListener itemClickListener
    ) {
        this.context = context;
        this.notificationList = notificationList;
        this.extendedProps = extendedProps;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View Binding kullanarak layout'u inflate etme
        ItemNotificationBellBinding binding = ItemNotificationBellBinding.inflate(LayoutInflater.from(context), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder sınıfı
    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBellBinding binding;

        public NotificationViewHolder(ItemNotificationBellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final NotificationBellTexts notification) {
            binding.tvNotificationText.setText(notification.getText());

            // Stilleri uygula (null kontrolü ile)
            if (extendedProps != null) {
                try {
                    binding.tvNotificationText.setTextColor(Color.parseColor(extendedProps.getText_text_color()));

                    float textSize;
                    try {
                        // Kotlin'deki ?: (elvis) operatörünün Java karşılığı
                        textSize = extendedProps.getText_text_size() != null ? Float.parseFloat(extendedProps.getText_text_size()) : 2f;
                    } catch (NumberFormatException e) {
                        textSize = 2f; // Varsayılan değer
                    }
                    binding.tvNotificationText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * 2 + 10);
                    binding.tvNotificationText.setTypeface(getFontFamily(extendedProps.getFont_family()));

                } catch (Exception e) {
                    // Renk veya font ayarlarında hata olursa logla
                }
            }

            // Tıklama olayını ayarla
            final String link = notification.getAndroid_lnk();
            if (link != null && !link.isEmpty()) {
                binding.getRoot().setClickable(true);
                binding.getRoot().setOnClickListener(v -> itemClickListener.onItemClick(link));
            } else {
                binding.getRoot().setClickable(false);
                binding.getRoot().setOnClickListener(null);
            }
        }
    }

    // Font ailesini çözümleyen yardımcı metot
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
}