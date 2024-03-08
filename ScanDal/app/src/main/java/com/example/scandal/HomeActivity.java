package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private ImageView profile;
    private LinearLayout scan;
    private TextView attendeeEvents;
    private TextView eventBrowser;
    private TextView adminLogin; // Declare the adminLogin TextView for ADMIN LOGIN button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanning_page); // Make sure this layout has the ADMIN LOGIN button with ID @+id/buttonAdminLogin

        profile = findViewById(R.id.profilePicture);
        scan = findViewById(R.id.buttonScanQRCode);
        attendeeEvents = findViewById(R.id.buttonViewMyAttendeeEvents);
        eventBrowser = findViewById(R.id.buttonBrowseEvents);
        adminLogin = findViewById(R.id.buttonAdminLogin); // Initialize the adminLogin TextView

        ImageView settings = findViewById(R.id.imageGearOne);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(HomeActivity.this, SettingsAndOrganiserActivity.class);
                startActivity(myintent);
            }
        });
        eventBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(HomeActivity.this, BrowseEventActivity.class);
                startActivity(myintent);
            }
        });
        attendeeEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(HomeActivity.this, AttendeeEventActivity.class); // replace placeholder class with the activity for attendee
                startActivity(myintent);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(HomeActivity.this, QRCodeScanner.class);
                myintent.putExtra("Activity", 1);
                startActivity(myintent);
            }
        });

        scan.setOnClickListener(view -> {
            Intent myintent = new Intent(HomeActivity.this, QRCodeScanner.class);
            myintent.putExtra("Activity", 1);
            startActivity(myintent);
        });

        profile.setOnClickListener(view -> {
            Intent myintent = new Intent(HomeActivity.this, Profile.class);
            startActivity(myintent);
        });

        // Setup click listener for ADMIN LOGIN button
        adminLogin.setOnClickListener(view -> {
            Intent myintent = new Intent(HomeActivity.this, AdminActivity.class);
            startActivity(myintent);
        });
    }
}
