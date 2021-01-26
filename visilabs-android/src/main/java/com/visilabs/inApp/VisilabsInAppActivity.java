package com.visilabs.inApp;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.Visilabs;
import com.visilabs.android.databinding.ActivityInAppFullBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.util.StringUtils;


public class VisilabsInAppActivity extends AppCompatActivity implements IVisilabs {

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    InAppMessage mInApp;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private int mIntentId = -1;
    private ActivityInAppFullBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInAppFullBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(view);

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

        mInApp = inAppNotificationState.getInAppMessage();

        setInAppData();

        clickEvents();
    }

    private void setInAppData() {


        binding.tvInAppTitle.setText(mInApp.getTitle().replace("\\n","\n"));
        binding.tvInAppSubtitle.setText(mInApp.getBody().replace("\\n","\n"));

        if (mInApp.getButtonText() != null && mInApp.getButtonText().length() > 0) {
            binding.btnInApp.setText(mInApp.getButtonText());
        }
        Picasso.get().load(mInApp.getImageUrl()).into(binding.fivInAppImage);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {

        binding.btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInApp.getButtonURL() != null && mInApp.getButtonURL().length() > 0) {

                    try {
                        Visilabs.CallAPI().trackInAppMessageClick(mInApp, null);
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInApp.getButtonURL()));
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
        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
    }

}
