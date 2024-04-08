package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying the list of user who signed up for an event
 */
public class OrganizerListCheckedInActivity extends AppCompatActivity implements CustomArrayAdapter.OnItemClickListener {
    /**
     * FrameLayout for navigating back to the main page.
     */
    FrameLayout backMain;
    /**
     * ListView for displaying signed up users.
     */
    ListView userList;
    /**
     * Firebase Firestore instance for database operations.
     */
    FirebaseFirestore db;
    CustomArrayAdapter adapter;
    List<Pair<String, String>> checkedInAttendeeNamesWithCount;
    String attendeeNames;
    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after being previously shut down, this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    private Button viewLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_attendees_page); // Assume this is the correct layout
        TextView txtMyEvents = findViewById(R.id.list_view_header);
        txtMyEvents.setText("Checked-In Attendees");

        backMain = findViewById(R.id.buttonBack_EventsAttendeesPage);
        userList = findViewById(R.id.listView_EventsAttendeesPage);
        viewLocationBtn = findViewById(R.id.buttonViewLocation_EventsAttendeesPage);
        db = FirebaseFirestore.getInstance();

        // Retrieve the event name from the intent
        String eventName = getIntent().getStringExtra("eventName");

        backMain.setOnClickListener(v -> finish());

        //Initialize Adapter
        checkedInAttendeeNamesWithCount = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.list_item_layout, checkedInAttendeeNamesWithCount);
        adapter.setOnItemClickListener(OrganizerListCheckedInActivity.this);
        userList.setAdapter(adapter);
        loadCheckedInUsers(eventName);

        viewLocationBtn.setOnClickListener(v -> {
            if(attendeeNames !=null){
                db.collection("profiles")
                        .whereEqualTo("name", attendeeNames)
                        .limit(1)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Device is already registered, fetch and display profile data
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                Map<String, Object> profileData = documentSnapshot.getData();
                                if (Integer.parseInt(profileData.get("GeoTracking").toString()) == 1 && profileData.get("userLocation") != null) {
                                    Intent mapIntent = new Intent(OrganizerListCheckedInActivity.this, MapActivity.class);
                                    mapIntent.putExtra("attendeeName", attendeeNames);
                                    startActivity(mapIntent);

                                } else {
                                    Toast.makeText(getApplicationContext(), "user disabled geo-tracking", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());
            }
            else{
                Toast.makeText(OrganizerListCheckedInActivity.this, "Please select an attendee first", Toast.LENGTH_SHORT).show();
            }

        });

    }
    /**
     * Retrieves and displays users checked in for the specified event.
     */
    private void loadCheckedInUsers(String eventName) {
        List<String> checkedInAttendeeNamesWithCount = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, checkedInAttendeeNamesWithCount);
        userList.setAdapter(adapter);

        db.collection("events")
                .whereEqualTo("name", eventName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot eventDoc = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> eventData = eventDoc.getData();
                        if (eventData != null) {
                            List<String> checkedInDeviceIds = (List<String>) eventData.get("checkedIn");
                            Map<String, String> signedUpUsers = (Map<String, String>) eventData.get("signedUp");
                            Map<String, Long> checkedInCount = (Map<String, Long>) eventData.get("checkedIn_count");

                            if (checkedInDeviceIds != null && signedUpUsers != null && checkedInCount != null) {
                                for (String deviceId : checkedInDeviceIds) {
                                    String attendeeName = signedUpUsers.get(deviceId);
                                    Long count = checkedInCount.get(deviceId);
                                    if (attendeeName != null && count != null) {
                                        checkedInAttendeeNamesWithCount.add(new Pair<>(attendeeName, String.valueOf(count)));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(OrganizerListCheckedInActivity.this, "No attendees checked in yet.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(OrganizerListCheckedInActivity.this, "Error loading checked in users.", Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onItemClick(int position) {
        Pair<String, String> eventObject = adapter.getItem(position);
        String eventName = eventObject.first;
        //Intent intent = new Intent(OrganizerEventActivity.this, SignedUpEventDetailsActivity.class); // Use appropriate activity to show event details
        Intent intent = new Intent(OrganizerListCheckedInActivity.this, OrganizerViewEventActivity.class);
        intent.putExtra("eventName", eventName);
        startActivity(intent);
    }
}
