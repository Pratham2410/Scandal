package com.example.scandal;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

public class ProfileTest {
    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public class ProfileActivityTest {

        @Rule
        public ActivityScenarioRule<Profile> activityRule =
                new ActivityScenarioRule<>(Profile.class);

        @Before
        public void setUp() throws Exception {
            // Point your app to use Firestore Emulator
            // FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080);
        }

        @Test
        public void testProfileDataSaving() {
            // Input mock data into EditTexts
            onView(withId(R.id.editTextName)).perform(typeText("John Doe"), closeSoftKeyboard());
            onView(withId(R.id.editTextPhoneNumber)).perform(typeText("123456789"), closeSoftKeyboard());
            onView(withId(R.id.editHomePage)).perform(typeText("http://www.example.com"), closeSoftKeyboard());

            // Simulate button click to save profile
            onView(withId(R.id.buttonSave)).perform(click());

            // Assume delay for Firestore operation - use IdlingResource in real tests
            // Query Firestore to verify data saved - pseudocode, actual Firestore querying not shown
            // This part would involve fetching the document based on deviceId and verifying the content
        }
        // Test if user's profile can be retrieved from firebase
        @Test
        public void testProfileDataRetrieving() {
            // Fill in the data
            onView(withId(R.id.editTextName)).perform(typeText("TestName"), closeSoftKeyboard());
            onView(withId(R.id.editTextPhoneNumber)).perform(typeText("123456789"), closeSoftKeyboard());
            onView(withId(R.id.editHomePage)).perform(typeText("test.io"), closeSoftKeyboard());

            // Simulate pressing the go-back button
            Espresso.pressBack();

            // relaunching the Profile activity
            activityRule.getScenario().onActivity(activity -> {
                activity.finish();
                activity.startActivity(new Intent(activity, Profile.class));
            });

            // Verify the data remains
            onView(withId(R.id.editTextName)).check(matches(withText("TestName")));
            onView(withId(R.id.editTextPhoneNumber)).check(matches(withText("123456789")));
            onView(withId(R.id.editHomePage)).check(matches(withText("test.io")));
        }
    }


}
