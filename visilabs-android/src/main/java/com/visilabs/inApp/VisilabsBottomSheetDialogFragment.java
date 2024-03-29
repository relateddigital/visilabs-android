package com.visilabs.inApp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.FragmentInAppBottomSheetBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;

public class VisilabsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String LOG_TAG = "VisilabsBottomSheet";
    private Context mParent;
    private int mInAppStateId;
    private InAppNotificationState mInAppNotificationState;
    private FragmentInAppBottomSheetBinding binding;
    private InAppMessage mInAppMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentInAppBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (mInAppMessage == null) {
            cleanUp();
            return view;
        }
        binding.tvTitle.setText(mInAppMessage.getActionData().getMsgTitle().replace("\\n","\n"));
        binding.tvBody.setText(mInAppMessage.getActionData().getMsgBody().replace("\\n","\n"));
        binding.tvButton.setText(mInAppMessage.getActionData().getBtnText().toUpperCase());
        binding.tvClose.setText(mInAppMessage.getActionData().getCloseButtonText().toUpperCase());
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = context;
        if (mInAppMessage == null || mInAppNotificationState == null) {
            Log.e(LOG_TAG, "InAppMessage is null! Could not get display state!");
            cleanUp();
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
        mInAppStateId = stateId;
        mInAppNotificationState = inAppState;
        if(mInAppNotificationState != null) {
            mInAppMessage = mInAppNotificationState.getInAppMessage();
        }
    }

    private void setListeners() {
        binding.tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uriString = mInAppMessage.getActionData().getAndroidLnk();
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
                Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, null);
                cleanUp();
            }
        });
        binding.tvClose.setOnClickListener(new View.OnClickListener() {
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
