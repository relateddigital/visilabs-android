package com.visilabs.mailSub;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.visilabs.InAppNotificationState;
import com.visilabs.android.R;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.inApp.FontFamily;
import com.visilabs.inApp.InAppMessage;

import java.net.URISyntaxException;

public class MailSubscriptionFormActivity extends AppCompatActivity {

    MailSubscriptionForm mailSubscriptionForm;
    ExtendedProps extendedProps;

    private VisilabsUpdateDisplayState mUpdateDisplayState;

    private int mIntentId = -1;

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    LinearLayout llTextContainer;
    ImageButton ibClose;
    TextView tvBody, tvTitle;
    EditText etEmail;
    CheckBox cbEmailPermit;
    TextView tvEmailPermit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        mailSubscriptionForm = getMailSubscriptionForm();
        try {
            extendedProps = new Gson().fromJson(new java.net.URI(mailSubscriptionForm.getActiondata().getExtendedProps()).getPath(), ExtendedProps.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        setContentView( R.layout.activity_mail_subscription_form);
        this.setFinishOnTouchOutside(false);


        llTextContainer = findViewById(R.id.ll_text_container);
        ibClose = findViewById(R.id.ib_close);
        tvBody = findViewById(R.id.tv_body);
        tvTitle = findViewById(R.id.tv_title);
        etEmail = findViewById(R.id.et_email);
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        cbEmailPermit = findViewById(R.id.cb_email_permit);
        tvEmailPermit = findViewById(R.id.tv_email_permit);
        tvEmailPermit.setMovementMethod(LinkMovementMethod.getInstance());



        if (isShowingInApp() && mailSubscriptionForm != null) {
            setUpView();
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        }

        /*


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

    private void setUpView() {
        llTextContainer.setBackgroundColor(Color.parseColor(extendedProps.getBackground_color()));
        setCloseButton();
        setTitle();
        setBody();
        setEmail();
        setCheckBoxes();
    }

    private void setTitle() {
        tvTitle.setTypeface(getFont_family(extendedProps.getTitle_font_family()), Typeface.BOLD);
        tvTitle.setText(mailSubscriptionForm.getActiondata().getTitle());
        tvTitle.setTextColor(Color.parseColor(extendedProps.getTitle_text_color()));
        tvTitle.setTextSize(Float.parseFloat(extendedProps.getTitle_text_size()) + 12);
    }

    private void setBody() {
        tvBody.setText(mailSubscriptionForm.getActiondata().getMessage());
        tvBody.setTypeface(getFont_family(extendedProps.getText_font_family()));
        tvBody.setTextColor(Color.parseColor(extendedProps.getText_color()));
        tvBody.setTextSize(Float.parseFloat(extendedProps.getText_size()) + 8);
    }

    private void setEmail() {
        etEmail.setHint(mailSubscriptionForm.getActiondata().getPlaceholder());
    }

    private void setCheckBoxes() {
        tvEmailPermit.setText(Html.fromHtml("Don't have account? <a href=\"https://visilabs.com\">Register</a> here."));
        //tvEmailPermit.setText(mailSubscriptionForm.getActiondata().getEmailpermit_text());
        tvEmailPermit.setTextSize(Float.parseFloat(extendedProps.getEmailpermit_text_size()) + 8);
    }

    //Email Permit TextEmail Permit <LINK>TextEmail</LINK> Permit Text
    private Spanned createHtml(String text, String url) {
        String[] textParts = text.split("LINK");

        return Html.fromHtml("Don't have account? <a href=\"https://visilabs.com\">Register</a> here.");
    }

    private Typeface getFont_family(String font_family) {
        if (font_family == null) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansaSerif.toString().equals(font_family.toLowerCase())) {
            return Typeface.SANS_SERIF;
        }
        if (FontFamily.Serif.toString().equals(font_family.toLowerCase())) {
            return Typeface.SERIF;
        }
        if (FontFamily.Default.toString().equals(font_family.toLowerCase())) {
            return Typeface.DEFAULT;
        }
        return Typeface.DEFAULT;
    }

    public void setCloseButton() {
        ibClose.setBackgroundResource(getCloseIcon());
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        });
    }

    private int getCloseIcon() {
        switch (extendedProps.getClose_button_color()) {
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
        return InAppNotificationState.TYPE.equals(mUpdateDisplayState.getDisplayState().getType());
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
        finish();
    }

    private MailSubscriptionForm getMailSubscriptionForm() {
        InAppNotificationState inAppNotificationState;
        mUpdateDisplayState = VisilabsUpdateDisplayState.claimDisplayState(mIntentId);
        if (mUpdateDisplayState == null || mUpdateDisplayState.getDisplayState() == null) {
            Log.e("Visilabs", "VisilabsNotificationActivity intent received, but nothing was found to show.");
            return null;
        } else {
            inAppNotificationState = (InAppNotificationState) mUpdateDisplayState.getDisplayState();
            return inAppNotificationState.getMailSubscriptionForm();
        }
    }

}
