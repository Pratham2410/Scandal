package com.example.scandal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_page);

//        db.collection("events")
//                .whereEqualTo("deviceId", deviceId)
//                .limit(1)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        // Device is already registered, fetch and display profile data
//                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
//                        Map<String, Object> profileData = documentSnapshot.getData();
//                        if (profileData != null) {
//                            editTextName.setText((String) profileData.get("name"));
//                            editTextPhoneNumber.setText((String) profileData.get("phoneNumber"));
//                            editTextHomePage.setText((String) profileData.get("homePage"));
//                            String imageString = (String) profileData.get("imageString");
//                            if (imageString != null) {
//                                Bitmap bitmap = convertImageStringToBitmap(imageString);
//                                if (bitmap != null) {
//                                    imageView.setImageBitmap(bitmap);
//                                }
//                            }
//                        }
//                    }
//                }
    }
}
