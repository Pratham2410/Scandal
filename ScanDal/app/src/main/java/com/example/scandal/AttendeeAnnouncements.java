package com.example.scandal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activity to display announcements for a specific event.
 */
public class AttendeeAnnouncements extends AppCompatActivity {
    FrameLayout backMain;
    ListView announcementList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_page); // Assuming this layout fits the purpose
        TextView txtMyEvents = findViewById(R.id.list_view_header);
        txtMyEvents.setText("Announcements");
        backMain = findViewById(R.id.buttonBack_MyEventsPage);
        announcementList = findViewById(R.id.listView_MyEventsPage);
        db = FirebaseFirestore.getInstance();

        backMain.setOnClickListener(v -> finish());

        // Retrieve the event name from the intent
        String eventName = getIntent().getStringExtra("eventName");

        loadAnnouncements(eventName); // Pass the event name to the method
    }

    /**
     * Retrieves and displays announcements for the specified event.
     */
    private void loadAnnouncements(String eventName) {
        List<String> announcements = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, announcements);
        announcementList.setAdapter(adapter);

        db.collection("events")
                .whereEqualTo("name", eventName) // Filter by the event name
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData != null && eventData.containsKey("announcements")) {
                            Map<String, String> eventAnnouncements = (Map<String, String>) eventData.get("announcements");
                            for (Map.Entry<String, String> entry : eventAnnouncements.entrySet()) {
                                String announcementEntry = entry.getKey() + ": " + entry.getValue();
                                announcements.add(announcementEntry);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                });
    }
}
