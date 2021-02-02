package com.visilabs.scratchToWin;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityScratchToWinBinding;

public class ScratchToWinActivity extends Activity {

    private ActivityScratchToWinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchToWinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //mScratchToWinMessage = getScratchToWinMessage();

        setupInitialView();;
    }

    private void getScratchToWinMessage() {

    }

    private void setupInitialView() {

        binding.closeButton.setBackgroundResource(getCloseIcon());
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Picasso.get().load("https://img.visilabs.net/in-app-message/uploaded_images/163_1100_153_20210201150721831.jpg").into(binding.mainImage);
        binding.titleText.setText("scratch-to-win title here");
        binding.promotionCodeText.setText("FSODAFOVK");

        binding.copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promotionCode = binding.promotionCodeText.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", promotionCode);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getCloseIcon() {

        return R.drawable.ic_close_black_24dp;
        //TODO use below when backend side gets ready
       /* switch (mInAppMessage.getActionData().getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;*/
    }
}