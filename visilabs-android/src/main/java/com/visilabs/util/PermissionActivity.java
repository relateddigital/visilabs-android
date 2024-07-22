package com.visilabs.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.model.LocationPermission;

public class PermissionActivity extends Activity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 20;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 21;

    private String locationTitle;

    private String locationMessage;

    private String backgroundTitle;

    private String backgroundMessage;
    private String backgroundRequest;

    private String positiveButton;
    private String negativeButton;
    private Boolean isLocationPopUpValid = false;

    private Boolean isBackgroundPopUpValid = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            backgroundRequest = intent.getStringExtra("Background");
            backgroundMessage = intent.getStringExtra("BackgroundMessage");
            backgroundTitle = intent.getStringExtra("BackgroundTitle");
            locationMessage = intent.getStringExtra("LocationMessage");
            locationTitle = intent.getStringExtra("LocationTitle");
            positiveButton = intent.getStringExtra("PositiveButton");
            negativeButton = intent.getStringExtra("NegativeButton");

            isLocationPopUpValid = (locationMessage != null || locationTitle != null);
            isBackgroundPopUpValid = (backgroundMessage != null || backgroundTitle != null);

        }


        // Check if ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission has been already granted
        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if ((!accessFineLocationPermission) || (!accessCoarseLocationPermission)) {
            if (isLocationPopUpValid) {
                showLocationPermissionPopUp();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE
                );
            }
        } else if (backgroundRequest != null) {
            if (isBackgroundPopUpValid) {
                showBackgroundLocationPermissionPopUp();
            } else {
                // Check if the ACCESS_BACKGROUND_LOCATION has been already granted
                LocationPermission locationPermission = AppUtils.getLocationPermissionStatus(this);
                if (locationPermission != LocationPermission.ALWAYS) {
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

    }


    private void showLocationPermissionPopUp() {
        new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(locationTitle)
                .setMessage(locationMessage)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(PermissionActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        isLocationPopUpValid = false;
    }

    private void showBackgroundLocationPermissionPopUp() {
        LocationPermission locationPermission = AppUtils.getLocationPermissionStatus(this);
        if (locationPermission != LocationPermission.ALWAYS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                        .setTitle(backgroundTitle)
                        .setMessage(backgroundMessage)
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Check if the ACCESS_BACKGROUND_LOCATION has been already granted
                                LocationPermission locationPermission = AppUtils.getLocationPermissionStatus(PermissionActivity.this);
                                if (locationPermission != LocationPermission.ALWAYS) {
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                                        ActivityCompat.requestPermissions(PermissionActivity.this,
                                                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
                                        );
                                    }
                                } else {
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        } else {
            finish();
        }
        isBackgroundPopUpValid = false;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) ||
                    (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                if (backgroundRequest != null) {
                    if (isBackgroundPopUpValid) {
                        showBackgroundLocationPermissionPopUp();
                    } else {
                        // Check if the ACCESS_BACKGROUND_LOCATION has been already granted
                        LocationPermission locationPermission = AppUtils.getLocationPermissionStatus(this);
                        if (locationPermission != LocationPermission.ALWAYS) {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                        BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
                                );
                            }
                        }
                    }
                } else {
                    finish();

                }
            } else {
                finish();
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
                Visilabs.CallAPI().startGpsManager();
            } else {
                finish();
            }
        }
    }
}
