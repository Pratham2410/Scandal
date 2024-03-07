package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    private String name;
    private String phoneNumber;
    private String homePage;
    private Uri imageUri;
    ImageView imageView;
    AppCompatButton editButton;
    AppCompatButton deleteButton;
    FrameLayout goBackButton;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextHomePage;
    private FirebaseFirestore db;


    // Getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        imageView = findViewById(R.id.profilePicture);
        editButton = findViewById(R.id.editImageButton);
        deleteButton = findViewById(R.id.deleteImageButton);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextHomePage = findViewById(R.id.editHomePage);
        goBackButton = findViewById(R.id.buttonBack_EditProfilePage);
        Button buttonSave = findViewById(R.id.buttonSave);
        db = FirebaseFirestore.getInstance();

        // Image Edit Button
        editButton.setOnClickListener(new View.OnClickListener() {
            /**
             * The api is from https://github.com/Dhaval2404/ImagePicker
             */
            @Override
            public void onClick(View view) {
                ImagePicker.with(Profile.this)
                        .crop() // Crop image(Optional)
                        .compress(1024) // Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 10800
                        .start();
            }

        });
        // Image Delete Button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.baseline_person_24); // Show the default profile picture after deletion
            }
        });
        // Save information button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileData();
            }
        });
        // Navigation button
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
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
                        imageView.setImageBitmap(bitmap);
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

    // Method to save profile data to Firestore
    private void saveProfileData() {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String homePage = editTextHomePage.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNumber)) {
            final String imageString = convertImageUriToString(imageUri);

            if (imageString != null) {
                final Map<String, Object> profileData = new HashMap<>();
                profileData.put("deviceId", deviceId);
                profileData.put("name", name);
                profileData.put("phoneNumber", phoneNumber);
                profileData.put("homePage", homePage);
                profileData.put("imageString", imageString);

                // Check if a document with the same deviceId already exists
                db.collection("profiles").whereEqualTo("deviceId", deviceId)
                        .limit(1) // Assuming deviceId is unique, limit to 1
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Document exists, so update it
                                String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                db.collection("profiles").document(documentId)
                                        .set(profileData)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show());
                            } else {
                                // No such document, create a new one
                                db.collection("profiles")
                                        .add(profileData)
                                        .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to save profile", Toast.LENGTH_SHORT).show());
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getApplicationContext(), "Failed to convert image to string", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter name and phone number", Toast.LENGTH_SHORT).show();
        }
    }


}
