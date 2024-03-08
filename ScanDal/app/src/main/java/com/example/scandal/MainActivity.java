package com.example.scandal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The main activity for ScanDal
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Button to initialize QRCode scanner
     */
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
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}