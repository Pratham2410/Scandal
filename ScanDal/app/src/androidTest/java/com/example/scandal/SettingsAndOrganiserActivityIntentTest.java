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

@RunWith(AndroidJUnit4.class)
public class SettingsAndOrganiserActivityIntentTest {

    @Rule
    public IntentsTestRule<SettingsAndOrganiserActivity> intentsTestRule = new IntentsTestRule<>(SettingsAndOrganiserActivity.class);

    @Test
    public void clickingBackToHomepageButtonNavigatesToHomeActivity() {
        // Perform a click on the "Back to Homepage" button
        onView(withId(R.id.buttonBackToHomepage)).perform(click());

        // Verify that the HomeActivity is launched after clicking the button
        intended(hasComponent(HomeActivity.class.getName()));
    }

    @Test
    public void clickingGotoOrganisorPageButtonNavigatesToOrganisorActivity() {
        // Perform a click on the "Go to Organiser Page" button
        onView(withId(R.id.buttonGotoOrganisorPage)).perform(click());

        // Verify that the OrganisorActivity is launched after clicking the button
        intended(hasComponent(OrganisorActivity.class.getName()));
    }

    @Test
    public void clickingBackSettingsAndOrganisorPageButtonNavigatesToHomeActivity() {
        // Perform a click on the "Back to Settings and Organiser Page" button
        onView(withId(R.id.buttonBack_SettingsAndOrganisorPage)).perform(click());

        // Verify that the HomeActivity is launched after clicking the button
        intended(hasComponent(HomeActivity.class.getName()));
    }

    // Add more tests for additional functionality as needed
}
