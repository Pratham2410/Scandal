package com.example.scandal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button toQrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets Emulator Key and Checks if Device is New
        Intent intent_userLog = new Intent(MainActivity.this, User.class);
        startService(intent_userLog);

        setContentView(R.layout.starting_page);
        toQrScan = findViewById(R.id.buttonGetStarted);
        toQrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_profile = new Intent(MainActivity.this, QRActivity.class);
                startActivity(intent_profile);
            }
        });
    }
}