package com.relateddigital.visilabs;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class VisilabsFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        SharedPreferences.Editor editor = getSharedPreferences("key", MODE_PRIVATE).edit();
        editor.putString("token", s);
        editor.apply();
    }
}
