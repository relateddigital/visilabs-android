package com.visilabs.inappnotification;

public class DrawerActive {
    private static boolean isDrawerActive = false;

    public static void setDrawerActive(boolean isActive) {
        isDrawerActive = isActive;
    }

    public static boolean isDrawerActive() {
        return isDrawerActive;
    }

}
