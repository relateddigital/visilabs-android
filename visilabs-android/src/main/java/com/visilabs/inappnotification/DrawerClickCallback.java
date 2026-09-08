package com.visilabs.inappnotification;

import androidx.annotation.Nullable;

/**
 * Receives drawer item clicks so that the host app can handle the link itself. Register it
 * through {@code Visilabs.CallAPI().setDrawerClickCallback(...)}. While a callback is set the
 * SDK does not open the link, which lets the app route deep links on its own.
 */
public interface DrawerClickCallback {

    /**
     * @param link the link of the clicked item, exactly as it arrives from the panel
     * @param itemIndex index of the clicked item, 0 for a single item drawer
     * @param staticCode promo code of the clicked item, empty when it has none
     */
    void onDrawerClick(@Nullable String link, int itemIndex, @Nullable String staticCode);
}
