package com.visilabs.util;

import java.util.List;
import java.util.TimerTask;

import android.app.Activity;
import android.util.Log;

import com.visilabs.Visilabs;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.inApp.InAppMessageManager;

public class VisilabsTimerTask extends TimerTask {

    private static final String LOG_TAG = "VisilabsTimerTask";
    private final String mType;
    private final int mActId;
    private final List<InAppMessage> mMessages;
    private final Activity mParent;
    private InAppMessage mMessage;

    public VisilabsTimerTask(final String type, final int actId, final List<InAppMessage> messages , final Activity parent) {
        mType = type;
        mActId = actId;
        mMessages = messages;
        mParent = parent;
        selectMessage();
    }

    private void selectMessage() {
        if (mActId > 0) {
            for (int i = 0; i < mMessages.size(); i++) {
                if (mMessages.get(i) != null && mMessages.get(i).getActId() == mActId) {
                    mMessage = mMessages.get(i);
                    break;
                }
            }
        }
        if (mMessage == null && mType != null) {
            for (int i = 0; i < mMessages.size(); i++) {
                if (mMessages.get(i) != null && mMessages.get(i).getActionData().getMsgType().toString().equals(mType)) {
                    mMessage = mMessages.get(i);
                    break;
                }
            }
        }
        if (mMessage == null && mMessages.size() != 0) {
            mMessage = mMessages.get(0);
        }
    }

    public InAppMessage getMessage() {
        return mMessage;
    }

    @Override
    public void run() {
        cancel();
        if(mParent == null) {
            Log.e(LOG_TAG, "Could not display the in-app template since the user has changed the original page!");
            return;
        }
        if(mMessage != null) {
            new InAppMessageManager(Visilabs.CallAPI().getCookieID(), Visilabs.CallAPI().getDataSource()).showInAppMessage(mMessage, mParent);

            if (mMessage.getActionData().getVisitData() != null && !mMessage.getActionData().getVisitData().equals("")) {
                Log.v("mVisitData", mMessage.getActionData().getVisitData());
                Visilabs.CallAPI().setVisitData(mMessage.getActionData().getVisitData());
            }

            if (mMessage.getActionData().getVisitorData() != null && !mMessage.getActionData().getVisitorData().equals("")) {
                Visilabs.CallAPI().setVisitorData(mMessage.getActionData().getVisitorData());
                Log.v("mVisitorData", mMessage.getActionData().getVisitorData());
            }
        }
    }
}
