package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity for organizers to view details about an event, including the event poster,
 * name, description, time, location, and functionalities for viewing attendee lists and notifying users.
 */
public class OrganizerViewEventActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView eventName, eventTime, eventDescription, eventLocation;
    private ImageView eventPoster;
    private FrameLayout backBtn;
    private Button signedUpListBtn, checkedInListBtn, notifyUsersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_view_events_page);

        eventName = findViewById(R.id.textEventName_OrganisorViewEventPage);
        eventTime = findViewById(R.id.textEventTime_OrganisorViewEventPage);
        eventDescription = findViewById(R.id.textEventDescription_OrganisorViewEventPage);
        eventLocation = findViewById(R.id.textEventLocation_OrganisorViewEventPage);
        eventPoster = findViewById(R.id.imageView_OrganisorViewEventPage);
        backBtn = findViewById(R.id.buttonBack_OrganisorViewEventPage);
        signedUpListBtn = findViewById(R.id.buttoncSignedUpList_OraganisorViewEventsPage);
        checkedInListBtn = findViewById(R.id.buttoncCheckedInList_OraganisorViewEventsPage);
        //notifyUsersBtn = findViewById(R.id.button2);

        db = FirebaseFirestore.getInstance();

        // Back button functionality
        backBtn.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");

        loadEventData(eventId);
    }

    private void loadEventData(String eventId) {
        db.collection("events").document(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    eventName.setText(document.getString("name"));
                    eventTime.setText(document.getString("time"));
                    eventDescription.setText(document.getString("description"));
                    eventLocation.setText(document.getString("location"));
                    String imageString = document.getString("posterImage");
                    if (imageString != null && !imageString.isEmpty()) {
                        Bitmap bitmap = convertImageStringToBitmap(imageString);
                        if (bitmap != null) {
                            eventPoster.setImageBitmap(bitmap);
                        }
                    }
                } else {
                    Toast.makeText(OrganizerViewEventActivity.this, "No such event found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(OrganizerViewEventActivity.this, "Failed to load event data", Toast.LENGTH_SHORT).show();
            }
        });

        // Implement the functionalities for the signedUpListBtn, checkedInListBtn, and notifyUsersBtn here.
    }

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
