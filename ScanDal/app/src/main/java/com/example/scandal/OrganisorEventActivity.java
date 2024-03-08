package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity for creating a new event from the organizer's perspective
 */
public class OrganisorEventActivity extends AppCompatActivity {
    /**
     * Button that leads to organizer's homepage
     */
    FrameLayout backToOrganiser;
    /**
     * Button leading back to homepage
     */
    FrameLayout buttonBackToHomepage;
    /**
     * A View to display all of an organizer's events
     */

    ListView eventsList;
    /**
     * An instance of firestore database
     */
    FirebaseFirestore db;

    /**
     * Provides functionality for layout buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_events_page);

        db = FirebaseFirestore.getInstance();
        eventsList = findViewById(R.id.eventsList_OrganisorEventsPage);
        backToOrganiser = findViewById(R.id.buttonBack_OrganisorEventsPage);
        buttonBackToHomepage = findViewById(R.id.buttonBackToHomepage);

        backToOrganiser.setOnClickListener(v -> finish());
        // Navigate back to home page
        buttonBackToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_home = new Intent(OrganisorEventActivity.this, HomeActivity.class);
                startActivity(intent_home);
            }
        });

        loadEvents();
    }

    /**
     * Loads and displays events from firebase
     */
    private void loadEvents() {
        List<String> eventNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        eventsList.setAdapter(adapter);

        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String eventName = document.getString("name"); // Assuming you have a 'name' field for event names
                    if (eventName != null) {
                        eventNames.add(eventName);
                    }
                }
                adapter.notifyDataSetChanged(); // Refresh the list view with the new data
            } else {
                // Handle errors here
            }
        });
    }
}
