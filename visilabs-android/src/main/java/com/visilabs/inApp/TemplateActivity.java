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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityTemplateBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.StringUtils;
import com.visilabs.view.BaseRating;
import com.visilabs.view.SmileRating;

public class TemplateActivity extends AppCompatActivity implements SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {


    ActivityTemplateBinding mBinding;

    InAppMessage inApp;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);

        InAppNotificationState inAppNotificationState =
                (InAppNotificationState) mUpdateDisplayState.getDisplayState();

        if (mUpdateDisplayState == null) {

            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");
            finish();
            return;
        }

        inApp = inAppNotificationState.getInAppMessage();

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_template);

        mBinding.sr.setOnSmileySelectionListener(this);
        mBinding.sr.setOnRatingSelectedListener(this);


        if (isShowingInApp()) {
            setUpView();
        } else {
            finish();
        }
    }

    private void setUpView() {

        setTemplate();

        mBinding.btnTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inApp.getButtonURL() != null && inApp.getButtonURL().length() > 0) {

                    try {
                        Visilabs.CallAPI().trackInAppMessageClick(inApp);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(inApp.getButtonURL()));
                        startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                }
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });

        mBinding.ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });
    }

    private int getCloseIcon() {

        switch (inApp.getCloseButton()) {

            case "White":
                return R.drawable.ic_close_white_24dp;

            case "Black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;
    }

    private void setText() {

        mBinding.tvTitle.setVisibility(View.VISIBLE);
        mBinding.tvTitle.setTypeface(inApp.getFont(), Typeface.BOLD);
        mBinding.tvTitle.setText(inApp.getTitle());
        mBinding.tvTitle.setTextColor(Color.parseColor(inApp.getTitleColor()));
        mBinding.tvTitle.setTextSize(Float.parseFloat(inApp.getTitleSize()));
    }

    private void setBody() {
        mBinding.tvBody.setText(inApp.getBody());
        mBinding.tvBody.setTypeface(inApp.getFont());
        mBinding.tvBody.setVisibility(View.VISIBLE);
        mBinding.tvBody.setTextColor(Color.parseColor(inApp.getBodyColor()));
        mBinding.tvBody.setTextSize(Float.parseFloat(inApp.getBodySize()));
    }

    void showNps() {
        mBinding.rb.setVisibility(View.VISIBLE);
    }

    void showSmileRating() {
        mBinding.sr.setVisibility(View.VISIBLE);
    }

    private void setButton() {

        mBinding.btnTemplate.setTypeface(inApp.getFont());
        mBinding.btnTemplate.setVisibility(View.VISIBLE);
        mBinding.btnTemplate.setText(inApp.getButtonText());
        mBinding.btnTemplate.setTextColor(Color.parseColor(inApp.getButtonTextColor()));
        mBinding.btnTemplate.setBackgroundColor(Color.parseColor(inApp.getButtonColor()));
    }

    private void setTemplate() {

        mBinding.llOverlay.setBackgroundColor(Color.parseColor(inApp.getBackground()));
        mBinding.ibClose.setBackgroundResource(getCloseIcon());

        switch (inApp.getType()) {

            case IMAGE_TEXT_BUTTON:

                setText();
                setBody();
                setButton();
                mBinding.sr.setVisibility(View.GONE);

                break;

            case FULL_IMAGE:

                mBinding.tvBody.setVisibility(View.GONE);
                mBinding.tvTitle.setVisibility(View.GONE);
                mBinding.sr.setVisibility(View.GONE);
                mBinding.btnTemplate.setVisibility(View.GONE);

                break;

            case IMAGE_BUTTON:

                mBinding.sr.setVisibility(View.GONE);
                mBinding.llTextContainer.setVisibility(View.GONE);
                setButton();

                break;

            case NPS:

                setText();
                setBody();
                setButton();
                showNps();

                break;

            case SMILE_RATING:

                setBody();
                setText();
                setButton();
                showSmileRating();

                break;
        }
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
