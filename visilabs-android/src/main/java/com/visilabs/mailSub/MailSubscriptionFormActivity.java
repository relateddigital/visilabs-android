package com.visilabs.mailSub;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.visilabs.android.R;
import com.visilabs.api.VisilabsUpdateDisplayState;

public class MailSubscriptionFormActivity extends AppCompatActivity {

    MailSubscriptionForm mailSubscriptionForm;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    ImageButton ibClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);

        /*
        inAppMessage = getInAppMessage();

        setContentView( R.layout.activity_mail_subscription_form);
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

         */
    }
}
