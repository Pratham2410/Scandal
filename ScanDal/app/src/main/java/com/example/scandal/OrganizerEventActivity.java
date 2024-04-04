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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying events organized by the user (as determined by device ID).
 */
public class OrganizerEventActivity extends AppCompatActivity {
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
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = (String) parent.getItemAtPosition(position);
                //Intent intent = new Intent(OrganizerEventActivity.this, SignedUpEventDetailsActivity.class); // Use appropriate activity to show event details
                Intent intent = new Intent(OrganizerEventActivity.this, OrganizerViewEventActivity.class);
                intent.putExtra("eventName", eventName);
                startActivity(intent);
            }
        });
        loadEvents();
    }

    /**
     * Retrieves and displays events organized by the user.
     */
    private void loadEvents() {
        List<String> eventNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        eventsList.setAdapter(adapter);

        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .whereEqualTo("organizer", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String eventName = documentSnapshot.getString("name");
                        if (eventName != null) {
                            eventNames.add(eventName);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
