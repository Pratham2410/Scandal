package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scandal.HomeActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EventPage extends AppCompatActivity {
    ImageView poster;
    FrameLayout back;
    TextView eventName;
    TextView eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_page);

        back = findViewById(R.id.buttonBack_ViewEventPage);
        poster = findViewById(R.id.imageView_ViewEventPage);
        eventName = findViewById(R.id.textEventName_ViewEventPage);
        eventDescription = findViewById(R.id.textEventDescription_ViewEventPage);

        String token = getIntent().getStringExtra("QRToken");

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query Firestore for events with matching QRCode or PromoQRCode
        db.collection("events")
                .whereEqualTo("QRCode", token)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Get the first matching document
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            // Retrieve values from the document
                            String name = document.getString("name");
                            String description = document.getString("description");
                            String posterImage = document.getString("posterImage");
                            // Convert posterImage to Bitmap
                            Bitmap posterBitmap = convertImageStringToBitmap(posterImage);
                            // Set the event name, description, and poster image
                            eventName.setText(name);
                            eventDescription.setText(description);
                            if (posterBitmap != null) {
                                poster.setImageBitmap(posterBitmap);
                            }
                        } else {
                            // No matching document found with QRCode, try PromoQRCode
                            searchWithPromoQRCode(token);
                        }
                    } else {
                        // Failed to retrieve documents
                        Toast.makeText(EventPage.this, "Failed to fetch event data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set OnClickListener for back button
        back.setOnClickListener(view -> {
            // Navigate to HomeActivity when back button is clicked
            Intent intent = new Intent(EventPage.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to search for events based on PromoQRCode
    private void searchWithPromoQRCode(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereEqualTo("PromoQRCode", token)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Get the first matching document
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            // Retrieve values from the document
                            String name = document.getString("name");
                            String description = document.getString("description");
                            String posterImage = document.getString("posterImage");
                            // Convert posterImage to Bitmap
                            Bitmap posterBitmap = convertImageStringToBitmap(posterImage);
                            // Set the event name, description, and poster image
                            eventName.setText(name);
                            eventDescription.setText(description);
                            if (posterBitmap != null) {
                                poster.setImageBitmap(posterBitmap);
                            }
                        } else {
                            // No matching document found with PromoQRCode as well
                            Toast.makeText(EventPage.this, "Event not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Failed to retrieve documents
                        Toast.makeText(EventPage.this, "Failed to fetch event data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Helper method to decode Base64 string to Bitmap
    private Bitmap convertImageStringToBitmap(String imageString) {
        try {
            byte[] decodedByteArray = android.util.Base64.decode(imageString, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
