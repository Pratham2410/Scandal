package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for creating and saving event details, including the event name,
 * description, and an associated poster image, into Firebase Firestore.
 */
public class EventActivity extends AppCompatActivity {
    // URI of the selected poster image
    private Uri imageUri;
    // ImageView to display the selected poster
    ImageView poster;
    // EditText for event name input
    private EditText editEventName;
    // EditText for event description input
    private EditText editEventDescription;
    // Button to trigger event data saving
    AppCompatButton generateEventButton;
    // Button to upload a poster image
    AppCompatButton uploadPosterButton;
    // Button to delete the selected poster image
    AppCompatButton deletePosterButton;

    // Firebase Firestore instance for database operations
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_page);

        // Initialize UI components and Firestore
        initializeUIComponents();

        // Setup listeners for button actions
        setupButtonListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Process result from Image Picker
        handleImagePickerResult(requestCode, resultCode, data);
    }

    /**
     * Saves event details to Firebase Firestore. Includes the event name, description,
     * and a Base64 encoded string of the poster image.
     */
    private void saveEventData() {
        String name = editEventName.getText().toString().trim();
        String description = editEventDescription.getText().toString().trim();

        if (!name.isEmpty() && !description.isEmpty() && imageUri != null) {
            String imageString = convertImageUriToString(imageUri);
            if (imageString != null) {
                Map<String, Object> event = new HashMap<>();
                event.put("name", name);
                event.put("description", description);
                event.put("posterImage", imageString); // Add the image string to the event map

                // Save event to Firestore
                db.collection("events")
                        .add(event)
                        .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Event created successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to create event", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getApplicationContext(), "Failed to convert image to string", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter event name and description, and upload a poster", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Converts the image located at the provided Uri to a Base64 encoded string.
     * @param imageUri The Uri of the image to be converted.
     * @return A Base64 encoded string representation of the image, or null if conversion fails.
     */
    private String convertImageUriToString(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes UI components and configures the Firebase Firestore instance.
     */
    private void initializeUIComponents() {
        poster = findViewById(R.id.imageView3);
        editEventName = findViewById(R.id.editTextEventName);
        editEventDescription = findViewById(R.id.editTextEventDescription);
        generateEventButton = findViewById(R.id.buttonSave_CreateEventPage);
        uploadPosterButton = findViewById(R.id.editPosterButton);
        deletePosterButton = findViewById(R.id.deletePosterButton);
        FrameLayout backToOrganiser = findViewById(R.id.buttonBack_CreateEventPage);

        db = FirebaseFirestore.getInstance();

        // Navigate back to OrganisorActivity
        backToOrganiser.setOnClickListener(v -> finish());
    }

    /**
     * Sets up listeners for various button actions within the activity.
     */
    private void setupButtonListeners() {
        generateEventButton.setOnClickListener(v -> saveEventData());

        uploadPosterButton.setOnClickListener(view -> ImagePicker.with(EventActivity.this)
                .crop() // Optional image cropping
                .compress(1024) // Compress image size
                .maxResultSize(1080, 1080) // Max resolution
                .start());

        deletePosterButton.setOnClickListener(view -> poster.setImageResource(R.drawable.sample_poster)); // Reset to default poster
    }

    /**
     * Handles the result returned from the image picker activity. Sets the selected
     * image as the poster's ImageView content if successful.
     * @param requestCode The request code passed in startActivityForResult().
     * @param resultCode The result code returned by the child activity.
     * @param data An Intent that carries the result data.
     */
    private void handleImagePickerResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE && data != null) {
            imageUri = data.getData();
            poster.setImageURI(imageUri); // Directly update the ImageView with the image Uri
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
