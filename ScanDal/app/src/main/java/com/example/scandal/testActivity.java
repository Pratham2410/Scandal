package com.example.scandal;

import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class testActivity extends AppCompatActivity{
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    Button LocBtn;
    FusedLocationProviderClient fusedLocationProviderClient;
    public Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        backBtn = findViewById(R.id.button4);
        LocBtn  = findViewById(R.id.button);
        backBtn.setOnClickListener(v -> finish());



    }


}