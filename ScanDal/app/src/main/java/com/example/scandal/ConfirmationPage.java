package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Activity for displaying a confirmation page to ensure the user
 * goes to the right event
 */
public class ConfirmationPage extends AppCompatActivity {
    /**
     * loading to display the poster of the event.
     */
    ProgressBar bar;
    /**
     * yes button procceeds to next page .
     */
    Button yesButton;
    /**
     * No button will nav back.
     */
    Button noButton;
    /**
     * TextView to display the description of the event.
     */
    TextView eventDescription;
    /**
     * TextView to display the loading bar.
     */
    TextView loading;
    /**
     * stores the fetched poster image from the db
     */
    String posterImage;
    /**
     * stroes the description of the event as in the db
     */
    String description;
    /**
     * stores the name of the event
     */
    String name;
    /**
     * stores the location of the event
     */
    String location;
    /**
     * stores the time of the event
     */
    String time;
    /**
     * stores a flag to indicate whether it was a promo or checkin QR
     */
    String checked;
    /**
     * the token to checkin
     */
    String checkinToken;
    /**
     * the token to view the promo page
     */
    String promoToken;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);
        String defaultText = "Do you want to ";
        yesButton = findViewById(R.id.confYes);
        noButton = findViewById(R.id.confNo);
        bar = findViewById(R.id.progressBarConf);
        loading = findViewById(R.id.loadingBar);
        eventDescription = findViewById(R.id.conf_descrip);
        String token = getIntent().getStringExtra("QRToken");

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("etowsley", "Beggining data access");
        // Query Firestore for events with matching QRCode or PromoQRCode
        db.collection("events")
                .whereEqualTo("checkinToken", token)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("etowsley", "Successful data retrieval");
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            Log.d("etowsley", "Inside inner if statement");
                            // Get the first matching document
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            // Retrieve values from the document
                            // Set the event name, description, poster image, location, time, and tokens

                            name = document.getString("name");
                            bar.setProgress(10);
                            description = document.getString("description");
                            bar.setProgress(20);
                            location = document.getString("location");
                            bar.setProgress(30);
                            time = document.getString("time");
                            bar.setProgress(40);
                            promoToken = document.getString("promoToken");
                            posterImage = document.getString("posterImage");
                            bar.setProgress(100);
                            checkinToken = token;
                            eventDescription.setText(defaultText + "checkin to " + name + "?");
                            eventDescription.setVisibility(View.VISIBLE);
                            yesButton.setVisibility(View.VISIBLE);
                            noButton.setVisibility(View.VISIBLE);
                            bar.setVisibility(View.INVISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                            checked = "1";
                            Log.d("etowsley", checked);
                        } else {
                            // No matching document found with QRCode, try PromoQRCode
                            searchWithPromoQRCode(token);
                            checked = "0";
                            Log.d("etowsley", "No matching doc");
                        }
                    } else {
                        // Failed to retrieve documents
                        Toast.makeText(ConfirmationPage.this, "Failed to fetch event", Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(ConfirmationPage.this, HomeActivity.class);
                        startActivity(home);
                    }
                });
        //Log.d("etowsley", checked);
        // Set OnClickListener for yes button
        yesButton.setOnClickListener(view -> {
            if (Objects.equals(checked, "1")) {
                checkInUserToEvent();
            } else {
                startConditionalIntent(false);
            }
        });
        // Set OnClickListener for no button

        noButton.setOnClickListener(view -> {
                    // Navigate to HomeActivity when no button is clicked
                    Intent intent = new Intent(ConfirmationPage.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
        );
    }

        /**
         * Method to search for events based on PromoQRCode.
         *
         * @param token The QR token to search for.
         */
        private void searchWithPromoQRCode(String token){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("events")
                    .whereEqualTo("promoToken", token)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Get the first matching document
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                // Retrieve values from the document
                                name = document.getString("name");
                                bar.setProgress(10);
                                description = document.getString("description");
                                bar.setProgress(20);
                                location = document.getString("location");
                                bar.setProgress(30);
                                time = document.getString("time");
                                bar.setProgress(40);
                                checkinToken = document.getString("checkinToken");
                                promoToken = token;
                                posterImage = document.getString("posterImage");
                                bar.setProgress(100);

                                eventDescription.setText("Do you want to view " + name + "?");
                                eventDescription.setVisibility(View.VISIBLE);
                                yesButton.setVisibility(View.VISIBLE);
                                noButton.setVisibility(View.VISIBLE);
                                bar.setVisibility(View.INVISIBLE);
                                loading.setVisibility(View.INVISIBLE);


                            } else {
                                // No matching document found with PromoQRCode as well
                                Toast.makeText(ConfirmationPage.this, "Event Not Found", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(ConfirmationPage.this, HomeActivity.class);
                                startActivity(home);
                            }
                        } else {
                            // Failed to retrieve documents
                            Toast.makeText(ConfirmationPage.this, "Failed to fetch event", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(ConfirmationPage.this, HomeActivity.class);
                            startActivity(home);
                        }
                    });
        }
    private void checkInUserToEvent() {
        Log.d("etowsley", "checkInUserTOEvent is called");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .whereEqualTo("name", name)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        Map<String, Object> eventData = documentSnapshot.getData();
                        //Check if user is signed up
                        if (eventData.containsKey("signedUp")) {
                            Log.d("etowsley", "Event has key signedUp");
                            Map<String, Object> signedUpUsers = (Map<String, Object>) eventData.get("signedUp");
                            if (signedUpUsers.containsKey(deviceId)) {
                                Log.d("etowsley", "User has key deviceId in signedUp list");
                                // Assuming each event document has a 'name' field
                                //String eventName = documentSnapshot.getString("name");
                                Map<String, Object> update = new HashMap<>();
                                if (eventData != null) {
                                    Log.d("etowsley", "Doing Check In");
                                    // Handle the checkedIn list
                                    List<String> existingCheckedIn = (List<String>) eventData.get("checkedIn");
                                    if (existingCheckedIn == null) {
                                        existingCheckedIn = new ArrayList<>();
                                    }
                                    if (!existingCheckedIn.contains(deviceId)) {
                                        existingCheckedIn.add(deviceId);
                                        update.put("checkedIn", existingCheckedIn);
                                    }

                                    // Handle the checkedIn_count map
                                    Map<String, Long> checkedInCount = (Map<String, Long>) eventData.get("checkedIn_count");
                                    if (checkedInCount == null) {
                                        checkedInCount = new HashMap<>();
                                    }
                                    Long currentCount = checkedInCount.getOrDefault(deviceId, 0L);
                                    checkedInCount.put(deviceId, currentCount + 1);
                                    update.put("checkedIn_count", checkedInCount);

                                    // Perform the update
                                    db.collection("events").document(documentId)
                                            .update(update)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Checked in successfully", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check in", Toast.LENGTH_SHORT).show());
                                }
                                startConditionalIntent(false);
                            }
                            // If user has not signed up yet
                            else {
                                startConditionalIntent(true);
                                Log.d("etowsley", "User is not in signedUp");

                            }
                        }
                        //If no user had signed up yet
                        else {
                            Log.d("etowsley", "Event has no key signedUp");
                            startConditionalIntent(true);
                            Log.d("etowsley", "Event has no key signedUp2");

                        }
                    } else {
                        Log.d("etowsley", "Document retrieval failed");
                        Toast.makeText(getApplicationContext(), "Checkin failed", Toast.LENGTH_SHORT).show();
                        startConditionalIntent(true);
                    }
                });
    }

    private void startConditionalIntent(boolean userSignUpError) {
       // Nav to EventDetailsActivity when yes is clicked
        EventDetailsActivity.imageString = posterImage;
        Intent intent = new Intent(ConfirmationPage.this, EventDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("time", time);
        intent.putExtra("promo", promoToken);
        intent.putExtra("checkin", checkinToken);
        intent.putExtra("location", location);

        intent.putExtra("check", checked);

        if (userSignUpError) {
            Log.d("etowsley", "sign up error found");
            intent.putExtra("singUpError", true);
        }
        else {
            Log.e("etowsley", String.valueOf(userSignUpError));
        }
        startActivity(intent);
        finish();
    }

}
