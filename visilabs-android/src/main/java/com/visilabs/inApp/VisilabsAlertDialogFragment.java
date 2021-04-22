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
    private InAppNotificationState mInAppNotificationState;
    private Activity mParent;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final InAppMessage inAppMessage = mInAppNotificationState.getInAppMessage();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mParent, R.style.AlertDialogStyle);
        alertDialogBuilder.setTitle(inAppMessage.getActionData().getMsgTitle().replace("\\n","\n"))
                .setMessage(inAppMessage.getActionData().getMsgBody().replace("\\n","\n"))
                .setCancelable(false)
                .setPositiveButton(inAppMessage.getActionData().getBtnText(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String uriString = inAppMessage.getActionData().getAndroidLnk();
                        InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
                        if(buttonInterface != null) {
                            Visilabs.CallAPI().setInAppButtonInterface(null);
                            buttonInterface.onPress(uriString);
                        } else {
                            Uri uri = null;
                            if (uriString != null && uriString.length() > 0) {
                                try {
                                    uri = Uri.parse(uriString);
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                                    mParent.startActivity(viewIntent);
                                } catch (IllegalArgumentException e) {
                                    Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                                } catch (ActivityNotFoundException e) {
                                    Log.i(LOG_TAG, "User doesn't have an activity for notification URI " + uri);
                                }
                            }
                        }
                        Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, null);
                        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
                        dismiss();
                    }
                })
                .setNegativeButton(inAppMessage.getActionData().getCloseButtonText(), new DialogInterface.OnClickListener() {
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
        if (mInAppNotificationState == null) {
            cleanUp();
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
        mInAppNotificationState = inAppState;
        mParent = parent;
    }

    private void cleanUp() {
        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
        dismiss();
    }

}
