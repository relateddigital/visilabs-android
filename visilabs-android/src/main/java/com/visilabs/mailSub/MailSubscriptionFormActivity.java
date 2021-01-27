package com.visilabs.mailSub;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.gson.Gson;
import com.visilabs.InAppNotificationState;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityMailSubscriptionFormBinding;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.inApp.FontFamily;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailSubscriptionFormActivity extends Activity {

    public static final String INTENT_ID_KEY = "INTENT_ID_KEY";

    MailSubscriptionForm mMailSubscriptionForm;
    ExtendedProps mExtendedProps;
    private VisilabsUpdateDisplayState mUpdateDisplayState;
    private int mIntentId = -1;
    private ActivityMailSubscriptionFormBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMailSubscriptionFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mIntentId = getIntent().getIntExtra(INTENT_ID_KEY, Integer.MAX_VALUE);
        mMailSubscriptionForm = getMailSubscriptionForm();
        try {
            mExtendedProps = new Gson().fromJson(new java.net.URI(mMailSubscriptionForm.
                    getActiondata().getExtendedProps()).getPath(), ExtendedProps.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        setContentView(view);
        this.setFinishOnTouchOutside(false);

        binding.etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    if(checkEmail(binding.etEmail.getText().toString())) {
                        binding.tvInvalidEmailMessage.setVisibility(View.GONE);
                    } else {
                        binding.tvInvalidEmailMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.tvEmailPermit.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvConsent.setMovementMethod(LinkMovementMethod.getInstance());

        if (isShowingInApp() && mMailSubscriptionForm != null) {
            setUpView();
        } else {
            VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
            finish();
        }
    }

    private void setUpView() {
        binding.llTextContainer.setBackgroundColor(Color.parseColor(mExtendedProps.getBackground_color()));
        setCloseButton();
        setTitle();
        setBody();
        setEmail();
        setInvalidEmailMessage();
        setCheckBoxes();
        setCheckConsentMessage();
        setButton();
    }

    public void setCloseButton() {
        binding.ibClose.setBackgroundResource(getCloseIcon());
        binding.ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                finish();
            }
        });
    }

    private void setTitle() {
        binding.tvTitle.setTypeface(getFont_family(mExtendedProps.getTitle_font_family()), Typeface.BOLD);
        binding.tvTitle.setText(mMailSubscriptionForm.getActiondata().getTitle().replace("\\n","\n"));
        binding.tvTitle.setTextColor(Color.parseColor(mExtendedProps.getTitle_text_color()));
        binding.tvTitle.setTextSize(Float.parseFloat(mExtendedProps.getTitle_text_size()) + 12);
    }

    private void setBody() {
        binding.tvBody.setText(mMailSubscriptionForm.getActiondata().getMessage().replace("\\n","\n"));
        binding.tvBody.setTypeface(getFont_family(mExtendedProps.getText_font_family()));
        binding.tvBody.setTextColor(Color.parseColor(mExtendedProps.getText_color()));
        binding.tvBody.setTextSize(Float.parseFloat(mExtendedProps.getText_size()) + 8);
    }

    private void setEmail() {
        binding.etEmail.setHint(mMailSubscriptionForm.getActiondata().getPlaceholder());
    }

    private void setInvalidEmailMessage(){
        binding.tvInvalidEmailMessage.setText(mMailSubscriptionForm.getActiondata().getInvalid_email_message());
        binding.tvInvalidEmailMessage.setTextSize(Float.parseFloat(mExtendedProps.getText_size()) + 8);
        binding.tvInvalidEmailMessage.setTextColor(Color.RED);
    }

    private void setCheckBoxes() {
        if (mMailSubscriptionForm.getActiondata().getEmailpermit_text() == null || mMailSubscriptionForm
                .getActiondata().getEmailpermit_text().isEmpty()) {
            binding.llEmailPermit.setVisibility(View.GONE);
        } else {
            binding.tvEmailPermit.setText(createHtml(mMailSubscriptionForm.getActiondata()
                    .getEmailpermit_text(), mExtendedProps.getEmailpermit_text_url() ));
            binding.tvEmailPermit.setTextSize(Float.parseFloat(mExtendedProps.getEmailpermit_text_size()) + 8);
        }
        if (mMailSubscriptionForm.getActiondata().getConsent_text() == null || mMailSubscriptionForm
                .getActiondata().getConsent_text().isEmpty()) {
            binding.llConsent.setVisibility(View.GONE);
        } else {
            binding.tvConsent.setText(createHtml(mMailSubscriptionForm.getActiondata().getConsent_text(),
                    mExtendedProps.getConsent_text_url() ));
            binding.tvConsent.setTextSize(Float.parseFloat(mExtendedProps.getConsent_text_size()) + 8);
        }
    }

    private void setCheckConsentMessage(){
        binding.tvCheckConsentMessage.setText(mMailSubscriptionForm.getActiondata().getCheck_consent_message());
        binding.tvCheckConsentMessage.setTextSize(Float.parseFloat(mExtendedProps.getText_size()) + 8);
        binding.tvCheckConsentMessage.setTextColor(Color.RED);
    }

    private void setButton() {
        binding.btn.setText(mMailSubscriptionForm.getActiondata().getButton_label());
        binding.btn.setTypeface(getFont_family(mExtendedProps.getButton_font_family()));
        binding.btn.setTextColor(Color.parseColor(mExtendedProps.getButton_text_color()));
        binding.btn.setBackgroundColor(Color.parseColor(mExtendedProps.getButton_color()));
        binding.btn.setTextSize(Float.parseFloat(mExtendedProps.getButton_text_size()) + 8);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.etEmail.getText().toString();

                if(checkEmail(email)) {
                    binding.tvInvalidEmailMessage.setVisibility(View.GONE);
                } else {
                    binding.tvInvalidEmailMessage.setVisibility(View.VISIBLE);
                    return;
                }

                if(!checkCheckBoxes()) {
                    return;
                }

                Visilabs.CallAPI().trackMailSubscriptionFormClick(mMailSubscriptionForm.getActiondata().getReport());

                Visilabs.CallAPI().createSubsJsonRequest(mMailSubscriptionForm.getActid(),
                        mMailSubscriptionForm.getActiondata().getAuth(), email);

                binding.tvCheckConsentMessage.setVisibility(View.VISIBLE);
                binding.tvCheckConsentMessage.setTextColor(Color.GREEN);
                binding.tvCheckConsentMessage.setText(mMailSubscriptionForm.getActiondata().getSuccess_message());


                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                        finish();
                    }
                }, 1000);


            }
        });
    }

    private Boolean checkCheckBoxes() {
        boolean isCheckboxesOk = true;

        if(binding.llEmailPermit.getVisibility() != View.GONE){
            if(!binding.cbEmailPermit.isChecked()) {
                isCheckboxesOk = false;
                binding.tvCheckConsentMessage.setVisibility(View.VISIBLE);
                return isCheckboxesOk;
            } else {
                isCheckboxesOk = true;
                binding.tvCheckConsentMessage.setVisibility(View.GONE);
            }
        }
        if(binding.llConsent.getVisibility() != View.GONE){
            if(!binding.cbConsent.isChecked()) {
                isCheckboxesOk = false;
                binding.tvCheckConsentMessage.setVisibility(View.VISIBLE);
                return isCheckboxesOk;
            } else {
                isCheckboxesOk = true;
                binding.tvCheckConsentMessage.setVisibility(View.GONE);
            }
        }

        return isCheckboxesOk;
    }

    private Boolean checkEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Email Permit TextEmail Permit <LINK>TextEmail</LINK> Permit Text
    private Spanned createHtml(String text, String url) {
        if(url == null || url.isEmpty() || !Patterns.WEB_URL.matcher(url).matches()){
            return Html.fromHtml(url.replace("<LINK>", "").replace("</LINK>", ""));
        }
        Pattern pattern = Pattern.compile("<LINK>(.+?)</LINK>");
        Matcher matcher = pattern.matcher(text);
        boolean linkMatched = false;
        while (matcher.find()) {
            linkMatched = true;
            String outerHtml = matcher.group(0);
            String innerText = matcher.group(1);
            String s = "<a href=\"" + url + "\">" + innerText +  "</a>";
            text = text.replace(outerHtml, s);
        }
        if(!linkMatched) {
            text = "<a href=\"" + url + "\">" + text +  "</a>";
        }
        return Html.fromHtml(text);
    }

    private Typeface getFont_family(String font_family) {
        if (font_family == null) {
            return Typeface.DEFAULT;
        }
        if (FontFamily.Monospace.toString().equals(font_family.toLowerCase())) {
            return Typeface.MONOSPACE;
        }
        if (FontFamily.SansSerif.toString().equals(font_family.toLowerCase())) {
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



    private int getCloseIcon() {
        switch (mExtendedProps.getClose_button_color()) {
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
