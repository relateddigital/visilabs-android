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
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.ActivityInAppFullBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.AnimationManager;
import com.visilabs.util.StringUtils;


public class VisilabsInAppActivity extends AppCompatActivity implements IVisilabs {

    ActivityInAppFullBinding mainBinding;

    InAppMessage inApp;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    private static final int SHADOW_SIZE_THRESHOLD_PX = 100;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

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

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_in_app_full);

   //     AnimationManager.setBackgroundGradient(mainBinding.ivNotDisplay, AnimationManager.getGradient(mainBinding.llClose, this));

        mainBinding.fivInAppImage.setBackgroundResource(R.drawable.bg_square_dropshadow);

        setAnimationToViews();

        setInAppData();

        clickEvents();
    }

    private void setInAppData() {

        mainBinding.tvInAppTitle.setText(inApp.getTitle());
        mainBinding.tvInAppSubtitle.setText(inApp.getBody());

        if (inApp.getButtonText() != null && inApp.getButtonText().length() > 0) {
            mainBinding.btnInApp.setText(inApp.getButtonText());
        }
        setInAppImage();
      }

    private void setInAppImage() {

        new RetrieveImageTask(new AsyncResponse() {
            @Override
            public void processFinish(Bitmap output) {
                if (output.getWidth() < SHADOW_SIZE_THRESHOLD_PX || output.getHeight() < SHADOW_SIZE_THRESHOLD_PX) { mainBinding.fivInAppImage.setBackgroundResource(R.drawable.bg_square_nodropshadow);
                } else {
                    AnimationManager.setNoDropShadowBackgroundToView(mainBinding.fivInAppImage, output);

                    ImageView iv = (ImageView) mainBinding.fivInAppImage;
                    iv.setImageBitmap(output);
                }            }
        }).execute(inApp);

    }

    private void setAnimationToViews() {

        mainBinding.fivInAppImage.startAnimation(AnimationManager.getScaleAnimation());
        mainBinding.tvInAppTitle.startAnimation(AnimationManager.getTranslateAnimation());
        mainBinding.tvInAppSubtitle.startAnimation(AnimationManager.getTranslateAnimation());
        mainBinding.btnInApp.startAnimation(AnimationManager.getTranslateAnimation());
        mainBinding.llClose.startAnimation(AnimationManager.getFadeInAnimation(this));

    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {

        mainBinding.btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inApp.getButtonURL() != null && inApp.getButtonURL().length() > 0) {

                    try {
                        Visilabs.CallAPI().trackInAppMessageClick(inApp);
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


        mainBinding.btnInApp.setOnTouchListener(new View.OnTouchListener() {
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

        mainBinding.llClose.setOnClickListener(new View.OnClickListener() {
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
