package com.example.scandal;

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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminActivityIntentTest {

    @Rule
    public IntentsTestRule<AdminActivity> intentsTestRule = new IntentsTestRule<>(AdminActivity.class);

    @Before
    public void setUp() {
        // Initialize anything before your tests, if necessary
    }

    @Test
    public void testManageEventsIntent() {
        onView(withId(R.id.buttonManageEvents)).perform(click());
        intended(hasComponent(AdminEventActivity.class.getName()));
    }



    @Test
    public void testManageProfilesIntent() {
        onView(withId(R.id.buttonManageProfile)).perform(click());
        intended(hasComponent(AdminProfileActivity.class.getName()));
    }

}
