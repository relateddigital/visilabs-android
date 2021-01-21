package com.visilabs.inApp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityTemplateBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.StringUtils;
import com.visilabs.view.BaseRating;
import com.visilabs.view.SmileRating;

public class TemplateActivity extends AppCompatActivity implements SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";
    InAppMessage mInAppMessage;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private int mIntentId = -1;
    private ActivityTemplateBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);

        mInAppMessage = getInAppMessage();

        setContentView(view);
        this.setFinishOnTouchOutside(false);

        if (isShowingInApp() && mInAppMessage != null) {
            setUpView();
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        }


        //TODO: daha sonra bu kısım opsiyonel olabilir. Burada releaseDisplayState metodu da çağırılmalı
        this.setFinishOnTouchOutside(true);
    }

    private InAppMessage getInAppMessage() {

        InAppNotificationState inAppNotificationState;

        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);

        if (mUpdateDisplayState == null || mUpdateDisplayState.getDisplayState() == null) {
            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");

            return null;
        } else {
            inAppNotificationState =
                    (InAppNotificationState) mUpdateDisplayState.getDisplayState();
            return inAppNotificationState.getInAppMessage();

        }
    }

    private void setUpView() {
        Picasso.get().load(mInAppMessage.getImageUrl()).into(binding.ivTemplate);

        binding.smileRating.setOnSmileySelectionListener(this);
        binding.smileRating.setOnRatingSelectedListener(this);

        setCloseButton();

        setTemplate();
    }

    private void setTemplate() {

        //llOverlay.setBackgroundColor(Color.parseColor(inAppMessage.getBackground()));
        binding.ibClose.setBackgroundResource(getCloseIcon());

        switch (mInAppMessage.getType()) {

            case IMAGE_TEXT_BUTTON:

                setTitle();
                setBody();
                setButton();
                binding.ratingBar.setVisibility(View.GONE);
                binding.smileRating.setVisibility(View.GONE);

                break;

            case FULL_IMAGE:

                binding.tvBody.setVisibility(View.GONE);
                binding.tvTitle.setVisibility(View.GONE);
                binding.smileRating.setVisibility(View.GONE);
                binding.btnTemplate.setVisibility(View.GONE);

                binding.ivTemplate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInAppMessage.getButtonURL() != null && mInAppMessage.getButtonURL().length() > 0) {

                            try {

                                Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getButtonURL()));
                                startActivity(viewIntent);

                            } catch (final ActivityNotFoundException e) {
                                Log.i("Visilabs", "User doesn't have an activity for notification URI");
                            }
                        } else {
                            Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                        }

                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                        finish();
                    }
                });

                break;

            case IMAGE_BUTTON:

                binding.smileRating.setVisibility(View.GONE);
                binding.llTextContainer.setVisibility(View.GONE);
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

        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvTitle.setTypeface(mInAppMessage.getFont_family(), Typeface.BOLD);
        binding.tvTitle.setText(mInAppMessage.getTitle().replace("\\n","\n"));
        binding.tvTitle.setTextColor(Color.parseColor(mInAppMessage.getMsg_title_color()));
        binding.tvTitle.setTextSize(Float.parseFloat(mInAppMessage.getMsg_body_textsize()) + 12);
    }

    private void setBody() {
        binding.tvBody.setText(mInAppMessage.getBody().replace("\\n","\n"));
        binding.tvBody.setTypeface(mInAppMessage.getFont_family());
        binding.tvBody.setVisibility(View.VISIBLE);
        binding.tvBody.setTextColor(Color.parseColor(mInAppMessage.getMsg_body_color()));
        binding.tvBody.setTextSize(Float.parseFloat(mInAppMessage.getMsg_body_textsize()) + 8);
    }

    private void setButton() {

        binding.btnTemplate.setTypeface(mInAppMessage.getFont_family());
        binding.btnTemplate.setVisibility(View.VISIBLE);
        binding.btnTemplate.setText(mInAppMessage.getButtonText());
        binding.btnTemplate.setTextColor(Color.parseColor(mInAppMessage.getButton_text_color()));
        binding.btnTemplate.setBackgroundColor(Color.parseColor(mInAppMessage.getButton_color()));

        binding.btnTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInAppMessage.getButtonURL() != null && mInAppMessage.getButtonURL().length() > 0) {

                    try {

                        Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getButtonURL()));
                        startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                } else {
                    Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport());
                }

                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        });
    }

    private String getRateReport() {
        switch (mInAppMessage.getType()) {
            case SMILE_RATING:
                return "&OM.s_point=" + binding.smileRating.getRating() + "&OM.s_cat=" + mInAppMessage.getType() + "&OM.s_page=act-" + mInAppMessage.getId();

            case NPS:
                return "&OM.s_point=" + binding.ratingBar.getRating() + "&OM.s_cat=" + mInAppMessage.getType() + "&OM.s_page=act-" + mInAppMessage.getId();
        }

        return "";
    }


    public void setCloseButton() {

        binding.ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);

                finish();
            }
        });
    }

    void showNps() {

        binding.ratingBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.yellow)));
        }
    }

    void showSmileRating() {
        binding.smileRating.setVisibility(View.VISIBLE);
    }

    private int getCloseIcon() {

        switch (mInAppMessage.getCloseButton()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
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
                Log.i("VL", "Bad");
                break;
            case SmileRating.GOOD:
                Log.i("VL", "Good");
                break;
            case SmileRating.GREAT:
                Log.i("VL", "Great");
                break;
            case SmileRating.OKAY:
                Log.i("VL", "Okay");
                break;
            case SmileRating.TERRIBLE:
                Log.i("VL", "Terrible");
                break;
            case SmileRating.NONE:
                Log.i("VL", "None");
                break;
        }
    }

    @Override
    public void onRatingSelected(int level, boolean reselected) {
        Log.i("VL", "Rated as: " + level + " - " + reselected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
    }
}
