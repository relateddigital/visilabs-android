package com.visilabs.scratchToWin;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visilabs.Visilabs;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityScratchToWinBinding;
import com.visilabs.mailSub.Report;
import com.visilabs.scratchToWin.model.ExtendedProps;
import com.visilabs.scratchToWin.model.ScratchToWinModel;
import com.visilabs.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScratchToWinActivity extends Activity implements ScratchToWinInterface {

    private ActivityScratchToWinBinding binding;
    private ScratchToWinModel mScratchToWinMessage;
    private static final String LOG_TAG = "ScratchToWinActivity";
    private Boolean isMailSubsForm = false;
    private ExtendedProps mExtendedProps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchToWinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.setFinishOnTouchOutside(true);

        getScratchToWinMessage();
        parseExtendedProps();
        setupInitialView();
    }

    private void getScratchToWinMessage() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra("scratch-to-win-data")) {
                mScratchToWinMessage = (ScratchToWinModel) intent.getSerializableExtra("scratch-to-win-data");
            }
        }
        if(mScratchToWinMessage == null) {
            Log.e(LOG_TAG, "Could not get the content from the server!");
            finish();
        }
    }

    private void setupInitialView() {
        setupCloseButton();
        setupScratchToWin();
        isMailSubsForm = mScratchToWinMessage.getActiondata().getMailSubscription();
        if (isMailSubsForm) {
            setupEmail();
        } else {
            removeEmailViews();
            binding.viewToBeScratched.enableScratching();
        }
    }

    private void setupCloseButton() {
        binding.closeButton.setBackgroundResource(getCloseIcon());
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupScratchToWin() {
        if (mExtendedProps.getBackgroundColor() != null &&
                !mExtendedProps.getBackgroundColor().isEmpty()) {
            binding.scratchToWinContainer.setBackgroundColor(getResources().getColor(R.color.yellow));
        }

        if(!mScratchToWinMessage.getActiondata().getImg().isEmpty()) {
            Picasso.get().load(mScratchToWinMessage.getActiondata().getImg()).into(binding.mainImage);
        }

        binding.titleText.setText(mScratchToWinMessage.getActiondata().getContentTitle().replace("\\n", "\n"));
        binding.titleText.setTextColor(Color.parseColor(mExtendedProps.getContentTitleTextColor()));
        binding.titleText.setTextSize(Float.parseFloat(mExtendedProps.getContentTitleTextSize()) + 12);
        binding.titleText.setTypeface(mExtendedProps.getContentTitleFontFamily(), Typeface.BOLD);

        binding.bodyText.setText(mScratchToWinMessage.getActiondata().getContentBody().replace("\\n", "\n"));
        binding.bodyText.setTextColor(Color.parseColor(mExtendedProps.getContentBodyTextColor()));
        binding.bodyText.setTextSize(Float.parseFloat(mExtendedProps.getContentBodyTextSize()) + 8);
        binding.bodyText.setTypeface(mExtendedProps.getContentBodyTextFontFamily());

        binding.promotionCodeText.setText(mScratchToWinMessage.getActiondata().getPromotionCode());
        binding.promotionCodeText.setTextColor(Color.parseColor(mExtendedProps.getPromoCodeTextColor()));
        binding.promotionCodeText.setTextSize(Float.parseFloat(mExtendedProps.getPromoCodeTextSize()) + 12);
        binding.promotionCodeText.setTypeface(mExtendedProps.getPromoCodeFontFamily());

        binding.copyToClipboard.setText(mScratchToWinMessage.getActiondata().getCopybuttonLabel());
        binding.copyToClipboard.setTextColor(Color.parseColor(mExtendedProps.getCopyButtonTextColor()));
        binding.copyToClipboard.setTextSize(Float.parseFloat(mExtendedProps.getCopyButtonTextSize()) + 10);
        binding.copyToClipboard.setTypeface(mExtendedProps.getCopyButtonFontFamily());
        binding.copyToClipboard.setBackgroundColor(Color.parseColor(mExtendedProps.getCopyButtonColor()));

        binding.viewToBeScratched.setColor(Color.parseColor(mScratchToWinMessage.getActiondata().getScratchColor()));

        binding.copyToClipboard.setVisibility(View.GONE);
        binding.viewToBeScratched.setContainer(binding.scratchToWinContainer);
        binding.viewToBeScratched.setListener(this);

        binding.copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", mScratchToWinMessage.getActiondata().getPromotionCode());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    private void setupEmail() {
        binding.invalidEmailMessage.setText(mScratchToWinMessage.getActiondata().getMailSubscriptionForm().getInvalidEmailMessage());
        binding.resultText.setText(mScratchToWinMessage.getActiondata().getMailSubscriptionForm().getCheckConsentMessage());

        binding.emailPermitText.setText(createHtml(mScratchToWinMessage.getActiondata().getMailSubscriptionForm().getEmailpermitText(),
                mExtendedProps.getEmailPermitTextUrl()));
        binding.emailPermitText.setTextSize(Float.parseFloat(mExtendedProps.getEmailPermitTextSize()) + 10);
        binding.emailPermitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExtendedProps.getEmailPermitTextUrl() != null &&
                        !mExtendedProps.getEmailPermitTextUrl().isEmpty()) {
                    try {
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mExtendedProps.getEmailPermitTextUrl()));
                        startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i(LOG_TAG, "Could not direct to the url entered!");
                    }
                }
            }
        });
        binding.consentText.setText(createHtml(mScratchToWinMessage.getActiondata().getMailSubscriptionForm().getConsentText(),
                mExtendedProps.getConsentTextUrl()));
        binding.consentText.setTextSize(Float.parseFloat(mExtendedProps.getConsentTextSize()) + 10);
        binding.consentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExtendedProps.getConsentTextUrl() != null &&
                        !mExtendedProps.getConsentTextUrl().isEmpty()) {
                    try {
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mExtendedProps.getConsentTextUrl()));
                        startActivity(viewIntent);

                    } catch (final ActivityNotFoundException e) {
                        Log.i(LOG_TAG, "Could not direct to the url entered!");
                    }
                }
            }
        });

        binding.emailEdit.setHint(mScratchToWinMessage.getActiondata().getMailSubscriptionForm().getPlaceholder());
        binding.saveMail.setText(mScratchToWinMessage.getActiondata().getMailSubscriptionForm().getButtonLabel());
        binding.saveMail.setTextColor(Color.parseColor(mExtendedProps.getButtonTextColor()));
        binding.saveMail.setTextSize(Float.parseFloat(mExtendedProps.getButtonTextSize()) + 10);
        binding.saveMail.setTypeface(mExtendedProps.getButtonFontFamily());
        binding.saveMail.setBackgroundColor(Color.parseColor(mExtendedProps.getButtonColor()));

        binding.emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    if (checkEmail(binding.emailEdit.getText().toString())) {
                        binding.invalidEmailMessage.setVisibility(View.GONE);
                    } else {
                        binding.invalidEmailMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.saveMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEdit.getText().toString();

                binding.invalidEmailMessage.setVisibility(View.GONE);
                binding.resultText.setVisibility(View.GONE);

                if (checkEmail(email) && checkTheBoxes()) {
                    binding.mailContainer.setVisibility(View.GONE);
                    binding.emailEdit.setVisibility(View.GONE);
                    binding.saveMail.setVisibility(View.GONE);

                    Visilabs.CallAPI().createSubsJsonRequest(mScratchToWinMessage.getActiondata().getType(),
                            mScratchToWinMessage.getActid().toString(),
                            mScratchToWinMessage.getActiondata().getAuth(), email);

                    binding.viewToBeScratched.enableScratching();
                } else {
                    if (!checkEmail(email)) {
                        binding.invalidEmailMessage.setVisibility(View.VISIBLE);
                    } else {
                        binding.resultText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private int getCloseIcon() {
        switch (mExtendedProps.getCloseButtonColor()) {
            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;
    }

    private void parseExtendedProps() {
        try {
            mExtendedProps = new Gson().fromJson(new java.net.URI(mScratchToWinMessage.getActiondata().
                    getExtendedProps()).getPath(), ExtendedProps.class);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Extended properties could not be parsed properly!");
            finish();
        }
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

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private Boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private Boolean checkTheBoxes() {
        return binding.emailPermitCheckbox.isChecked() && binding.consentCheckbox.isChecked();
    }

    private void removeEmailViews() {
        binding.invalidEmailMessage.setVisibility(View.GONE);
        binding.resultText.setVisibility(View.GONE);
        binding.mailContainer.setVisibility(View.GONE);
        binding.emailEdit.setVisibility(View.GONE);
        binding.saveMail.setVisibility(View.GONE);
    }

    @Override
    public void onScratchingComplete() {
        sendReport();
        binding.copyToClipboard.setVisibility(View.VISIBLE);
    }

    private void sendReport() {
        Report report = null;
        try {
            report = new Report();
            report.setImpression(mScratchToWinMessage.getActiondata().getReport().getImpression());
            report.setClick(mScratchToWinMessage.getActiondata().getReport().getClick());
        } catch (Exception e) {
            Log.e(LOG_TAG, "There is no report to send!");
            e.printStackTrace();
            report = null;
        }

        if(report != null) {
            Visilabs.CallAPI().trackActionClick(report);
        }
    }
}