package com.example.scandal;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test class for MainActivity.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    /**
     * Test that clicking the "Get Started" button opens the HomeActivity.
     */
    @Test
    public void clickingGetStartedButton_opensHomeActivity() {
        // Click the "Get Started" button
        onView(withId(R.id.buttonGetStarted)).perform(click());

        // Verify that the HomeActivity is launched
        intended(allOf(hasComponent(HomeActivity.class.getName())));
    }
}
