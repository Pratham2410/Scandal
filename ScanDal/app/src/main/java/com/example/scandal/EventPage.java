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

import java.util.Map;

/**
 * Activity for displaying details of an event based on QR code.
 */
public class
EventPage extends AppCompatActivity {
    /** ImageView to display the poster of the event. */
    ImageView poster;
    /** FrameLayout for navigating back. */
    FrameLayout back;
    /** TextView to display the name of the event. */
    TextView eventName;
    /** TextView to display the description of the event. */
    TextView eventDescription;
    /**
     * TextView to display the event time
     */
    TextView eventTime;
    /**
     * TextView to display event location
     */
    TextView eventLocation;
    /** Button to see QRCode */
    Button button_seeQR;
    /**
     * Signup button to sign up for an event
     */
    LinearLayout signUp;
    /**
     *  data base instance
     */
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * string of the event poster to make the passed intents smaller
     */
    static String imageString;
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
        setContentView(R.layout.view_event_page);

       initializeUI();

        // Set OnClickListener for back button
        back.setOnClickListener(view -> {
            // Navigate to HomeActivity when back button is clicked
            Intent intent = new Intent(EventPage.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
        // Sign Attendee up for the event
        signUp.setOnClickListener(v -> {
            final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);//gets device ID
            // Retrieve the event name from the TextView
            String event_Name = eventName.getText().toString();
                // Subscribe to the event topic
                FirebaseMessaging.getInstance().subscribeToTopic(event_Name)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Topic subscription failed");

                            } else {

                                // Optionally notify the user of successful subscription
                                Toast.makeText(EventPage.this, "Subscribed to event notifications", Toast.LENGTH_SHORT).show();
                            }
                        });
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
//                                attendeeName = (String) profileData.get("name");
//                                saveSignUpToEvent(eventName);
//                                saveSignUpToAttendee(eventName);
                            }
                            else {
                                // Device is not registered, let the user enter new information
                                Toast.makeText(getApplicationContext(), "Please enter your information", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());
        });
        button_seeQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkInQRCode =getIntent().getStringExtra("checkin");
                String promoQRCode =getIntent().getStringExtra("promo");

                Log.e("etowsley", "SeeQRCode Button pushed");
                Intent myIntent = new Intent(EventPage.this, NewEventActivity.class);
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
    }


    /**
     * Helper method to decode Base64 string to Bitmap.
     *
     * @param imageString The Base64-encoded image string.
     * @return The decoded Bitmap, or null if decoding fails.
     */
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
    private void initializeUI(){
        signUp = findViewById(R.id.buttonSignUp);
        back = findViewById(R.id.buttonBack_ViewEventPage);
        poster = findViewById(R.id.imageView_ViewEventPage);
        eventTime = findViewById(R.id.textEventTime_ViewEventPage);
        eventLocation = findViewById(R.id.textEventLocation_ViewEventPage);
        eventName = findViewById(R.id.textEventName_ViewEventPage);
        eventDescription = findViewById(R.id.textEventDescription_ViewEventPage);
        button_seeQR = findViewById(R.id.button_seeQRCode);

        Bitmap posterBitmap = convertImageStringToBitmap(imageString);
        poster.setImageBitmap(posterBitmap);
        eventLocation.setText(getIntent().getStringExtra("location")); //gets the location
        eventTime.setText(getIntent().getStringExtra("time")); // gets the time
        eventDescription.setText(getIntent().getStringExtra("description")); // gets description
        eventName.setText(getIntent().getStringExtra("name")); // gets the name
        if (getIntent().getStringExtra("check").equals("1")){
            signUp.setVisibility(View.INVISIBLE); // if checked in no sign up button is provided
        }
    }

}
