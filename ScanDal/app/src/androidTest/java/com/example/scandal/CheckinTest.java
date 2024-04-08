package com.example.scandal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

/**
 * Instrumented test for checking in to an event.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CheckinTest {
    static Intent intent;
    static String QRToken = "Recreational Sporting2436"; // going to the promo page first to sign up
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ConfirmationPage.class);
        intent.putExtra("QRToken", QRToken); // token for the QR that will be used as an example
    }

    @Rule
    public ActivityScenarioRule<ConfirmationPage> activityScenarioRule = new ActivityScenarioRule<>(intent);

    /**
     * Tests checking in to make sure it works.
     * @throws InterruptedException for interrupt exceptions
     */
    @Test
    public void TestCheckingIn() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.confYes)).perform(click());
        Thread.sleep(1000); // testing whether promo is displayed properly
        onView(withText("Outside")).check(matches(isDisplayed())); // checks whether the location shows up
        onView(withText("Page Tester for testing")).check(matches(isDisplayed())); // checks description
        onView(withText("Recreational Sporting")).check(matches(isDisplayed())); // checks name
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String deviceId = Settings.Secure.getString(InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        db.collection("events")
                .whereEqualTo("name", "Recreational Sporting")
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        Map<String, Object> eventData = documentSnapshot.getData();
                        // Check if user is signed up
                        if (eventData.containsKey("checkedIn")) {
                            List<String> existingCheckedIn = (List<String>) eventData.get("checkedIn");
                            assert (existingCheckedIn.contains(deviceId));
                        } else {
                            assert false;
                        }
                    }
                });
    }
}
