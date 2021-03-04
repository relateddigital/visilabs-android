package com.visilabs.inApp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentCountdownTimerBinding;

import java.util.Timer;
import java.util.TimerTask;

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
    private static final short TIMER_SCHEDULE = 1000;
    private static final short TIMER_PERIOD = 1000;

    private short mWeekNum;
    private short mDayNum;
    private short mHourNum;
    private short mMinuteNum;
    private short mSecondNum;
    private Timer mTimer;
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
        //TODO: Check the position and assign it to mIsTop
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
                //TODO: get coupon text from the response instead of view here
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
                //TODO: get coupon text from the response instead of view here
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
                Toast.makeText(getActivity(), "Go to the Link", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void adjustButtonBot() {
        //TODO this should direct to somewhere
        binding.buttonBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Go to the Link", Toast.LENGTH_LONG).show();
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

        return R.drawable.ic_close_white_24dp;
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
        setTimerValues();
        //TODO check the format here and set the visibilities of the views accordingly
        //TODO: convert bigger part like week to smaller parts like day if necessary according to the format
        binding.weekNumTop.setText(String.valueOf(mWeekNum));
        binding.weekNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.dayNumTop.setText(String.valueOf(mDayNum));
        binding.dayNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.hourNumTop.setText(String.valueOf(mHourNum));
        binding.hourNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.minuteNumTop.setText(String.valueOf(mMinuteNum));
        binding.minuteNumTop.setTextColor(getResources().getColor(R.color.white));
        binding.secNumTop.setText(String.valueOf(mSecondNum));
        binding.secNumTop.setTextColor(getResources().getColor(R.color.white));

        startTimer();

    }

    private void adjustTimerViewBot() {
        setTimerValues();
        //TODO check the format here and set the visibilities of the views accordingly
        //TODO: convert bigger part like week to smaller parts like day if necessary according to the format
        binding.weekNumBot.setText(String.valueOf(mWeekNum));
        binding.weekNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.dayNumBot.setText(String.valueOf(mDayNum));
        binding.dayNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.hourNumBot.setText(String.valueOf(mHourNum));
        binding.hourNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.minuteNumBot.setText(String.valueOf(mMinuteNum));
        binding.minuteNumBot.setTextColor(getResources().getColor(R.color.white));
        binding.secNumBot.setText(String.valueOf(mSecondNum));
        binding.secNumBot.setTextColor(getResources().getColor(R.color.white));

        startTimer();
    }

    private void setTimerValues() {
        //TODO: When real data came, adjust here accordingly
        mWeekNum = 3;
        mDayNum = 5;
        mHourNum = 17;
        mMinuteNum = 0;
        mSecondNum = 6;
    }

    private void startTimer() {
        mTimer = new Timer("CountDownTimer", false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                reAdjustTimerViews();
            }
        };
        mTimer.schedule(task, TIMER_SCHEDULE, TIMER_PERIOD);
    }

    private void reAdjustTimerViews(){
        calculateTimeFields();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mIsTop) {
                        if (binding.weekNumTop.getVisibility() != View.GONE) {
                            binding.weekNumTop.setText(String.valueOf(mWeekNum));
                        }

                        if (binding.dayNumTop.getVisibility() != View.GONE) {
                            binding.dayNumTop.setText(String.valueOf(mDayNum));
                        }

                        if (binding.hourNumTop.getVisibility() != View.GONE) {
                            binding.hourNumTop.setText(String.valueOf(mHourNum));
                        }

                        if (binding.minuteNumTop.getVisibility() != View.GONE) {
                            binding.minuteNumTop.setText(String.valueOf(mMinuteNum));
                        }

                        if (binding.secNumTop.getVisibility() != View.GONE) {
                            binding.secNumTop.setText(String.valueOf(mSecondNum));
                        }
                    } else {
                        if (binding.weekNumBot.getVisibility() != View.GONE) {
                            binding.weekNumBot.setText(String.valueOf(mWeekNum));
                        }

                        if (binding.dayNumBot.getVisibility() != View.GONE) {
                            binding.dayNumBot.setText(String.valueOf(mDayNum));
                        }

                        if (binding.hourNumBot.getVisibility() != View.GONE) {
                            binding.hourNumBot.setText(String.valueOf(mHourNum));
                        }

                        if (binding.minuteNumBot.getVisibility() != View.GONE) {
                            binding.minuteNumBot.setText(String.valueOf(mMinuteNum));
                        }

                        if (binding.secNumBot.getVisibility() != View.GONE) {
                            binding.secNumBot.setText(String.valueOf(mSecondNum));
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Log.e(LOG_TAG, "The fields for countdown timer could not be set!");
                }
            }
        });
    }

    private void calculateTimeFields() {
        //TODO: Adjust the logic here for each format. For example, if there is no week field
        //in the format do not set day to max 6 below.
        if(mSecondNum > 0) {
            mSecondNum--;
        } else {
            mSecondNum = 59;
            if(mMinuteNum > 0){
                mMinuteNum--;
            } else {
                mMinuteNum = 59;
                if(mHourNum > 0) {
                    mHourNum--;
                } else {
                    mHourNum = 23;
                    if(mDayNum > 0) {
                        mDayNum--;
                    } else {
                        mDayNum = 6;
                        if(mWeekNum > 0) {
                            mWeekNum--;
                        } else {
                            expireTime();
                        }
                    }
                }
            }
        }
    }

    private void expireTime() {
        mSecondNum = 0;
        mMinuteNum = 0;
        mHourNum = 0;
        mDayNum = 0;
        mWeekNum = 0;
        if(mTimer!=null){
            mTimer.cancel();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getString(R.string.time_is_over), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void endFragment() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        getActivity().getFragmentManager().beginTransaction().remove(CountdownTimerFragment.this).commit();
    }
}