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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
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
    private boolean isArrow = false;
    private boolean isBackgroundImage = false;

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

        //TODO : get from the real data here
        isRight = true;
        isTopToBottom = true;
        positionOnScreen = PositionOnScreen.BOTTOM;
        isSmallImage = true;
        shape = Shape.SOFT_EDGE;
        isArrow = true;
        isBackgroundImage = true;

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
        //TODO : from real data here
        bindingRt.smallSquareContainerRt.setVisibility(View.VISIBLE);
        bindingRt.smallCircleContainerRt.setVisibility(View.VISIBLE);
        bindingRt.arrowSquareRt.setVisibility(View.VISIBLE);
        bindingRt.arrowCircleRt.setVisibility(View.VISIBLE);
        bindingRt.smallSquareTextRt.setVisibility(View.VISIBLE);
        bindingRt.smallCircleTextRt.setVisibility(View.VISIBLE);
        bindingRt.smallSquareImageRt.setVisibility(View.VISIBLE);
        bindingRt.smallCircleImageRt.setVisibility(View.VISIBLE);
        bindingRt.smallCircleBackgroundImageRt.setVisibility(View.VISIBLE);
        bindingRt.smallSquareBackgroundImageRt.setVisibility(View.VISIBLE);
        bindingRt.bigContainerRt.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                if(isBackgroundImage) {
                    Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRt.smallSquareBackgroundImageRt);
                } else {
                    bindingRt.smallSquareContainerRt.setBackgroundColor(getResources().getColor(R.color.blue));
                    bindingRt.smallSquareBackgroundImageRt.setVisibility(View.GONE);
                }
                bindingRt.smallCircleContainerRt.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRt.smallSquareBackgroundImageRt);
                } else {
                    bindingRt.smallSquareContainerRt.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRt.smallSquareTextRt.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRt.smallSquareImageRt.setBackgroundResource(R.drawable.rounded_corners_left);
                    GradientDrawable gd = (GradientDrawable) bindingRt.smallSquareContainerRt.getBackground();
                    gd.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdText = (GradientDrawable) bindingRt.smallSquareTextRt.getBackground();
                    gdText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdImage = (GradientDrawable) bindingRt.smallSquareImageRt.getBackground();
                    gdImage.setColor(getResources().getColor(R.color.blue));
                    bindingRt.smallSquareBackgroundImageRt.setVisibility(View.GONE);
                }
                bindingRt.smallCircleContainerRt.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRt.smallCircleBackgroundImageRt);
                } else {
                    bindingRt.smallCircleContainerRt.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRt.smallCircleTextRt.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRt.smallCircleImageRt.setBackgroundResource(R.drawable.left_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingRt.smallCircleContainerRt.getBackground();
                    gdCircle.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleText = (GradientDrawable) bindingRt.smallCircleTextRt.getBackground();
                    gdCircleText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingRt.smallCircleImageRt.getBackground();
                    gdCircleImage.setColor(getResources().getColor(R.color.blue));
                    bindingRt.smallCircleBackgroundImageRt.setVisibility(View.GONE);
                }
                bindingRt.smallSquareContainerRt.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(!isArrow) {
                bindingRt.arrowCircleRt.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingRt.arrowCircleRt.setText(getString(R.string.notification_left_arrow));
            } else {
                bindingRt.arrowCircleRt.setText(getString(R.string.notification_right_arrow));
            }
            bindingRt.arrowCircleRt.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingRt.smallCircleImageRt);
                bindingRt.smallCircleTextRt.setVisibility(View.GONE);
            } else {
                bindingRt.smallCircleTextRt.setText("Discount");
                bindingRt.smallCircleTextRt.setTextColor(getResources().getColor(R.color.white));
                bindingRt.smallCircleTextRt.setTypeface(Typeface.MONOSPACE);
                bindingRt.smallCircleImageRt.setVisibility(View.GONE);
                bindingRt.smallCircleTextRt.topDown = isTopToBottom;
                bindingRt.smallCircleTextRt.isCircle = true;
                bindingRt.smallCircleTextRt.isRight = isRight;
            }

            bindingRt.smallCircleContainerRt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRt.bigContainerRt.setVisibility(View.GONE);
                    bindingRt.arrowCircleRt.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRt.bigContainerRt.setVisibility(View.VISIBLE);
                    bindingRt.arrowCircleRt.setText(getString(R.string.notification_left_arrow));
                }
            });
        } else {
            if(!isArrow) {
                bindingRt.arrowSquareRt.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingRt.arrowSquareRt.setText(getString(R.string.notification_left_arrow));
            } else {
                bindingRt.arrowSquareRt.setText(getString(R.string.notification_right_arrow));
            }
            bindingRt.arrowSquareRt.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingRt.smallSquareImageRt);
                bindingRt.smallSquareTextRt.setVisibility(View.GONE);
            } else {
                bindingRt.smallSquareTextRt.setText("Discount");
                bindingRt.smallSquareTextRt.setTextColor(getResources().getColor(R.color.white));
                bindingRt.smallSquareTextRt.setTypeface(Typeface.MONOSPACE);
                bindingRt.smallSquareImageRt.setVisibility(View.GONE);
                bindingRt.smallSquareTextRt.topDown = isTopToBottom;
                bindingRt.smallCircleTextRt.isCircle = false;
                bindingRt.smallCircleTextRt.isRight = isRight;
            }

            bindingRt.smallSquareContainerRt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRt.bigContainerRt.setVisibility(View.GONE);
                    bindingRt.arrowSquareRt.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRt.bigContainerRt.setVisibility(View.VISIBLE);
                    bindingRt.arrowSquareRt.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isBackgroundImage) {
            Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                    .into(bindingRt.bigBackgroundImageRt);
        } else {
            bindingRt.bigContainerRt.setBackgroundColor(getResources().getColor(R.color.blue));
            bindingRt.bigBackgroundImageRt.setVisibility(View.GONE);
        }

        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingRt.bigImageRt);

        bindingRt.bigContainerRt.setOnClickListener(v -> {
            // TODO : Check buttonInterface first
            // TODO : send report here
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
            getActivity().startActivity(viewIntent);

            endFragment();

        });
    }

    private void adjustRm() {
        //TODO : from real data here
        bindingRm.smallSquareContainerRm.setVisibility(View.VISIBLE);
        bindingRm.smallCircleContainerRm.setVisibility(View.VISIBLE);
        bindingRm.arrowSquareRm.setVisibility(View.VISIBLE);
        bindingRm.arrowCircleRm.setVisibility(View.VISIBLE);
        bindingRm.smallSquareTextRm.setVisibility(View.VISIBLE);
        bindingRm.smallCircleTextRm.setVisibility(View.VISIBLE);
        bindingRm.smallSquareImageRm.setVisibility(View.VISIBLE);
        bindingRm.smallCircleImageRm.setVisibility(View.VISIBLE);
        bindingRm.smallCircleBackgroundImageRm.setVisibility(View.VISIBLE);
        bindingRm.smallSquareBackgroundImageRm.setVisibility(View.VISIBLE);
        bindingRm.bigContainerRm.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                if(isBackgroundImage) {
                    Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRm.smallSquareBackgroundImageRm);
                } else {
                    bindingRm.smallSquareContainerRm.setBackgroundColor(getResources().getColor(R.color.blue));
                    bindingRm.smallSquareBackgroundImageRm.setVisibility(View.GONE);
                }
                bindingRm.smallCircleContainerRm.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRm.smallSquareBackgroundImageRm);
                } else {
                    bindingRm.smallSquareContainerRm.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRm.smallSquareTextRm.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRm.smallSquareImageRm.setBackgroundResource(R.drawable.rounded_corners_left);
                    GradientDrawable gd = (GradientDrawable) bindingRm.smallSquareContainerRm.getBackground();
                    gd.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdText = (GradientDrawable) bindingRm.smallSquareTextRm.getBackground();
                    gdText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdImage = (GradientDrawable) bindingRm.smallSquareImageRm.getBackground();
                    gdImage.setColor(getResources().getColor(R.color.blue));
                    bindingRm.smallSquareBackgroundImageRm.setVisibility(View.GONE);
                }
                bindingRm.smallCircleContainerRm.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRm.smallCircleBackgroundImageRm);
                } else {
                    bindingRm.smallCircleContainerRm.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRm.smallCircleTextRm.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRm.smallCircleImageRm.setBackgroundResource(R.drawable.left_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingRm.smallCircleContainerRm.getBackground();
                    gdCircle.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleText = (GradientDrawable) bindingRm.smallCircleTextRm.getBackground();
                    gdCircleText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingRm.smallCircleImageRm.getBackground();
                    gdCircleImage.setColor(getResources().getColor(R.color.blue));
                    bindingRm.smallCircleBackgroundImageRm.setVisibility(View.GONE);
                }
                bindingRm.smallSquareContainerRm.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(!isArrow) {
                bindingRm.arrowCircleRm.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingRm.arrowCircleRm.setText(getString(R.string.notification_left_arrow));
            } else {
                bindingRm.arrowCircleRm.setText(getString(R.string.notification_right_arrow));
            }
            bindingRm.arrowCircleRm.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingRm.smallCircleImageRm);
                bindingRm.smallCircleTextRm.setVisibility(View.GONE);
            } else {
                bindingRm.smallCircleTextRm.setText("Discount");
                bindingRm.smallCircleTextRm.setTextColor(getResources().getColor(R.color.white));
                bindingRm.smallCircleTextRm.setTypeface(Typeface.MONOSPACE);
                bindingRm.smallCircleImageRm.setVisibility(View.GONE);
                bindingRm.smallCircleTextRm.topDown = isTopToBottom;
                bindingRm.smallCircleTextRm.isCircle = true;
                bindingRm.smallCircleTextRm.isRight = isRight;
            }

            bindingRm.smallCircleContainerRm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRm.bigContainerRm.setVisibility(View.GONE);
                    bindingRm.arrowCircleRm.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRm.bigContainerRm.setVisibility(View.VISIBLE);
                    bindingRm.arrowCircleRm.setText(getString(R.string.notification_left_arrow));
                }
            });
        } else {
            if(!isArrow) {
                bindingRm.arrowSquareRm.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingRm.arrowSquareRm.setText(getString(R.string.notification_left_arrow));
            } else {
                bindingRm.arrowSquareRm.setText(getString(R.string.notification_right_arrow));
            }
            bindingRm.arrowSquareRm.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingRm.smallSquareImageRm);
                bindingRm.smallSquareTextRm.setVisibility(View.GONE);
            } else {
                bindingRm.smallSquareTextRm.setText("Discount");
                bindingRm.smallSquareTextRm.setTextColor(getResources().getColor(R.color.white));
                bindingRm.smallSquareTextRm.setTypeface(Typeface.MONOSPACE);
                bindingRm.smallSquareImageRm.setVisibility(View.GONE);
                bindingRm.smallSquareTextRm.topDown = isTopToBottom;
                bindingRm.smallCircleTextRm.isCircle = false;
                bindingRm.smallCircleTextRm.isRight = isRight;
            }

            bindingRm.smallSquareContainerRm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRm.bigContainerRm.setVisibility(View.GONE);
                    bindingRm.arrowSquareRm.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRm.bigContainerRm.setVisibility(View.VISIBLE);
                    bindingRm.arrowSquareRm.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isBackgroundImage) {
            Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                    .into(bindingRm.bigBackgroundImageRm);
        } else {
            bindingRm.bigContainerRm.setBackgroundColor(getResources().getColor(R.color.blue));
            bindingRm.bigBackgroundImageRm.setVisibility(View.GONE);
        }

        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingRm.bigImageRm);

        bindingRm.bigContainerRm.setOnClickListener(v -> {
            // TODO : Check buttonInterface first
            // TODO : send report here
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
            getActivity().startActivity(viewIntent);

            endFragment();

        });
    }

    private void adjustRb() {
        //TODO : from real data here
        bindingRb.smallSquareContainerRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleContainerRb.setVisibility(View.VISIBLE);
        bindingRb.arrowSquareRb.setVisibility(View.VISIBLE);
        bindingRb.arrowCircleRb.setVisibility(View.VISIBLE);
        bindingRb.smallSquareTextRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleTextRb.setVisibility(View.VISIBLE);
        bindingRb.smallSquareImageRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleImageRb.setVisibility(View.VISIBLE);
        bindingRb.smallCircleBackgroundImageRb.setVisibility(View.VISIBLE);
        bindingRb.smallSquareBackgroundImageRb.setVisibility(View.VISIBLE);
        bindingRb.bigContainerRb.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                if(isBackgroundImage) {
                    Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRb.smallSquareBackgroundImageRb);
                } else {
                    bindingRb.smallSquareContainerRb.setBackgroundColor(getResources().getColor(R.color.blue));
                    bindingRb.smallSquareBackgroundImageRb.setVisibility(View.GONE);
                }
                bindingRb.smallCircleContainerRb.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRb.smallSquareBackgroundImageRb);
                } else {
                    bindingRb.smallSquareContainerRb.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRb.smallSquareTextRb.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRb.smallSquareImageRb.setBackgroundResource(R.drawable.rounded_corners_left);
                    GradientDrawable gd = (GradientDrawable) bindingRb.smallSquareContainerRb.getBackground();
                    gd.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdText = (GradientDrawable) bindingRb.smallSquareTextRb.getBackground();
                    gdText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdImage = (GradientDrawable) bindingRb.smallSquareImageRb.getBackground();
                    gdImage.setColor(getResources().getColor(R.color.blue));
                    bindingRb.smallSquareBackgroundImageRb.setVisibility(View.GONE);
                }
                bindingRb.smallCircleContainerRb.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingRb.smallCircleBackgroundImageRb);
                } else {
                    bindingRb.smallCircleContainerRb.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRb.smallCircleTextRb.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRb.smallCircleImageRb.setBackgroundResource(R.drawable.left_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingRb.smallCircleContainerRb.getBackground();
                    gdCircle.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleText = (GradientDrawable) bindingRb.smallCircleTextRb.getBackground();
                    gdCircleText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingRb.smallCircleImageRb.getBackground();
                    gdCircleImage.setColor(getResources().getColor(R.color.blue));
                    bindingRb.smallCircleBackgroundImageRb.setVisibility(View.GONE);
                }
                bindingRb.smallSquareContainerRb.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(!isArrow) {
                bindingRb.arrowCircleRb.setVisibility(View.GONE);
            }
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
                bindingRb.smallCircleTextRb.isRight = isRight;
            }

            bindingRb.smallCircleContainerRb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRb.bigContainerRb.setVisibility(View.GONE);
                    bindingRb.arrowCircleRb.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRb.bigContainerRb.setVisibility(View.VISIBLE);
                    bindingRb.arrowCircleRb.setText(getString(R.string.notification_left_arrow));
                }
            });
        } else {
            if(!isArrow) {
                bindingRb.arrowSquareRb.setVisibility(View.GONE);
            }
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
                bindingRb.smallCircleTextRb.isRight = isRight;
            }

            bindingRb.smallSquareContainerRb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRb.bigContainerRb.setVisibility(View.GONE);
                    bindingRb.arrowSquareRb.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRb.bigContainerRb.setVisibility(View.VISIBLE);
                    bindingRb.arrowSquareRb.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isBackgroundImage) {
            Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                    .into(bindingRb.bigBackgroundImageRb);
        } else {
            bindingRb.bigContainerRb.setBackgroundColor(getResources().getColor(R.color.blue));
            bindingRb.bigBackgroundImageRb.setVisibility(View.GONE);
        }

        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingRb.bigImageRb);

        bindingRb.bigContainerRb.setOnClickListener(v -> {
            // TODO : Check buttonInterface first
            // TODO : send report here
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
            getActivity().startActivity(viewIntent);

            endFragment();

        });
    }

    private void adjustLt() {
        //TODO : from real data here
        bindingLt.smallSquareContainerLt.setVisibility(View.VISIBLE);
        bindingLt.smallCircleContainerLt.setVisibility(View.VISIBLE);
        bindingLt.arrowSquareLt.setVisibility(View.VISIBLE);
        bindingLt.arrowCircleLt.setVisibility(View.VISIBLE);
        bindingLt.smallSquareTextLt.setVisibility(View.VISIBLE);
        bindingLt.smallCircleTextLt.setVisibility(View.VISIBLE);
        bindingLt.smallSquareImageLt.setVisibility(View.VISIBLE);
        bindingLt.smallCircleImageLt.setVisibility(View.VISIBLE);
        bindingLt.smallCircleBackgroundImageLt.setVisibility(View.VISIBLE);
        bindingLt.smallSquareBackgroundImageLt.setVisibility(View.VISIBLE);
        bindingLt.bigContainerLt.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                if(isBackgroundImage) {
                    Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLt.smallSquareBackgroundImageLt);
                } else {
                    bindingLt.smallSquareContainerLt.setBackgroundColor(getResources().getColor(R.color.blue));
                    bindingLt.smallSquareBackgroundImageLt.setVisibility(View.GONE);
                }
                bindingLt.smallCircleContainerLt.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLt.smallSquareBackgroundImageLt);
                } else {
                    bindingLt.smallSquareContainerLt.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLt.smallSquareTextLt.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLt.smallSquareImageLt.setBackgroundResource(R.drawable.rounded_corners_right);
                    GradientDrawable gd = (GradientDrawable) bindingLt.smallSquareContainerLt.getBackground();
                    gd.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdText = (GradientDrawable) bindingLt.smallSquareTextLt.getBackground();
                    gdText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdImage = (GradientDrawable) bindingLt.smallSquareImageLt.getBackground();
                    gdImage.setColor(getResources().getColor(R.color.blue));
                    bindingLt.smallSquareBackgroundImageLt.setVisibility(View.GONE);
                }
                bindingLt.smallCircleContainerLt.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLt.smallCircleBackgroundImageLt);
                } else {
                    bindingLt.smallCircleContainerLt.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLt.smallCircleTextLt.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLt.smallCircleImageLt.setBackgroundResource(R.drawable.right_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingLt.smallCircleContainerLt.getBackground();
                    gdCircle.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleText = (GradientDrawable) bindingLt.smallCircleTextLt.getBackground();
                    gdCircleText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingLt.smallCircleImageLt.getBackground();
                    gdCircleImage.setColor(getResources().getColor(R.color.blue));
                    bindingLt.smallCircleBackgroundImageLt.setVisibility(View.GONE);
                }
                bindingLt.smallSquareContainerLt.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(!isArrow) {
                bindingLt.arrowCircleLt.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingLt.arrowCircleLt.setText(getString(R.string.notification_right_arrow));
            } else {
                bindingLt.arrowCircleLt.setText(getString(R.string.notification_left_arrow));
            }
            bindingLt.arrowCircleLt.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingLt.smallCircleImageLt);
                bindingLt.smallCircleTextLt.setVisibility(View.GONE);
            } else {
                bindingLt.smallCircleTextLt.setText("Discount");
                bindingLt.smallCircleTextLt.setTextColor(getResources().getColor(R.color.white));
                bindingLt.smallCircleTextLt.setTypeface(Typeface.MONOSPACE);
                bindingLt.smallCircleImageLt.setVisibility(View.GONE);
                bindingLt.smallCircleTextLt.topDown = isTopToBottom;
                bindingLt.smallCircleTextLt.isCircle = true;
                bindingLt.smallCircleTextLt.isRight = isRight;
            }

            bindingLt.smallCircleContainerLt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLt.bigContainerLt.setVisibility(View.GONE);
                    bindingLt.arrowCircleLt.setText(getString(R.string.notification_left_arrow));
                } else {
                    isExpanded = true;
                    bindingLt.bigContainerLt.setVisibility(View.VISIBLE);
                    bindingLt.arrowCircleLt.setText(getString(R.string.notification_right_arrow));
                }
            });
        } else {
            if(!isArrow) {
                bindingLt.arrowSquareLt.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingLt.arrowSquareLt.setText(getString(R.string.notification_right_arrow));
            } else {
                bindingLt.arrowSquareLt.setText(getString(R.string.notification_left_arrow));
            }
            bindingLt.arrowSquareLt.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingLt.smallSquareImageLt);
                bindingLt.smallSquareTextLt.setVisibility(View.GONE);
            } else {
                bindingLt.smallSquareTextLt.setText("Discount");
                bindingLt.smallSquareTextLt.setTextColor(getResources().getColor(R.color.white));
                bindingLt.smallSquareTextLt.setTypeface(Typeface.MONOSPACE);
                bindingLt.smallSquareImageLt.setVisibility(View.GONE);
                bindingLt.smallSquareTextLt.topDown = isTopToBottom;
                bindingLt.smallCircleTextLt.isCircle = false;
                bindingLt.smallCircleTextLt.isRight = isRight;
            }

            bindingLt.smallSquareContainerLt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLt.bigContainerLt.setVisibility(View.GONE);
                    bindingLt.arrowSquareLt.setText(getString(R.string.notification_left_arrow));
                } else {
                    isExpanded = true;
                    bindingLt.bigContainerLt.setVisibility(View.VISIBLE);
                    bindingLt.arrowSquareLt.setText(getString(R.string.notification_right_arrow));
                }
            });
        }

        if(isBackgroundImage) {
            Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                    .into(bindingLt.bigBackgroundImageLt);
        } else {
            bindingLt.bigContainerLt.setBackgroundColor(getResources().getColor(R.color.blue));
            bindingLt.bigBackgroundImageLt.setVisibility(View.GONE);
        }

        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingLt.bigImageLt);

        bindingLt.bigContainerLt.setOnClickListener(v -> {
            // TODO : Check buttonInterface first
            // TODO : send report here
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
            getActivity().startActivity(viewIntent);

            endFragment();

        });
    }

    private void adjustLm() {
        //TODO : from real data here
        bindingLm.smallSquareContainerLm.setVisibility(View.VISIBLE);
        bindingLm.smallCircleContainerLm.setVisibility(View.VISIBLE);
        bindingLm.arrowSquareLm.setVisibility(View.VISIBLE);
        bindingLm.arrowCircleLm.setVisibility(View.VISIBLE);
        bindingLm.smallSquareTextLm.setVisibility(View.VISIBLE);
        bindingLm.smallCircleTextLm.setVisibility(View.VISIBLE);
        bindingLm.smallSquareImageLm.setVisibility(View.VISIBLE);
        bindingLm.smallCircleImageLm.setVisibility(View.VISIBLE);
        bindingLm.smallCircleBackgroundImageLm.setVisibility(View.VISIBLE);
        bindingLm.smallSquareBackgroundImageLm.setVisibility(View.VISIBLE);
        bindingLm.bigContainerLm.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                if(isBackgroundImage) {
                    Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLm.smallSquareBackgroundImageLm);
                } else {
                    bindingLm.smallSquareContainerLm.setBackgroundColor(getResources().getColor(R.color.blue));
                    bindingLm.smallSquareBackgroundImageLm.setVisibility(View.GONE);
                }
                bindingLm.smallCircleContainerLm.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLm.smallSquareBackgroundImageLm);
                } else {
                    bindingLm.smallSquareContainerLm.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLm.smallSquareTextLm.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLm.smallSquareImageLm.setBackgroundResource(R.drawable.rounded_corners_right);
                    GradientDrawable gd = (GradientDrawable) bindingLm.smallSquareContainerLm.getBackground();
                    gd.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdText = (GradientDrawable) bindingLm.smallSquareTextLm.getBackground();
                    gdText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdImage = (GradientDrawable) bindingLm.smallSquareImageLm.getBackground();
                    gdImage.setColor(getResources().getColor(R.color.blue));
                    bindingLm.smallSquareBackgroundImageLm.setVisibility(View.GONE);
                }
                bindingLm.smallCircleContainerLm.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLm.smallCircleBackgroundImageLm);
                } else {
                    bindingLm.smallCircleContainerLm.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLm.smallCircleTextLm.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLm.smallCircleImageLm.setBackgroundResource(R.drawable.right_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingLm.smallCircleContainerLm.getBackground();
                    gdCircle.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleText = (GradientDrawable) bindingLm.smallCircleTextLm.getBackground();
                    gdCircleText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingLm.smallCircleImageLm.getBackground();
                    gdCircleImage.setColor(getResources().getColor(R.color.blue));
                    bindingLm.smallCircleBackgroundImageLm.setVisibility(View.GONE);
                }
                bindingLm.smallSquareContainerLm.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(!isArrow) {
                bindingLm.arrowCircleLm.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingLm.arrowCircleLm.setText(getString(R.string.notification_right_arrow));
            } else {
                bindingLm.arrowCircleLm.setText(getString(R.string.notification_left_arrow));
            }
            bindingLm.arrowCircleLm.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingLm.smallCircleImageLm);
                bindingLm.smallCircleTextLm.setVisibility(View.GONE);
            } else {
                bindingLm.smallCircleTextLm.setText("Discount");
                bindingLm.smallCircleTextLm.setTextColor(getResources().getColor(R.color.white));
                bindingLm.smallCircleTextLm.setTypeface(Typeface.MONOSPACE);
                bindingLm.smallCircleImageLm.setVisibility(View.GONE);
                bindingLm.smallCircleTextLm.topDown = isTopToBottom;
                bindingLm.smallCircleTextLm.isCircle = true;
                bindingLm.smallCircleTextLm.isRight = isRight;
            }

            bindingLm.smallCircleContainerLm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLm.bigContainerLm.setVisibility(View.GONE);
                    bindingLm.arrowCircleLm.setText(getString(R.string.notification_left_arrow));
                } else {
                    isExpanded = true;
                    bindingLm.bigContainerLm.setVisibility(View.VISIBLE);
                    bindingLm.arrowCircleLm.setText(getString(R.string.notification_right_arrow));
                }
            });
        } else {
            if(!isArrow) {
                bindingLm.arrowSquareLm.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingLm.arrowSquareLm.setText(getString(R.string.notification_right_arrow));
            } else {
                bindingLm.arrowSquareLm.setText(getString(R.string.notification_left_arrow));
            }
            bindingLm.arrowSquareLm.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingLm.smallSquareImageLm);
                bindingLm.smallSquareTextLm.setVisibility(View.GONE);
            } else {
                bindingLm.smallSquareTextLm.setText("Discount");
                bindingLm.smallSquareTextLm.setTextColor(getResources().getColor(R.color.white));
                bindingLm.smallSquareTextLm.setTypeface(Typeface.MONOSPACE);
                bindingLm.smallSquareImageLm.setVisibility(View.GONE);
                bindingLm.smallSquareTextLm.topDown = isTopToBottom;
                bindingLm.smallCircleTextLm.isCircle = false;
                bindingLm.smallCircleTextLm.isRight = isRight;
            }

            bindingLm.smallSquareContainerLm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLm.bigContainerLm.setVisibility(View.GONE);
                    bindingLm.arrowSquareLm.setText(getString(R.string.notification_left_arrow));
                } else {
                    isExpanded = true;
                    bindingLm.bigContainerLm.setVisibility(View.VISIBLE);
                    bindingLm.arrowSquareLm.setText(getString(R.string.notification_right_arrow));
                }
            });
        }

        if(isBackgroundImage) {
            Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                    .into(bindingLm.bigBackgroundImageLm);
        } else {
            bindingLm.bigContainerLm.setBackgroundColor(getResources().getColor(R.color.blue));
            bindingLm.bigBackgroundImageLm.setVisibility(View.GONE);
        }

        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingLm.bigImageLm);

        bindingLm.bigContainerLm.setOnClickListener(v -> {
            // TODO : Check buttonInterface first
            // TODO : send report here
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
            getActivity().startActivity(viewIntent);

            endFragment();

        });
    }

    private void adjustLb() {
        //TODO : from real data here
        bindingLb.smallSquareContainerLb.setVisibility(View.VISIBLE);
        bindingLb.smallCircleContainerLb.setVisibility(View.VISIBLE);
        bindingLb.arrowSquareLb.setVisibility(View.VISIBLE);
        bindingLb.arrowCircleLb.setVisibility(View.VISIBLE);
        bindingLb.smallSquareTextLb.setVisibility(View.VISIBLE);
        bindingLb.smallCircleTextLb.setVisibility(View.VISIBLE);
        bindingLb.smallSquareImageLb.setVisibility(View.VISIBLE);
        bindingLb.smallCircleImageLb.setVisibility(View.VISIBLE);
        bindingLb.smallCircleBackgroundImageLb.setVisibility(View.VISIBLE);
        bindingLb.smallSquareBackgroundImageLb.setVisibility(View.VISIBLE);
        bindingLb.bigContainerLb.setVisibility(View.GONE);

        switch (shape) {
            case SHARP_EDGE:
                if(isBackgroundImage) {
                    Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLb.smallSquareBackgroundImageLb);
                } else {
                    bindingLb.smallSquareContainerLb.setBackgroundColor(getResources().getColor(R.color.blue));
                    bindingLb.smallSquareBackgroundImageLb.setVisibility(View.GONE);
                }
                bindingLb.smallCircleContainerLb.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLb.smallSquareBackgroundImageLb);
                } else {
                    bindingLb.smallSquareContainerLb.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLb.smallSquareTextLb.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLb.smallSquareImageLb.setBackgroundResource(R.drawable.rounded_corners_right);
                    GradientDrawable gd = (GradientDrawable) bindingLb.smallSquareContainerLb.getBackground();
                    gd.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdText = (GradientDrawable) bindingLb.smallSquareTextLb.getBackground();
                    gdText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdImage = (GradientDrawable) bindingLb.smallSquareImageLb.getBackground();
                    gdImage.setColor(getResources().getColor(R.color.blue));
                    bindingLb.smallSquareBackgroundImageLb.setVisibility(View.GONE);
                }
                bindingLb.smallCircleContainerLb.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isBackgroundImage) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                            .load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                            .into(bindingLb.smallCircleBackgroundImageLb);
                } else {
                    bindingLb.smallCircleContainerLb.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLb.smallCircleTextLb.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLb.smallCircleImageLb.setBackgroundResource(R.drawable.right_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingLb.smallCircleContainerLb.getBackground();
                    gdCircle.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleText = (GradientDrawable) bindingLb.smallCircleTextLb.getBackground();
                    gdCircleText.setColor(getResources().getColor(R.color.blue));
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingLb.smallCircleImageLb.getBackground();
                    gdCircleImage.setColor(getResources().getColor(R.color.blue));
                    bindingLb.smallCircleBackgroundImageLb.setVisibility(View.GONE);
                }
                bindingLb.smallSquareContainerLb.setVisibility(View.GONE);
                break;
        }

        if(shape == Shape.CIRCLE) {
            if(!isArrow) {
                bindingLb.arrowCircleLb.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingLb.arrowCircleLb.setText(getString(R.string.notification_right_arrow));
            } else {
                bindingLb.arrowCircleLb.setText(getString(R.string.notification_left_arrow));
            }
            bindingLb.arrowCircleLb.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingLb.smallCircleImageLb);
                bindingLb.smallCircleTextLb.setVisibility(View.GONE);
            } else {
                bindingLb.smallCircleTextLb.setText("Discount");
                bindingLb.smallCircleTextLb.setTextColor(getResources().getColor(R.color.white));
                bindingLb.smallCircleTextLb.setTypeface(Typeface.MONOSPACE);
                bindingLb.smallCircleImageLb.setVisibility(View.GONE);
                bindingLb.smallCircleTextLb.topDown = isTopToBottom;
                bindingLb.smallCircleTextLb.isCircle = true;
                bindingLb.smallCircleTextLb.isRight = isRight;
            }

            bindingLb.smallCircleContainerLb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLb.bigContainerLb.setVisibility(View.GONE);
                    bindingLb.arrowCircleLb.setText(getString(R.string.notification_left_arrow));
                } else {
                    isExpanded = true;
                    bindingLb.bigContainerLb.setVisibility(View.VISIBLE);
                    bindingLb.arrowCircleLb.setText(getString(R.string.notification_right_arrow));
                }
            });
        } else {
            if(!isArrow) {
                bindingLb.arrowSquareLb.setVisibility(View.GONE);
            }
            if(isExpanded) {
                bindingLb.arrowSquareLb.setText(getString(R.string.notification_right_arrow));
            } else {
                bindingLb.arrowSquareLb.setText(getString(R.string.notification_left_arrow));
            }
            bindingLb.arrowSquareLb.setTextColor(getResources().getColor(R.color.white));

            if(isSmallImage) {
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Circle-icons-mail.svg/2048px-Circle-icons-mail.svg.png")
                        .into(bindingLb.smallSquareImageLb);
                bindingLb.smallSquareTextLb.setVisibility(View.GONE);
            } else {
                bindingLb.smallSquareTextLb.setText("Discount");
                bindingLb.smallSquareTextLb.setTextColor(getResources().getColor(R.color.white));
                bindingLb.smallSquareTextLb.setTypeface(Typeface.MONOSPACE);
                bindingLb.smallSquareImageLb.setVisibility(View.GONE);
                bindingLb.smallSquareTextLb.topDown = isTopToBottom;
                bindingLb.smallCircleTextLb.isCircle = false;
                bindingLb.smallCircleTextLb.isRight = isRight;
            }

            bindingLb.smallSquareContainerLb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLb.bigContainerLb.setVisibility(View.GONE);
                    bindingLb.arrowSquareLb.setText(getString(R.string.notification_left_arrow));
                } else {
                    isExpanded = true;
                    bindingLb.bigContainerLb.setVisibility(View.VISIBLE);
                    bindingLb.arrowSquareLb.setText(getString(R.string.notification_right_arrow));
                }
            });
        }

        if(isBackgroundImage) {
            Picasso.get().load("https://digitalsynopsis.com/wp-content/uploads/2019/11/color-schemes-palettes-feature-image.jpg")
                    .into(bindingLb.bigBackgroundImageLb);
        } else {
            bindingLb.bigContainerLb.setBackgroundColor(getResources().getColor(R.color.blue));
            bindingLb.bigBackgroundImageLb.setVisibility(View.GONE);
        }

        Picasso.get().load("https://upload.wikimedia.org//wikipedia/en/a/a9/MarioNSMBUDeluxe.png")
                .into(bindingLb.bigImageLb);

        bindingLb.bigContainerLb.setOnClickListener(v -> {
            // TODO : Check buttonInterface first
            // TODO : send report here
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.relateddigital.com/"));
            getActivity().startActivity(viewIntent);

            endFragment();

        });
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
