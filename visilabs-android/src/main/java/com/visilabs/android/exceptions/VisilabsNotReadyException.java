package com.visilabs.android.exceptions;

/**
 * Created by egemen on 26.11.2015.
 */
public class VisilabsNotReadyException extends Exception {

    private static final String mMessage = "Visilabs SDK is not ready. You need to ensure Visilabs.CreateAPI is called.";

    public VisilabsNotReadyException() {
        super(mMessage);
    }
}