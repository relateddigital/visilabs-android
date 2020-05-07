package com.visilabs.inApp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.visilabs.InAppNotificationState;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.ActivityImageUtils;
import com.visilabs.util.VisilabsConfig;

import java.util.concurrent.locks.ReentrantLock;

public class InAppMessageManager {

    private String _cookieID;
    private String _dataSource;

    private String LOG_TAG = "InAppManager";

    public InAppMessageManager(String cookieID, String dataSource) {
        this._cookieID = cookieID;
        this._dataSource = dataSource;
    }

    public void showInAppMessage(final InAppMessage inAppMessage, final Activity parent) {

        if (Build.VERSION.SDK_INT < VisilabsConfig.UI_FEATURES_MIN_API) {

            showDebugMessage("Android version is below necessary version");
        }

        parent.runOnUiThread(new Runnable() {
            @Override
            @TargetApi(VisilabsConfig.UI_FEATURES_MIN_API)
            public void run() {

                ReentrantLock lock = VisilabsUpdateDisplayState.getLockObject();
                lock.lock();

                try {

                    if (VisilabsUpdateDisplayState.hasCurrentProposal()) {
                        showDebugMessage("DisplayState is locked, will not show notifications");
                    }
                    if (inAppMessage.getType() == null) {
                        showDebugMessage("No in app available, will not show.");
                    }

                    if (inAppMessage.getType() == InAppActionType.FULL && !VisilabsConfig.checkNotificationActivityAvailable(parent.getApplicationContext())) {
                        showDebugMessage("Application is not configured to show full screen in app, none will be shown.");
                    }

                    AppCompatActivity context = (AppCompatActivity) parent;

                    Intent intent = new Intent(context, TemplateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    switch (inAppMessage.getType()) {


                        case UNKNOWN:

                            break;

                        case MINI:

                            VisilabsUpdateDisplayState visilabsUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(getStateId(parent, inAppMessage));

                            if (visilabsUpdateDisplayState == null) {
                                showDebugMessage("Notification's display proposal was already consumed, no notification will be shown.");
                            } else {

                                openInAppMiniFragment(getStateId(parent, inAppMessage), parent, visilabsUpdateDisplayState);
                            }

                            break;


                        case FULL:

                            openInAppActivity(parent, getStateId(parent, inAppMessage));

                            break;

                        case FULL_IMAGE:

                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            context.startActivity(intent);

                            break;

                        case SMILE_RATING:

                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            context.startActivity(intent);

                            break;

                        case NPS:


                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            context.startActivity(intent);

                            break;

                        case IMAGE_TEXT_BUTTON:

                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            context.startActivity(intent);

                            break;


                        case IMAGE_BUTTON:

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra(VisilabsInAppActivity.INTENT_ID_KEY, getStateId(parent, inAppMessage));

                            context.startActivity(intent);

                            break;

                        default:
                            Log.e(LOG_TAG, "Unrecognized notification type " + inAppMessage.getType() + " can't be shown");
                    }

                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                } finally {
                    lock.unlock();
                }
            }
        });

    }

    private int getStateId(Activity parent, InAppMessage inAppMessage) {
        int highlightColor = ActivityImageUtils.getHighlightColorFromBackground(parent);

        InAppNotificationState inAppNotificationState =
                new InAppNotificationState(inAppMessage, highlightColor);

        int stateID = VisilabsUpdateDisplayState.proposeDisplay(inAppNotificationState, _cookieID, _dataSource);

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

    private void showDebugMessage(String message) {
        if (VisilabsConfig.DEBUG) {
            Log.v(LOG_TAG, message);
        }
    }
}
