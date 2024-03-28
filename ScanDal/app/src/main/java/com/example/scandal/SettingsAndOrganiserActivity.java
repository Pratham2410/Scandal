package com.example.scandal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/** An Activity for managing the settings page */
public class SettingsAndOrganiserActivity extends AppCompatActivity {
    /**
     * An integer representing the GeoTracking Status(Set to 0 representing no as default)
     */
    public Integer GeoTracking;
    /**
     * Button leading to organizer page
     */
    LinearLayout buttonGotoOrganisorPage;
    /**
     * Button to allow geotracking for event verification
     */
    AppCompatButton buttonGeoTracking;
    /**
     * Button leading back to settings page from organizer page
     */
    FrameLayout buttonBack_SettingsAndOrganisorPage; // Corrected variable name
    /**
     * Button leading back to homepage
     */
    FrameLayout buttonBackToHomepage;
    /**
     * Provides functionality for buttons on settings page
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    private String onMessage = "Geo-Tracking: On";
    private String offMessage = "Geo-Tracking: Off";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_and_organisor_page);

        buttonGotoOrganisorPage = findViewById(R.id.buttonGotoOrganisorPage);
        buttonBack_SettingsAndOrganisorPage = findViewById(R.id.buttonBack_SettingsAndOrganisorPage); // Corrected ID reference
        buttonBackToHomepage = findViewById(R.id.buttonBackToHomepage);
        buttonGeoTracking = findViewById(R.id.buttonGeoTracking_SettingsPage);

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        // Check if the device is already registered
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        db.collection("profiles")
                .whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Device is already registered
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> profileData = documentSnapshot.getData();
                        if (profileData != null) {
                            if(Integer.parseInt(profileData.get("GeoTracking").toString()) == 1){
                                buttonGeoTracking.setText((String)onMessage);
                            }
                            buttonGeoTracking.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(buttonGeoTracking.getText().equals((String)offMessage)){
                                        Integer onInt=1;
                                        profileData.put("GeoTracking", onInt);
                                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                        db.collection("profiles").document(documentId)
                                                .set(profileData)
                                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Geolocation tracking enabled", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update GeoTracking", Toast.LENGTH_SHORT).show());
                                        buttonGeoTracking.setText((String)onMessage);
                                    }
                                    else{
                                        Integer offInt=0;
                                        profileData.put("GeoTracking", offInt);
                                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                        db.collection("profiles").document(documentId)
                                                .set(profileData)
                                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Geolocation tracking disabled", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update GeoTracking", Toast.LENGTH_SHORT).show());
                                        buttonGeoTracking.setText((String)offMessage);
                                    }
                                }
                            });
                        }
                    } else {
                        // Device is not registered, let the user enter new information
                        Toast.makeText(getApplicationContext(), "Please enter your information first", Toast.LENGTH_SHORT).show();
                        buttonGeoTracking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Please enter your information first", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());

        // Navigate back to home page
        buttonBackToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_home = new Intent(SettingsAndOrganiserActivity.this, HomeActivity.class);
                startActivity(intent_home);
            }
        });

        buttonGotoOrganisorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open OrganisorActivity
                Intent intent = new Intent(SettingsAndOrganiserActivity.this, OrganisorActivity.class);
                startActivity(intent);
            }
        });

        buttonBack_SettingsAndOrganisorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open HomeActivity
                Intent intent = new Intent(SettingsAndOrganiserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Initialize other components and set their listeners if necessary
    }
}
