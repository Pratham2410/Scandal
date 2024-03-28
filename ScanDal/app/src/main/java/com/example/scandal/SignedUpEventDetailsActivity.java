package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/** An activity for managing the viewing of the details for the events I signed up */
public class SignedUpEventDetailsActivity extends AppCompatActivity {
    /** Firestore instance for database operations */
    private FirebaseFirestore db;
    /** TextView to display the event name. */
    TextView textEventName_ViewEventPage;
    /** TextView to display the event description. */
    TextView textEventDescription_ViewEventPage;
    /** ImageView to display the event image. */
    TextView textEventTime_ViewEventPage;
    /** ImageView to display the event time. */
    TextView textEventLocation_ViewEventPage;
    /** ImageView to display the event location. */
    ImageView imageView;
    /** Button to navigate back from the event details page. */
    FrameLayout buttonBack_ViewEventPage;
    String promoQRCode;
    /**
     * Calle when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_up_event_page);

        textEventName_ViewEventPage = findViewById(R.id.textEventName_ViewEventPage);
        textEventLocation_ViewEventPage = findViewById(R.id.textEventLocation_ViewEventPage);
        textEventTime_ViewEventPage = findViewById(R.id.textEventTime_ViewEventPage);

        textEventDescription_ViewEventPage = findViewById(R.id.textEventDescription_ViewEventPage);
        imageView = findViewById(R.id.imageView_ViewEventPage);
        buttonBack_ViewEventPage = findViewById(R.id.buttonBack_ViewEventPage);
        db = FirebaseFirestore.getInstance();

        buttonBack_ViewEventPage.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        // Retrieve the event name from the intent
        String eventName = intent.getStringExtra("eventName");
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    Map<String, Object> eventData = documentSnapshot.getData();
                    if (eventData != null) {
                        textEventName_ViewEventPage.setText((String) eventData.get("name"));
                        textEventTime_ViewEventPage.setText((String) eventData.get("time"));
                        textEventLocation_ViewEventPage.setText((String) eventData.get("location"));
                        textEventDescription_ViewEventPage.setText((String) eventData.get("description"));
                        promoQRCode = (String) eventData.get("PromoQRCode");
                        String imageString = (String) eventData.get("posterImage");
                        if (imageString != null) {
                            Bitmap bitmap = convertImageStringToBitmap(imageString);
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    }

                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());
    }
    /**
     * Helper method to decode Base64 string to Bitmap.
     *
     * @param imageString The Base64-encoded image string.
     * @return The decoded Bitmap, or null if decoding fails.
     */
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
