package com.relateddigital.visilabs;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.relateddigital.visilabs.utils.RatingViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests the NPS with Feedback Form
 * If this test fails, check if the threshold value to show the feedback form is reasonable
 */

@RunWith(AndroidJUnit4.class)
public class NpsFeedbackTest {

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

        //Click on NPS3 button
        onView(ViewMatchers.withId(R.id.inApp21)).perform(scrollTo()).perform(ViewActions.click());

        //Wait a bit for the request's response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check if first NPS popup is shown
        onView(ViewMatchers.withId(R.id.ratingBar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        RatingViewAction ratingAction = new RatingViewAction();
        ratingAction.rating =0.5f;

        //Give rating : 0.5
        onView(ViewMatchers.withId(R.id.ratingBar)).perform(ratingAction);
        onView(ViewMatchers.withId(R.id.btn_template)).perform(ViewActions.click());

        //Check if the feedback form is shown
        onView(ViewMatchers.withId(R.id.commentBox)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        //Click on the button and close
        onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());

        //Click on NPS3 button
        onView(ViewMatchers.withId(R.id.inApp21)).perform(scrollTo()).perform(ViewActions.click());

        //Wait a bit for the request's response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check if first NPS popup is shown
        onView(ViewMatchers.withId(R.id.ratingBar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        ratingAction.rating = 5f;

        //Give rating : 5
        onView(ViewMatchers.withId(R.id.ratingBar)).perform(ratingAction);
        onView(ViewMatchers.withId(R.id.btn_template)).perform(ViewActions.click());

        //Check if the feedback form is not shown
        onView(ViewMatchers.withId(R.id.inApp21)).perform(scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));


    }

    @After
    public void afterTheTest(){

    }
}
