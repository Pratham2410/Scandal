package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying events attended by an attendee.
 */
public class AttendeeEventActivity extends AppCompatActivity {
    /**
     * FrameLayout for navigating back to the main page.
     */
    FrameLayout backMain;
    /**
     * ListView for displaying events.
     */
    ListView eventsList;
    /**
     * Firebase Firestore instance for database operations.
     */
    FirebaseFirestore db;
    /**
     * A string storing the divice id
     */

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, initializing objects, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_page);

        backMain = findViewById(R.id.buttonBack_MyEventsPage);
        eventsList = findViewById(R.id.listView_MyEventsPage);
        db = FirebaseFirestore.getInstance();

        backMain.setOnClickListener(v -> finish());
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fullEventName = (String) parent.getItemAtPosition(position);
                // Remove the status part from the event name
                String eventName = fullEventName.split("   \\(")[0];
                Intent intent = new Intent(AttendeeEventActivity.this, SignedUpEventDetailsActivity.class);
                intent.putExtra("eventName", eventName);
                startActivity(intent);

            }
        });
        loadEvents();

    }
    /**
     * Retrieves and displays event pulled from firebase
     */

    private String getCheckedInEventName() {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final String[] checkedInEventName = new String[1];
        db.collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData.containsKey("checkedIn")) {
                            List<String> checkedInUsers = (List<String>) eventData.get("checkedIn");
                            if (checkedInUsers.contains(deviceId)) {
                                // Assuming each event document has a 'name' field
                                Log.e("etowsley", "Event found");
                                checkedInEventName[0] = documentSnapshot.getString("name");
                                Log.e("etowsley", checkedInEventName[0]);
                            }
                        }
                    }
                });
        if (checkedInEventName[0] == null) {
            Log.e("etowsley", "checkInEventName was null");
        }
        return checkedInEventName[0];
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh your events list every time the activity resumes
        loadEvents();
    }
    private void loadEvents() {
        List<String> eventNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        eventsList.setAdapter(adapter);

        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData.containsKey("signedUp")) {
                            Map<String, Object> signedUpUsers = (Map<String, Object>) eventData.get("signedUp");
                            if (signedUpUsers.containsKey(deviceId)) {
                                // Assuming each event document has a 'name' field
                                String eventName = documentSnapshot.getString("name");
                                if (eventName != null) {
                                    // Check if the user is checked in
                                    List<String> checkedInUsers = (List<String>) eventData.get("checkedIn");
                                    if (checkedInUsers != null && checkedInUsers.contains(deviceId)) {
                                        eventNames.add(eventName + "   (Checked In)");
                                    } else {
                                        eventNames.add(eventName);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("loadEvents", "Error loading events", e));
    }


}
