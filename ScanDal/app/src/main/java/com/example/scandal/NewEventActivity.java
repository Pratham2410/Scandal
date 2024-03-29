package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewEventActivity extends AppCompatActivity {


    // Assuming these ImageViews are for displaying the QR codes or posters if needed
    ImageView checkinQRCode, promoQRCode;
    AppCompatButton saveEventButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_created_page); // Ensure you have this layout

        db = FirebaseFirestore.getInstance();

        initializeUI();

        // Retrieve event details passed from EventActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String eventTime = intent.getStringExtra("Time");
        String eventLocation = intent.getStringExtra("Location");
        String checkinToken = intent.getStringExtra("CheckinToken");
        String promoToken = intent.getStringExtra("PromoToken");
        String posterImage = intent.getStringExtra("posterImage"); // Base64 image string
        // Fetch device ID to use as organizer
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        // Save button action
        saveEventButton.setOnClickListener(v -> {
            Map<String, Object> event = new HashMap<>();
            event.put("name", name);
            event.put("description", description);
            event.put("time", eventTime);
            event.put("location", eventLocation);
            event.put("checkinToken", checkinToken);
            event.put("promoToken", promoToken);
            event.put("posterImage", posterImage);
            event.put("organizer", deviceId); // Add device ID as organizer

            // Save the event to Firestore
            saveEventToFirestore(event);

        });
    }

    private void initializeUI() {
        // Initialize your UI components here
        // Example:
        saveEventButton = findViewById(R.id.buttonSaveProject);
        checkinQRCode = findViewById(R.id.checkinQRCode); // If you're displaying QR codes or images
        promoQRCode = findViewById(R.id.promoQRCode); // If you're displaying QR codes or images
    }

    private void saveEventToFirestore(Map<String, Object> event) {
        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(NewEventActivity.this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally finish this activity
                })
                .addOnFailureListener(e -> Toast.makeText(NewEventActivity.this, "Error saving event", Toast.LENGTH_SHORT).show());
    }
}
