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

public class AdminProfileActivity extends AppCompatActivity {
    FrameLayout backToAdmin;
    ListView profilesList;
    FirebaseFirestore db;
    // Store profile names and their Firestore document IDs
    Map<String, String> profileNameToId = new HashMap<>();
    ArrayAdapter<String> adapter; // Declare the adapter at the class level to access it easily
    List<String> profileNames; // Store the profile names here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_list);

        db = FirebaseFirestore.getInstance();
        profilesList = findViewById(R.id.profilesList_AdminProfilePage);
        backToAdmin = findViewById(R.id.buttonBack_AdminProfilePage);

        // Initialize your adapter and profileNames list here
        profileNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profileNames);
        profilesList.setAdapter(adapter);

        backToAdmin.setOnClickListener(v -> finish());
        loadProfiles();

        // Set an item click listener to delete the profile on click
        profilesList.setOnItemClickListener((parent, view, position, id) -> {
            String profileName = (String) parent.getItemAtPosition(position);
            String profileId = profileNameToId.get(profileName);
            if (profileId != null) {
                showDeleteConfirmationDialog(profileId, profileName);
            }
        });
    }

    private void loadProfiles() {
        profileNames.clear(); // Clear previous data
        profileNameToId.clear(); // Clear previous data
        db.collection("profiles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String profileName = document.getString("name"); // Assuming a 'name' field exists
                    if (profileName != null) {
                        profileNames.add(profileName);
                        profileNameToId.put(profileName, document.getId());
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
            } else {
                Toast.makeText(AdminProfileActivity.this, "Failed to load profiles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProfile(String profileId) {
        db.collection("profiles").document(profileId).delete().addOnSuccessListener(aVoid -> {
            Toast.makeText(AdminProfileActivity.this, "Profile deleted successfully", Toast.LENGTH_SHORT).show();
            loadProfiles(); // Reload to reflect changes
        }).addOnFailureListener(e -> Toast.makeText(AdminProfileActivity.this, "Error deleting profile", Toast.LENGTH_SHORT).show());
    }

    private void showDeleteConfirmationDialog(String profileId, String profileName) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete " + profileName + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteProfile(profileId);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
