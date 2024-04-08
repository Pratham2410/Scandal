package com.example.scandal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This activity displays a map with the location of an attendee.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap myMap;
    private FirebaseFirestore db;
    public FrameLayout backBtn;
    public String attendeeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_view);

        attendeeName = getIntent().getStringExtra("attendeeName");

        backBtn = findViewById(R.id.buttonBack_GoogleMapView);
        backBtn.setOnClickListener(v -> finish());



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);



    }


    /**
     * Called when the map is ready to be used.
     *
     * @param googleMap The GoogleMap object representing the map.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        db = FirebaseFirestore.getInstance();
        db.collection("profiles")
                .whereEqualTo("name", attendeeName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Device is already registered, fetch and display profile data
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> profileData = documentSnapshot.getData();
                        if (profileData.get("userLocation") != null && Integer.parseInt(profileData.get("GeoTracking").toString())==1) {
                            Map<String, Object> longlat = (Map<String, Object>) profileData.get("userLocation");
                            //Toast.makeText(getApplicationContext(), "asd"+longlat.get("longitude")+longlat.get("latitude"), Toast.LENGTH_SHORT).show();
                            LatLng sydney = new LatLng(Double.parseDouble(longlat.get("latitude").toString()), Double.parseDouble(longlat.get("longitude").toString()));
                            myMap.addMarker(new MarkerOptions().position(sydney).title(""));
                            myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                        }else{
                            Toast.makeText(getApplicationContext(), "user disabled geo-tracking", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Device is not registered, let the user enter new information
                        Toast.makeText(getApplicationContext(), "Please enter your information", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());


    }


}