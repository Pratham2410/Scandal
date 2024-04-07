package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

/** An activity for organizers to view details of events */
public class OrganizerViewEventActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    TextView textEventName, textEventDescription, textEventTime, textEventLocation;
    ImageView imageView;
    FrameLayout buttonBack;
    String promoQRCode;
    private Button signedUpListBtn, checkedInListBtn, notifyUsersBtn;
    private Button milestonesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_view_events_page); // Change layout name accordingly

        textEventName = findViewById(R.id.textEventName_OrganisorViewEventPage);
        textEventDescription = findViewById(R.id.textEventDescription_OrganisorViewEventPage);
        textEventTime = findViewById(R.id.textEventTime_OrganisorViewEventPage);
        textEventLocation = findViewById(R.id.textEventLocation_OrganisorViewEventPage);
        imageView = findViewById(R.id.imageView_OrganisorViewEventPage);
        buttonBack = findViewById(R.id.buttonBack_OrganisorViewEventPage);
        signedUpListBtn = findViewById(R.id.buttoncSignedUpList_OraganisorViewEventsPage);
        checkedInListBtn = findViewById(R.id.buttoncCheckedInList_OraganisorViewEventsPage);
        notifyUsersBtn = findViewById(R.id.button2);
        milestonesBtn = findViewById(R.id.buttonMilestones); // Make sure you have this ID in your layout

        milestonesBtn.setOnClickListener(v -> {
            Intent milestoneIntent = new Intent(OrganizerViewEventActivity.this, MilestoneActivity.class);
            milestoneIntent.putExtra("eventName", textEventName.getText().toString()); // Pass the event name to MilestoneActivity
            startActivity(milestoneIntent);
        });


        db = FirebaseFirestore.getInstance();

        buttonBack.setOnClickListener(v -> finish());

        notifyUsersBtn.setOnClickListener(v -> {
            // Get the event name from the textEventName TextView
            String event_Name = textEventName.getText().toString();

            // Create an intent to start OrganiserNotificationActivity
            Intent notifyIntent = new Intent(OrganizerViewEventActivity.this, OrganiserNotificationActivity.class);

            // Put the event name into the intent to pass to the OrganiserNotificationActivity
            notifyIntent.putExtra("event_Name", event_Name);

            // Start the OrganiserNotificationActivity
            startActivity(notifyIntent);
        });


        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Button to view the list of users who signed up
        signedUpListBtn.setOnClickListener(v -> {
            Intent signedUpIntent = new Intent(OrganizerViewEventActivity.this, OrganizerListSignedUpActivity.class);
            signedUpIntent.putExtra("eventName", eventName); // Pass the event ID to the next activity if needed
            startActivity(signedUpIntent);
        });

        // Button to view the list of users who checked in
        checkedInListBtn.setOnClickListener(v -> {
            Intent checkedInIntent = new Intent(OrganizerViewEventActivity.this, OrganizerListCheckedInActivity.class);
            checkedInIntent.putExtra("eventName", eventName); // Pass the event ID to the next activity if needed
            startActivity(checkedInIntent);
        });

        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData != null) {
                            textEventName.setText((String) eventData.get("name"));
                            textEventTime.setText((String) eventData.get("time"));
                            textEventLocation.setText((String) eventData.get("location"));
                            textEventDescription.setText((String) eventData.get("description"));
                            promoQRCode = (String) eventData.get("PromoQRCode");
                            String imageString = (String) eventData.get("posterImage");
                            if (imageString != null) {
                                Bitmap bitmap = convertImageStringToBitmap(imageString);
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Event not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch event data", Toast.LENGTH_SHORT).show());
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
