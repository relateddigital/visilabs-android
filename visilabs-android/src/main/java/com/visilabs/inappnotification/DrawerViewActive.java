package com.visilabs.inappnotification;

public class DrawerViewActive {

    private static boolean isDrawerViewActive = false;

    public static void setDrawerViewActive(boolean isActive) {
        isDrawerViewActive = isActive;
    }

    public static boolean isDrawerViewActive() {
        return isDrawerViewActive;
    }
}
