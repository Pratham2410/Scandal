package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/** Activity for managing the homepage of ScanDal */

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
public class HomeActivity extends AppCompatActivity {
    /**
     * Profile image
     */
    private ImageView profile;
    /**
     * Layout for QRCode scanner
     */
    private LinearLayout scan;
    /**
     * Clickable text for viewing attendee events
     */
    private TextView attendeeEvents;
    /**
     * Clickable text for browsing events
     */
    private TextView eventBrowser;
    /**
     * Admin login button
     */
    private TextView adminLogin; // Declare the adminLogin TextView for ADMIN LOGIN button
    /**
     * Makes class attributes clickable
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanning_page);
        profile = findViewById(R.id.profilePicture);
        scan = findViewById(R.id.buttonScanQRCode);
        //attendeeEvents = findViewById(R.id.buttonViewMyAttendeeEvents);
        eventBrowser = findViewById(R.id.buttonBrowseEvents);
        adminLogin = findViewById(R.id.buttonAdminLogin); // Initialize the adminLogin TextView
        attendeeEvents = findViewById(R.id.buttonViewMyAttendeeEvents);
        ImageView settings = findViewById(R.id.imageGearOne);
        // check if user is in database
        // check if there is a non empty profile picture
        // get profile picture from database
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        db.collection("profiles")
                .whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Device is already registered, fetch and display profile data
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> profileData = documentSnapshot.getData();
                        if (profileData != null) {
                            String imageString = (String) profileData.get("imageString");
                            if (imageString != null) {
                                Bitmap bitmap = convertImageStringToBitmap(imageString);
                                if (bitmap != null) {
                                    profile.setImageBitmap(bitmap);
                                }
                                else {
                                    // Generate TextDrawable if there's no original image or if the user deleted the image
                                    String name = (String) profileData.get("name");
                                    if (!name.isEmpty()) {
                                        String initials = getInitials(name);
                                        ColorGenerator generator = ColorGenerator.MATERIAL;
                                        int color = generator.getColor(name);
                                        TextDrawable drawable = TextDrawable.builder()
                                                .buildRound(initials, color);
                                        profile.setImageDrawable(drawable);
                                    }
                                }
                            }
                        }
                    } else {
                        // Device is not registered, let the user enter new information
                        Toast.makeText(getApplicationContext(), "Please enter your information", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());

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
        //Log.e("hpeebles", "Inside HomeAct Before Attendee events");

        attendeeEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(HomeActivity.this, AttendeeEventActivity.class);
                startActivity(myintent);
            }
        });
        //Log.e("hpeebles", "Inside HomeAct After Attendee events");

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(HomeActivity.this, QRCodeScanner.class);
                myintent.putExtra("Activity", 1);
                startActivity(myintent);
            }
        });

//        scan.setOnClickListener(view -> {
//            Intent myintent = new Intent(HomeActivity.this, QRCodeScanner.class);
//            myintent.putExtra("Activity", 1);
//            startActivity(myintent);
//        });
        //Log.e("hpeebles", "Inside HomeAct");
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
    // Helper method to extract initials from a name
    private String getInitials(String name) {
        StringBuilder initials = new StringBuilder();
        for (String part : name.split(" ")) {
            if (!part.trim().isEmpty()) {
                initials.append(part.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }
    private Bitmap convertImageStringToBitmap(String imageString) {
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
