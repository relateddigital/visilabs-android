package com.visilabs.android.exceptions;

public class VisilabsNotReadyException extends Exception {

    private static final String mMessage = "Visilabs SDK is not ready. You need to ensure Visilabs.CreateAPI is called.";

    public VisilabsNotReadyException() {
        super(mMessage);
    }
}