package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AnimationManager;
import com.visilabs.util.StringUtils;
import com.visilabs.view.FadingImageView;


public class VisilabsInAppActivity extends AppCompatActivity implements IVisilabs {

    InAppMessage inApp;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    TextView tvInAppSubtitle, tvInAppTitle;
    private int mIntentId = -1;

    Button btnInApp;

    FadingImageView fivInAppImage;

    LinearLayout llClose;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_in_app_full);

        tvInAppSubtitle = findViewById(R.id.tv_in_app_subtitle);
        tvInAppTitle = findViewById(R.id.tv_in_app_title);
        btnInApp = findViewById(R.id.btn_in_app);
        fivInAppImage = findViewById(R.id.fiv_in_app_image);
        llClose = findViewById(R.id.ll_close);

        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
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
    public void setUpView() {


        InAppNotificationState inAppNotificationState =
                (InAppNotificationState) mUpdateDisplayState.getDisplayState();

        inApp = inAppNotificationState.getInAppMessage();

        setInAppData();

        clickEvents();
    }

    private void setInAppData() {


        tvInAppTitle.setText(inApp.getTitle());
        tvInAppSubtitle.setText(inApp.getBody());

        if (inApp.getButtonText() != null && inApp.getButtonText().length() > 0) {
            btnInApp.setText(inApp.getButtonText());
        }
        Picasso.get().load(inApp.getImageUrl()).into(fivInAppImage);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {

        btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inApp.getButtonURL() != null && inApp.getButtonURL().length() > 0) {

                    try {
                        Visilabs.CallAPI().trackInAppMessageClick(inApp, null);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(inApp.getButtonURL()));
                        VisilabsInAppActivity.this.startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i("Visilabs", "User doesn't have an activity for notification URI");
                    }
                }
                finish();
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            }
        });


        btnInApp.setOnTouchListener(new View.OnTouchListener() {
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

        llClose.setOnClickListener(new View.OnClickListener() {
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
}
