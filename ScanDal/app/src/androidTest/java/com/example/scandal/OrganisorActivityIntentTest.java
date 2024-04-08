package com.example.scandal;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test class for OrganisorActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganisorActivityIntentTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<OrganisorActivity> intentsTestRule = new IntentsTestRule<>(OrganisorActivity.class);

    /**
     * Setup tasks before each test, if needed.
     */
    @Before
    public void setUp() {
        // Setup tasks before each test, if needed
    }

    /**
     * Test that clicking the button to create new events opens the EventActivity.
     */
    @Test
    public void testCreateNewEventsButtonIntent() {
        // Click on the button to create new events
        onView(withId(R.id.buttonCreateNewEvents)).perform(click());

        // Verify that the EventActivity is started
        intended(hasComponent(EventActivity.class.getName()));
    }

    /**
     * Test that clicking the button to send notifications opens the OrganiserNotificationActivity.
     */
    @Test
    public void testSendNotificationsButtonIntent() {
        // Click on the button to send notifications
        onView(withId(R.id.sendnotifs)).perform(click());

        // Verify that the OrganiserNotificationActivity is started
        intended(hasComponent(OrganiserNotificationActivity.class.getName()));
    }

    /**
     * Test that clicking the button to view organizer's events opens the OrganizerEventActivity.
     */
    @Test
    public void testViewMyEventsButtonIntent() {
        // Click on the button to view organizer's events
        onView(withId(R.id.buttonViewMyEvents_OrganisorHomepage)).perform(click());

        // Verify that the OrganizerEventActivity is started
        intended(hasComponent(OrganizerEventActivity.class.getName()));
    }
}
