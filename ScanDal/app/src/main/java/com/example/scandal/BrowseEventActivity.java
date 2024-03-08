package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
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
 * Activity for browsing events.
 */
public class BrowseEventActivity extends AppCompatActivity {

    /**
     * FrameLayout for navigating back to the previous page.
     */
    FrameLayout buttonBack_BrowseEventsPage;

    /**
     * ListView for displaying events.
     */
    ListView eventsList;

    /**
     * Firebase Firestore instance for database operations.
     */
    FirebaseFirestore db;

       /**
       * Provides functionality for event list
       */
        buttonBack_BrowseEventsPage.setOnClickListener(v -> finish());
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(BrowseEventActivity.this, EventDetailsActivity.class);
                intent.putExtra("eventName", eventName);
                startActivity(intent);
            }
        });
        loadEvents();
    }
   /**
     * Retrieves and displays profiles pulled from firebase
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
