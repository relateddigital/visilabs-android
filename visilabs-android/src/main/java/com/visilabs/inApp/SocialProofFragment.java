package com.visilabs.inApp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentSocialProofBinding;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialProofFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialProofFragment extends Fragment {

    private static final String LOG_TAG = "SocialProofFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "stateIdKey";
    private static final String ARG_PARAM2 = "inAppStateKey";

    private int mStateId;
    private InAppNotificationState mInAppState;
    private boolean mIsTop;
    private Timer mTimer;
    private FragmentSocialProofBinding binding;

    public SocialProofFragment() {
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
    // TODO: Rename and change types and number of parameters
    public static SocialProofFragment newInstance(int stateId, InAppNotificationState inAppState) {
        SocialProofFragment fragment = new SocialProofFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSocialProofBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //TODO: send impression report here

        if(isUnderThreshold()){
            endFragment();
        }

        setupInitialView();
        return view;
    }

    private void setupInitialView() {
        //Check the position and assign it to mIsTop
        mIsTop = true;
        if(mIsTop){
            adjustTop();
        } else {
            adjustBottom();
        }
        //TODO check if there is timer
        setTimer();
        setupCloseButton();
    }

    private void adjustTop() {
        //TODO remove the code below when the actual data gets ready
        binding.socialProofContainerTop.setBackgroundColor(getResources().getColor(R.color.yellow));
        binding.numberTextViewTop.setTextColor(getResources().getColor(R.color.design_default_color_error));
        binding.explanationTextViewTop.setTextColor(getResources().getColor(R.color.bottom_sheet_button_color));

        binding.socialProofContainerBot.setVisibility(View.GONE);
    }

    private void adjustBottom() {
        //TODO remove the code below when the actual data gets ready
        binding.socialProofContainerBot.setBackgroundColor(getResources().getColor(R.color.yellow));
        binding.numberTextViewBot.setTextColor(getResources().getColor(R.color.design_default_color_error));
        binding.explanationTextViewBot.setTextColor(getResources().getColor(R.color.bottom_sheet_button_color));

        binding.socialProofContainerTop.setVisibility(View.GONE);
    }

    private void setTimer() {
        //TODO check the data if it is "will stay until clicked"
        mTimer = new Timer("SocialProofTimer", false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                endFragment();
            }
        };
        mTimer.schedule(task, 5000); // TODO instead of dummy here, put real data.

        //TODO If will stay until clicked, then
        /*if(mIsTop){
            binding.socialProofContainerTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        } else {
            binding.socialProofContainerBot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        }*/
    }

    private void setupCloseButton() {
        //TODO check if close button will be displayed first
        if(mIsTop){
            binding.closeButtonTop.setBackgroundResource(getCloseIcon());
            binding.closeButtonTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        } else {
            binding.closeButtonBot.setBackgroundResource(getCloseIcon());
            binding.closeButtonBot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        }
    }

    private int getCloseIcon() {

        return R.drawable.ic_close_black_24dp;
        //TODO when real data comes:
       /* switch (mInAppMessage.getActionData().getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;*/
    }

    private boolean isUnderThreshold() {
        //TODO Check if the number is smaller than the threshold
        return false;
    }

    private void endFragment() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        getActivity().getFragmentManager().beginTransaction().remove(SocialProofFragment.this).commit();
    }
}