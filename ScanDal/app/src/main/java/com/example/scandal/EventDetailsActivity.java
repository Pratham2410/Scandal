package com.example.scandal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
/** An activity for managing the viewing of event details */
public class EventDetailsActivity extends AppCompatActivity {
    /** Firestore instance for database operations */
    private FirebaseFirestore db;
    /** TextView to display the event name. */
    TextView textEventName_ViewEventPage;
    /** TextView to display the event description. */
    TextView textEventDescription_ViewEventPage;
    /** ImageView to display the event image. */
    TextView textEventTime_ViewEventPage;
    /** ImageView to display the event time. */
    TextView textEventLocation_ViewEventPage;
    /** ImageView to display the event location. */
    ImageView imageView;
    /** Button to see QRCode */
    Button button_seeQR;
    /** Button to navigate back from the event details page. */
    FrameLayout buttonBack_ViewEventPage;
    LinearLayout buttonSignUp;
    String attendeeName;
    String promoQRCode;
    String checkInQRCode;
    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_page);

        textEventName_ViewEventPage = findViewById(R.id.textEventName_ViewEventPage);
        textEventLocation_ViewEventPage = findViewById(R.id.textEventLocation_ViewEventPage);
        textEventTime_ViewEventPage = findViewById(R.id.textEventTime_ViewEventPage);

        textEventDescription_ViewEventPage = findViewById(R.id.textEventDescription_ViewEventPage);
        imageView = findViewById(R.id.imageView_ViewEventPage);
        buttonBack_ViewEventPage = findViewById(R.id.buttonBack_ViewEventPage);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        button_seeQR = findViewById(R.id.button_seeQRCode);
        db = FirebaseFirestore.getInstance();

        buttonBack_ViewEventPage.setOnClickListener(v -> finish());



        Intent intent = getIntent();
        // Retrieve the event name from the intent
        String eventName = intent.getStringExtra("eventName");
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                        //Log here
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData != null) {
                            textEventName_ViewEventPage.setText((String) eventData.get("name"));
                            textEventTime_ViewEventPage.setText((String) eventData.get("time"));
                            textEventLocation_ViewEventPage.setText((String) eventData.get("location"));
                            textEventDescription_ViewEventPage.setText((String) eventData.get("description"));
                            promoQRCode = (String) eventData.get("promoToken");
                            checkInQRCode = (String) eventData.get("checkinToken");


                            String imageString = (String) eventData.get("posterImage");
                            if (imageString != null) {
                                Bitmap bitmap = convertImageStringToBitmap(imageString);
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            }
                        }

                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());

        button_seeQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("etowsley", "SeeQRCode Button pushed");
                Intent myIntent = new Intent(EventDetailsActivity.this, NewEventActivity.class);
                myIntent.putExtra("source", "EventDetails");
                myIntent.putExtra("CheckInQRCodeEventDetails", checkInQRCode);
                if (checkInQRCode != null) {
                    Log.e("etowsley", "checkInQRCode is not null");
                    myIntent.putExtra("PromoQRCodeEventDetails", promoQRCode);
                    startActivity(myIntent);
                    Log.e("etowsley", "Intent was started");
                }
            }
        });


        // Sign Attendee up for the event
        buttonSignUp.setOnClickListener(v -> {
            // Retrieve the event name from the TextView
            String event_Name = textEventName_ViewEventPage.getText().toString();

            // Check if eventName is not empty
            if (!eventName.isEmpty()) {
                // Subscribe to the event topic
                FirebaseMessaging.getInstance().subscribeToTopic(event_Name)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Topic subscription failed");
                            } else {
                                // Optionally notify the user of successful subscription
                                Toast.makeText(EventDetailsActivity.this, "Subscribed to event notifications", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            db.collection("profiles")
                    .whereEqualTo("deviceId", deviceId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Device is already registered, fetch and display profile data
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Map<String, Object> profileData = documentSnapshot.getData();
                            if (profileData != null) {
                                attendeeName = (String) profileData.get("name");
                                saveSignUpToEvent(eventName);
                                saveSignUpToAttendee(eventName);
                            }
                            else {
                                // Device is not registered, let the user enter new information
                                Toast.makeText(getApplicationContext(), "Please enter your information", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());
        });
    }
    private void saveSignUpToAttendee(String eventName) {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final Map<String, Object> signedUp = new HashMap<>();
        signedUp.put(promoQRCode, eventName);
        db.collection("profiles")
                .whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        Map<String, Object> profileData = documentSnapshot.getData();
                        // If there is dictionary storing signed up event
                        if (profileData != null && profileData.containsKey("signedUp")) {
                            Map<String, Object> existingSignedUp = (Map<String, Object>) profileData.get("signedUp");
                            existingSignedUp.put(promoQRCode, eventName);
                            Map<String, Object> update = new HashMap<>();
                            update.put("signedUp", existingSignedUp);
                            // Perform the update
                            db.collection("profiles").document(documentId)
                                    .update(update)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    });
                        }
                        else {
                            Map<String, Object> update = new HashMap<>();
                            update.put("signedUp", signedUp);
                            // Perform the update
                            db.collection("profiles").document(documentId)
                                    .update(update)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }
    private void saveSignUpToEvent(String eventName) {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final Map<String, Object> signedUp = new HashMap<>();
        signedUp.put(deviceId, attendeeName);
        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        Map<String, Object> eventData = documentSnapshot.getData();
                        // If there is dictionary storing signed up user
                        if (eventData != null && eventData.containsKey("signedUp")) {
                            Map<String, Object> existingSignedUp = (Map<String, Object>) eventData.get("signedUp");
                            existingSignedUp.put(deviceId, attendeeName);
                            Map<String, Object> update = new HashMap<>();
                            update.put("signedUp", existingSignedUp);
                            // Perform the update
                            db.collection("events").document(documentId)
                                    .update(update)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    });
                        }
                        else {
                            Map<String, Object> update = new HashMap<>();
                            update.put("signedUp", signedUp);
                            // Perform the update
                            db.collection("events").document(documentId)
                                    .update(update)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }
    /**
     * Helper method to decode Base64 string to Bitmap.
     *
     * @param imageString The Base64-encoded image string.
     * @return The decoded Bitmap, or null if decoding fails.
     */
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
