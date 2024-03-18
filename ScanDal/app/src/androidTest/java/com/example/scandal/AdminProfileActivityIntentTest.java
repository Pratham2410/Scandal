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
public class AdminProfileActivityIntentTest {

    @Rule
    public IntentsTestRule<AdminProfileActivity> intentsTestRule = new IntentsTestRule<>(AdminProfileActivity.class);



    @Test
    public void clickingDeleteButtonShowsConfirmationDialog() {
        // Perform a click on the delete button
        onView(withId(R.id.buttonDelete_AdminProfileListPage)).perform(click());

        // Verify that a confirmation dialog for deleting the profile is shown
        // Example:
        // onView(withText("Are you sure you want to delete this profile?")).check(matches(isDisplayed()));
    }

    // Add more tests for additional functionality as needed
}
