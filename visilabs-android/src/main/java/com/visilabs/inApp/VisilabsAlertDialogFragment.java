package com.visilabs.inApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.api.VisilabsUpdateDisplayState;

public class VisilabsAlertDialogFragment extends DialogFragment {

    private static final String LOG_TAG = "VisilabsAlertDialog";
    private int mInAppStateId;
    private InAppNotificationState inAppNotificationState;
    private Activity parent;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final InAppMessage inAppMessage = inAppNotificationState.getInAppMessage();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent, R.style.AlertDialogStyle);
        alertDialogBuilder.setTitle(inAppMessage.getTitle().replace("\\n","\n"))
                .setMessage(inAppMessage.getBody().replace("\\n","\n"))
                .setCancelable(false)
                .setPositiveButton(inAppMessage.getButtonText(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String uriString = inAppMessage.getButtonURL();
                        Uri uri = null;
                        if (uriString != null && uriString.length() > 0) {
                            try {
                                uri = Uri.parse(uriString);
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                                parent.startActivity(viewIntent);
                            } catch (IllegalArgumentException e) {
                                Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                            } catch (ActivityNotFoundException e) {
                                Log.i(LOG_TAG, "User doesn't have an activity for notification URI " + uri);
                            }
                        }
                        Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, null);
                        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
                        dismiss();
                    }
                })
                .setNegativeButton(inAppMessage.getCloseButtonText(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
                        dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (inAppNotificationState == null) {
            cleanUp();
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
    }

    public static VisilabsAlertDialogFragment newInstance() {
        return new VisilabsAlertDialogFragment();
    }

    public void setInAppState(int stateId, InAppNotificationState inAppState, Activity parent) {
        this.mInAppStateId = stateId;
        this.inAppNotificationState = inAppState;
        this.parent = parent;
    }

    private void cleanUp() {
        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
        dismiss();
    }

}
