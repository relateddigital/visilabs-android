package com.visilabs.inApp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentCountdownTimerBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownTimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountdownTimerFragment extends Fragment {

    private static final String LOG_TAG = "CountdownTimerFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "stateIdKey";
    private static final String ARG_PARAM2 = "inAppStateKey";

    private int mStateId;
    private InAppNotificationState mInAppState;
    private boolean mIsTop;
    private FragmentCountdownTimerBinding binding;

    public CountdownTimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stateId    Parameter 1.
     * @param inAppState Parameter 2.
     * @return A new instance of fragment SocialProofFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CountdownTimerFragment newInstance(int stateId, InAppNotificationState inAppState) {
        CountdownTimerFragment fragment = new CountdownTimerFragment();
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
        binding = FragmentCountdownTimerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupInitialView();
        return view;
    }

    private void setupInitialView() {
        //Check the position and assign it to mIsTop
        mIsTop = true;
        if (mIsTop) {
            adjustTop();
        } else {
            adjustBottom();
        }
        setupCloseButton();
    }

    private void adjustTop() {
        //TODO remove the code below when the actual data gets ready
        binding.countdownTimerContainerTop.setBackgroundColor(getResources().getColor(R.color.blue));
        binding.titleTop.setTextColor(getResources().getColor(R.color.white));
        binding.titleTop.setText("Sana Özel Fırsatı Kaçırma!");
        binding.bodyTextTop.setTextColor(getResources().getColor(R.color.white));
        binding.bodyTextTop.setText("Bugün sana özel indirim kodu için geri sayım başladı.");
        binding.buttonTop.setBackgroundColor(getResources().getColor(R.color.white));
        binding.buttonTop.setText("Alışverişe Başla");
        binding.buttonTop.setTextColor(getResources().getColor(R.color.black));

        binding.countdownTimerContainerBot.setVisibility(View.GONE);

        adjustCouponViewTop();
        adjustTimerViewTop();
        adjustButtonTop();
    }

    private void adjustBottom() {
        //TODO remove the code below when the actual data gets ready
        binding.countdownTimerContainerBot.setBackgroundColor(getResources().getColor(R.color.blue));
        binding.titleBot.setTextColor(getResources().getColor(R.color.white));
        binding.titleBot.setText("Sana Özel Fırsatı Kaçırma!");
        binding.bodyTextBot.setTextColor(getResources().getColor(R.color.white));
        binding.bodyTextBot.setText("Bugün sana özel indirim kodu için geri sayım başladı.");
        binding.buttonBot.setBackgroundColor(getResources().getColor(R.color.white));
        binding.buttonBot.setText("Alışverişe Başla");
        binding.buttonBot.setTextColor(getResources().getColor(R.color.black));

        binding.countdownTimerContainerTop.setVisibility(View.GONE);

        adjustCouponViewBot();
        adjustTimerViewBot();
        adjustButtonBot();
    }

    private void adjustCouponViewTop() {
        //TODO if there is coupon code
        binding.couponTop.setVisibility(View.VISIBLE);
        binding.couponTextTop.setText("1D48KNSDF92A");
        binding.couponTextTop.setTextColor(getResources().getColor(R.color.black));
        binding.couponTop.setBackgroundColor(getResources().getColor(R.color.white));
        binding.couponButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), binding.couponTextTop.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                //TODO track this click later
            }
        });
    }

    private void adjustCouponViewBot() {
        //TODO if there is coupon code
        binding.couponBot.setVisibility(View.VISIBLE);
        binding.couponTextBot.setText("1D48KNSDF92A");
        binding.couponTextBot.setTextColor(getResources().getColor(R.color.black));
        binding.couponBot.setBackgroundColor(getResources().getColor(R.color.white));
        binding.couponButtonBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), binding.couponTextBot.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                //TODO track this click later
            }
        });
    }

    private void adjustButtonTop() {
        //TODO this should direct to somewhere
        binding.buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Alışverişe Başla", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void adjustButtonBot() {
        //TODO this should direct to somewhere
        binding.buttonBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Alışverişe Başla", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupCloseButton() {
        //TODO check if close button will be displayed first
        if (mIsTop) {
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

    private void adjustTimerViewTop() {
        //TODO check the format here and set the visibilities of the views accordingly
        binding.weekNumTop.setText("3");
        binding.weekNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.dayNumTop.setText("4");
        binding.dayNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.hourNumTop.setText("18");
        binding.hourNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.minuteNumTop.setText("47");
        binding.minuteNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.secNumTop.setText("33");
        binding.secNumTop.setTextColor(getResources().getColor(R.color.white));

    }

    private void adjustTimerViewBot() {
        //TODO check the format here and set the visibilities of the views accordingly
        binding.weekNumBot.setText("3");
        binding.weekNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.dayNumBot.setText("4");
        binding.dayNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.hourNumBot.setText("18");
        binding.hourNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.minuteNumBot.setText("47");
        binding.minuteNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.secNumBot.setText("33");
        binding.secNumBot.setTextColor(getResources().getColor(R.color.white));
    }

    private void endFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(CountdownTimerFragment.this).commit();
    }
}