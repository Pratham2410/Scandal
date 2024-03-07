package com.example.scandal;
import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * This is an activity involving
 * Adding new events' information to the database
 */

public class EventActivity extends AppCompatActivity {
    private String name;
    private String description;
    private Uri imageUri;
    ImageView poster;
    private EditText editEventName;
    private EditText editEventDescription;
    AppCompatButton generateEventButton;
    AppCompatButton uploadPosterButton;
    AppCompatButton deletePosterButton;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_page);

        poster = findViewById(R.id.imageView3);
        editEventName = findViewById(R.id.editTextEventName);
        editEventDescription = findViewById(R.id.editTextEventDescription);
        generateEventButton = findViewById(R.id.buttonSave_CreateEventPage);
        uploadPosterButton = findViewById(R.id.editPosterButton);
        deletePosterButton = findViewById(R.id.deletePosterButton);

        db = FirebaseFirestore.getInstance();
        // Generate Event Button
        generateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventData();
            }
        });
        // Image Edit Button
        uploadPosterButton.setOnClickListener(new View.OnClickListener() {
            /**
             * The api is from https://github.com/Dhaval2404/ImagePicker
             */
            @Override
            public void onClick(View view) {
                ImagePicker.with(EventActivity.this)
                        .crop() // Crop image(Optional)
                        .compress(1024) // Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 10800
                        .start();
            }

        });
        // Image Delete Button
        deletePosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poster.setImageResource(R.drawable.sample_poster); // Show the default profile picture after deletion
            }
        });
    }
    // Method to update the image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                // Convert image URI to string
                imageUri = data.getData();
                String imageString = convertImageUriToString(imageUri);
                if (imageString != null) {
                    // Convert image string to bitmap
                    Bitmap bitmap = convertImageStringToBitmap(imageString);
                    if (bitmap != null) {
                        // Set image view to display the bitmap
                        poster.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(this, "Failed to convert image string to bitmap", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to convert image to string", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to convert image string to bitmap
    private Bitmap convertImageStringToBitmap(String imageString) {
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert image URI to string
    private String convertImageUriToString(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void saveEventData() {
        name = editEventName.getText().toString();
        description = editEventDescription.getText().toString();

        if (!name.isEmpty() && !description.isEmpty()) {
            // Convert image URI to string
            String imageString = convertImageUriToString(imageUri);

            if (imageString != null) {
                Map<String, Object> event = new HashMap<>();
                event.put("name", name);
                event.put("description", description);

                // Add a new document with a generated ID
                db.collection("events")
                        .add(event)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Event created successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to create event", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Failed to convert image to string", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter event name and description", Toast.LENGTH_SHORT).show();
        }
    }
}

