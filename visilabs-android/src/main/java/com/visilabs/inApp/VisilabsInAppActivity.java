package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.ActivityInAppFullBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.StringUtils;


public class VisilabsInAppActivity extends Activity implements IVisilabs {

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    InAppMessage mInApp;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private int mIntentId = -1;
    private ActivityInAppFullBinding binding;
    private boolean mIsRotation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInAppFullBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        if(savedInstanceState != null) {
            mIntentId = savedInstanceState.getInt(INTENT_ID_KEY, Integer.MAX_VALUE);
        } else {
            mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        }

        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);


        if (null == mUpdateDisplayState) {

            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");
            finish();
            return;
        }

        if (isShowingInApp()) {
            setUpView();
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INTENT_ID_KEY, mIntentId);
        mIsRotation = true;
    }

    @Override
    public void setUpView() {

        InAppNotificationState inAppNotificationState =
                (InAppNotificationState) mUpdateDisplayState.getDisplayState();

        mInApp = inAppNotificationState.getInAppMessage();

        setInAppData();
        setPromotionCode();
        clickEvents();
    }

    private void setInAppData() {


        binding.tvInAppTitle.setText(mInApp.getActionData().getMsgTitle().replace("\\n","\n"));
        binding.tvInAppSubtitle.setText(mInApp.getActionData().getMsgBody().replace("\\n","\n"));

        if (mInApp.getActionData().getBtnText() != null && mInApp.getActionData().getBtnText().length() > 0) {
            binding.btnInApp.setText(mInApp.getActionData().getBtnText());
        }
        if(!mInApp.getActionData().getImg().equals("")) {
            binding.fivInAppImage.setVisibility(View.VISIBLE);
            Picasso.get().load(mInApp.getActionData().getImg()).into(binding.fivInAppImage);
        } else {
            binding.fivInAppImage.setVisibility(View.GONE);
        }
    }

    private void setPromotionCode() {
        if(!StringUtils.isNullOrWhiteSpace(mInApp.getActionData().getPromotionCode())
                && !StringUtils.isNullOrWhiteSpace(mInApp.getActionData().getPromoCodeBackgroundColor())
                && !StringUtils.isNullOrWhiteSpace(mInApp.getActionData().getPromoCodeTextColor())){
            binding.llCouponContainer.setVisibility(View.VISIBLE);
            binding.llCouponContainer.setBackgroundColor(Color.parseColor(mInApp.getActionData().getPromoCodeBackgroundColor()));
            binding.tvCouponCode.setText(mInApp.getActionData().getPromotionCode());
            binding.tvCouponCode.setTextColor(Color.parseColor(mInApp.getActionData().getPromoCodeTextColor()));
            binding.llCouponContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), mInApp.getActionData().getPromotionCode());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            binding.llCouponContainer.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {

        binding.btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInApp.getActionData().getAndroidLnk() != null && mInApp.getActionData().getAndroidLnk().length() > 0) {

                    try {
                        Visilabs.CallAPI().trackInAppMessageClick(mInApp, null);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInApp.getActionData().getAndroidLnk()));
                        VisilabsInAppActivity.this.startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                }
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });


        binding.btnInApp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.cta_button_highlight);
                } else {
                    v.setBackgroundResource(R.drawable.cta_button);
                }
                return false;
            }
        });

        binding.llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });

    }

    private InAppNotificationState getSurveyState() {
        return (InAppNotificationState) mUpdateDisplayState.getDisplayState();
    }

    private boolean isShowingSurvey() {
        if (null == mUpdateDisplayState) {
            return false;
        }
        return InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }

    private boolean isShowingInApp() {
        if (null == mUpdateDisplayState) {
            return false;
        }
        return InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }

    @Override
    public void onBackPressed() {
        if (isShowingInApp()) {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIsRotation) {
            mIsRotation = false;
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        }
    }

}
