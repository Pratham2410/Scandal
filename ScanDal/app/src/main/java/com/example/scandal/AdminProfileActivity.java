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

public class AdminProfileActivity extends AppCompatActivity {
    FrameLayout backToAdmin;
    ListView profilesList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_list); // Use profile_list.xml

        db = FirebaseFirestore.getInstance();
        profilesList = findViewById(R.id.profilesList_AdminProfilePage);
        backToAdmin = findViewById(R.id.buttonBack_AdminProfilePage);

        backToAdmin.setOnClickListener(v -> finish());

        loadProfiles(); // Changed method name to reflect loading profiles
    }

    private void loadProfiles() {
        List<String> profileNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profileNames);
        profilesList.setAdapter(adapter);

        db.collection("profiles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String profileName = document.getString("name");
                    if (profileName != null) {
                        profileNames.add(profileName);
                    }
                }
                adapter.notifyDataSetChanged(); // Refresh the ListView
            } else {
                // Handle errors here
            }
        });
    }
}
