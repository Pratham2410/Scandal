package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
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
 * Activity for displaying the list of users who have checked in for an event
 */
public class OrganizerListCheckedInActivity extends AppCompatActivity {
    FrameLayout backMain;
    ListView userList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_page); // Use the same layout as for signed up
        TextView txtMyEvents = findViewById(R.id.txtMyEvents);
        txtMyEvents.setText("CheckedIn Attendees");
        backMain = findViewById(R.id.buttonBack_MyEventsPage);
        userList = findViewById(R.id.listView_MyEventsPage);
        db = FirebaseFirestore.getInstance();

        backMain.setOnClickListener(v -> finish());

        loadCheckedInUsers();
    }

    /**
     * Retrieves and displays events checked in by the user.
     */
    private void loadCheckedInUsers() {
        List<String> userNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        userList.setAdapter(adapter);

        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.collection("events")
                .whereEqualTo("organizer", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData.containsKey("checkedIn")) {
                            Map<String, Object> checkedInUsers = (Map<String, Object>) eventData.get("checkedIn");
                            for (Object userNameObj : checkedInUsers.values()) {
                                String userName = (String) userNameObj;
                                if (userName != null) {
                                    userNames.add(userName);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }
}
