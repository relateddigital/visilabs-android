package com.relateddigital.visilabs.utils;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;

public class ImageTextViewAction implements ViewAction {

    public String code = "";


    @Override
    public Matcher<View> getConstraints() {
       return ViewMatchers.isAssignableFrom(TextView.class);
    }

    @Override
    public String getDescription() {
        return "Image Text Button View Action";
    }

    @Override
    public void perform(UiController uiController, View view) {

        TextView text = (TextView) view;
        code = text.getText().toString();
    }
}
