package com.example.scandal;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Instrumented test class for SignedUpEventDetailsActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignedUpEventDetailsActivityTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<SignedUpEventDetailsActivity> intentsTestRule =
            new IntentsTestRule<>(SignedUpEventDetailsActivity.class);

    /**
     * Test to verify if the correct intent extras are received by the activity.
     */
    @Test
    public void testIntentExtras() {
        // Check if the correct intent extras are received by the activity
        intended(allOf(
                hasExtra(equalTo("eventName"), equalTo("YourEventNameHere"))
        ));
    }

    /**
     * Test clicking the Announcements button.
     * Verifies if the correct intent is sent to AttendeeAnnouncements.
     */
    @Test
    public void testAnnouncementsButtonIntent() {
        // Click the Announcements button
        onView(withId(R.id.buttonAnnouncements)).perform(click());

        // Verify if the correct intent is sent to AttendeeAnnouncements
        intended(allOf(
                hasComponent(AttendeeAnnouncements.class.getName()),
                hasExtra("eventName", "YourEventNameHere")
        ));
    }

    /**
     * Test clicking the Sign Out button.
     * You may want to verify additional behavior here, such as a toast message or UI changes.
     */
    @Test
    public void testSignOutButtonIntent() {
        // Click the Sign Out button
        onView(withId(R.id.buttonSignOut)).perform(click());

        // You may want to verify additional behavior here, such as a toast message or UI changes
    }
}
