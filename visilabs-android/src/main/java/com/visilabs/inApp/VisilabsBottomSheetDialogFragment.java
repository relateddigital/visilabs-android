package com.visilabs.inApp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.api.VisilabsUpdateDisplayState;

public class VisilabsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String LOG_TAG = "VisilabsBottomSheet";
    private Activity mParent;
    private int mInAppStateId;
    private InAppNotificationState inAppNotificationState;

    TextView tvTitle, tvBody, tvButton, tvClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_in_app_bottom_sheet, container, false);
        if (inAppNotificationState == null) {
            return view;
        }
        InAppMessage inApp = inAppNotificationState.getInAppMessage();
        tvTitle = view.findViewById(R.id.tv_title);
        tvBody = view.findViewById(R.id.tv_body);
        tvButton = view.findViewById(R.id.tv_button);
        tvClose = view.findViewById(R.id.tv_close);
        tvTitle.setText(inApp.getTitle().replace("\\n","\n"));
        tvBody.setText(inApp.getBody().replace("\\n","\n"));
        tvButton.setText(inApp.getButtonText().toUpperCase());
        tvClose.setText(inApp.getCloseButtonText().toUpperCase());
        setListeners();
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet); //(FrameLayout) dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent = activity;
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

    public static VisilabsBottomSheetDialogFragment newInstance() {
        return new VisilabsBottomSheetDialogFragment();
    }

    public void setInAppState(int stateId, InAppNotificationState inAppState) {
        this.mInAppStateId = stateId;
        this.inAppNotificationState = inAppState;
    }

    private void setListeners() {
        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InAppMessage inAppMessage = inAppNotificationState.getInAppMessage();
                final String uriString = inAppMessage.getButtonURL();
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
                Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, null);
                cleanUp();
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanUp();
            }
        });
    }

    private void cleanUp() {
        VisilabsUpdateDisplayState.releaseDisplayState(mInAppStateId);
        dismiss();
    }

}
