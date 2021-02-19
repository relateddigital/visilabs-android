package com.visilabs.spinToWin;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.fragment.app.FragmentActivity;

import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;

public class SpinToWinActivity extends FragmentActivity implements SpinToWinCompleteInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO get the real data from intent extra
        String response = getRequestResponse();
        WebViewDialogFragment webViewDialogFragment = new WebViewDialogFragment("spintowin.html", response);
        webViewDialogFragment.display(getSupportFragmentManager());
        webViewDialogFragment.setSpinToWinCompleteListener(this);
        webViewDialogFragment.getJavaScriptInterface().setSpinToWinCompleteListener(this);
    }

    private String getRequestResponse() {
        //TODO get the real data here
        JSONObject config = new JSONObject();
        JSONObject actionData = new JSONObject();
        JSONArray slices = new JSONArray();
        JSONObject slice1 = new JSONObject();
        JSONObject slice2 = new JSONObject();
        JSONObject slice3 = new JSONObject();
        JSONObject slice4 = new JSONObject();
        JSONObject slice5 = new JSONObject();
        JSONObject spinToWinObject = new JSONObject();

        slice1.put("displayName", "Promotion 1");
        slice1.put("color", "#00bcd4");
        slice1.put("code", "null");
        slice1.put("type", "promotion");

        slice2.put("displayName", "Promotion 2");
        slice2.put("color", "#ff79ff");
        slice2.put("code", "null");
        slice2.put("type", "promotion");

        slice3.put("displayName", "Static Code 1");
        slice3.put("color", "#e4e65e");
        slice3.put("code", "CF,CE,CD,CC,CB,CA");
        slice3.put("type", "staticcode");

        slice4.put("displayName", "Static Code 2");
        slice4.put("color", "#6200ee");
        slice4.put("code", "CF,CE,CD,CC,CB,CC");
        slice4.put("type", "staticcode");

        slice5.put("displayName", "Pass");
        slice5.put("color", "#f50000");
        slice5.put("code", "");
        slice5.put("type", "pass");

        slices.put(slice1);
        slices.put(slice2);
        slices.put(slice3);
        slices.put(slice4);
        slices.put(slice5);

        actionData.put("background_color", "#eeefff");
        actionData.put("close_button_color", "#000000");
        actionData.put("msg_title_color", "#6200ee");
        actionData.put("msg_title_font_family", "Monospace");
        actionData.put("msg_title_textsize", 10);
        actionData.put("msg_body_color", "#0000ff");
        actionData.put("msg_body_font_family", "sansserif");
        actionData.put("msg_body_textsize", 5);
        actionData.put("button_color", "#ff79ff");
        actionData.put("button_text_color", "#ffffff");
        actionData.put("button_font_family", "sansserif");
        actionData.put("button_textsize", 0);
        actionData.put("consent_text_textsize", 5);
        actionData.put("promotion_code_color", "#ff79ff");
        actionData.put("promotion_code_text_color", "#ffffff");
        actionData.put("promotion_code_font_family", "sansserif");
        actionData.put("promotion_code_textsize", 0);
        actionData.put("copy_button_text", "Panoya Kopyala");
        actionData.put("copy_button_color", "#00bcd4");
        actionData.put("copy_button_text_color", "#ffffff");
        actionData.put("copy_button_font_family", "sansserif");
        actionData.put("copy_button_textsize", 0);

        actionData.put("slices", slices);

        actionData.put("view_type", "Lightbox");
        actionData.put("mail_subscription", true);

        spinToWinObject.put("title", "çArKıFeLeK");
        spinToWinObject.put("message", "Tarık Tarcan, Mehmet Ali Erbil, Dobrovski, Parmaktan Sonra");
        spinToWinObject.put("placeholder", "E-mail gir");
        spinToWinObject.put("button_label", "Start");
        spinToWinObject.put("title_style", "");
        spinToWinObject.put("message_style", "");
        spinToWinObject.put("button_style", "");
        spinToWinObject.put("consent_text", "Kullanım koşullarını okudum. Tekrar tekrar okudum");
        spinToWinObject.put("container_style", "");
        spinToWinObject.put("invalid_email_message", "E-Mail Adresini düzgün gir.");
        spinToWinObject.put("input_style", "");
        spinToWinObject.put("validation_message_style", "");
        spinToWinObject.put("consent_text_container_style", "");
        spinToWinObject.put("success_message", "Tebrikle başarı mesajı.");
        spinToWinObject.put("success_message_style", "");
        spinToWinObject.put("emailpermit_text", "E-Posta Göndermeye İzin Ver");


        actionData.put("spin_to_win_content", spinToWinObject);
        actionData.put("font_size", 20);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        actionData.put("circle_R", width / 2);

        actionData.put("auth", "3D8C108037744386D0C4BD317DE6C31E64F6BFF705349726E56B0687032C8ADA");
        actionData.put("type", "spin_to_win_email");
        actionData.put("waiting_time", 0);
        actionData.put("promoAuth", "51B3A13FE2948E3A19BC606617539A2476ADE08B493EE11F270626DD94E480A5");
        actionData.put("slice_count", "5");
        actionData.put("sendemail", false);
        actionData.put("esp_cmpid", "");
        actionData.put("courseofaction", "GetExistingPromoCode");
        actionData.put("Javascript", "");

        config.put("actid", 1);
        config.put("actiondata", actionData);

        return config.toString();
    }

    @Override
    public void onCompleted() {
        finish();
    }
}
