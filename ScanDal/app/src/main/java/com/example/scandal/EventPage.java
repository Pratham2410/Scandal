package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
/**
 * Activity for displaying details of an event based on QR code.
 */
public class
EventPage extends AppCompatActivity {
    /** ImageView to display the poster of the event. */
    ImageView poster;
    /** FrameLayout for navigating back. */
    FrameLayout back;
    /** TextView to display the name of the event. */
    TextView eventName;
    /** TextView to display the description of the event. */
    TextView eventDescription;
    /**
     * TextView to display the event time
     */
    TextView eventTime;
    /**
     * TextView to display event location
     */
    TextView eventLocation;
    /**
     * string of the event poster to make the passed intents smaller
     */
    static String imageString;
    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_page);

        back = findViewById(R.id.buttonBack_ViewEventPage);
        poster = findViewById(R.id.imageView_ViewEventPage);
        eventTime = findViewById(R.id.textEventTime_ViewEventPage);
        eventLocation = findViewById(R.id.textEventLocation_ViewEventPage);
        eventName = findViewById(R.id.textEventName_ViewEventPage);
        eventDescription = findViewById(R.id.textEventDescription_ViewEventPage);
        Bitmap posterBitmap = convertImageStringToBitmap(imageString);

        poster.setImageBitmap(posterBitmap);
        eventLocation.setText(getIntent().getStringExtra("location")); //gets the location
        eventTime.setText(getIntent().getStringExtra("time")); // gets the time
        eventDescription.setText(getIntent().getStringExtra("description")); // gets description
        eventName.setText(getIntent().getStringExtra("name")); // gets the name


        // Set OnClickListener for back button
        back.setOnClickListener(view -> {
            // Navigate to HomeActivity when back button is clicked
            Intent intent = new Intent(EventPage.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }


    /**
     * Helper method to decode Base64 string to Bitmap.
     *
     * @param imageString The Base64-encoded image string.
     * @return The decoded Bitmap, or null if decoding fails.
     */
    // Helper method to decode Base64 string to Bitmap
    private Bitmap convertImageStringToBitmap(String imageString) {
        try {
            byte[] decodedByteArray = android.util.Base64.decode(imageString, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
