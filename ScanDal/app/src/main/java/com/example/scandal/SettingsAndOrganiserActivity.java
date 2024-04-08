package com.example.scandal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/** An Activity for managing the settings page */
public class SettingsAndOrganiserActivity extends AppCompatActivity implements IBaseGpsListener {
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
     * Provides functionality for buttons on settings page
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    private String onMessage = "Geo-Tracking: On";
    private String offMessage = "Geo-Tracking: Off";
    FirebaseFirestore db;
    final int FINE_PERMISSION_CODE = 1;
    private static final int PERMISSION_LOCATION = 1000;
    Location currentLocation;
    CountDownTimer timer;
    Integer verifyLocation = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_and_organisor_page);

        buttonGotoOrganisorPage = findViewById(R.id.buttonGotoOrganisorPage);
        buttonBack_SettingsAndOrganisorPage = findViewById(R.id.buttonBack_SettingsAndOrganisorPage); // Corrected ID reference
        buttonGeoTracking = findViewById(R.id.buttonGeoTracking_SettingsPage);


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

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                                        } else {
                                            showLocation();
                                        }

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
                        }else{
                            Toast.makeText(getApplicationContext(), "Please enter your name first", Toast.LENGTH_SHORT).show();
                            buttonGeoTracking.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(), "Please enter your name first", Toast.LENGTH_SHORT).show();
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
    private void showLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }else {
            Toast.makeText(getApplicationContext(), "Please enable gps", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_LOCATION){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                showLocation();
            }
        }else{
            Toast.makeText(getApplicationContext(), "permission not allowed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void hereLocation(Location location) {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        currentLocation = location;
        //Toast.makeText(getApplicationContext(), "Long: "+location.getLongitude()+" Lat:"+location.getLatitude(), Toast.LENGTH_SHORT).show();
        db.collection("profiles").whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> preData = documentSnapshot.getData();
                        if (preData.get("GeoTracking") != null && Integer.parseInt(preData.get("GeoTracking").toString()) == 1) {

                            preData.put("userLocation", location);
                        }

                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("profiles").document(documentId)
                                .set(preData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Location updated successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update location", Toast.LENGTH_SHORT).show());
                        //Toast.makeText(getApplicationContext(), "asd "+preData.get("userLocation"), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(getApplicationContext(), "asd "+location.getLatitude()+location.getLongitude(), Toast.LENGTH_SHORT).show();
        if(verifyLocation == 1){
            verifyLocation = 0;
            final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            db.collection("profiles").whereEqualTo("deviceId", deviceId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Map<String, Object> preData = documentSnapshot.getData();
                            if (preData.get("GeoTracking") != null && Integer.parseInt(preData.get("GeoTracking").toString()) == 1) {
                                timer = new CountDownTimer(100000,100000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        verifyLocation = 1;
                                    }
                                }.start();
                                hereLocation(location);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        //n
    }

    @Override
    public void onProviderEnabled(String provider) {
        //n
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //n
    }

    @Override
    public void onGpsStatusChanged(int event) {
        //n
    }
}
