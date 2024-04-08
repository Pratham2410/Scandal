package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity allows organizers to send notifications to attendees of an event.
 */
public class OrganiserNotificationActivity extends AppCompatActivity {

    private EditText editTitle, editDescription; // Input fields for title and description
    private FirebaseFirestore db; // Firestore instance
    private Button buttonSend; // Button to send notification
    private FrameLayout buttonBack; // Back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification); // Use your layout file name

        // Initialize views
        editTitle = findViewById(R.id.editTitle);
        db = FirebaseFirestore.getInstance();
        editDescription = findViewById(R.id.editDescription);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBack = findViewById(R.id.buttonBack_Send_Notification);

        // Retrieve the event name from the intent
        Intent intent = getIntent();
        String event_Name = intent.getStringExtra("event_Name");
        String event_Name1 = intent.getStringExtra("event_Name");
        if (event_Name == null) {
            event_Name = "all"; // Fallback to "all" if no event name is provided
        } else {
            event_Name = event_Name.replace(" ", "_"); // Replace spaces with underscores
        }

        // Final topic string
        String topic = "/topics/" + event_Name;

        // Set the onClickListener for the back button
        buttonBack.setOnClickListener(v -> finish());

        String finalEvent_Name = event_Name;
        buttonSend.setOnClickListener(v -> {
            // Retrieve title and message from EditTexts
            String title = editTitle.getText().toString().trim();
            String message = editDescription.getText().toString().trim();

            // Check if the title or message is empty
            if (title.isEmpty() || message.isEmpty()) {
                // You can show a Toast message here to inform the user to fill in the fields
                return;
            }

            // Find the event document by name
            db.collection("events").whereEqualTo("name", event_Name1).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        // Assuming event names are unique, get the first document found
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        DocumentReference eventRef = documentSnapshot.getReference();

                        // Prepare the announcement to be added
                        Map<String, String> announcement = new HashMap<>();
                        announcement.put(title, message);

                        // Add announcement to the "announcements" map in the document
                        eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> eventDetails = documentSnapshot.getData();
                                if (eventDetails != null) {
                                    Map<String, String> announcements;
                                    if (eventDetails.containsKey("announcements")) {
                                        announcements = (Map<String, String>) eventDetails.get("announcements");
                                    } else {
                                        announcements = new HashMap<>();
                                    }
                                    announcements.put(title, message);
                                    eventRef.update("announcements", announcements)
                                            .addOnSuccessListener(aVoid -> Log.d("UpdateSuccess", "Announcements updated successfully"))
                                            .addOnFailureListener(e -> Log.e("UpdateFailure", "Failed to update announcements", e));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FirestoreError", "Error fetching document", e);
                            }
                        });
                    } else {
                        Log.d("Firestore", "No matching event found");
                    }
                } else {
                    Log.e("FirestoreError", "Failed to search for event", task.getException());
                }
            });

            // Initialize your FcmNotificationsSender with the title and message
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                    topic, // Use the event-specific topic
                    title,
                    message,
                    getApplicationContext(),
                    OrganiserNotificationActivity.this
            );

            Log.d("NotificationButton", "Send Notification button clicked");

            // Send the notification
            notificationsSender.SendNotifications();

            // Log after sending notification
            Log.d("NotificationButton", "Notification sent, no further action");
        });
    }
}
