package com.example.scandal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminEventActivity extends AppCompatActivity {
    FrameLayout backToAdmin;
    ListView eventsList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_events_page);

        db = FirebaseFirestore.getInstance();
        eventsList = findViewById(R.id.eventsList_AdminEventsPage);
        backToAdmin = findViewById(R.id.buttonBack_AdminEventsPage);

        backToAdmin.setOnClickListener(v -> finish());

        loadEvents();
    }

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
