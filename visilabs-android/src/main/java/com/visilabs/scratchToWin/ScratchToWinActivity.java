package com.visilabs.scratchToWin;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityScratchToWinBinding;

public class ScratchToWinActivity extends Activity implements ScratchToWinInterface {

    private ActivityScratchToWinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchToWinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //mScratchToWinMessage = getScratchToWinMessage();

        setupInitialView();
    }

    private void getScratchToWinMessage() {
        //TODO get and set message from intent extras.
    }

    private void setupInitialView() {
        setupCloseButton();
        setupScratchToWin();
        //TODO control if there is email
        //TODO change this when real data gets ready
        if (true) {
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
        //TODO When real data comes, change the code below
        //TODO if img = "", dont use Picasso -> crash
        binding.scratchToWinContainer.setBackgroundColor(getResources().getColor(R.color.yellow));
        /*
        if(!imgUrl.equals("")) {
            Picasso.get().load(imgUrl).into(binding.mainImage);
        }
        */
        Picasso.get().load("https://img.visilabs.net/in-app-message/uploaded_images/163_1100_153_20210201150721831.jpg").into(binding.mainImage);
        binding.titleText.setText("scratch-to-win title here");
        binding.bodyText.setText("scratch-to-win body text here");
        binding.promotionCodeText.setText("ABFSODAFABFA");
        binding.copyToClipboard.setText("Copy the Code");
        binding.viewToBeScratched.setColor(Color.parseColor("#000000"));


        binding.copyToClipboard.setVisibility(View.GONE);
        binding.viewToBeScratched.setContainer(binding.scratchToWinContainer);
        binding.viewToBeScratched.setListener(this);

        binding.copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promotionCode = binding.promotionCodeText.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", promotionCode);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    private void setupEmail() {
        //TODO When real data comes, change the code below
        binding.invalidEmailMessage.setText("Invalid email");
        binding.resultText.setText("Check the boxes");
        binding.emailEdit.setHint("Please, enter your email");
        //TODO: When the real data comes, add links to email texts to send browser.
        binding.emailPermitText.setText("I've read and accept Terms of Use");
        binding.consentText.setText("To read terms and conditions click here");
        binding.saveMail.setText("Save & Scratch");
        //Set the visibilities of the checkboxes and related textViews according to real data


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
                    //TODO update here when real data comes
                    /*Visilabs.CallAPI().trackMailSubscriptionFormClick(mMailSubscriptionForm.getActiondata().getReport());
                    Visilabs.CallAPI().createSubsJsonRequest(mMailSubscriptionForm.getActid(),
                        mMailSubscriptionForm.getActiondata().getAuth(), email);
                    binding.tvCheckConsentMessage.setVisibility(View.VISIBLE);
                    binding.tvCheckConsentMessage.setTextColor(Color.GREEN);
                    binding.tvCheckConsentMessage.setText(mMailSubscriptionForm.getActiondata().getSuccess_message());*/
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

        return R.drawable.ic_close_black_24dp;
        //TODO when real data comes:
       /* switch (mInAppMessage.getActionData().getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;*/
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
        binding.copyToClipboard.setVisibility(View.VISIBLE);
    }
}