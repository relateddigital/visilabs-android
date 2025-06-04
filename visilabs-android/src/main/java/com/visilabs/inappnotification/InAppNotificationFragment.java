package com.visilabs.inappnotification;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.FragmentInAppNotificationLbBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationLmBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationLtBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationRbBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationRmBinding;
import com.visilabs.android.databinding.FragmentInAppNotificationRtBinding;
import com.visilabs.inApp.InAppButtonInterface;
import com.visilabs.mailSub.Report;
import java.net.URISyntaxException;

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

    private DrawerModel response = null;
    private ExtendedProps mExtendedProps = null;
    private boolean isRight = true;
    private PositionOnScreen positionOnScreen;
    private boolean isTopToBottom = true;
    private boolean isExpanded = false;
    private boolean isSmallImage = false;
    private Shape shape = Shape.SOFT_EDGE;
    private boolean isArrow = false;
    private boolean isMiniBackgroundImage = false;
    private boolean isMaxiBackgroundImage = false;


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
    public static InAppNotificationFragment newInstance(DrawerModel model) {
        InAppNotificationFragment fragment = new InAppNotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(DrawerActive.isDrawerActive() == false) {
        if(savedInstanceState != null) {
            response = (DrawerModel) savedInstanceState.getSerializable("drawer");
        } else {
            response = (DrawerModel) getArguments().getSerializable(ARG_PARAM1);
        }

        if(response == null) {
            Log.e(LOG_TAG, "The data could not get properly!");
            endFragment();
        } else {
            try {
                mExtendedProps = new Gson().fromJson(new java.net.URI(response.
                        getActionData().getExtendedProps()).getPath(), ExtendedProps.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                endFragment();
            } catch (Exception e) {
                e.printStackTrace();
                endFragment();
            }
        }

        DrawerActive.setDrawerActive(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(DrawerViewActive.isDrawerViewActive() == false) {
        View view;

        isRight = !response.getActionData().getPos().equals("topLeft") &&
                !response.getActionData().getPos().equals("left") &&
                !response.getActionData().getPos().equals("bottomLeft");

        if(response.getActionData().getPos().equals("topRight") ||
                response.getActionData().getPos().equals("topLeft")) {
            positionOnScreen = PositionOnScreen.TOP;
        } else if(response.getActionData().getPos().equals("right") ||
                response.getActionData().getPos().equals("left")){
            positionOnScreen = PositionOnScreen.MIDDLE;
        } else {
            positionOnScreen = PositionOnScreen.BOTTOM;
        }

        isTopToBottom = mExtendedProps.getMiniTextOrientation().equals("topToBottom");

        isSmallImage = response.getActionData().getContentMinimizedImage() != null &&
                !response.getActionData().getContentMinimizedImage().equals("");

        if(response.getActionData().getShape().equals("circle")) {
            shape = Shape.CIRCLE;
        } else if (response.getActionData().getShape().equals("roundedCorners")) {
            shape = Shape.SOFT_EDGE;
        } else {
            shape = Shape.SHARP_EDGE;
        }

        isArrow = mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("");

        isMiniBackgroundImage = mExtendedProps.getMiniBackgroundImage() != null && !mExtendedProps.getMiniBackgroundImage().equals("");

        isMaxiBackgroundImage = mExtendedProps.getMaxiBackgroundImage() != null && !mExtendedProps.getMaxiBackgroundImage().equals("");

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
            DrawerViewActive.setDrawerViewActive(true);
            return view;
    }
        return null;

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
        bindingRt.closeFrameLayoutRt.setVisibility(View.GONE);
        bindingRt.closeButtonRt.setOnClickListener(v -> endFragment());

        switch (shape) {
            case SHARP_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Picasso.get().load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRt.smallSquareBackgroundImageRt);
                    }
                } else {
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        bindingRt.smallSquareContainerRt.setBackgroundColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        bindingRt.smallSquareContainerRt.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    bindingRt.smallSquareBackgroundImageRt.setVisibility(View.GONE);
                }
                bindingRt.smallCircleContainerRt.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRt.smallSquareBackgroundImageRt);
                    }
                } else {
                    bindingRt.smallSquareContainerRt.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRt.smallSquareTextRt.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRt.smallSquareImageRt.setBackgroundResource(R.drawable.rounded_corners_left);
                    GradientDrawable gd = (GradientDrawable) bindingRt.smallSquareContainerRt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gd.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gd.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdText = (GradientDrawable) bindingRt.smallSquareTextRt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdImage = (GradientDrawable) bindingRt.smallSquareImageRt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdImage.setColor(getResources().getColor(R.color.white));
                    }
                    bindingRt.smallSquareBackgroundImageRt.setVisibility(View.GONE);
                }
                bindingRt.smallCircleContainerRt.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRt.smallCircleBackgroundImageRt);
                    }
                } else {
                    bindingRt.smallCircleContainerRt.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRt.smallCircleTextRt.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRt.smallCircleImageRt.setBackgroundResource(R.drawable.left_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingRt.smallCircleContainerRt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircle.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircle.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleText = (GradientDrawable) bindingRt.smallCircleTextRt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingRt.smallCircleImageRt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleImage.setColor(getResources().getColor(R.color.white));
                    }
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingRt.arrowCircleRt.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingRt.arrowCircleRt.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                Glide.with(getActivity())
                        .asBitmap()
                        .transform(new MultiTransformation(new CenterCrop(),
                                new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                        .load(response.getActionData().getContentMinimizedImage())
                        .into(bindingRt.smallCircleImageRt);
                bindingRt.smallCircleTextRt.setVisibility(View.GONE);
            } else {
                bindingRt.smallCircleTextRt.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingRt.smallCircleTextRt.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingRt.smallCircleTextRt.setTextColor(getResources().getColor(R.color.white));
                }
                bindingRt.smallCircleTextRt.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingRt.smallCircleImageRt.setVisibility(View.GONE);
                bindingRt.smallCircleTextRt.topDown = isTopToBottom;
                bindingRt.smallCircleTextRt.isCircle = true;
                bindingRt.smallCircleTextRt.isRight = isRight;
            }

            bindingRt.smallCircleContainerRt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRt.closeFrameLayoutRt.setVisibility(View.GONE);
                    bindingRt.closeButtonRt.setVisibility(View.GONE);
                    bindingRt.bigContainerRt.setVisibility(View.GONE);
                    bindingRt.arrowCircleRt.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRt.bigContainerRt.setVisibility(View.VISIBLE);
                    bindingRt.closeFrameLayoutRt.setVisibility(View.VISIBLE);
                    bindingRt.closeButtonRt.setVisibility(View.VISIBLE);
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingRt.arrowSquareRt.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingRt.arrowSquareRt.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                if(shape == Shape.SOFT_EDGE) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                            .load(response.getActionData().getContentMinimizedImage())
                            .into(bindingRt.smallSquareImageRt);
                } else {
                    Picasso.get().load(response.getActionData().getContentMinimizedImage())
                            .into(bindingRt.smallSquareImageRt);
                }
                bindingRt.smallSquareTextRt.setVisibility(View.GONE);
            } else {
                bindingRt.smallSquareTextRt.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingRt.smallSquareTextRt.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingRt.smallSquareTextRt.setTextColor(getResources().getColor(R.color.white));
                }
                bindingRt.smallSquareTextRt.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingRt.smallSquareImageRt.setVisibility(View.GONE);
                bindingRt.smallSquareTextRt.topDown = isTopToBottom;
                bindingRt.smallCircleTextRt.isCircle = false;
                bindingRt.smallCircleTextRt.isRight = isRight;
            }

            bindingRt.smallSquareContainerRt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRt.closeFrameLayoutRt.setVisibility(View.GONE);
                    bindingRt.closeButtonRt.setVisibility(View.GONE);
                    bindingRt.bigContainerRt.setVisibility(View.GONE);
                    bindingRt.arrowSquareRt.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRt.closeFrameLayoutRt.setVisibility(View.VISIBLE);
                    bindingRt.closeButtonRt.setVisibility(View.VISIBLE);
                    bindingRt.bigContainerRt.setVisibility(View.VISIBLE);
                    bindingRt.arrowSquareRt.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isMaxiBackgroundImage) {
            Picasso.get().load(mExtendedProps.getMaxiBackgroundImage())
                    .into(bindingRt.bigBackgroundImageRt);
        } else {
            if(mExtendedProps.getMaxiBackgroundColor() != null && !mExtendedProps.getMaxiBackgroundColor().equals("")) {
                bindingRt.bigContainerRt.setBackgroundColor(Color.parseColor(mExtendedProps.getMaxiBackgroundColor()));
            } else {
                bindingRt.bigContainerRt.setBackgroundColor(getResources().getColor(R.color.white));
            }
            bindingRt.bigBackgroundImageRt.setVisibility(View.GONE);
        }

        if(response.getActionData().getContentMaximizedImage() != null && !response.getActionData().getContentMaximizedImage().equals("")) {
            Picasso.get().load(response.getActionData().getContentMaximizedImage())
                    .into(bindingRt.bigImageRt);
        }

        bindingRt.bigContainerRt.setOnClickListener(v -> {
            final String uriString = response.getActionData().getAndroidLnk();
            InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
            Report report = new Report();
            report.impression = response.getActionData().getReport().getImpression();
            report.click = response.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report);
            if(buttonInterface != null) {
                Visilabs.CallAPI().setInAppButtonInterface(null);
                buttonInterface.onPress(uriString);
            } else {
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(viewIntent);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                    }
                }
            }
        });
    }

    private void adjustRm() {
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
        bindingRm.closeFrameLayoutRm.setVisibility(View.GONE);
        bindingRm.closeButtonRm.setOnClickListener(v -> endFragment());

        switch (shape) {
            case SHARP_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Picasso.get().load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRm.smallSquareBackgroundImageRm);
                    }
                } else {
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        bindingRm.smallSquareContainerRm.setBackgroundColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        bindingRm.smallSquareContainerRm.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    bindingRm.smallSquareBackgroundImageRm.setVisibility(View.GONE);
                }
                bindingRm.smallCircleContainerRm.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRm.smallSquareBackgroundImageRm);
                    }
                } else {
                    bindingRm.smallSquareContainerRm.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRm.smallSquareTextRm.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRm.smallSquareImageRm.setBackgroundResource(R.drawable.rounded_corners_left);
                    GradientDrawable gd = (GradientDrawable) bindingRm.smallSquareContainerRm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gd.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gd.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdText = (GradientDrawable) bindingRm.smallSquareTextRm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdImage = (GradientDrawable) bindingRm.smallSquareImageRm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdImage.setColor(getResources().getColor(R.color.white));
                    }
                    bindingRm.smallSquareBackgroundImageRm.setVisibility(View.GONE);
                }
                bindingRm.smallCircleContainerRm.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRm.smallCircleBackgroundImageRm);
                    }
                } else {
                    bindingRm.smallCircleContainerRm.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRm.smallCircleTextRm.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRm.smallCircleImageRm.setBackgroundResource(R.drawable.left_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingRm.smallCircleContainerRm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircle.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircle.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleText = (GradientDrawable) bindingRm.smallCircleTextRm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingRm.smallCircleImageRm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleImage.setColor(getResources().getColor(R.color.white));
                    }
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingRm.arrowCircleRm.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingRm.arrowCircleRm.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                Glide.with(getActivity())
                        .asBitmap()
                        .transform(new MultiTransformation(new CenterCrop(),
                                new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                        .load(response.getActionData().getContentMinimizedImage())
                        .into(bindingRm.smallCircleImageRm);
                bindingRm.smallCircleTextRm.setVisibility(View.GONE);
            } else {
                bindingRm.smallCircleTextRm.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingRm.smallCircleTextRm.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingRm.smallCircleTextRm.setTextColor(getResources().getColor(R.color.white));
                }
                bindingRm.smallCircleTextRm.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingRm.smallCircleImageRm.setVisibility(View.GONE);
                bindingRm.smallCircleTextRm.topDown = isTopToBottom;
                bindingRm.smallCircleTextRm.isCircle = true;
                bindingRm.smallCircleTextRm.isRight = isRight;
            }

            bindingRm.smallCircleContainerRm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRm.closeFrameLayoutRm.setVisibility(View.GONE);
                    bindingRm.closeButtonRm.setVisibility(View.GONE);
                    bindingRm.bigContainerRm.setVisibility(View.GONE);
                    bindingRm.arrowCircleRm.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRm.closeFrameLayoutRm.setVisibility(View.VISIBLE);
                    bindingRm.closeButtonRm.setVisibility(View.VISIBLE);
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingRm.arrowSquareRm.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingRm.arrowSquareRm.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                if(shape == Shape.SOFT_EDGE) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                            .load(response.getActionData().getContentMinimizedImage())
                            .into(bindingRm.smallSquareImageRm);
                } else {
                    Picasso.get().load(response.getActionData().getContentMinimizedImage())
                            .into(bindingRm.smallSquareImageRm);
                }
                bindingRm.smallSquareTextRm.setVisibility(View.GONE);
            } else {
                bindingRm.smallSquareTextRm.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingRm.smallSquareTextRm.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingRm.smallSquareTextRm.setTextColor(getResources().getColor(R.color.white));
                }
                bindingRm.smallSquareTextRm.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingRm.smallSquareImageRm.setVisibility(View.GONE);
                bindingRm.smallSquareTextRm.topDown = isTopToBottom;
                bindingRm.smallCircleTextRm.isCircle = false;
                bindingRm.smallCircleTextRm.isRight = isRight;
            }

            bindingRm.smallSquareContainerRm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRm.closeFrameLayoutRm.setVisibility(View.GONE);
                    bindingRm.closeButtonRm.setVisibility(View.GONE);
                    bindingRm.bigContainerRm.setVisibility(View.GONE);
                    bindingRm.arrowSquareRm.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRm.closeFrameLayoutRm.setVisibility(View.VISIBLE);
                    bindingRm.closeButtonRm.setVisibility(View.VISIBLE);
                    bindingRm.bigContainerRm.setVisibility(View.VISIBLE);
                    bindingRm.arrowSquareRm.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isMaxiBackgroundImage) {
            Picasso.get().load(mExtendedProps.getMaxiBackgroundImage())
                    .into(bindingRm.bigBackgroundImageRm);
        } else {
            if(mExtendedProps.getMaxiBackgroundColor() != null && !mExtendedProps.getMaxiBackgroundColor().equals("")) {
                bindingRm.bigContainerRm.setBackgroundColor(Color.parseColor(mExtendedProps.getMaxiBackgroundColor()));
            } else {
                bindingRm.bigContainerRm.setBackgroundColor(getResources().getColor(R.color.white));
            }
            bindingRm.bigBackgroundImageRm.setVisibility(View.GONE);
        }

        if(response.getActionData().getContentMaximizedImage() != null && !response.getActionData().getContentMaximizedImage().equals("")) {
            Picasso.get().load(response.getActionData().getContentMaximizedImage())
                    .into(bindingRm.bigImageRm);
        }

        bindingRm.bigContainerRm.setOnClickListener(v -> {
            final String uriString = response.getActionData().getAndroidLnk();
            InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
            Report report = new Report();
            report.impression = response.getActionData().getReport().getImpression();
            report.click = response.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report);
            if(buttonInterface != null) {
                Visilabs.CallAPI().setInAppButtonInterface(null);
                buttonInterface.onPress(uriString);
            } else {
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(viewIntent);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                    }
                }
            }
        });
    }

    private void adjustRb() {
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
        bindingRb.closeFrameLayoutRb.setVisibility(View.GONE);
        bindingRb.closeButtonRb.setOnClickListener(v -> endFragment());

        switch (shape) {
            case SHARP_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Picasso.get().load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRb.smallSquareBackgroundImageRb);
                    }
                } else {
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        bindingRb.smallSquareContainerRb.setBackgroundColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        bindingRb.smallSquareContainerRb.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    bindingRb.smallSquareBackgroundImageRb.setVisibility(View.GONE);
                }
                bindingRb.smallCircleContainerRb.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRb.smallSquareBackgroundImageRb);
                    }
                } else {
                    bindingRb.smallSquareContainerRb.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRb.smallSquareTextRb.setBackgroundResource(R.drawable.rounded_corners_left);
                    bindingRb.smallSquareImageRb.setBackgroundResource(R.drawable.rounded_corners_left);
                    GradientDrawable gd = (GradientDrawable) bindingRb.smallSquareContainerRb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gd.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gd.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdText = (GradientDrawable) bindingRb.smallSquareTextRb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdImage = (GradientDrawable) bindingRb.smallSquareImageRb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdImage.setColor(getResources().getColor(R.color.white));
                    }
                    bindingRb.smallSquareBackgroundImageRb.setVisibility(View.GONE);
                }
                bindingRb.smallCircleContainerRb.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingRb.smallCircleBackgroundImageRb);
                    }
                } else {
                    bindingRb.smallCircleContainerRb.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRb.smallCircleTextRb.setBackgroundResource(R.drawable.left_half_circle);
                    bindingRb.smallCircleImageRb.setBackgroundResource(R.drawable.left_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingRb.smallCircleContainerRb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircle.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircle.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleText = (GradientDrawable) bindingRb.smallCircleTextRb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingRb.smallCircleImageRb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleImage.setColor(getResources().getColor(R.color.white));
                    }
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingRb.arrowCircleRb.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingRb.arrowCircleRb.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                Glide.with(getActivity())
                        .asBitmap()
                        .transform(new MultiTransformation(new CenterCrop(),
                                new GranularRoundedCorners(500f, 0f, 0f, 500f)))
                        .load(response.getActionData().getContentMinimizedImage())
                        .into(bindingRb.smallCircleImageRb);
                bindingRb.smallCircleTextRb.setVisibility(View.GONE);
            } else {
                bindingRb.smallCircleTextRb.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingRb.smallCircleTextRb.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingRb.smallCircleTextRb.setTextColor(getResources().getColor(R.color.white));
                }
                bindingRb.smallCircleTextRb.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingRb.smallCircleImageRb.setVisibility(View.GONE);
                bindingRb.smallCircleTextRb.topDown = isTopToBottom;
                bindingRb.smallCircleTextRb.isCircle = true;
                bindingRb.smallCircleTextRb.isRight = isRight;
            }

            bindingRb.smallCircleContainerRb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRb.closeFrameLayoutRb.setVisibility(View.GONE);
                    bindingRb.closeButtonRb.setVisibility(View.GONE);
                    bindingRb.bigContainerRb.setVisibility(View.GONE);
                    bindingRb.arrowCircleRb.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRb.closeFrameLayoutRb.setVisibility(View.VISIBLE);
                    bindingRb.closeButtonRb.setVisibility(View.VISIBLE);
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingRb.arrowSquareRb.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingRb.arrowSquareRb.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                if(shape == Shape.SOFT_EDGE) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(40f, 0f, 0f, 40f)))
                            .load(response.getActionData().getContentMinimizedImage())
                            .into(bindingRb.smallSquareImageRb);
                } else {
                    Picasso.get().load(response.getActionData().getContentMinimizedImage())
                            .into(bindingRb.smallSquareImageRb);
                }
                bindingRb.smallSquareTextRb.setVisibility(View.GONE);
            } else {
                bindingRb.smallSquareTextRb.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingRb.smallSquareTextRb.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingRb.smallSquareTextRb.setTextColor(getResources().getColor(R.color.white));
                }
                bindingRb.smallSquareTextRb.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingRb.smallSquareImageRb.setVisibility(View.GONE);
                bindingRb.smallSquareTextRb.topDown = isTopToBottom;
                bindingRb.smallCircleTextRb.isCircle = false;
                bindingRb.smallCircleTextRb.isRight = isRight;
            }

            bindingRb.smallSquareContainerRb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingRb.closeFrameLayoutRb.setVisibility(View.GONE);
                    bindingRb.closeButtonRb.setVisibility(View.GONE);
                    bindingRb.bigContainerRb.setVisibility(View.GONE);
                    bindingRb.arrowCircleRb.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingRb.closeFrameLayoutRb.setVisibility(View.VISIBLE);
                    bindingRb.closeButtonRb.setVisibility(View.VISIBLE);
                    bindingRb.bigContainerRb.setVisibility(View.VISIBLE);
                    bindingRb.arrowCircleRb.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isMaxiBackgroundImage) {
            Picasso.get().load(mExtendedProps.getMaxiBackgroundImage())
                    .into(bindingRb.bigBackgroundImageRb);
        } else {
            if(mExtendedProps.getMaxiBackgroundColor() != null && !mExtendedProps.getMaxiBackgroundColor().equals("")) {
                bindingRb.bigContainerRb.setBackgroundColor(Color.parseColor(mExtendedProps.getMaxiBackgroundColor()));
            } else {
                bindingRb.bigContainerRb.setBackgroundColor(getResources().getColor(R.color.white));
            }
            bindingRb.bigBackgroundImageRb.setVisibility(View.GONE);
        }

        if(response.getActionData().getContentMaximizedImage() != null && !response.getActionData().getContentMaximizedImage().equals("")) {
            Picasso.get().load(response.getActionData().getContentMaximizedImage())
                    .into(bindingRb.bigImageRb);
        }

        bindingRb.bigContainerRb.setOnClickListener(v -> {
            final String uriString = response.getActionData().getAndroidLnk();
            InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
            Report report = new Report();
            report.impression = response.getActionData().getReport().getImpression();
            report.click = response.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report);
            if(buttonInterface != null) {
                Visilabs.CallAPI().setInAppButtonInterface(null);
                buttonInterface.onPress(uriString);
            } else {
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(viewIntent);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                    }
                }
            }
        });
    }

    private void adjustLt() {
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
        bindingLt.closeFrameLayoutLt.setVisibility(View.GONE);
        bindingLt.closeButtonLt.setOnClickListener(v -> endFragment());

        switch (shape) {
            case SHARP_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Picasso.get().load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLt.smallSquareBackgroundImageLt);
                    }
                } else {
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        bindingLt.smallSquareContainerLt.setBackgroundColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        bindingLt.smallSquareContainerLt.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    bindingLt.smallSquareBackgroundImageLt.setVisibility(View.GONE);
                }
                bindingLt.smallCircleContainerLt.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLt.smallSquareBackgroundImageLt);
                    }
                } else {
                    bindingLt.smallSquareContainerLt.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLt.smallSquareTextLt.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLt.smallSquareImageLt.setBackgroundResource(R.drawable.rounded_corners_right);
                    GradientDrawable gd = (GradientDrawable) bindingLt.smallSquareContainerLt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gd.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gd.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdText = (GradientDrawable) bindingLt.smallSquareTextLt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdImage = (GradientDrawable) bindingLt.smallSquareImageLt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdImage.setColor(getResources().getColor(R.color.white));
                    }
                    bindingLt.smallSquareBackgroundImageLt.setVisibility(View.GONE);
                }
                bindingLt.smallCircleContainerLt.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLt.smallCircleBackgroundImageLt);
                    }
                } else {
                    bindingLt.smallCircleContainerLt.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLt.smallCircleTextLt.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLt.smallCircleImageLt.setBackgroundResource(R.drawable.right_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingLt.smallCircleContainerLt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircle.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircle.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleText = (GradientDrawable) bindingLt.smallCircleTextLt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingLt.smallCircleImageLt.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleImage.setColor(getResources().getColor(R.color.white));
                    }
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingLt.arrowCircleLt.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingLt.arrowCircleLt.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                Glide.with(getActivity())
                        .asBitmap()
                        .transform(new MultiTransformation(new CenterCrop(),
                                new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                        .load(response.getActionData().getContentMinimizedImage())
                        .into(bindingLt.smallCircleImageLt);
                bindingLt.smallCircleTextLt.setVisibility(View.GONE);
            } else {
                bindingLt.smallCircleTextLt.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingLt.smallCircleTextLt.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingLt.smallCircleTextLt.setTextColor(getResources().getColor(R.color.white));
                }
                bindingLt.smallCircleTextLt.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingLt.smallCircleImageLt.setVisibility(View.GONE);
                bindingLt.smallCircleTextLt.topDown = isTopToBottom;
                bindingLt.smallCircleTextLt.isCircle = true;
                bindingLt.smallCircleTextLt.isRight = isRight;
            }

            bindingLt.smallCircleContainerLt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLt.closeFrameLayoutLt.setVisibility(View.GONE);
                    bindingLt.closeButtonLt.setVisibility(View.GONE);
                    bindingLt.bigContainerLt.setVisibility(View.GONE);
                    bindingLt.arrowCircleLt.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingLt.closeFrameLayoutLt.setVisibility(View.VISIBLE);
                    bindingLt.closeButtonLt.setVisibility(View.VISIBLE);
                    bindingLt.bigContainerLt.setVisibility(View.VISIBLE);
                    bindingLt.arrowCircleLt.setText(getString(R.string.notification_left_arrow));
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingLt.arrowSquareLt.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingLt.arrowSquareLt.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                if(shape == Shape.SOFT_EDGE) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                            .load(response.getActionData().getContentMinimizedImage())
                            .into(bindingLt.smallSquareImageLt);
                } else {
                    Picasso.get().load(response.getActionData().getContentMinimizedImage())
                            .into(bindingLt.smallSquareImageLt);
                }
                bindingLt.smallSquareTextLt.setVisibility(View.GONE);
            } else {
                bindingLt.smallSquareTextLt.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingLt.smallSquareTextLt.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingLt.smallSquareTextLt.setTextColor(getResources().getColor(R.color.white));
                }
                bindingLt.smallSquareTextLt.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingLt.smallSquareImageLt.setVisibility(View.GONE);
                bindingLt.smallSquareTextLt.topDown = isTopToBottom;
                bindingLt.smallCircleTextLt.isCircle = false;
                bindingLt.smallCircleTextLt.isRight = isRight;
            }

            bindingLt.smallSquareContainerLt.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLt.closeFrameLayoutLt.setVisibility(View.GONE);
                    bindingLt.closeButtonLt.setVisibility(View.GONE);
                    bindingLt.bigContainerLt.setVisibility(View.GONE);
                    bindingLt.arrowCircleLt.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingLt.closeFrameLayoutLt.setVisibility(View.VISIBLE);
                    bindingLt.closeButtonLt.setVisibility(View.VISIBLE);
                    bindingLt.bigContainerLt.setVisibility(View.VISIBLE);
                    bindingLt.arrowCircleLt.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isMaxiBackgroundImage) {
            Picasso.get().load(mExtendedProps.getMaxiBackgroundImage())
                    .into(bindingLt.bigBackgroundImageLt);
        } else {
            if(mExtendedProps.getMaxiBackgroundColor() != null && !mExtendedProps.getMaxiBackgroundColor().equals("")) {
                bindingLt.bigContainerLt.setBackgroundColor(Color.parseColor(mExtendedProps.getMaxiBackgroundColor()));
            } else {
                bindingLt.bigContainerLt.setBackgroundColor(getResources().getColor(R.color.white));
            }
            bindingLt.bigBackgroundImageLt.setVisibility(View.GONE);
        }

        if(response.getActionData().getContentMaximizedImage() != null && !response.getActionData().getContentMaximizedImage().equals("")) {
            Picasso.get().load(response.getActionData().getContentMaximizedImage())
                    .into(bindingLt.bigImageLt);
        }

        bindingLt.bigContainerLt.setOnClickListener(v -> {
            final String uriString = response.getActionData().getAndroidLnk();
            InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
            Report report = new Report();
            report.impression = response.getActionData().getReport().getImpression();
            report.click = response.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report);
            if(buttonInterface != null) {
                Visilabs.CallAPI().setInAppButtonInterface(null);
                buttonInterface.onPress(uriString);
            } else {
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(viewIntent);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                    }
                }
            }
        });
    }

    private void adjustLm() {
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
        bindingLm.closeFrameLayoutLm.setVisibility(View.GONE);
        bindingLm.closeButtonLm.setOnClickListener(v -> endFragment());

        switch (shape) {
            case SHARP_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Picasso.get().load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLm.smallSquareBackgroundImageLm);
                    }
                } else {
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        bindingLm.smallSquareContainerLm.setBackgroundColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        bindingLm.smallSquareContainerLm.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    bindingLm.smallSquareBackgroundImageLm.setVisibility(View.GONE);
                }
                bindingLm.smallCircleContainerLm.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLm.smallSquareBackgroundImageLm);
                    }
                } else {
                    bindingLm.smallSquareContainerLm.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLm.smallSquareTextLm.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLm.smallSquareImageLm.setBackgroundResource(R.drawable.rounded_corners_right);
                    GradientDrawable gd = (GradientDrawable) bindingLm.smallSquareContainerLm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gd.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gd.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdText = (GradientDrawable) bindingLm.smallSquareTextLm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdImage = (GradientDrawable) bindingLm.smallSquareImageLm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdImage.setColor(getResources().getColor(R.color.white));
                    }
                    bindingLm.smallSquareBackgroundImageLm.setVisibility(View.GONE);
                }
                bindingLm.smallCircleContainerLm.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLm.smallCircleBackgroundImageLm);
                    }
                } else {
                    bindingLm.smallCircleContainerLm.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLm.smallCircleTextLm.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLm.smallCircleImageLm.setBackgroundResource(R.drawable.right_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingLm.smallCircleContainerLm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircle.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircle.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleText = (GradientDrawable) bindingLm.smallCircleTextLm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingLm.smallCircleImageLm.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleImage.setColor(getResources().getColor(R.color.white));
                    }
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingLm.arrowCircleLm.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingLm.arrowCircleLm.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                Glide.with(getActivity())
                        .asBitmap()
                        .transform(new MultiTransformation(new CenterCrop(),
                                new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                        .load(response.getActionData().getContentMinimizedImage())
                        .into(bindingLm.smallCircleImageLm);
                bindingLm.smallCircleTextLm.setVisibility(View.GONE);
            } else {
                bindingLm.smallCircleTextLm.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingLm.smallCircleTextLm.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingLm.smallCircleTextLm.setTextColor(getResources().getColor(R.color.white));
                }
                bindingLm.smallCircleTextLm.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingLm.smallCircleImageLm.setVisibility(View.GONE);
                bindingLm.smallCircleTextLm.topDown = isTopToBottom;
                bindingLm.smallCircleTextLm.isCircle = true;
                bindingLm.smallCircleTextLm.isRight = isRight;
            }

            bindingLm.smallCircleContainerLm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLm.closeFrameLayoutLm.setVisibility(View.GONE);
                    bindingLm.closeButtonLm.setVisibility(View.GONE);
                    bindingLm.bigContainerLm.setVisibility(View.GONE);
                    bindingLm.arrowCircleLm.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingLm.closeFrameLayoutLm.setVisibility(View.VISIBLE);
                    bindingLm.closeButtonLm.setVisibility(View.VISIBLE);
                    bindingLm.bigContainerLm.setVisibility(View.VISIBLE);
                    bindingLm.arrowCircleLm.setText(getString(R.string.notification_left_arrow));
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingLm.arrowSquareLm.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingLm.arrowSquareLm.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                if(shape == Shape.SOFT_EDGE) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                            .load(response.getActionData().getContentMinimizedImage())
                            .into(bindingLm.smallSquareImageLm);
                } else {
                    Picasso.get().load(response.getActionData().getContentMinimizedImage())
                            .into(bindingLm.smallSquareImageLm);
                }
                bindingLm.smallSquareTextLm.setVisibility(View.GONE);
            } else {
                bindingLm.smallSquareTextLm.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingLm.smallSquareTextLm.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingLm.smallSquareTextLm.setTextColor(getResources().getColor(R.color.white));
                }
                bindingLm.smallSquareTextLm.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingLm.smallSquareImageLm.setVisibility(View.GONE);
                bindingLm.smallSquareTextLm.topDown = isTopToBottom;
                bindingLm.smallCircleTextLm.isCircle = false;
                bindingLm.smallCircleTextLm.isRight = isRight;
            }

            bindingLm.smallSquareContainerLm.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLm.closeFrameLayoutLm.setVisibility(View.GONE);
                    bindingLm.closeButtonLm.setVisibility(View.GONE);
                    bindingLm.bigContainerLm.setVisibility(View.GONE);
                    bindingLm.arrowCircleLm.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingLm.closeFrameLayoutLm.setVisibility(View.VISIBLE);
                    bindingLm.closeButtonLm.setVisibility(View.VISIBLE);
                    bindingLm.bigContainerLm.setVisibility(View.VISIBLE);
                    bindingLm.arrowCircleLm.setText(getString(R.string.notification_left_arrow));
                }
            });
        }

        if(isMaxiBackgroundImage) {
            Picasso.get().load(mExtendedProps.getMaxiBackgroundImage())
                    .into(bindingLm.bigBackgroundImageLm);
        } else {
            if(mExtendedProps.getMaxiBackgroundColor() != null && !mExtendedProps.getMaxiBackgroundColor().equals("")) {
                bindingLm.bigContainerLm.setBackgroundColor(Color.parseColor(mExtendedProps.getMaxiBackgroundColor()));
            } else {
                bindingLm.bigContainerLm.setBackgroundColor(getResources().getColor(R.color.white));
            }
            bindingLm.bigBackgroundImageLm.setVisibility(View.GONE);
        }

        if(response.getActionData().getContentMaximizedImage() != null && !response.getActionData().getContentMaximizedImage().equals("")) {
            Picasso.get().load(response.getActionData().getContentMaximizedImage())
                    .into(bindingLm.bigImageLm);
        }

        bindingLm.bigContainerLm.setOnClickListener(v -> {
            final String uriString = response.getActionData().getAndroidLnk();
            InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
            Report report = new Report();
            report.impression = response.getActionData().getReport().getImpression();
            report.click = response.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report);
            if(buttonInterface != null) {
                Visilabs.CallAPI().setInAppButtonInterface(null);
                buttonInterface.onPress(uriString);
            } else {
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(viewIntent);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                    }
                }
            }
        });
    }

    private void adjustLb() {
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
        bindingLb.closeFrameLayoutLb.setVisibility(View.GONE);
        bindingLb.closeButtonLb.setOnClickListener(v -> endFragment());

        switch (shape) {
            case SHARP_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Picasso.get().load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLb.smallSquareBackgroundImageLb);
                    }
                } else {
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        bindingLb.smallSquareContainerLb.setBackgroundColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        bindingLb.smallSquareContainerLb.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    bindingLb.smallSquareBackgroundImageLb.setVisibility(View.GONE);
                }
                bindingLb.smallCircleContainerLb.setVisibility(View.GONE);
                break;
            case SOFT_EDGE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLb.smallSquareBackgroundImageLb);
                    }
                } else {
                    bindingLb.smallSquareContainerLb.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLb.smallSquareTextLb.setBackgroundResource(R.drawable.rounded_corners_right);
                    bindingLb.smallSquareImageLb.setBackgroundResource(R.drawable.rounded_corners_right);
                    GradientDrawable gd = (GradientDrawable) bindingLb.smallSquareContainerLb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gd.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gd.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdText = (GradientDrawable) bindingLb.smallSquareTextLb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdImage = (GradientDrawable) bindingLb.smallSquareImageLb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdImage.setColor(getResources().getColor(R.color.white));
                    }
                    bindingLb.smallSquareBackgroundImageLb.setVisibility(View.GONE);
                }
                bindingLb.smallCircleContainerLb.setVisibility(View.GONE);
                break;
            case CIRCLE:
                if(isMiniBackgroundImage) {
                    if(!isSmallImage) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .transform(new MultiTransformation(new CenterCrop(),
                                        new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                                .load(mExtendedProps.getMiniBackgroundImage())
                                .into(bindingLb.smallCircleBackgroundImageLb);
                    }
                } else {
                    bindingLb.smallCircleContainerLb.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLb.smallCircleTextLb.setBackgroundResource(R.drawable.right_half_circle);
                    bindingLb.smallCircleImageLb.setBackgroundResource(R.drawable.right_half_circle);
                    GradientDrawable gdCircle = (GradientDrawable) bindingLb.smallCircleContainerLb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircle.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircle.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleText = (GradientDrawable) bindingLb.smallCircleTextLb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleText.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleText.setColor(getResources().getColor(R.color.white));
                    }
                    GradientDrawable gdCircleImage = (GradientDrawable) bindingLb.smallCircleImageLb.getBackground();
                    if(mExtendedProps.getMiniBackgroundColor() != null && !mExtendedProps.getMiniBackgroundColor().equals("")) {
                        gdCircleImage.setColor(Color.parseColor(mExtendedProps.getMiniBackgroundColor()));
                    } else {
                        gdCircleImage.setColor(getResources().getColor(R.color.white));
                    }
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingLb.arrowCircleLb.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingLb.arrowCircleLb.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                Glide.with(getActivity())
                        .asBitmap()
                        .transform(new MultiTransformation(new CenterCrop(),
                                new GranularRoundedCorners(0f, 500f, 500f, 0f)))
                        .load(response.getActionData().getContentMinimizedImage())
                        .into(bindingLb.smallCircleImageLb);
                bindingLb.smallCircleTextLb.setVisibility(View.GONE);
            } else {
                bindingLb.smallCircleTextLb.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingLb.smallCircleTextLb.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingLb.smallCircleTextLb.setTextColor(getResources().getColor(R.color.white));
                }
                bindingLb.smallCircleTextLb.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
                bindingLb.smallCircleImageLb.setVisibility(View.GONE);
                bindingLb.smallCircleTextLb.topDown = isTopToBottom;
                bindingLb.smallCircleTextLb.isCircle = true;
                bindingLb.smallCircleTextLb.isRight = isRight;
            }

            bindingLb.smallCircleContainerLb.setOnClickListener(v -> {
                if(isExpanded) {
                    isExpanded = false;
                    bindingLb.closeFrameLayoutLb.setVisibility(View.GONE);
                    bindingLb.closeButtonLb.setVisibility(View.GONE);
                    bindingLb.bigContainerLb.setVisibility(View.GONE);
                    bindingLb.arrowCircleLb.setText(getString(R.string.notification_right_arrow));
                } else {
                    isExpanded = true;
                    bindingLb.closeFrameLayoutLb.setVisibility(View.VISIBLE);
                    bindingLb.closeButtonLb.setVisibility(View.VISIBLE);
                    bindingLb.bigContainerLb.setVisibility(View.VISIBLE);
                    bindingLb.arrowCircleLb.setText(getString(R.string.notification_left_arrow));
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

            if(mExtendedProps.getArrowColor() != null && !mExtendedProps.getArrowColor().equals("")) {
                bindingLb.arrowSquareLb.setTextColor(Color.parseColor(mExtendedProps.getArrowColor()));
            } else {
                bindingLb.arrowSquareLb.setTextColor(getResources().getColor(R.color.white));
            }

            if(isSmallImage) {
                if(shape == Shape.SOFT_EDGE) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .transform(new MultiTransformation(new CenterCrop(),
                                    new GranularRoundedCorners(0f, 40f, 40f, 0f)))
                            .load(response.getActionData().getContentMinimizedImage())
                            .into(bindingLb.smallSquareImageLb);
                } else {
                    Picasso.get().load(response.getActionData().getContentMinimizedImage())
                            .into(bindingLb.smallSquareImageLb);
                }
                bindingLb.smallSquareTextLb.setVisibility(View.GONE);
            } else {
                bindingLb.smallSquareTextLb.setText(response.getActionData().getContentMinimizedText());
                if(mExtendedProps.getMiniTextColor() != null && !mExtendedProps.getMiniTextColor().equals("")) {
                    bindingLb.smallSquareTextLb.setTextColor(Color.parseColor(mExtendedProps.getMiniTextColor()));
                } else {
                    bindingLb.smallSquareTextLb.setTextColor(getResources().getColor(R.color.white));
                }
                bindingLb.smallSquareTextLb.setTypeface(mExtendedProps.getMiniFontFamily(getActivity()));
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

        if(isMaxiBackgroundImage) {
            Picasso.get().load(mExtendedProps.getMaxiBackgroundImage())
                    .into(bindingLb.bigBackgroundImageLb);
        } else {
            if(mExtendedProps.getMaxiBackgroundColor() != null && !mExtendedProps.getMaxiBackgroundColor().equals("")) {
                bindingLb.bigContainerLb.setBackgroundColor(Color.parseColor(mExtendedProps.getMaxiBackgroundColor()));
            } else {
                bindingLb.bigContainerLb.setBackgroundColor(getResources().getColor(R.color.white));
            }
            bindingLb.bigBackgroundImageLb.setVisibility(View.GONE);
        }

        if(response.getActionData().getContentMaximizedImage() != null && !response.getActionData().getContentMaximizedImage().equals("")) {
            Picasso.get().load(response.getActionData().getContentMaximizedImage())
                    .into(bindingLb.bigImageLb);
        }

        bindingLb.bigContainerLb.setOnClickListener(v -> {
            final String uriString = response.getActionData().getAndroidLnk();
            InAppButtonInterface buttonInterface = Visilabs.CallAPI().getInAppButtonInterface();
            Report report = new Report();
            report.impression = response.getActionData().getReport().getImpression();
            report.click = response.getActionData().getReport().getClick();
            Visilabs.CallAPI().trackActionClick(report);
            if(buttonInterface != null) {
                Visilabs.CallAPI().setInAppButtonInterface(null);
                buttonInterface.onPress(uriString);
            } else {
                if (uriString != null && uriString.length() > 0) {
                    Uri uri;
                    try {
                        uri = Uri.parse(uriString);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(viewIntent);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, "Can't parse notification URI, will not take any action", e);
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("drawer", response);
    }

    private void endFragment() {
        if (getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(InAppNotificationFragment.this).commit();
        }
    }
}
