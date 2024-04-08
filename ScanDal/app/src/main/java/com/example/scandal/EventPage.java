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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
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
       
        if (getIntent().getBooleanExtra("singUpError", false)) {
            Toast.makeText(EventPage.this, "Please sign up before checking in", Toast.LENGTH_LONG).show();
            Log.e("etowsley", "This code is being accessed");
        }

      
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
    private void saveSignUpToAttendee(String eventName) {
        String promoQRCode =getIntent().getStringExtra("promo");
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
                                        //Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Map<String, Object> update = new HashMap<>();
                            update.put("signedUp", signedUp);
                            // Perform the update
                            db.collection("profiles").document(documentId)
                                    .update(update)
                                    .addOnSuccessListener(aVoid -> {
                                        //Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }

    /**
     * registers that the user has signed up for the event
     * @param eventName name of the event
     * @param attendeeName name of the attendee
     */
    private void saveSignUpToEvent(String eventName, String attendeeName) {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot eventDoc = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = eventDoc.getId();
                        Map<String, Object> eventData = eventDoc.getData();

                        if (eventData != null) {
                            String attendeeLimitStr = (String) eventData.get("attendeeLimit");
                            Long attendeeLimit = null;
                            Long currentAttendeeCount = 0L;


                            try {
                                attendeeLimit = attendeeLimitStr != null ? Long.parseLong(attendeeLimitStr) : null;
                                Number currentAttendeeCountNumber = (Number) eventData.get("attendeeCount");
                                currentAttendeeCount = currentAttendeeCountNumber != null ? currentAttendeeCountNumber.longValue() : 0L;
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Failed to parse attendee limit or count", e);
                            }

                            Map<String, Object> signedUp = (Map<String, Object>) eventData.getOrDefault("signedUp", new HashMap<>());
                            boolean isAlreadySignedUp = signedUp.containsKey(deviceId);

                            if (isAlreadySignedUp) {
                                Log.d(TAG, "User is already signed up");
                                Toast.makeText(getApplicationContext(), "You are already signed up for this event", Toast.LENGTH_SHORT).show();
                                return; // Stop execution if user is already signed up
                            }

                            if (attendeeLimit != null && currentAttendeeCount >= attendeeLimit) {
                                Log.d(TAG, "Attendee limit has been reached");
                                Toast.makeText(getApplicationContext(), "Attendee limit has been reached", Toast.LENGTH_SHORT).show();
                                return; // Stop execution if event is full
                            }

                            Log.d(TAG, "Signing up the user");
                            signedUp.put(deviceId, attendeeName);
                            performSignUp(documentId, signedUp, attendeeLimit, currentAttendeeCount);
                        } else {
                            Toast.makeText(getApplicationContext(), "Event data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Event not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error fetching event", Toast.LENGTH_SHORT).show());
    }

    /**
     * signs the attendee up for an event
     * @param documentId the events ID in the db
     * @param signedUp the map of signed up users
     * @param attendeeLimit the limit of attendees
     * @param currentAttendeeCount the current number of attendees
     */
    private void performSignUp(String documentId, Map<String, Object> signedUp, long attendeeLimit, long currentAttendeeCount) {
        db.collection("events").document(documentId)
                .update("signedUp", signedUp, "attendeeCount", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Sign up successful");
                    Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();

                    // Calculate the new attendee count and capacity percentage
                    long newAttendeeCount = currentAttendeeCount + 1;
                    double capacityPercentage = ((double) newAttendeeCount / attendeeLimit) * 100;

                    // Check for milestones and send notification if necessary
                   // checkAndSendMilestoneNotification(capacityPercentage, , documentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Sign up failed", e);
                    Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * checks if a milestone is reached and sends the organizer a notif if need be
     * @param capacityPercentage percentage of the capacity reached
     * @param eventName name of the event
     * @param documentId the db ID of the event
     */
    private void checkAndSendMilestoneNotification(double capacityPercentage, String eventName, String documentId) {
        String milestoneKey;

        if (capacityPercentage >= 100) {
            milestoneKey = "100";
        } else if (capacityPercentage >= 80) {
            milestoneKey = "80";
        } else if (capacityPercentage >= 50) {
            milestoneKey = "50";
        } else if (capacityPercentage >= 30) {
            milestoneKey = "30";
        } else {
            milestoneKey = null;
        }

        if (milestoneKey != null) {
            // First, check if this milestone has already been sent
            db.collection("events").document(documentId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Map<String, Boolean> sentMilestones = (Map<String, Boolean>) documentSnapshot.get("sentMilestones");
                        if (sentMilestones == null) {
                            sentMilestones = new HashMap<>();
                        }
                        // If the milestone has not been sent yet, send the notification and update Firestore
                        if (sentMilestones.getOrDefault(milestoneKey, false) == false) {
                            String milestoneMessage = milestoneKey + "% capacity reached";
                            sendMilestoneNotification(eventName + "organizer", "Alert", milestoneMessage);

                            // Update the sent milestone
                            sentMilestones.put(milestoneKey, true);
                            db.collection("events").document(documentId)
                                    .update("sentMilestones", sentMilestones)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Milestone " + milestoneKey + "% updated successfully"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error updating milestone", e));
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching event for milestone check", e));
        }
    }

    /**
     * sends a notification
     * @param topic the topic of the notification
     * @param title the title of the notification
     * @param message the message attached to the notification
     */
    private void sendMilestoneNotification(String topic, String title, String message) {
        // Implementation of this method should be similar to how you're sending notifications
        // in OrganiserNotificationActivity using FcmNotificationsSender or an equivalent approach.
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                topic, // Event-specific topic for organizers
                title,
                message,
                getApplicationContext(),
                EventPage.this
        );
        notificationsSender.SendNotifications();
    }
}
