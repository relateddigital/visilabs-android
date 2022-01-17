package com.visilabs.inApp.notification;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentNotificationSmallBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.inApp.InAppMessage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationSmallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationSmallFragment extends Fragment {

    private static final String LOG_TAG = "NotificationSFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "stateIdKey";
    private static final String ARG_PARAM2 = "inAppStateKey";

    private int mStateId;
    private InAppNotificationState mInAppState;
    private InAppMessage mInAppMessage;
    private boolean mIsRight;
    private FragmentNotificationSmallBinding binding;

    public NotificationSmallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stateId Parameter 1.
     * @param inAppState Parameter 2.
     * @return A new instance of fragment SocialProofFragment.
     */
    public static NotificationSmallFragment newInstance(int stateId, InAppNotificationState inAppState) {
        NotificationSmallFragment fragment = new NotificationSmallFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, stateId);
        args.putParcelable(ARG_PARAM2, inAppState);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStateId = getArguments().getInt(ARG_PARAM1);
        mInAppState = getArguments().getParcelable(ARG_PARAM2);
        if(mInAppState != null) {
            mInAppMessage = mInAppState.getInAppMessage();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationSmallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // TODO: Open this
        /*if (mInAppState != null) {
            if(mInAppMessage == null) {
                endFragment();
                Log.e(LOG_TAG, "Could not get the data, closing in app");
            } else {
                setupInitialView();
            }

        } else {
            endFragment();
            Log.e(LOG_TAG, "Could not get the data, closing in app");
        }*/

        setupInitialView();
        return view;
    }

    private void setupInitialView() {

        //TODO : get real value of mIsRight
        mIsRight = false;
        if(mIsRight){
            adjustRight();
        } else {
            adjustLeft();
        }
    }

    private void adjustRight() {
        //TODO : real data here
        GradientDrawable gd = (GradientDrawable) binding.rightContainer.getBackground();
        gd.setColor(getResources().getColor(R.color.blue));
        binding.rightContainer.setBackgroundResource(R.drawable.rounded_corners_left);
        binding.rightArrowView.setTextColor(getResources().getColor(R.color.white));
        binding.rightArrowView.setTextSize(32);
        binding.rightTitleView.setText("Discount");
        binding.rightTitleView.setTextColor(getResources().getColor(R.color.white));
        binding.rightTitleView.setTextSize(32);
        binding.rightContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFragment();
                //TODO : Open notification big fragment here
            }
        });

        binding.leftContainer.setVisibility(View.GONE);
    }

    private void adjustLeft() {
        //TODO : real data here
        GradientDrawable gd = (GradientDrawable) binding.leftContainer.getBackground();
        gd.setColor(getResources().getColor(R.color.blue));
        binding.leftArrowView.setTextColor(getResources().getColor(R.color.white));
        binding.leftArrowView.setTextSize(32);
        binding.leftTitleView.setText("Discount");
        binding.leftTitleView.setTextColor(getResources().getColor(R.color.white));
        binding.leftTitleView.setTextSize(32);
        binding.leftContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFragment();
                //TODO : Open notification big fragment here
            }
        });

        binding.rightContainer.setVisibility(View.GONE);
    }

    private void endFragment() {
        if(getActivity() != null) {
            VisilabsUpdateDisplayState.releaseDisplayState(mStateId);
            getActivity().getFragmentManager().beginTransaction().remove(NotificationSmallFragment.this).commit();
        }
    }
}
