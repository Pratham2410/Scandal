package com.example.scandal;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

import android.app.Instrumentation;

@RunWith(AndroidJUnit4.class)
public class AdminEventActivityIntentTest {

    @Rule
    public IntentsTestRule<AdminEventActivity> intentsTestRule = new IntentsTestRule<>(AdminEventActivity.class);

    @Before
    public void stubFirestoreIntent() {
        // Stub the Firestore query intent to prevent it from launching.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(0, null));

    }


    @Test
    public void clickingEventShowsDeleteConfirmationDialog() {
        // Perform a click on an event in the ListView
        onView(withId(R.id.eventsList_AdminEventsPage)).perform(click());

        // Verify that a delete confirmation dialog is shown
        // Example:
        // onView(withText("Are you sure you want to delete Event Name?")).check(matches(isDisplayed()));
    }


}
