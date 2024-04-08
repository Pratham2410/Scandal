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

import org.w3c.dom.Text;

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

    TextView attendeeCount;

    ImageView imageView;
    static String imageString;
    /** Button to see QRCode */
    Button button_seeQR;
    /** Button to navigate back from the event details page. */
    FrameLayout buttonBack_ViewEventPage;
    LinearLayout buttonSignUp;
    String attendeeName;
    String promoQRCode;
    String checkInQRCode;
    String eventName;

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
        eventName = intent.getStringExtra("eventName");
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        final Map<String, String> checkedInStatus = new HashMap<>();
        checkedInStatus.put(deviceId, "No");
        if (intent.getExtras().containsKey("check")){
            if (imageString!="") {
                Bitmap posterBitmap = convertImageStringToBitmap(imageString);
                imageView.setImageBitmap(posterBitmap);
            }
            textEventLocation_ViewEventPage.setText(getIntent().getStringExtra("location")); //gets the location
            textEventTime_ViewEventPage.setText(getIntent().getStringExtra("time")); // gets the time
            textEventDescription_ViewEventPage.setText(getIntent().getStringExtra("description")); // gets description
            textEventName_ViewEventPage.setText(getIntent().getStringExtra("name")); // gets the name
            checkInQRCode = intent.getStringExtra("checkin");
            promoQRCode = intent.getStringExtra("promo");
            eventName = intent.getStringExtra("name");
            if (getIntent().getStringExtra("check").equals("1")){
                buttonSignUp.setVisibility(View.INVISIBLE); // if checked in no sign up button is provided
            }
            if (getIntent().getBooleanExtra("singUpError", false)) {
                Toast.makeText(EventDetailsActivity.this, "Please sign up before checking in", Toast.LENGTH_LONG).show();
                buttonSignUp.setVisibility(View.VISIBLE); // if checked in no sign up button is provided
                Log.e("etowsley", "This code is being accessed");
            }
        }else {
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
        }
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
            if (event_Name == null) {
                event_Name = "all"; // Fallback to "all" if no event name is provided
            } else {
                event_Name = event_Name.replace(" ", "_"); // Replace spaces with underscores
            }

// Final topic string
            String topic = "/topics/" + event_Name;
            // Check if eventName is not empty
            if (!event_Name.isEmpty()) {
                // Subscribe to the event topic
                FirebaseMessaging.getInstance().subscribeToTopic(event_Name)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(EventDetailsActivity.this, "Subscribed to event failed", Toast.LENGTH_SHORT).show();
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
                                Intent home = new Intent(EventDetailsActivity.this, HomeActivity.class);
                                startActivity(home);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());
        });
    }

    private void incrementAttendeeCount(String eventName) {
        // Reference to the event document based on the event name
        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot eventDocSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String eventDocId = eventDocSnapshot.getId();

                        // Retrieve the current attendee count and increment it
                        Number currentAttendeeCount = (Number) eventDocSnapshot.get("attendeeCount");
                        int newAttendeeCount = currentAttendeeCount != null ? currentAttendeeCount.intValue() + 1 : 1;

                        // Update the attendee count in the document
                        db.collection("events").document(eventDocId)
                                .update("attendeeCount", newAttendeeCount)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Attendee count incremented successfully"))
                                .addOnFailureListener(e -> Log.e(TAG, "Error incrementing attendee count", e));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching event to increment attendee count", e));
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

    private void saveSignUpToEvent(String eventName) {
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

                            //Changed to check if string is null before checking if string == ""
                            try {
                                if (attendeeLimitStr != null) {
                                    if (!attendeeLimitStr.isEmpty()) {
                                        attendeeLimit = Long.parseLong(attendeeLimitStr);
                                    }
                                    Number currentAttendeeCountNumber = (Number) eventData.get("attendeeCount");
                                    currentAttendeeCount = currentAttendeeCountNumber != null ? currentAttendeeCountNumber.longValue() : 0L;
                                }
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Failed to parse attendee limit or count", e);
                            }

                            Map<String, Object> signedUp = (Map<String, Object>) eventData.getOrDefault("signedUp", new HashMap<>());
                            assert signedUp != null;
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

                            if (attendeeLimit != null) {
                                Log.d(TAG, "Signing up the user with attendee limit");
                                signedUp.put(deviceId, attendeeName);
                                performSignUp(documentId, signedUp, attendeeLimit, currentAttendeeCount);
                            }
                            else {
                                Log.d(TAG, "Signing up the user without attendee limit");
                                signedUp.put(deviceId, attendeeName);
                                performSignUp(documentId, signedUp, currentAttendeeCount);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Event data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Event not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error fetching event", Toast.LENGTH_SHORT).show());
    }

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
                    checkAndSendMilestoneNotification(capacityPercentage, eventName, documentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Sign up failed", e);
                    Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                });
    }

    private void performSignUp(String documentId, Map<String, Object> signedUp, long currentAttendeeCount) {
        db.collection("events").document(documentId)
                .update("signedUp", signedUp, "attendeeCount", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Sign up successful");
                    Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();

                    // Check for milestones and send notification if necessary
                    checkAndSendMilestoneNotification(currentAttendeeCount + 1, eventName, documentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Sign up failed", e);
                    Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
                });
    }

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
    //Function for when no attendee limit has been set
    private void checkAndSendMilestoneNotification(long attendeeCount, String eventName, String documentId) {
        String milestoneKey;

        //Adjusted for milestones to function if no attendeeLimit has been set.
        if (attendeeCount >= 20) {
            milestoneKey = "20";
        } else if (attendeeCount >= 15) {
            milestoneKey = "15";
        } else if (attendeeCount >= 10) {
            milestoneKey = "10";
        } else if (attendeeCount >= 5) {
            milestoneKey = "5";
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
                            String milestoneMessage = milestoneKey + " attendees present";
                            String event_Name = eventName.replace(" ", "_");
                            String topic = "/topics/" + event_Name;
                            sendMilestoneNotification(topic + "organizer", "Alert", milestoneMessage);

                            // Update the sent milestone
                            sentMilestones.put(milestoneKey, true);
                            db.collection("events").document(documentId)
                                    .update("sentMilestones", sentMilestones)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Milestone " + milestoneKey + " updated successfully"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error updating milestone", e));
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching event for milestone check", e));
        }
    }

    private void sendMilestoneNotification(String topic, String title, String message) {
        // Implementation of this method should be similar to how you're sending notifications
        // in OrganiserNotificationActivity using FcmNotificationsSender or an equivalent approach.
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                topic, // Event-specific topic for organizers
                title,
                message,
                getApplicationContext(),
                EventDetailsActivity.this
        );
        notificationsSender.SendNotifications();
    }



    /**
     * Helper method to decode Base64 string to Bitmap.
     *
     * @param imageString The Base64-encoded image string.
     * @return The decoded Bitmap, or null if decoding fails.
     */
    private Bitmap convertImageStringToBitmap (String imageString){
        try {
            byte[] decodedByteArray = android.util.Base64.decode(imageString, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}