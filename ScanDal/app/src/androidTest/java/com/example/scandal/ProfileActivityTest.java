package com.example.scandal;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test class for ProfileActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileActivityTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<ProfileActivity> intentsTestRule = new IntentsTestRule<>(ProfileActivity.class);

    /**
     * Test saving profile with name and homepage.
     */
    @Test
    public void testSaveProfileWithNameAndHomePage() {
        // Enter name and homepage
        onView(withId(R.id.editTextName)).perform(replaceText("John Doe"));
        onView(withId(R.id.editHomePage)).perform(replaceText("www.example.com"));

        // Click save profile button
        onView(withId(R.id.buttonSave)).perform(click());
    }

    /**
     * Test pressing the back button to return to HomeActivity.
     */
    @Test
    public void testBackButtonToHomeActivity() {
        // Click the back button
        onView(withId(R.id.buttonBack_EditProfilePage)).perform(click());

        // Check if HomeActivity is opened
        intended(hasComponent(HomeActivity.class.getName()));
    }

    /**
     * Test saving profile with name only.
     */
    @Test
    public void testSaveProfileWithNameOnly() {
        // Enter name only
        onView(withId(R.id.editTextName)).perform(replaceText("Alice Smith"));

        // Click save profile button
        onView(withId(R.id.buttonSave)).perform(click());
    }

    /**
     * Test saving profile with name and contact number.
     */
    @Test
    public void testSaveProfileWithNameAndContactNo() {
        // Enter name and contact number
        onView(withId(R.id.editTextName)).perform(replaceText("Bob Johnson"));
        onView(withId(R.id.editTextPhoneNumber)).perform(replaceText("1234567890"));

        // Click save profile button
        onView(withId(R.id.buttonSave)).perform(click());
    }

    /**
     * Test saving profile with all fields empty.
     */
    @Test
    public void testSaveProfileWithEmptyFields() {
        // Leave all fields empty

        // Click save profile button
        onView(withId(R.id.buttonSave)).perform(click());
    }
}
