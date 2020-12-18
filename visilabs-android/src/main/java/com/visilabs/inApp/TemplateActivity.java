package com.visilabs.inApp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.StringUtils;
import com.visilabs.view.BaseRating;
import com.visilabs.view.SmileRating;

public class TemplateActivity extends AppCompatActivity implements SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {

    InAppMessage inAppMessage;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    ImageView ivTemplate;

    SmileRating smileRating;

    LinearLayout llTextContainer;

    ImageButton ibClose;

    RatingBar ratingBar;

    TextView tvBody, tvTitle;

    Button btnTemplate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);

        inAppMessage = getInAppMessage();

        setContentView( R.layout.activity_template);
        this.setFinishOnTouchOutside(false);

        ratingBar = findViewById(R.id.ratingBar);
        tvBody = findViewById(R.id.tv_body);
        tvTitle = findViewById(R.id.tv_title);
        btnTemplate = findViewById(R.id.btn_template);
        smileRating = findViewById(R.id.smileRating);
        ivTemplate = findViewById(R.id.iv_template);
        llTextContainer = findViewById(R.id.ll_text_container);
        ibClose = findViewById(R.id.ib_close);


        if (isShowingInApp() && inAppMessage != null) {
            setUpView();
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        }
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
        Picasso.get().load(inAppMessage.getImageUrl()).into(ivTemplate);

        smileRating.setOnSmileySelectionListener(this);
        smileRating.setOnRatingSelectedListener(this);

        setCloseButton();

        setTemplate();
    }

    private void setTemplate() {

       // llOverlay.setBackgroundColor(Color.parseColor(inAppMessage.getBackground()));
        ibClose.setBackgroundResource(getCloseIcon());

        switch (inAppMessage.getType()) {

            case IMAGE_TEXT_BUTTON:

                setTitle();
                setBody();
                setButton();
                ratingBar.setVisibility(View.GONE);
                smileRating.setVisibility(View.GONE);

                break;

            case FULL_IMAGE:

                tvBody.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                smileRating.setVisibility(View.GONE);
                btnTemplate.setVisibility(View.GONE);

                ivTemplate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inAppMessage.getButtonURL() != null && inAppMessage.getButtonURL().length() > 0) {

                            try {

                                Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, getRateReport());
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(inAppMessage.getButtonURL()));
                                startActivity(viewIntent);

                            } catch (final ActivityNotFoundException e) {
                                Log.i("Visilabs", "User doesn't have an activity for notification URI");
                            }
                        } else {
                            Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, getRateReport());
                        }

                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                        finish();
                    }
                });

                break;

            case IMAGE_BUTTON:

                smileRating.setVisibility(View.GONE);
                llTextContainer.setVisibility(View.GONE);
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

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTypeface(inAppMessage.getFont_family(), Typeface.BOLD);
        tvTitle.setText(inAppMessage.getTitle().replace("\\n","\n"));
        tvTitle.setTextColor(Color.parseColor(inAppMessage.getMsg_title_color()));
        tvTitle.setTextSize(Float.parseFloat(inAppMessage.getMsg_body_textsize()) + 12);
    }

    private void setBody() {
        tvBody.setText(inAppMessage.getBody().replace("\\n","\n"));
        tvBody.setTypeface(inAppMessage.getFont_family());
        tvBody.setVisibility(View.VISIBLE);
        tvBody.setTextColor(Color.parseColor(inAppMessage.getMsg_body_color()));
        tvBody.setTextSize(Float.parseFloat(inAppMessage.getMsg_body_textsize()) + 8);
    }

    private void setButton() {

        btnTemplate.setTypeface(inAppMessage.getFont_family());
        btnTemplate.setVisibility(View.VISIBLE);
        btnTemplate.setText(inAppMessage.getButtonText());
        btnTemplate.setTextColor(Color.parseColor(inAppMessage.getButton_text_color()));
        btnTemplate.setBackgroundColor(Color.parseColor(inAppMessage.getButton_color()));

        btnTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inAppMessage.getButtonURL() != null && inAppMessage.getButtonURL().length() > 0) {

                    try {

                        Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, getRateReport());
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(inAppMessage.getButtonURL()));
                        startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                } else {
                    Visilabs.CallAPI().trackInAppMessageClick(inAppMessage, getRateReport());
                }

                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        });
    }

    private String getRateReport() {
        switch (inAppMessage.getType()) {
            case SMILE_RATING:
                return "&OM.s_point=" + smileRating.getRating() + "&OM.s_cat=" + inAppMessage.getType() + "&OM.s_page=act-" + inAppMessage.getId();

            case NPS:
                return "&OM.s_point=" + ratingBar.getRating() + "&OM.s_cat=" + inAppMessage.getType() + "&OM.s_page=act-" + inAppMessage.getId();
        }

        return "";
    }


    public void setCloseButton() {

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);

                finish();
            }
        });
    }

    void showNps() {

        ratingBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.yellow)));
        }
    }

    void showSmileRating() {
        smileRating.setVisibility(View.VISIBLE);
    }

    private int getCloseIcon() {

        switch (inAppMessage.getCloseButton()) {

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
}
