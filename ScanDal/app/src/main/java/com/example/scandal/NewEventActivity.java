package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/** Activity for managing the creation of a new event */
public class NewEventActivity extends AppCompatActivity {

    /**
     * ImageView for displaying the check-in QR code.
     */
    ImageView checkinQRCode;

    /**
     * ImageView for displaying the promo QR code.
     */
    ImageView promoQRCode;

    /**
     * Button for saving the check-in code.
     */
    AppCompatButton saveCheckinCode;

    /**
     * Button for saving the promo code.
     */
    AppCompatButton savePromoCode;
    /**
     * Button for going back.
     */
    FrameLayout backButton;
    /**
     * Button for saving project.
     */
    AppCompatButton saveProj;
    /**
     * Firebase Firestore instance for database operations
     */
    FirebaseFirestore db;

    /**
     * QRCode object for generating and handling QR codes.
     */
    QRCode QR;
    /**
     * token to be encoded in the default QR code for checkins
     */
    String token;
    /**
     *  the token to be encoded for the default QR code for event promo
     */
    String token2;
    /**
     *
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, initializing objects, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_created_page);

        // Initialize your components here
        backButton = findViewById(R.id.buttonBack_EventsCreatedPage);
        checkinQRCode = findViewById(R.id.checkinQRCode);
        promoQRCode = findViewById(R.id.promoQRCode);
        saveCheckinCode = findViewById(R.id.buttonSaveCheckinCode);
        savePromoCode = findViewById(R.id.buttonSavePromoCode);
        saveProj = findViewById(R.id.buttonSaveProject);
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String imageString = getIntent().getStringExtra("posterImage");
        String eventLocation = getIntent().getStringExtra("Location");
        String eventTime = getIntent().getStringExtra("Time");

        QR = new QRCode(); // Assuming you have a default constructor

        token = getIntent().getStringExtra("CheckinToken");

        if (QR.generateQR(checkinQRCode, token)) {
            checkinQRCode.setImageBitmap(QR.getQRPic());
        } else {
            Log.e("NewEventActivity", "Checkin QR generation failed");
        }
        token2 = getIntent().getStringExtra("PromoToken");
        if (QR.generateQR(promoQRCode, token2)) {
            promoQRCode.setImageBitmap(QR.getQRPic());
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
        saveProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> event = new HashMap<>();
                event.put("name", name);
                event.put("Time", eventTime);
                event.put("Location", eventLocation);
                event.put("description", description);
                event.put("QRCode", token);
                event.put("PromoQRCode", token2);
                event.put("posterImage", imageString); // Add the image string to the event map
                // Save event to Firestore
                db.collection("events")
                        .add(event)
                        .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Event created successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to create event", Toast.LENGTH_SHORT).show());

            }
        });

        /**
         * activates the QR scanner to get the custom qr code for checkins
         */
        saveCheckinCode.setOnClickListener(v -> {
            Intent scanner = new Intent(NewEventActivity.this, QRCodeScanner.class);
            scanner.putExtra("Activity", 2);
            scanner.putExtra("name", name);
            scanner.putExtra("Time", eventTime);
            scanner.putExtra("Location", eventLocation);
            scanner.putExtra("description", description);
            scanner.putExtra("PromoQRCode", token2);
            scanner.putExtra("posterImage", imageString);
            startActivity(scanner);

        });
        /**
         * activates the QR scanner to get the custom qr code for event promotion
         */
        savePromoCode.setOnClickListener(v -> {
            Intent scanner = new Intent(NewEventActivity.this, QRCodeScanner.class);
            scanner.putExtra("Activity", 2);
            scanner.putExtra("name", name);
            scanner.putExtra("Time", eventTime);
            scanner.putExtra("Location", eventLocation);
            scanner.putExtra("description", description);
            scanner.putExtra("QRCode", token);
            scanner.putExtra("posterImage", imageString);
            startActivity(scanner);        });
    }
}
