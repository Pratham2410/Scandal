package com.example.scandal;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.scandal.EventActivity;

@RunWith(AndroidJUnit4.class)
public class EventActivityIntentTest {

    // Define a rule to launch the activity under test
    @Rule
    public ActivityTestRule<EventActivity> activityTestRule =
            new ActivityTestRule<>(EventActivity.class);

    // Initialize Espresso-Intents before each test
    @Before
    public void setUp() {
        Intents.init();
    }

    // Release Espresso-Intents after each test
    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Test for verifying EditText inputs.
     */
    @Test
    public void testEditTextInputs() {
        String eventName = "Test Event";
        String eventDescription = "Test Description";
        String location = "Test Location";
        String limit = "100";

        onView(withId(R.id.editTextEventName_CreateEventPage)).perform(typeText(eventName), closeSoftKeyboard());
        onView(withId(R.id.editTextEventDescription_CreateEventPage)).perform(typeText(eventDescription), closeSoftKeyboard());
        onView(withId(R.id.editTextEventLocation_CreateEventPage)).perform(typeText(location), closeSoftKeyboard());
        onView(withId(R.id.editTextEventLimit_CreateEventPage)).perform(typeText(limit), closeSoftKeyboard());

        // Verify inputs
        onView(withId(R.id.editTextEventName_CreateEventPage)).check(matches(withText(eventName)));
        onView(withId(R.id.editTextEventDescription_CreateEventPage)).check(matches(withText(eventDescription)));
        onView(withId(R.id.editTextEventLocation_CreateEventPage)).check(matches(withText(location)));
        onView(withId(R.id.editTextEventLimit_CreateEventPage)).check(matches(withText(limit)));
        testGenerateEventButtonIntent();
    }

    /**
     * Test for verifying the intent when clicking the generate event button.
     */
    @Test
    public void testGenerateEventButtonIntent() {
        // Assume inputs are filled correctly.
        onView(withId(R.id.buttonSave_CreateEventPage)).perform(click());

        // Verify NewEventActivity was started.
        intended(hasComponent(NewEventActivity.class.getName()));
    }
}
