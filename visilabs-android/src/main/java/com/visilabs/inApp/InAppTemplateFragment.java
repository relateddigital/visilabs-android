package com.visilabs.inApp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentTemplateBinding;

public class InAppTemplateFragment extends Fragment {

    FragmentTemplateBinding mBinding;

    private int mInAppStateId;
    private InAppNotificationState inAppNotificationState;

    public void setInAppState(int stateID, InAppNotificationState inAppNotificationState) {
        this.mInAppStateId = stateID;
        this.inAppNotificationState = inAppNotificationState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(inAppNotificationState != null) {

            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_template, container, false);

            InAppMessage inAppMessage = inAppNotificationState.getInAppMessage();

        }
        return mBinding.getRoot();
    }
}
