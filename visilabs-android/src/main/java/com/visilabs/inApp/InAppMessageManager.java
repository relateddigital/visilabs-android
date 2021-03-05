package com.visilabs.inApp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.visilabs.InAppNotificationState;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.mailSub.MailSubscriptionForm;
import com.visilabs.mailSub.MailSubscriptionFormActivity;
import com.visilabs.util.ActivityImageUtils;
import com.visilabs.util.VisilabsConstant;

import java.util.concurrent.locks.ReentrantLock;

public class InAppMessageManager {

    private final String LOG_TAG = "InAppManager";

    private final String mCookieID;
    private final String mDataSource;

    public InAppMessageManager(String cookieID, String dataSource) {
        mCookieID = cookieID;
        mDataSource = dataSource;
    }

    public void showMailSubscriptionForm(final MailSubscriptionForm mailSubscriptionForm, final Activity parent) {

        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            showDebugMessage("Android version is below necessary version");
        }

        parent.runOnUiThread(new Runnable() {
            @Override
            @TargetApi(VisilabsConstant.UI_FEATURES_MIN_API)
            public void run() {

                ReentrantLock lock = VisilabsUpdateDisplayState.getLockObject();
                lock.lock();
                try {
                    if (VisilabsUpdateDisplayState.hasCurrentProposal()) {
                        showDebugMessage("DisplayState is locked, will not show notifications");
                    } else {
                        Intent intent = new Intent(parent, MailSubscriptionFormActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, mailSubscriptionForm));
                        parent.startActivity(intent);
                    }
                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                } finally {
                    lock.unlock();
                }
            }
        });

    }

    public void showInAppMessage(final InAppMessage inAppMessage, final Activity parent) {

        if (Build.VERSION.SDK_INT < VisilabsConstant.UI_FEATURES_MIN_API) {
            showDebugMessage("Android version is below necessary version");
        }

        parent.runOnUiThread(new Runnable() {
            @Override
            @TargetApi(VisilabsConstant.UI_FEATURES_MIN_API)
            public void run() {

                ReentrantLock lock = VisilabsUpdateDisplayState.getLockObject();
                lock.lock();
                try {
                    boolean willShowInApp = true;
                    if (VisilabsUpdateDisplayState.hasCurrentProposal()) {
                        showDebugMessage("DisplayState is locked, will not show notifications");
                        willShowInApp = false;
                    }
                    if (inAppMessage.getActionData().getMsgType() == null) {
                        showDebugMessage("No in app available, will not show.");
                        willShowInApp = false;
                    }
                    if (inAppMessage.getActionData().getMsgType() == InAppActionType.FULL &&
                            !VisilabsConstant.checkNotificationActivityAvailable(parent.getApplicationContext())) {
                        showDebugMessage("Application is not configured to show full screen in app, none will be shown.");
                        willShowInApp = false;
                    }

                    if(!willShowInApp){
                        return;
                    }

                    Intent intent = new Intent(parent, TemplateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    int stateId;
                    VisilabsUpdateDisplayState visilabsUpdateDisplayState;

                    switch (inAppMessage.getActionData().getMsgType()) {


                        case UNKNOWN:

                            break;

                        case MINI:

                            stateId = getStateId(parent, inAppMessage);

                            visilabsUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(stateId);

                            if (visilabsUpdateDisplayState == null) {
                                showDebugMessage("Notification's display proposal was already consumed, no notification will be shown.");
                            } else {
                                openInAppMiniFragment(stateId, parent, visilabsUpdateDisplayState);
                            }

                            break;


                        case FULL:

                            openInAppActivity(parent, getStateId(parent, inAppMessage));

                            break;

                        case FULL_IMAGE:

                        case SMILE_RATING:

                        case NPS:

                        case IMAGE_TEXT_BUTTON:

                        case NPS_WITH_NUMBERS:


                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            parent.startActivity(intent);

                            break;


                        case IMAGE_BUTTON:

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            parent.startActivity(intent);

                            break;

                        case ALERT:

                            stateId = getStateId(parent, inAppMessage);

                            visilabsUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(stateId);

                            if (visilabsUpdateDisplayState == null) {
                                showDebugMessage("Notification's display proposal was already consumed, no notification will be shown.");
                            } else {
                                if(inAppMessage.getActionData().getAlertType() != null && inAppMessage.getActionData().getAlertType().equals("actionSheet")) {
                                    openInAppActionSheet(stateId, parent, visilabsUpdateDisplayState);
                                } else {
                                    openInAppAlert(stateId, parent, visilabsUpdateDisplayState);
                                }
                            }


                            break;

                        default:
                            Log.e(LOG_TAG, "Unrecognized notification type " + inAppMessage.getActionData().getMsgType() + " can't be shown");
                    }

                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                } finally {
                    lock.unlock();
                }
            }
        });

    }

    private int getStateId(Activity parent, MailSubscriptionForm mailSubscriptionForm) {
        int highlightColor = ActivityImageUtils.getHighlightColorFromBackground(parent);

        InAppNotificationState inAppNotificationState =  new InAppNotificationState(mailSubscriptionForm, highlightColor);

        int stateID = VisilabsUpdateDisplayState.proposeDisplay(inAppNotificationState, mCookieID, mDataSource);

        if (stateID <= 0) {
            Log.e(LOG_TAG, "DisplayState Lock in inconsistent state!");
        }

        return stateID;
    }

    private int getStateId(Activity parent, InAppMessage inAppMessage) {
        int highlightColor = ActivityImageUtils.getHighlightColorFromBackground(parent);

        InAppNotificationState inAppNotificationState =  new InAppNotificationState(inAppMessage, highlightColor);

        int stateID = VisilabsUpdateDisplayState.proposeDisplay(inAppNotificationState, mCookieID, mDataSource);

        if (stateID <= 0) {
            Log.e(LOG_TAG, "DisplayState Lock in inconsistent state!");
        }

        return stateID;
    }

    private void openInAppMiniFragment(int stateID, Activity parent, VisilabsUpdateDisplayState visilabsUpdateDisplayState) {

        VisilabsInAppFragment visilabsInAppFragment = new VisilabsInAppFragment();
        if (visilabsUpdateDisplayState.getDisplayState() != null) {
            visilabsInAppFragment.setInAppState(stateID, (InAppNotificationState) visilabsUpdateDisplayState.getDisplayState());

            visilabsInAppFragment.setRetainInstance(true);

            FragmentTransaction transaction = parent.getFragmentManager().beginTransaction();
            transaction.add(android.R.id.content, visilabsInAppFragment);
            transaction.commit();
        }
    }

    private void openInAppActivity(Activity parent, int inAppData) {

        Intent intent = new Intent(parent.getApplicationContext(), VisilabsInAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, inAppData);
        parent.startActivity(intent);
    }

    private void openInAppAlert(final int stateID, final Activity parent, VisilabsUpdateDisplayState visilabsUpdateDisplayState) {
        if(visilabsUpdateDisplayState.getDisplayState() == null){
            VisilabsUpdateDisplayState.releaseDisplayState(stateID);
            return;
        }
        if(parent instanceof FragmentActivity) {
            InAppNotificationState state = (InAppNotificationState) visilabsUpdateDisplayState.getDisplayState();
            FragmentActivity fragmentActivity = (FragmentActivity)parent;
            VisilabsAlertDialogFragment visilabsAlertDialogFragment = VisilabsAlertDialogFragment.newInstance();
            visilabsAlertDialogFragment.setCancelable(false);
            visilabsAlertDialogFragment.setInAppState(stateID, state, parent);
            visilabsAlertDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "visilabs_alert_dialog_fragment");
        }
    }

    private void openInAppActionSheet(final int stateID, final Activity parent, VisilabsUpdateDisplayState visilabsUpdateDisplayState) {
        if(visilabsUpdateDisplayState.getDisplayState() == null){
            VisilabsUpdateDisplayState.releaseDisplayState(stateID);
            return;
        }
        if(parent instanceof FragmentActivity) {
            InAppNotificationState state = (InAppNotificationState) visilabsUpdateDisplayState.getDisplayState();
            FragmentActivity fragmentActivity = (FragmentActivity)parent;
            VisilabsBottomSheetDialogFragment visilabsBottomSheetDialogFragment = VisilabsBottomSheetDialogFragment.newInstance();
            visilabsBottomSheetDialogFragment.setCancelable(false);
            visilabsBottomSheetDialogFragment.setInAppState(stateID, state);
            visilabsBottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "visilabs_bottom_sheet_dialog_fragment");
        }
    }


    private void showDebugMessage(String message) {
        if (VisilabsConstant.DEBUG) {
            Log.v(LOG_TAG, message);
        }
    }
}
