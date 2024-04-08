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
public class BrowseEventActivityIntentTest {

    @Rule
    public IntentsTestRule<BrowseEventActivity> intentsTestRule = new IntentsTestRule<>(BrowseEventActivity.class);



    @Test
    public void pressingBackButtonFinishesActivity() {
        // Perform a click on the back button
        onView(withId(R.id.buttonBack_BrowseEventsPage)).perform(click());
        intended(hasComponent(HomeActivity.class.getName()));
    }

    @Test
    public void listViewDisplayed() {
        // Assuming there is at least one item in the ListView
        // Perform a click on the first item in the ListView
        onView(withId(R.id.listView_BrowseEventPage)).perform(click());

    }
}
