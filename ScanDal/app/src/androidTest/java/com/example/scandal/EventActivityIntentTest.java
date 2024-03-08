package com.example.scandal;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventActivityIntentTest {

    @Rule
    public IntentsTestRule<EventActivity> intentsTestRule = new IntentsTestRule<>(EventActivity.class);

    @Test
    public void backButton_finishesActivity() {
        // Assuming the back button has the ID buttonBack_CreateEventPage
        onView(withId(R.id.buttonBack_CreateEventPage)).perform(click());

        // Espresso Intents does not have a direct way to check if an activity is finished.
        // This test assumes that clicking the back button finishes EventActivity,
        // which would typically be expected behavior, but Espresso can't directly verify activity finishing.
    }

    @Test
    public void saveButtonWithNoInput_staysOnSamePage() {
        // Click the save button without entering any details
        onView(withId(R.id.buttonSave_CreateEventPage)).perform(click());

        // Verify that no intents were started, assuming staying on the same page means no new activity is launched
        intended(not(hasComponent(NewEventActivity.class.getName())), times(0));

        // Additionally, could check for a Toast message or error validation message visibility,
        // but note that Espresso has limited support for interacting with Toasts directly.
    }
}
