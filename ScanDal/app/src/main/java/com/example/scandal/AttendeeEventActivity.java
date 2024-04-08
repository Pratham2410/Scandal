package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying events attended by an attendee.
 */
public class AttendeeEventActivity extends AppCompatActivity implements CustomArrayAdapter.OnItemClickListener {
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
    List<Pair<String, String>> eventNames;
    CustomArrayAdapter adapter;

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

        //Initialize Adapter
        eventNames = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.list_item_layout, eventNames);
        adapter.setOnItemClickListener(AttendeeEventActivity.this);
        eventsList.setAdapter(adapter);
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
    //Causes events to load twice
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Refresh your events list every time the activity resumes
//        loadEvents();
//    }
    private void loadEvents() {
        eventNames.clear();
        eventNames.add(new Pair<>("Event Name", "Status"));
        Log.d("etowsley", "Added header");
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
                                        Log.d("etowsley", "Adding event name: " + eventName);
                                        eventNames.add(new Pair<>(eventName, "Checked in"));
                                    } else {
                                        eventNames.add(new Pair<>(eventName, "Signed up"));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("loadEvents", "Error loading events", e));
    }


    @Override
    public void onItemClick(int position) {
        // Ensure the position is within the bounds of your data source
        if (position >= 0 && position < eventNames.size()) {
            Pair<String, String> eventObject = eventNames.get(position);
            String eventName = eventObject.first;
            Toast.makeText(AttendeeEventActivity.this, eventName, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AttendeeEventActivity.this, SignedUpEventDetailsActivity.class);
            intent.putExtra("eventName", eventName);
            startActivity(intent);
        }
    }
}
