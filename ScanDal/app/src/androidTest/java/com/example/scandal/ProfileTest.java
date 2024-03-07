package com.example.scandal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        // Additional tests and setup as needed
    }


}
