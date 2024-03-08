package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    TextView textEventName_ViewEventPage;
    TextView textEventDescription_ViewEventPage;
    ImageView imageView;
    FrameLayout buttonBack_ViewEventPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_page);

        textEventName_ViewEventPage = findViewById(R.id.textEventName_ViewEventPage);
        textEventDescription_ViewEventPage = findViewById(R.id.textEventDescription_ViewEventPage);
        imageView = findViewById(R.id.imageView_ViewEventPage);
        buttonBack_ViewEventPage = findViewById(R.id.buttonBack_ViewEventPage);
        db = FirebaseFirestore.getInstance();

        buttonBack_ViewEventPage.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        // Retrieve the event name from the intent
        String eventName = intent.getStringExtra("eventName");

        db.collection("events")
                .whereEqualTo("name", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> profileData = documentSnapshot.getData();
                        if (profileData != null) {
                            textEventName_ViewEventPage.setText((String) profileData.get("name"));
                            textEventDescription_ViewEventPage.setText((String) profileData.get("description"));
                            String imageString = (String) profileData.get("posterImage");
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
