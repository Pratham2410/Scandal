package com.example.scandal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            QRCodeScanner QR = new QRCodeScanner();
            String code;
            QR.ScanQR(this);
            code = QR.getQR();
            button.setText(code);

        });
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }
}