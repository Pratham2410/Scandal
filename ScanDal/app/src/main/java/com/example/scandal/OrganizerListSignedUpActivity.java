package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import java.util.Map;

/**
 * Activity for displaying the list of user who signed up for an event
 */
public class OrganizerListSignedUpActivity extends AppCompatActivity {
    /**
     * FrameLayout for navigating back to the main page.
     */
    FrameLayout backMain;
    /**
     * ListView for displaying signed up users.
     */
    ListView userList;
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
        setContentView(R.layout.my_events_page); // Change to correct page later

        backMain = findViewById(R.id.buttonBack_MyEventsPage);
        userList = findViewById(R.id.listView_MyEventsPage);
        db = FirebaseFirestore.getInstance();

        backMain.setOnClickListener(v -> finish());

        loadUsers();
    }

    /**
     * Retrieves and displays events organized by the user.
     */
    private void loadUsers() {
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
                        if (eventData.containsKey("signedUp")) {
                            Map<String, Object> signedUpUsers = (Map<String, Object>) eventData.get("signedUp");
                            for (Object userNameObj : signedUpUsers.values()) {
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
