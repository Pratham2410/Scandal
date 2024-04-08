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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying events organized by the user (as determined by device ID).
 */
public class OrganizerEventActivity extends AppCompatActivity implements CustomArrayAdapter.OnItemClickListener {
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
     * List for storing event names
     */
    List<Pair<String, String>> eventNames;
    /**
     * Custom Array adapter for displaying event names
     */
    CustomArrayAdapter adapter;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after being previously shut down, this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_page); // Assuming you're reusing the same layout

        backMain = findViewById(R.id.buttonBack_MyEventsPage);
        eventsList = findViewById(R.id.listView_MyEventsPage);
        db = FirebaseFirestore.getInstance();

        backMain.setOnClickListener(v -> finish());

        //Initialize Adapter & OnClickListener
        eventNames = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.list_item_layout, eventNames);
        adapter.setOnItemClickListener(OrganizerEventActivity.this);
        eventsList.setAdapter(adapter);
        loadEvents();
    }

    /**
     * Retrieves and displays events organized by the user.
     */
    private void loadEvents() {
        //Make Header
        eventNames.add(new Pair<>("Name", "Attendee Limit"));
        //Load rest of data
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .whereEqualTo("organizer", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String eventName = documentSnapshot.getString("name");
                        String limit = documentSnapshot.getString("attendeeLimit");
                        if (eventName != null && limit != null) {
                            eventNames.add(new Pair<>(eventName, limit));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Pair<String, String> eventObject = eventNames.get(position);
        String eventName = eventObject.first;
        //Intent intent = new Intent(OrganizerEventActivity.this, SignedUpEventDetailsActivity.class); // Use appropriate activity to show event details
        Intent intent = new Intent(OrganizerEventActivity.this, OrganizerViewEventActivity.class);
        intent.putExtra("eventName", eventName);
        startActivity(intent);
    }
}
