package com.visilabs.favs;

import java.util.Arrays;

public class FavsResponse {
    private FavoriteAttributeAction[] FavoriteAttributeAction;

    private String VERSION;

    private String capping;

    public FavoriteAttributeAction[] getFavoriteAttributeAction() {
        return FavoriteAttributeAction;
    }

    public void setFavoriteAttributeAction(FavoriteAttributeAction[] FavoriteAttributeAction) {
        this.FavoriteAttributeAction = FavoriteAttributeAction;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getCapping() {
        return capping;
    }

    public void setCapping(String capping) {
        this.capping = capping;
    }

    @Override
    public String toString() {
        return "FavsResponse [FavoriteAttributeAction = " + Arrays.toString(FavoriteAttributeAction) + ", VERSION = " + VERSION + ", capping = " + capping + "]";
    }
}
