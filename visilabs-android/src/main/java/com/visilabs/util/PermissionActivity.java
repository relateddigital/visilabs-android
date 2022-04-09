package com.visilabs.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.visilabs.Visilabs;
import com.visilabs.model.LocationPermission;

public class PermissionActivity extends Activity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 20;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 21;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission has been already granted
        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!(accessFineLocationPermission || accessCoarseLocationPermission)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            // Check if the ACCESS_BACKGROUND_LOCATION has been already granted
            LocationPermission locationPermission = AppUtils.getLocationPermissionStatus(this);
            if(locationPermission != LocationPermission.ALWAYS) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
                    );
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) ||
                    (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

                // Check if the ACCESS_BACKGROUND_LOCATION has been already granted
                LocationPermission locationPermission = AppUtils.getLocationPermissionStatus(this);
                if(locationPermission != LocationPermission.ALWAYS) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
                        );
                    }
                }
            } else {
                finish();
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
                Visilabs.CallAPI().startGpsManager();
            } else {
                finish();
            }
        }
    }
}
