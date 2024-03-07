package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * Manages the profile information of a use
 */
public class Profile extends AppCompatActivity {
    /**
     * The name of the user
     */
    private String name;
    /**
     * The phone number of the user
     */
    private String phoneNumber;
    /**
     * A string representing the homepage
     */
    private String homePage;
    /**
     * A URL link leading to the profile image
     */
    private Uri imageUri;
    /**
     * A view to display an image to the user
     */
    ImageView imageView;
    /**
     * A button used for editing the profile info
     */
    AppCompatButton editButton;
    /**
     * A button used for deleting the profile info
     */
    AppCompatButton deleteButton;
    /**
     * A button that exits out of the profile fragment
     */
    FrameLayout goBackButton;
    /**
     * Text that represents the user's name
     */
    private EditText editTextName;
    /**
     * Text that represents the user's phone number
     */
    private EditText editTextPhoneNumber;
    /**
     * Text that represents the user's Home Page
     */
    private EditText editTextHomePage;
    /**
     * Contains reference to database
     */
    private FirebaseFirestore db;


    // Getter and setter

    /**
     * @return the User's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Sets the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber Sets the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the homepage of the user
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * @param homePage The homepage of the user
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /**
     * Locates views associated with attributes
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
        /**
         * Provides functionality for edit button
         */
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
        /**
         * Provides functionality for delete button
         */
        // Image Delete Button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.baseline_person_24); // Show the default profile picture after deletion
            }
        });
        // Save information button
        /**
         * Implements the functionality for the save button
         */
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileData();
            }
        });
        // Navigation button
        /**
         * Provides functionality for the back button
         */
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to update the image view

    /**
     * Displays profile image to the user
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
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

    /**
     * Converts an image string to a bitmap
     * @param imageString A string representing the image
     * @return Returns the image bitmap if the conversion is successful, otherwise it return null object
     */
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

    /**
     * Convaerts an image URI to a string
     * @param imageUri An image URI
     * @return A string representing the URI
     */
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

    /**
     * Saves profile to online database
     */
    // Method to save profile data to Firestore
    private void saveProfileData() {
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String homePage = editTextHomePage.getText().toString().trim(); // You need to retrieve the homePage text from your layout or wherever it is set.

        // Check if name and phone number are not empty
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNumber)) {
            // Convert image URI to string
            String imageString = convertImageUriToString(imageUri);

            if (imageString != null) {
                // Create a new profile document with a unique ID
                Map<String, Object> profileData = new HashMap<>();
                profileData.put("name", name);
                profileData.put("phoneNumber", phoneNumber);
                profileData.put("homePage", homePage); // Add the homePage field
                profileData.put("imageString", imageString); // Add the imageString field

                db.collection("profiles")
                        .add(profileData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to save profile", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Failed to convert image to string", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter name and phone number", Toast.LENGTH_SHORT).show();
        }
    }

}
