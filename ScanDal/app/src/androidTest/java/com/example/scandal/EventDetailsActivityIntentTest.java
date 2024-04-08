package com.example.scandal;

import android.content.Intent;
import android.provider.Settings;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.scandal.EventDetailsActivity;
import com.example.scandal.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

/**
 * Instrumented test class for EventDetailsActivity.
 */
@RunWith(AndroidJUnit4.class)
public class EventDetailsActivityIntentTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<EventDetailsActivity> activityTestRule =
            new IntentsTestRule<>(EventDetailsActivity.class, true, false);

    /**
     * Tests signing up for an event.
     * <p>
     * This test simulates the scenario where the event details are displayed and
     * verifies that the displayed information matches the expected values.
     * </p>
     */
    @Test
    public void testSignUpForEvent() {
        // Mock the intent with necessary extras
        Intent intent = new Intent();
        intent.putExtra("eventName", "Test Event");
        intent.putExtra("location", "Test Location");
        intent.putExtra("time", "Test Time");
        intent.putExtra("description", "Test Description");
        intent.putExtra("check", "0"); // This simulates not being checked in yet
        activityTestRule.launchActivity(intent);

        // Verify that the displayed information matches the expected values
        Espresso.onView(withId(R.id.textEventName_ViewEventPage))
                .check(matches(withText("Test Event")));
        Espresso.onView(withId(R.id.textEventLocation_ViewEventPage))
                .check(matches(withText("Test Location")));
        Espresso.onView(withId(R.id.textEventTime_ViewEventPage))
                .check(matches(withText("Test Time")));
        Espresso.onView(withId(R.id.textEventDescription_ViewEventPage))
                .check(matches(withText("Test Description")));
    }
}
