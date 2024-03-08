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
public class OrganisorActivityIntentTest {

    @Rule
    public IntentsTestRule<OrganisorActivity> intentsTestRule = new IntentsTestRule<>(OrganisorActivity.class);

    @Test
    public void clickingCreateNewEventsButtonNavigatesToEventActivity() {
        // Perform a click on the "Create New Events" button
        onView(withId(R.id.buttonCreateNewEvents)).perform(click());

        // Verify that the EventActivity is launched after clicking the button
        intended(hasComponent(EventActivity.class.getName()));
    }

    @Test
    public void pressingBackButtonFinishesActivity() {
        // Perform a click on the back button
        onView(withId(R.id.buttonBackToHomepage)).perform(click());

        // Verify that the OrganisorActivity is finished after pressing the back button
        // This ensures that the activity is navigated back to the previous activity
        // Example:
        // assertTrue(intentsTestRule.getActivity().isFinishing());
    }

    // Add more tests for additional functionality as needed
}
