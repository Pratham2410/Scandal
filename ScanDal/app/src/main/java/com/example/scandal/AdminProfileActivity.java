package com.example.scandal;


import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
 * Activity for managing profiles from the admin's perspective.
 */
public class AdminProfileActivity extends AppCompatActivity {
    /**
     * FrameLayout for navigating back to the admin interface.
     */
    FrameLayout backToAdmin;
    /**
     * Button to delete profile
     */
    Button buttonDelete;
    /**
     * ListView for displaying the list of profiles.
     */
    ListView profilesList;
    /**
     * FirebaseFirestore instance for interacting with Firestore database.
     */
    FirebaseFirestore db;
    /**
     * Map to store profile names and their Firestore document IDs.
     */
    Map<String, String> profileNameToId = new HashMap<>();
    /**
     * ArrayAdapter to populate profile names in the ListView.
     */
    ArrayAdapter<String> adapter; // Declare the adapter at the class level to access it easily
    /**
     * List to store profile names.
     */
    List<String> profileNames; // Store the profile names here
    /**
     * String to store profile name
     */
    String profileName;
    /**
     * String to store profile id
     */
    String profileId;


    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, initializing objects, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_profile_list); // Use profile_list.xml


        db = FirebaseFirestore.getInstance();
        profilesList = findViewById(R.id.profilesList_AdminProfilePage);
        backToAdmin = findViewById(R.id.buttonBack_AdminProfilePage);
        buttonDelete = findViewById(R.id.buttonDelete_AdminProfileListPage);

        // Initialize your adapter and profileNames list here
        profileNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profileNames);
        profilesList.setAdapter(adapter);

        backToAdmin.setOnClickListener(v->{
            Intent intent = new Intent(AdminProfileActivity.this, AdminActivity.class);
            startActivity(intent);
        });
        loadProfiles();

        // Set an item click listener to delete the profile on click
        profilesList.setOnItemClickListener((parent, view, position, id) -> {
            profileName = (String) parent.getItemAtPosition(position);
            profileId = profileNameToId.get(profileName);
            Toast.makeText(AdminProfileActivity.this, profileName+" is selected", Toast.LENGTH_SHORT).show();
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileId != null) {
                    showDeleteConfirmationDialog(profileId, profileName);
                }
            }
        });
    }
    /**
     * Loads profiles from Firestore database and populates the ListView.
     */
    private void loadProfiles() {
        profileNames.clear(); // Clear previous data
        profileNameToId.clear(); // Clear previous data
        db.collection("profiles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String profileName = document.getString("name");
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
    /**
     * Deletes a profile from Firestore database.
     *
     * @param profileId The ID of the profile to be deleted.
     */
    private void deleteProfile(String profileId) {
        db.collection("profiles").document(profileId).delete().addOnSuccessListener(aVoid -> {
            Toast.makeText(AdminProfileActivity.this, "Profile deleted successfully", Toast.LENGTH_SHORT).show();
            loadProfiles(); // Reload to reflect changes
        }).addOnFailureListener(e -> Toast.makeText(AdminProfileActivity.this, "Error deleting profile", Toast.LENGTH_SHORT).show());
    }
    /**
     * Displays a confirmation dialog before deleting a profile.
     *
     * @param profileId   The ID of the profile to be deleted.
     * @param profileName The name of the profile to be deleted.
     */
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
