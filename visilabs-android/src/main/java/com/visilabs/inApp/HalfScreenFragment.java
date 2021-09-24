package com.visilabs.inApp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentHalfScreenBinding;
import com.visilabs.util.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HalfScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HalfScreenFragment extends Fragment {

    private static final String LOG_TAG = "HalfScreenFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "stateIdKey";
    private static final String ARG_PARAM2 = "inAppStateKey";

    private int mStateId;
    private InAppNotificationState mInAppState;
    private boolean mIsTop;
    private FragmentHalfScreenBinding binding;

    public HalfScreenFragment() {
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
    public static HalfScreenFragment newInstance(int stateId, InAppNotificationState inAppState) {
        HalfScreenFragment fragment = new HalfScreenFragment();
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
        binding = FragmentHalfScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if(savedInstanceState != null) {
            //TODO: get the json string here
        } else {
            //TODO: get the json string here
        }

        setupInitialView();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: save the json string here
    }

    private void setupInitialView() {
        //Check the position and assign it to mIsTop
        mIsTop = true;
        if(mIsTop){
            adjustTop();
        } else {
            adjustBottom();
        }
        setupCloseButton();
    }

    private void adjustTop() {
        //TODO remove the code below when the actual data gets ready
        //TODO check if title is on
        if(true) {
            binding.halfScreenContainerTop.setBackgroundColor(getResources().getColor(R.color.black));
            binding.topTitleView.setText("30 Ağustos Zafer Bayramı");
            binding.topTitleView.setTextColor(Color.parseColor("#E02B19"));
            binding.topTitleView.setTextSize(20);
            binding.topTitleView.setTypeface(Typeface.SANS_SERIF);
        } else {
            binding.topTitleView.setVisibility(View.GONE);
        }
        Picasso.get().
                load("https://brtk.net/wp-content/uploads/2021/08/28/30agustossss.jpg?ver=cf14dae8e18a0da9aee40b2c8f3f2b39")
                .into(binding.topImageView);
        binding.topImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO click report here
                //TODO Check if there is buttonCallback
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString("https://www.relateddigital.com/"));
                    startActivity(viewIntent);

                } catch (final ActivityNotFoundException e) {
                    Log.i("Visilabs", "User doesn't have an activity for notification URI");
                }
                endFragment();
            }
        });

        binding.halfScreenContainerBot.setVisibility(View.GONE);
    }

    private void adjustBottom() {
        //TODO remove the code below when the actual data gets ready
        //TODO check if title is on
        if(true) {
            binding.halfScreenContainerBot.setBackgroundColor(getResources().getColor(R.color.black));
            binding.botTitleView.setText("30 Ağustos Zafer Bayramı");
            binding.botTitleView.setTextColor(Color.parseColor("#E02B19"));
            binding.botTitleView.setTextSize(20);
            binding.botTitleView.setTypeface(Typeface.SANS_SERIF);
        } else {
            binding.botTitleView.setVisibility(View.GONE);
        }
        Picasso.get().
                load("https://brtk.net/wp-content/uploads/2021/08/28/30agustossss.jpg?ver=cf14dae8e18a0da9aee40b2c8f3f2b39")
                .into(binding.botImageView);
        binding.botImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO click report here
                //TODO Check if there is buttonCallback
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString("https://www.relateddigital.com/"));
                    startActivity(viewIntent);

                } catch (final ActivityNotFoundException e) {
                    Log.i("Visilabs", "User doesn't have an activity for notification URI");
                }
                endFragment();
            }
        });

        binding.halfScreenContainerTop.setVisibility(View.GONE);
    }

    private void setupCloseButton() {
        //TODO check if close button will be displayed first
        if(mIsTop){
            binding.topCloseButton.setBackgroundResource(getCloseIcon());
            binding.topCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endFragment();
                }
            });
        } else {
            binding.botCloseButton.setBackgroundResource(getCloseIcon());
            binding.botCloseButton.setOnClickListener(new View.OnClickListener() {
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

    private void endFragment() {
        //TODO Release display state here
        if(getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(HalfScreenFragment.this).commit();
        }
    }
}