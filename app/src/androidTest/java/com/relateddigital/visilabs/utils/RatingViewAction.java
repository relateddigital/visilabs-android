package com.relateddigital.visilabs.utils;

import android.view.View;
import android.widget.RatingBar;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;

public class RatingViewAction implements ViewAction {

    public float rating = 3f;


    @Override
    public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(RatingBar.class);
    }

    @Override
    public String getDescription() {
        return "Rating View Action";
    }

    @Override
    public void perform(UiController uiController, View view) {

        RatingBar ratingBar = (RatingBar) view;
        ratingBar.setRating(rating);
    }
}
