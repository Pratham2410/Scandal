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
 * This activity displays milestones for a specific event.
 */
public class MilestoneActivity extends AppCompatActivity {
    FrameLayout backBtn; // Back button
    ListView milestoneList; // ListView to display milestones
    FirebaseFirestore db; // Firebase Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_page); // You might need to adjust this layout
        TextView title = findViewById(R.id.list_view_header);
        title.setText("Milestones");
        backBtn = findViewById(R.id.buttonBack_MyEventsPage);
        milestoneList = findViewById(R.id.listView_MyEventsPage);
        db = FirebaseFirestore.getInstance();

        backBtn.setOnClickListener(v -> finish());

        String eventName = getIntent().getStringExtra("eventName");

        // Load milestones for the specified event
        loadMilestones(eventName);
    }

    /**
     * Load milestones for the specified event.
     *
     * @param eventName The name of the event.
     */
    private void loadMilestones(String eventName) {
        List<String> milestones = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, milestones);
        milestoneList.setAdapter(adapter);

        // Query Firestore for milestones related to the event
        db.collection("events")
                .whereEqualTo("name", eventName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        if (eventData != null && eventData.containsKey("sentMilestones")) {
                            Map<String, Boolean> sentMilestones = (Map<String, Boolean>) eventData.get("sentMilestones");
                            for (Map.Entry<String, Boolean> entry : sentMilestones.entrySet()) {
                                if (entry.getValue()) { // If the milestone has been reached/sent
                                    String milestoneEntry = entry.getKey() + "% capacity reached";
                                    milestones.add(milestoneEntry);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                });
    }
}
