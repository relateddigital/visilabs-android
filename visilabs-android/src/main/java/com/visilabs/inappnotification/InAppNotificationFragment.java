package com.visilabs.inappnotification;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentInAppNotificationLbBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationLmBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationLtBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationRbBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationRmBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationRtBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InAppNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InAppNotificationFragment extends Fragment {

    enum PositionOnScreen {
        TOP,
        MIDDLE,
        BOTTOM
    }

    enum Shape {
        CIRCLE,
        SHARP_EDGE,
        SOFT_EDGE
    }

    private static final String LOG_TAG = "InAppNotification";
    private static final String ARG_PARAM1 = "dataKey";

    private FragmentInAppNotificationLtBinding bindingLt;
    private FragmentInAppNotificationLmBinding bindingLm;
    private FragmentInAppNotificationLbBinding bindingLb;
    private FragmentInAppNotificationRtBinding bindingRt;
    private FragmentInAppNotificationRmBinding bindingRm;
    private FragmentInAppNotificationRbBinding bindingRb;

    private boolean isRight = true;
    private PositionOnScreen positionOnScreen;
    private boolean isTopToBottom = true;
    private boolean isExpanded = false;
    private boolean isSmallImage = false;
    private Shape shape = Shape.SOFT_EDGE;

    public InAppNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param model Parameter 1.
     * @return A new instance of fragment InAppNotificationFragment.
     */
    public static InAppNotificationFragment newInstance(InAppNotificationModel model) {
        InAppNotificationFragment fragment = new InAppNotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : get the real data here and if rotation, get mModel from savedInstanceState
        //mModel = (InAppNotificationModel) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

        //TODO : get the real data here
        isRight = true;
        isTopToBottom = true;
        positionOnScreen = PositionOnScreen.BOTTOM;
        isSmallImage = true;
        shape = Shape.CIRCLE;

        if(isRight) {
            switch (positionOnScreen) {
                case TOP:
                    bindingRt = FragmentInAppNotificationRtBinding.inflate(inflater, container, false);
                    view = bindingRt.getRoot();
                    break;
                case MIDDLE:
                    bindingRm = FragmentInAppNotificationRmBinding.inflate(inflater, container, false);
                    view = bindingRm.getRoot();
                    break;
                default:
                    bindingRb = FragmentInAppNotificationRbBinding.inflate(inflater, container, false);
                    view = bindingRb.getRoot();
                    break;
            }
        } else {
            switch (positionOnScreen) {
                case TOP:
                    bindingLt = FragmentInAppNotificationLtBinding.inflate(inflater, container, false);
                    view = bindingLt.getRoot();
                    break;
                case MIDDLE:
                    bindingLm = FragmentInAppNotificationLmBinding.inflate(inflater, container, false);
                    view = bindingLm.getRoot();
                    break;
                default:
                    bindingLb = FragmentInAppNotificationLbBinding.inflate(inflater, container, false);
                    view = bindingLb.getRoot();
                    break;
            }
        }

        setupInitialView();
        return view;
    }

    private void setupInitialView() {
        if(isRight) {
            switch (positionOnScreen) {
                case TOP:
                    adjustRt();
                    break;
                case MIDDLE:
                    adjustRm();
                    break;
                default:
                    adjustRb();
                    break;
            }
        } else {
            switch (positionOnScreen) {
                case TOP:
                    adjustLt();
                    break;
                case MIDDLE:
                    adjustLm();
                    break;
                default:
                    adjustLb();
                    break;
            }
        }
    }

    private void adjustRt() {
        //TODO : real data here
    }

    private void adjustRm() {
        //TODO : real data here
    }

    private void adjustRb() {
        //TODO : real data here
        bindingRb.smallSquareContainerRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleContainerRb.setVisibility(View.VISIBLE);
        bindingRb.arrowSquareRb.setVisibility(View.VISIBLE);
        bindingRb.arrowCircleRb.setVisibility(View.VISIBLE);
        bindingRb.smallSquareTextRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleTextRb.setVisibility(View.VISIBLE);
        bindingRb.smallSquareImageRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleImageRb.setVisibility(View.VISIBLE);
        bindingRb.bigContainerRb.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                bindingRb.smallSquareContainerRb.setBackgroundColor(getResources().getColor(R.color.blue));
                bindingRb.smallCircleContainerRb.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                bindingRb.smallSquareContainerRb.setBackgroundResource(R.drawable.rounded_corners_left);
                bindingRb.smallSquareTextRb.setBackgroundResource(R.drawable.rounded_corners_left);
                bindingRb.smallSquareImageRb.setBackgroundResource(R.drawable.rounded_corners_left);
                GradientDrawable gd = (GradientDrawable) bindingRb.smallSquareContainerRb.getBackground();
                gd.setColor(getResources().getColor(R.color.blue));
                GradientDrawable gdText = (GradientDrawable) bindingRb.smallSquareTextRb.getBackground();
                gdText.setColor(getResources().getColor(R.color.blue));
                GradientDrawable gdImage = (GradientDrawable) bindingRb.smallSquareImageRb.getBackground();
                gdImage.setColor(getResources().getColor(R.color.blue));
                bindingRb.smallCircleContainerRb.setVisibility(View.GONE);
                break;
            case CIRCLE:
                bindingRb.smallCircleContainerRb.setBackgroundResource(R.drawable.left_half_circle);
                bindingRb.smallCircleTextRb.setBackgroundResource(R.drawable.left_half_circle);
                bindingRb.smallCircleImageRb.setBackgroundResource(R.drawable.left_half_circle);
                GradientDrawable gdCircle = (GradientDrawable) bindingRb.smallCircleContainerRb.getBackground();
                gdCircle.setColor(getResources().getColor(R.color.blue));
                GradientDrawable gdCircleText = (GradientDrawable) bindingRb.smallCircleTextRb.getBackground();
                gdCircleText.setColor(getResources().getColor(R.color.blue));
                GradientDrawable gdCircleImage = (GradientDrawable) bindingRb.smallCircleImageRb.getBackground();
                gdCircleImage.setColor(getResources().getColor(R.color.blue));
                bindingRb.smallSquareContainerRb.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(isExpanded) {
                bindingRb.arrowCircleRb.setText(getString(R.string.notification_left_arrow));
            } else {
                bindingRb.arrowCircleRb.setText(getString(R.string.notification_right_arrow));
            }
            bindingRb.arrowCircleRb.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingRb.smallCircleImageRb);
                bindingRb.smallCircleTextRb.setVisibility(View.GONE);
            } else {
                bindingRb.smallCircleTextRb.setText("Discount");
                bindingRb.smallCircleTextRb.setTextColor(getResources().getColor(R.color.white));
                bindingRb.smallCircleTextRb.setTypeface(Typeface.MONOSPACE);
                bindingRb.smallCircleImageRb.setVisibility(View.GONE);
                bindingRb.smallCircleTextRb.topDown = isTopToBottom;
                bindingRb.smallCircleTextRb.isCircle = true;
            }

            bindingRb.smallCircleContainerRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isExpanded) {
                        isExpanded = false;
                        bindingRb.bigContainerRb.setVisibility(View.GONE);
                        bindingRb.arrowCircleRb.setText(getString(R.string.notification_right_arrow));
                    } else {
                        isExpanded = true;
                        bindingRb.bigContainerRb.setVisibility(View.VISIBLE);
                        bindingRb.arrowCircleRb.setText(getString(R.string.notification_left_arrow));
                    }
                }
            });
        } else {
            if(isExpanded) {
                bindingRb.arrowSquareRb.setText(getString(R.string.notification_left_arrow));
            } else {
                bindingRb.arrowSquareRb.setText(getString(R.string.notification_right_arrow));
            }
            bindingRb.arrowSquareRb.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingRb.smallSquareImageRb);
                bindingRb.smallSquareTextRb.setVisibility(View.GONE);
            } else {
                bindingRb.smallSquareTextRb.setText("Discount");
                bindingRb.smallSquareTextRb.setTextColor(getResources().getColor(R.color.white));
                bindingRb.smallSquareTextRb.setTypeface(Typeface.MONOSPACE);
                bindingRb.smallSquareImageRb.setVisibility(View.GONE);
                bindingRb.smallSquareTextRb.topDown = isTopToBottom;
                bindingRb.smallCircleTextRb.isCircle = false;
            }

            bindingRb.smallSquareContainerRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isExpanded) {
                        isExpanded = false;
                        bindingRb.bigContainerRb.setVisibility(View.GONE);
                        bindingRb.arrowSquareRb.setText(getString(R.string.notification_right_arrow));
                    } else {
                        isExpanded = true;
                        bindingRb.bigContainerRb.setVisibility(View.VISIBLE);
                        bindingRb.arrowSquareRb.setText(getString(R.string.notification_left_arrow));
                    }
                }
            });
        }

        bindingRb.bigContainerRb.setBackgroundColor(getResources().getColor(R.color.blue));
        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingRb.bigImageRb);

        bindingRb.bigContainerRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Check buttonInterface first
                // TODO : send report here
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
                getActivity().startActivity(viewIntent);

                endFragment();

            }
        });
    }

    private void adjustLt() {
        //TODO : real data here
    }

    private void adjustLm() {
        //TODO : real data here
    }

    private void adjustLb() {
        //TODO : real data here
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO : save mModel here
    }

    private void endFragment() {
        if (getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(InAppNotificationFragment.this).commit();
        }
    }
}
