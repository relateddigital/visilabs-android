package com.visilabs.inApp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityTemplateBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.StringUtils;
import com.visilabs.view.BaseRating;
import com.visilabs.view.SmileRating;

import java.util.HashMap;

public class TemplateActivity extends AppCompatActivity implements SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {

    ActivityTemplateBinding mBinding;

    InAppMessage inAppMessage;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_template);

        inAppMessage = getInAppMessage();

        if (isShowingInApp()) {
            setUpView();
        }
    }

    private InAppMessage getInAppMessage() {

        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);

        InAppNotificationState inAppNotificationState =
                (InAppNotificationState) mUpdateDisplayState.getDisplayState();

        if (mUpdateDisplayState == null) {
            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");

        }

       return inAppNotificationState.getInAppMessage();
    }

    private void setUpView() {

        mBinding.smileRating.setOnSmileySelectionListener(this);
        mBinding.smileRating.setOnRatingSelectedListener(this);

        setCloseButton();

        setTemplate();
    }

    private void setTemplate() {

        mBinding.llOverlay.setBackgroundColor(Color.parseColor(inAppMessage.getBackground()));
        mBinding.ibClose.setBackgroundResource(getCloseIcon());

        switch (inAppMessage.getType()) {

            case IMAGE_TEXT_BUTTON:

                setTitle();
                setBody();
                setButton();
                mBinding.smileRating.setVisibility(View.GONE);

                break;

            case FULL_IMAGE:

                mBinding.tvBody.setVisibility(View.GONE);
                mBinding.tvTitle.setVisibility(View.GONE);
                mBinding.smileRating.setVisibility(View.GONE);
                mBinding.btnTemplate.setVisibility(View.GONE);

                break;

            case IMAGE_BUTTON:

                mBinding.smileRating.setVisibility(View.GONE);
                mBinding.llTextContainer.setVisibility(View.GONE);
                setButton();

                break;

            case NPS:

                setTitle();
                setBody();
                setButton();
                showNps();

                break;

            case SMILE_RATING:

                setBody();
                setTitle();
                setButton();
                showSmileRating();

                break;
        }
    }

    private void setTitle() {

        mBinding.tvTitle.setVisibility(View.VISIBLE);
        mBinding.tvTitle.setTypeface(inAppMessage.getFont_family(), Typeface.BOLD);
        mBinding.tvTitle.setText(inAppMessage.getTitle());
        mBinding.tvTitle.setTextColor(Color.parseColor(inAppMessage.getMsg_title_color()));
        mBinding.tvTitle.setTextSize(Float.parseFloat(inAppMessage.getMsg_body_textsize())+12);
    }

    private void setBody() {
        mBinding.tvBody.setText(inAppMessage.getBody());
        mBinding.tvBody.setTypeface(inAppMessage.getFont_family());
        mBinding.tvBody.setVisibility(View.VISIBLE);
        mBinding.tvBody.setTextColor(Color.parseColor(inAppMessage.getMsg_body_color()));
        mBinding.tvBody.setTextSize(Float.parseFloat(inAppMessage.getMsg_body_textsize())+8);
    }

    private void setButton() {

        mBinding.btnTemplate.setTypeface(inAppMessage.getFont_family());
        mBinding.btnTemplate.setVisibility(View.VISIBLE);
        mBinding.btnTemplate.setText(inAppMessage.getButtonText());
        mBinding.btnTemplate.setTextColor(Color.parseColor(inAppMessage.getButton_text_color()));
        mBinding.btnTemplate.setBackgroundColor(Color.parseColor(inAppMessage.getButton_color()));

        mBinding.btnTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inAppMessage.getButtonURL() != null && inAppMessage.getButtonURL().length() > 0) {

                    try {

                        Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, getRate());
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(inAppMessage.getButtonURL()));
                        startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                }
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        });
    }

    private String getRate() {
        switch (inAppMessage.getType()) {
            case SMILE_RATING:
               return "&OM.smile=" + mBinding.smileRating.getRating();

            case NPS:
                return "&OM.nps=" + mBinding.rb.getRating();
        }

        return "";
    }


    public void setCloseButton() {

        mBinding.ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);

                finish();
            }
        });
    }

    void showNps() {
        mBinding.rb.setVisibility(View.VISIBLE);
    }

    void showSmileRating() {
        mBinding.smileRating.setVisibility(View.VISIBLE);
    }

    private int getCloseIcon() {

        switch (inAppMessage.getCloseButton()) {

            case "White":
                return R.drawable.ic_close_white_24dp;

            case "Black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;
    }

    private boolean isShowingInApp() {
        if (mUpdateDisplayState == null) {
            return false;
        }
        return InAppNotificationState.TYPE.equals(
                mUpdateDisplayState.getDisplayState().getType()
        );
    }

    @Override
    public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
        switch (smiley) {
            case SmileRating.BAD:
                Log.i("TAG", "Bad");
                break;
            case SmileRating.GOOD:
                Log.i("TAG", "Good");
                break;
            case SmileRating.GREAT:
                Log.i("TAG", "Great");
                break;
            case SmileRating.OKAY:
                Log.i("TAG", "Okay");
                break;
            case SmileRating.TERRIBLE:
                Log.i("TAG", "Terrible");
                break;
            case SmileRating.NONE:
                Log.i("TAG", "None");
                break;
        }
    }

    @Override
    public void onRatingSelected(int level, boolean reselected) {
        Log.i("TAG", "Rated as: " + level + " - " + reselected);
    }
}
