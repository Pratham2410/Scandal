package com.example.scandal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventDetailsActivityIntentTest {
    @Rule
    public IntentsTestRule<EventDetailsActivity> intentsTestRule = new IntentsTestRule<>(EventDetailsActivity.class);

    @Test
    public void clickingBackToHomepageButtonNavigatesToHomeActivity() {
        // Perform a click on the "Back to Homepage" button
        onView(withId(R.id.button_seeQRCode)).perform(click());

        // Verify that the HomeActivity is launched after clicking the button
        intended(hasComponent(NewEventActivity.class.getName()));
    }
}
