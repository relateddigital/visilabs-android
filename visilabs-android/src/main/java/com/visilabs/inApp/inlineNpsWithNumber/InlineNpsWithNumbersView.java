package com.visilabs.inApp.inlineNpsWithNumber;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.android.R;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.api.VisilabsUpdateDisplayState;
import com.visilabs.inApp.InAppActionType;
import com.visilabs.inApp.InAppButtonInterface;
import com.visilabs.inApp.InAppMessage;
import com.visilabs.inApp.VisilabsActionRequest;
import com.visilabs.util.ActivityUtils;
import com.visilabs.util.AppUtils;
import com.visilabs.util.StringUtils;
import com.visilabs.view.NpsWithNumbersView;

import java.util.HashMap;

public class InlineNpsWithNumbersView extends LinearLayout {
    private static final String TAG = "NpsWithNumbersView";
    private static final String LOG_TAG = "NpsWithNumbersView";
    private int mIntentId = -1;
    private InAppButtonInterface buttonCallback = null;
    Context mContext;
    NpsItemClickListener mNpsItemClickListener;


    public InlineNpsWithNumbersView(Context context) {
        super(context);
        init();
    }

    public InlineNpsWithNumbersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InlineNpsWithNumbersView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.inline_nps_with_numbers, this, true);


    }


    public void setNpsWithNumberAction(Context context, HashMap<String, String> properties, NpsItemClickListener npsItemClickListener, Activity parent) {

        if (Visilabs.CallAPI().isBlocked()) {
            Log.w(TAG, "Too much server load, ignoring the request!");
            return;
        }
        mNpsItemClickListener = npsItemClickListener;
        VisilabsActionRequest visilabsActionRequest;
        try {
            visilabsActionRequest = Visilabs.CallAPI().requestNpsAction(properties);
            visilabsActionRequest.executeAsyncNpsWithNumbersAction(getVisilabsNpsCallback(context, null));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public VisilabsCallback getVisilabsNpsCallback(final Context context,
                                                   final NpsRequestListener npsRequestListener) {


        return new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                if (npsRequestListener != null) {
                    npsRequestListener.onRequestResult(true);
                }
                try {

                    InAppMessage[] mInAppMessages =
                            new Gson().fromJson(response.getRawResponse(), InAppMessage[].class);

                    InAppMessage mInAppMessage = null;
                    if (mInAppMessages.length > 0) {
                        mInAppMessage = mInAppMessages[0];
                    }
                    if (mInAppMessage.getActionData().getDisplayType().equals("inline")) {
                        LinearLayout llback = findViewById(R.id.ll_back);
                        llback.setVisibility(View.VISIBLE);

                        setImage(mInAppMessage.getActionData().getImg());
                        setTitle(mInAppMessage.getActionData().getMsgTitle(), mInAppMessage.getActionData().getBackground(), mInAppMessage.getActionData().getFontFamily(mContext), mInAppMessage.getActionData().getMsgTitleColor(), mInAppMessage.getActionData().getMsgTitleTextSize());
                        setBody(mInAppMessage.getActionData().getMsgBody(), mInAppMessage.getActionData().getBackground(), mInAppMessage.getActionData().getFontFamily(mContext), mInAppMessage.getActionData().getMsgBodyColor(), mInAppMessage.getActionData().getMsgBodyTextSize());
                        setButton(mInAppMessage.getActionData().getBtnText(), mInAppMessage.getActionData().getButtonColor(), mInAppMessage.getActionData().getFontFamily(mContext), mInAppMessage.getActionData().getButtonTextColor(), mInAppMessage.getActionData().getMsgType(), mInAppMessage.getActId(), mInAppMessage);
                        setTemplate(mInAppMessage.getActionData().getBackground());
                        showNpsWithNumbers(mInAppMessage.getActionData().getNumberColors());
                    }

                } catch (Exception ex) {

                    Log.e(TAG, ex.getMessage(), ex);
                    if (npsRequestListener != null) {
                        npsRequestListener.onRequestResult(false);
                    }
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                Log.e(TAG, response.getRawResponse());
                if (npsRequestListener != null) {
                    npsRequestListener.onRequestResult(false);
                }
            }
        };
    }

    void showNpsWithNumbers(String[] colorNumber) {

        NpsWithNumbersView npsWithNumbersView = findViewById(R.id.npsWithNumbersView);
        npsWithNumbersView.setVisibility(View.VISIBLE);
        int[] colors = new int[colorNumber.length];

        for (int i = 0; i < colorNumber.length; i++) {
            colors[i] = Color.parseColor(colorNumber[i]);
        }
        npsWithNumbersView.setColors(colors);
    }

    public void setTemplate(String backgroundColor) {
        LinearLayout llBack = findViewById(R.id.ll_back);

        if (backgroundColor != null && backgroundColor.equals("")) {
            try {
                llBack.setBackgroundColor(Color.parseColor(backgroundColor));
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for background color\nSetting the default value.");
                e.printStackTrace();
            }
        }
    }

    public void setTitle(String title, String backgroundColor, Typeface fontFamily, String titleColor, String textSize) {
        TextView tvTitle = findViewById(R.id.tv_title);
        if (title.equals("") ||
                title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            if (backgroundColor != null &&
                    !backgroundColor.equals("")) {
                tvTitle.setBackgroundColor(Color.parseColor(backgroundColor));
            }
            tvTitle.setText(title.replace("\\n", "\n"));
            tvTitle.setTypeface(fontFamily);
            tvTitle.setVisibility(View.VISIBLE);

            if (titleColor != null && !titleColor.equals("")) {
                try {
                    tvTitle.setTextColor(Color.parseColor(titleColor));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for message body color\nSetting the default value.");
                    e.printStackTrace();
                    tvTitle.setTextColor(getResources().getColor(R.color.blue));
                }
            } else {
                tvTitle.setTextColor(getResources().getColor(R.color.blue));
            }
            try {
                tvTitle.setTextSize(Float.parseFloat(textSize) + 8);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for message body text size\nSetting the default value.");
                e.printStackTrace();
                tvTitle.setTextSize(12);
            }

        }


    }


    public void setBody(String body, String backgroundColor, Typeface fontFamily, String bodyColor, String textSize) {
        TextView tvBody = findViewById(R.id.tv_body);
        if (body.equals("") ||
                body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            if (backgroundColor != null &&
                    !backgroundColor.equals("")) {
                tvBody.setBackgroundColor(Color.parseColor(backgroundColor));
            }
            tvBody.setText(body.replace("\\n", "\n"));
            tvBody.setTypeface(fontFamily);
            tvBody.setVisibility(View.VISIBLE);

            if (bodyColor != null && !bodyColor.equals("")) {
                try {
                    tvBody.setTextColor(Color.parseColor(bodyColor));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for message body color\nSetting the default value.");
                    e.printStackTrace();
                }
            }
            try {
                tvBody.setTextSize(Float.parseFloat(textSize) + 8);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Could not parse the data given for message body text size\nSetting the default value.");
                e.printStackTrace();
                tvBody.setTextSize(12);
            }

        }
    }

    public void setButton(String btnText, String buttonColor, Typeface fontFamily, String buttonTextColor, InAppActionType msgType, Integer actId, InAppMessage mInAppMessage) {
        Button btnTemplate = findViewById(R.id.btn_template);
        btnTemplate.setVisibility(View.VISIBLE);
        if (btnText.equals("") ||
                btnText == null) {
            btnTemplate.setVisibility(View.GONE);
        } else {
            btnTemplate.setText(btnText);
            btnTemplate.setTypeface(fontFamily);
            btnTemplate.setVisibility(View.VISIBLE);
            if (buttonTextColor != null && !buttonTextColor.equals("")) {
                try {
                    btnTemplate.setTextColor(Color.parseColor(buttonTextColor));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for message body color\nSetting the default value.");
                    e.printStackTrace();
                }
            } else {
                btnTemplate.setTextColor(getResources().getColor(R.color.black));
            }
            if (buttonColor != null && !buttonColor.equals("")) {
                try {
                    btnTemplate.setBackgroundColor(Color.parseColor(buttonColor));
                } catch (Exception e) {
                    Log.w(LOG_TAG, "Could not parse the data given for button color\nSetting the default value.");
                    e.printStackTrace();
                }
            }

            btnTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRatingEntered()) {
                        Visilabs.CallAPI().trackInAppMessageClick(mInAppMessage, getRateReport(msgType, actId));
                        if (buttonCallback != null) {
                            Visilabs.CallAPI().setInAppButtonInterface(null);
                            buttonCallback.onPress(mInAppMessage.getActionData().getAndroidLnk());
                        } else {
                            if (mInAppMessage.getActionData().getAndroidLnk() != null && mInAppMessage.getActionData().getAndroidLnk().length() > 0) {
                                try {
                                    if(mNpsItemClickListener != null && mInAppMessage.getActionData().getAndroidLnk() != null) {
                                        mNpsItemClickListener.npsItemClicked(mInAppMessage.getActionData().getAndroidLnk());
                                    }
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.getURIfromUrlString(mInAppMessage.getActionData().getAndroidLnk()));
                                    getContext().startActivity(viewIntent);

                                } catch (final ActivityNotFoundException e) {
                                    Log.i("Visilabs", "User doesn't have an activity for notification URI");
                                }
                            }
                        }
                        VisilabsUpdateDisplayState.releaseDisplayState(mIntentId);
                    }

                }
            });

        }
    }

    private boolean isRatingEntered() {
        boolean result = false;
        NpsWithNumbersView npsWithNumbersView = findViewById(R.id.npsWithNumbersView);
        if (npsWithNumbersView.getSelectedRate() != 0) {
            result = true;
        }

        return result;
    }

    private String getRateReport(InAppActionType msgType, Integer actId) {
        NpsWithNumbersView npsWithNumbersView = findViewById(R.id.npsWithNumbersView);
        switch (msgType) {

            case NPS_WITH_NUMBERS:
                return "OM.s_point=" + npsWithNumbersView.getSelectedRate() + "&OM.s_cat=" + msgType + "&OM.s_page=act-" + actId;
        }

        return "";
    }

    public void setImage(String imageResId) {

        ImageView ivTemplate = findViewById(R.id.iv_template);
        if (imageResId.equals("") ||
                imageResId == null) {
            ivTemplate.setVisibility(View.GONE);
        } else {
            if (AppUtils.isAnImage(imageResId)) {
                Picasso.get().load(imageResId).into(ivTemplate);
            } else {
                Glide.with(this)
                        .load(imageResId)
                        .into(ivTemplate);
            }
        }

    }

}
