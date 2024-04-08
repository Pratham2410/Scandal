package com.example.scandal;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.not;

/**
 * Instrumented test class for HomeActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityIntentTest {

    // Rule to launch the activity under test
    @Rule
    public IntentsTestRule<HomeActivity> intentsTestRule = new IntentsTestRule<>(HomeActivity.class);

    /**
     * Method to set up tasks before each test.
     */
    @Before
    public void setUp() {
        // Setup tasks before each test, if needed
    }

    /**
     * Test for SettingsActivity intent.
     */
    @Test
    public void testSettingsActivityIntent() {
        onView(withId(R.id.imageGearOne)).perform(click());
        intended(hasComponent(SettingsAndOrganiserActivity.class.getName()));
    }

    /**
     * Test for BrowseEventActivity intent.
     */
    @Test
    public void testBrowseEventActivityIntent() {
        onView(withId(R.id.buttonBrowseEvents)).perform(click());
        intended(hasComponent(BrowseEventActivity.class.getName()));
    }

    /**
     * Test for AttendeeEventActivity intent.
     */
    @Test
    public void testAttendeeEventActivityIntent() {
        onView(withId(R.id.buttonViewMyAttendeeEvents)).perform(click());
        intended(hasComponent(AttendeeEventActivity.class.getName()));
    }

    /**
     * Test for QRCodeScanner intent.
     */
    @Test
    public void testQRCodeScannerIntent() {
        onView(withId(R.id.buttonScanQRCode)).perform(click());
        intended(hasComponent(QRCodeScanner.class.getName()));
    }

    /**
     * Test for ProfileActivity intent.
     */
    @Test
    public void testProfileActivityIntent() {
        onView(withId(R.id.profilePicture)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
    }

    /**
     * Test for Admin login with incorrect PIN.
     */
    @Test
    public void testAdminLoginIncorrectPin() {
        // Click on the admin login button
        onView(withId(R.id.buttonAdminLogin)).perform(click());

        // Type incorrect PIN (1244)
        onView(withClassName(endsWith("EditText"))).perform(typeText("1244"));

        // Click on the "Enter" button
        onView(withText("Enter")).perform(click());

        // Verify that the error message is displayed
        onView(withText("Password Invalid")).check(matches(isDisplayed()));
    }

    /**
     * Test for Admin login with correct PIN.
     */
    @Test
    public void testAdminLoginCorrectPin() {
        // Click on the admin login button
        onView(withId(R.id.buttonAdminLogin)).perform(click());

        // Type correct PIN (1234)
        onView(withClassName(endsWith("EditText"))).perform(typeText("1234"));

        // Click on the "Enter" button
        onView(withText("Enter")).perform(click());

        // Verify that the AdminActivity is intended
        intended(hasComponent(AdminActivity.class.getName()));
    }
}
