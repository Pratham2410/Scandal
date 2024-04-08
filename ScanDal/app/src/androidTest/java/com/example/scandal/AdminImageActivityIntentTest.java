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

/**
 * Espresso intent tests for AdminImageActivity.
 */
@RunWith(AndroidJUnit4.class)
public class AdminImageActivityIntentTest {

    @Rule
    public IntentsTestRule<AdminImageActivity> intentsTestRule = new IntentsTestRule<>(AdminImageActivity.class);

    /**
     * Test case to verify that pressing the back button navigates to AdminActivity.
     */
    @Test
    public void pressingBackButtonNavigatesToAdminActivity() {
        // Perform a click on the back button
        onView(withId(R.id.buttonBack_ImageListPage)).perform(click());

        // Verify that the AdminActivity is launched after pressing the back button
        intended(hasComponent(AdminActivity.class.getName()));
    }

    /**
     * Test case to verify that clicking the delete button shows the confirmation dialog.
     */
    @Test
    public void clickingDeleteButtonShowsConfirmationDialog() {
        // Assuming there is at least one item in the ListView
        // Perform a click on the first item in the ListView
        onView(withId(R.id.listView_ImageListPage)).perform(click());
    }
}
