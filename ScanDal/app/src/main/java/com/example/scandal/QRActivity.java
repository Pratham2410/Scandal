package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QRActivity extends AppCompatActivity {

    LinearLayout buttonScanQRCode;
    TextView txtSettingsOrgan;
    TextView txtEditProfilePi; // Reference for the Edit Profile button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanning_page);

        initializeUIComponents();
        setupListeners();
    }

    private void initializeUIComponents() {
        buttonScanQRCode = findViewById(R.id.buttonScanQRCode);
        txtSettingsOrgan = findViewById(R.id.txtSettingsOrgan);
        txtEditProfilePi = findViewById(R.id.txtEditProfilePi); // Initialize the Edit Profile TextView
    }

    private void setupListeners() {
        txtSettingsOrgan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open SettingsAndOrganiserActivity
                Intent intent = new Intent(QRActivity.this, SettingsAndOrganiserActivity.class);
                startActivity(intent);
            }
        });

        txtEditProfilePi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open ProfileActivity
                Intent intent = new Intent(QRActivity.this, Profile.class);
                startActivity(intent);
            }
        });
    }
}
