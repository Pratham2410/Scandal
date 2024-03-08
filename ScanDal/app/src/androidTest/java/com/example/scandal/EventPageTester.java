package com.example.scandal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public class EventPageTester {

        static Intent intent;

        static {
            intent = new Intent(ApplicationProvider.getApplicationContext(), EventPage.class);
            intent.putExtra("QRToken", "PageTesting401"); // token for the QR that will be used as an example
        }

        @Rule
        public ActivityScenarioRule<EventPage> activityScenarioRule = new ActivityScenarioRule<>(intent);

        @Test
        public void ViewEventDetails() throws InterruptedException {
            Thread.sleep(2000);
            onView(withText("Page Tester for testing")).check(matches(isDisplayed()));
        }

    }
