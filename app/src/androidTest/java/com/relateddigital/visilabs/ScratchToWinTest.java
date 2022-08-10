package com.relateddigital.visilabs;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.IsNot.not;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;


/**
 * This class tests the ScratchToWin action
 * If this test fails, check if the template has an email form first!!
 */
@RunWith(AndroidJUnit4.class)
public class ScratchToWinTest {

    static private String FAIL_EMAIL_ADDRESS = "test159";
    static private String SUCCESS_EMAIL_ADDRESS = "test159@g.com";

    @Before
    public void beforeTheTest(){

    }
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void runTheTest(){
        // Start the test application

        ActivityScenario scenario = rule.getScenario();
        ActivityScenario.launch(MainActivity.class);
        
        // Click on SCRATCH TO WIN button
        onView(withId(R.id.inApp12)).perform(click());

        // Wait a bit for the request's response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test invalid email address
        onView(withId(R.id.emailEdit)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEdit)).perform(typeText(FAIL_EMAIL_ADDRESS))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.save_mail)).perform(click());
        onView(withId(R.id.invalid_email_message)).check(matches(isDisplayed()));

        // Test valid email address
        onView(withId(R.id.emailEdit)).perform(clearText());
        onView(withId(R.id.emailEdit)).perform(typeText(SUCCESS_EMAIL_ADDRESS))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.save_mail)).perform(click());
        onView(withId(R.id.invalid_email_message)).check(matches(not(isDisplayed())));

        // Test email consent checkboxes
        onView(withId(R.id.email_permit_checkbox)).check(matches(isDisplayed()));
        onView(withId(R.id.consent_checkbox)).check(matches(isDisplayed()));
        onView(withId(R.id.result_text)).check(matches(isDisplayed()));
        onView(withId(R.id.email_permit_checkbox)).perform(click());
        onView(withId(R.id.save_mail)).perform(click());
        onView(withId(R.id.result_text)).check(matches(isDisplayed()));
        onView(withId(R.id.consent_checkbox)).perform(click());
        onView(withId(R.id.save_mail)).perform(click());
        onView(withId(R.id.result_text)).check(matches(not(isDisplayed())));

        // Test if email part is gone
        onView(withId(R.id.emailEdit)).check(matches(not(isDisplayed())));
        onView(withId(R.id.save_mail)).check(matches(not(isDisplayed())));

        // Test if copy-to-clipboard button is not available before scratching
        onView(withId(R.id.copy_to_clipboard)).check(matches(not(isDisplayed())));

        // Test close button
        onView(withId(R.id.close_button)).check(matches(isDisplayed()));
        onView(withId(R.id.close_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.inApp12)).check(matches(isDisplayed()));
    }







}
