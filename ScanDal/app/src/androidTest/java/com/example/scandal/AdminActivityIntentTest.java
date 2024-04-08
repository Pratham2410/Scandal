package com.example.scandal;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Intent tests for AdminActivity
 */
@RunWith(AndroidJUnit4.class)
public class AdminActivityIntentTest {

    @Rule
    public IntentsTestRule<AdminActivity> intentsTestRule = new IntentsTestRule<>(AdminActivity.class);

    @Test
    public void clickManageEventsButton_LaunchesAdminEventActivity() {
        onView(withId(R.id.buttonManageEvents)).perform(click());
        Intents.intended(hasComponent(AdminEventActivity.class.getName()));
    }

    @Test
    public void clickManageImagesButton_LaunchesAdminImageActivity() {
        onView(withId(R.id.buttonSignUp)).perform(click()); // Ensure ID matches your buttonManageImages ID
        Intents.intended(hasComponent(AdminImageActivity.class.getName()));
    }

    @Test
    public void clickManageProfileButton_LaunchesAdminProfileActivity() {
        onView(withId(R.id.buttonManageProfile)).perform(click());
        Intents.intended(hasComponent(AdminProfileActivity.class.getName()));
    }

    @Test
    public void clickGoBackButton_LaunchesHomeActivity() {
        onView(withId(R.id.buttonBack_Admin)).perform(click());
        intended(hasComponent(HomeActivity.class.getName()));
    }
}
