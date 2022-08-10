package com.relateddigital.visilabs;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.ClipboardManager;
import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.relateddigital.visilabs.utils.ImageTextViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)

public class CopyToClipboardTest {

    @Before
    public void beforeTheTest() {

    }

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void runTheTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


        // Start the test application
        ActivityScenario scenario = rule.getScenario();
        ActivityScenario.launch(MainActivity.class);

        //Click on IMAGE TEXT BUTTON
        onView(withId(R.id.inApp5)).perform(scrollTo()).perform(ViewActions.click());

        //Wait a bit for the request's response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the coupon code from textView
        ImageTextViewAction textViewAction = new ImageTextViewAction();

        onView(withId(R.id.tv_coupon_code)).perform(textViewAction);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String couponCode = textViewAction.code;

        // Click on the copy-to-clipboard button

        onView(withId(R.id.ll_coupon_container)).perform(ViewActions.click());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Check if the text is copied to the clipboard correctly

        ClipboardManager clipboard = (ClipboardManager) appContext.getSystemService(CLIPBOARD_SERVICE);
        assert(clipboard.getPrimaryClip().getItemAt(0).getText().toString().equals(couponCode));

        // Close the action
        onView(withId(R.id.ib_close))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.ib_close)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.inApp5))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @After
    public void afterTheTest() {

    }
}
