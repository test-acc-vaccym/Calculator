package com.wingoku.calculator;

import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // register IdlingResource used by MainActivity.java to enable syncing with Espresso for hand-made threads. For more info regarding
        // idling resource: https://developer.android.com/reference/android/support/test/espresso/IdlingResource.html
        CountingIdlingResource mainActivityIdlingResource = mActivityRule.getActivity().getMainActivityEspressoIdlingResource();
        registerIdlingResources(mainActivityIdlingResource);

        testDivisionByZeroError();
        testResultForArithematicSignOnly();
        testResultForEquationWithArithematicSignAtTrail();
        testFloatingPointNumbersAddition();
        testDeleteLongPress();
    }

    private void testDeleteLongPress() {
        resetTextView();
        onView(withId(R.id.tv_expression)).check(matches(withText(R.string.string_empty)));
        onView(withId(R.id.tv_result)).check(matches(withText(R.string.string_empty)));
    }

    private void testDivisionByZeroError() {
        onView(withId(R.id.button_1)).perform(click());
        onView(withId(R.id.button_divide)).perform(click());
        onView(withId(R.id.button_0)).perform(click());
        onView(withId(R.id.button_equal)).perform(click());
        onView(withId(R.id.tv_result)).check(matches(withText(R.string.string_bad_expression)));
        resetTextView();
    }

    private void testResultForEquationWithArithematicSignAtTrail() {
        onView(withId(R.id.button_1)).perform(click());
        onView(withId(R.id.button_plus)).perform(click());
        onView(withId(R.id.button_equal)).perform(click());
        onView(withId(R.id.tv_result)).check(matches(withText(R.string.string_bad_expression)));

        resetTextView();
    }

    private void testResultForArithematicSignOnly() {
        onView(withId(R.id.button_multiply)).perform(click());
        onView(withId(R.id.tv_expression)).check(matches(withText(R.string.string_multiply)));
        onView(withId(R.id.button_equal)).perform(click());
        onView(withId(R.id.tv_result)).check(matches(withText(R.string.string_bad_expression)));

        resetTextView();
    }

    private void testFloatingPointNumbersAddition() {
        String resultString = "8.7";

        onView(withId(R.id.button_3)).perform(click());
        onView(withId(R.id.button_decimal)).perform(click());
        onView(withId(R.id.button_2)).perform(click());

        onView(withId(R.id.button_plus)).perform(click());

        onView(withId(R.id.button_5)).perform(click());
        onView(withId(R.id.button_decimal)).perform(click());
        onView(withId(R.id.button_5)).perform(click());

        onView(withId(R.id.tv_result)).check(matches(withText(resultString)));
        onView(withId(R.id.button_equal)).perform(click());
        onView(withId(R.id.tv_expression)).check(matches(withText(resultString)));

        resetTextView();
    }

    private void resetTextView() {
        onView(withId(R.id.button_delete)).perform(longClick());
    }
}
