package com.example.scandal;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test class for SettingsAndOrganiserActivityIntentTest.
 */
@RunWith(AndroidJUnit4.class)
public class SettingsAndOrganiserActivityIntentTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<SettingsAndOrganiserActivity> intentsTestRule = new IntentsTestRule<>(SettingsAndOrganiserActivity.class);

    /**
     * Test clicking the geolocation verification button.
     * Checks if the button and other elements are still visible after clicking.
     */
    @Test
    public void testButtonClick_GeoLocationVerification() {
        // Click the button for geolocation verification
        onView(withId(R.id.buttonGeoTracking_SettingsPage)).perform(click());

        // Check if the current activity is still SettingsAndOrganiserActivity
        onView(withId(R.id.buttonGotoOrganisorPage)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonBack_SettingsAndOrganisorPage)).check(matches(isDisplayed()));

        // Check if the geolocation verification button is still visible
        onView(withId(R.id.buttonGeoTracking_SettingsPage)).check(matches(isDisplayed()));
    }

    /**
     * Test clicking the "Go to Organiser Page" button.
     * Verifies if the OrganiserActivity is launched after clicking the button.
     */
    @Test
    public void clickingGotoOrganisorPageButtonNavigatesToOrganisorActivity() {
        // Perform a click on the "Go to Organiser Page" button
        onView(withId(R.id.buttonGotoOrganisorPage)).perform(click());

        // Verify that the OrganisorActivity is launched after clicking the button
        intended(hasComponent(OrganisorActivity.class.getName()));
    }

    /**
     * Test clicking the "Back to Settings and Organiser Page" button.
     * Verifies if the HomeActivity is launched after clicking the button.
     */
    @Test
    public void clickingBackSettingsAndOrganisorPageButtonNavigatesToHomeActivity() {
        // Perform a click on the "Back to Settings and Organiser Page" button
        onView(withId(R.id.buttonBack_SettingsAndOrganisorPage)).perform(click());

        // Verify that the HomeActivity is launched after clicking the button
        intended(hasComponent(HomeActivity.class.getName()));
    }

    // Add more tests for additional functionality as needed
}
