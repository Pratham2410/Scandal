package com.example.scandal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Activity for managing events from the admin's perspective.
 */
public class AdminEventActivity extends AppCompatActivity {
    /**
     * FrameLayout for navigating back to the admin interface.
     */
    FrameLayout backToAdmin;

    /**
     * ListView for displaying the list of events.
     */
    ListView eventsList;

    /**
     * FirebaseFirestore instance for interacting with Firestore database.
     */
    FirebaseFirestore db;

    /**
     * Map to store event names and their corresponding Firestore document IDs.
     */
    Map<String, String> eventNameToId = new HashMap<>();

    /**
     * ArrayAdapter for populating event names in the ListView.
     */
    ArrayAdapter<String> adapter;

    /**
     * List to store event names.
     */
    List<String> eventNames;

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, initializing objects, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_events_page);

        db = FirebaseFirestore.getInstance();
        eventsList = findViewById(R.id.eventsList_AdminEventsPage);
        backToAdmin = findViewById(R.id.buttonBack_AdminEventsPage);

        // Initialize your adapter and eventNames list here
        eventNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        eventsList.setAdapter(adapter);

        backToAdmin.setOnClickListener(v -> finish());
        loadEvents();

        // Set an item click listener to delete the event on click
        eventsList.setOnItemClickListener((parent, view, position, id) -> {
            String eventName = (String) parent.getItemAtPosition(position);
            String eventId = eventNameToId.get(eventName);
            if (eventId != null) {
                showDeleteConfirmationDialog(eventId, eventName);
            }
        });
    }
    /**
     * Loads events from Firestore database and populates the ListView.
     */
    private void loadEvents() {
        eventNames.clear(); // Clear previous data
        eventNameToId.clear(); // Clear previous data
        db.collection("events").get().addOnCompleteListener(task -> { // Changed to "profiles" as per your request
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String eventName = document.getString("name"); // Assuming the profiles have a 'name' field
                    if (eventName != null) {
                        eventNames.add(eventName);
                        eventNameToId.put(eventName, document.getId());
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
            } else {
                Toast.makeText(AdminEventActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Deletes the event from Firestore database.
     *
     * @param eventId   ID of the event to be deleted.
     */
    private void deleteEvent(String eventId) {
        db.collection("events").document(eventId).delete().addOnSuccessListener(aVoid -> { // Changed to "profiles"
            Toast.makeText(AdminEventActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            loadEvents(); // Reload to reflect changes
        }).addOnFailureListener(e -> Toast.makeText(AdminEventActivity.this, "Error deleting event", Toast.LENGTH_SHORT).show());
    }
    /**
     * Displays a confirmation dialog before deleting the event.
     *
     * @param eventId   ID of the event to be deleted.
     * @param eventName Name of the event to be deleted.
     */
    private void showDeleteConfirmationDialog(String eventId, String eventName) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete " + eventName + "?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteEvent(eventId))
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
