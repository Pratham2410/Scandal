package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Query Firestore for events with matching QRCode or PromoQRCode
        db.collection("events")
                .whereEqualTo("checkinToken", token)
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
                            location = document.getString("Location");
                            bar.setProgress(30);
                            time = document.getString("Time");
                            bar.setProgress(40);
                            posterImage = document.getString("posterImage");
                            bar.setProgress(100);

                            // Convert posterImage to Bitmap
                            // Set the event name, description, and poster image
                            eventDescription.setText(defaultText + "checkin to " + name + "?");
                            eventDescription.setVisibility(View.VISIBLE);
                            yesButton.setVisibility(View.VISIBLE);
                            noButton.setVisibility(View.VISIBLE);
                            bar.setVisibility(View.INVISIBLE);
                            loading.setVisibility(View.INVISIBLE);

                        } else {
                            // No matching document found with QRCode, try PromoQRCode
                            searchWithPromoQRCode(token);
                        }
                    } else {
                        // Failed to retrieve documents
                        Toast.makeText(ConfirmationPage.this, "Failed to fetch event", Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(ConfirmationPage.this, HomeActivity.class);
                        startActivity(home);
                    }
                });

        // Set OnClickListener for yes button
        yesButton.setOnClickListener(view -> {
            // Navigate to Event page when yes button is clicked
            EventPage.imageString = posterImage;
            Intent intent = new Intent(ConfirmationPage.this, EventPage.class);
            intent.putExtra("name", name);
            intent.putExtra("description", description);
            intent.putExtra("time", time);
            intent.putExtra("location", location);
            checkInUserToEvent();
            startActivity(intent);
            finish();
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
                                location = document.getString("Location");
                                bar.setProgress(30);
                                time = document.getString("Time");
                                bar.setProgress(40);
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
                        // Check if there is an existing list storing checked in users
                        if (eventData != null && eventData.containsKey("checkedIn")) {
                            // Get the existing list of device IDs
                            List<String> existingCheckedIn = (List<String>) eventData.get("checkedIn");
                            // Add the new device ID, if it's not already in the list
                            if (!existingCheckedIn.contains(deviceId)) {
                                existingCheckedIn.add(deviceId);
                                Map<String, Object> update = new HashMap<>();
                                update.put("checkedIn", existingCheckedIn);
                                // Perform the update
                                db.collection("events").document(documentId)
                                        .update(update)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Checked in successfully", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check in", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            // If "checkedIn" does not exist, meaning no one has checked in yet
                            List<String> newCheckedInList = new ArrayList<>();
                            newCheckedInList.add(deviceId);
                            Map<String, Object> update = new HashMap<>();
                            update.put("checkedIn", newCheckedInList);
                            // Perform the update
                            db.collection("events").document(documentId)
                                    .update(update)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Checked in successfully", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check in", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
    }

}
