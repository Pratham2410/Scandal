package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Random;

public class NewEventActivity extends AppCompatActivity {
    ImageView checkinQRCode;
    ImageView promoQRCode;
    AppCompatButton saveCheckinCode;
    AppCompatButton savePromoCode;
    FrameLayout backButton;
    // Assume QRCode is a class you have for generating and handling QR codes.
    QRCode QR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_created_page); // Ensure this matches your layout file name

        // Initialize your components here
        backButton = findViewById(R.id.buttonBack_EventsCreatedPage);
        checkinQRCode = findViewById(R.id.checkinQRCode);
        promoQRCode = findViewById(R.id.promoQRCode);
        saveCheckinCode = findViewById(R.id.buttonSaveCheckinCode);
        savePromoCode = findViewById(R.id.buttonSavePromoCode);

        QR = new QRCode(); // Assuming you have a default constructor

        String token = getIntent().getStringExtra("CheckinToken");

        // Example QR code generation and setting, replace with your actual methods
        if (QR.generateQR(checkinQRCode, token)) { // Assuming generateQR returns a boolean
            checkinQRCode.setImageBitmap(QR.getQRPic()); // Placeholder method, replace with your actual QR code bitmap
        } else {
            Log.e("NewEventActivity", "Checkin QR generation failed");
        }
        token = getIntent().getStringExtra("PromoToken");
        if (QR.generateQR(promoQRCode, token)) { // Assuming generateQR returns a boolean
            promoQRCode.setImageBitmap(QR.getQRPic()); // Placeholder method, replace with your actual QR code bitmap
        } else {
            Log.e("NewEventActivity", "Promo QR generation failed");
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(NewEventActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });

        // Set up listeners for button interactions
        saveCheckinCode.setOnClickListener(v -> {
            // Implement your logic for customizing the checkin code
            // For example, starting another activity for customization
        });

        savePromoCode.setOnClickListener(v -> {
            // Implement your logic for customizing the promo code
            // For example, starting another activity for customization
        });
    }
}
