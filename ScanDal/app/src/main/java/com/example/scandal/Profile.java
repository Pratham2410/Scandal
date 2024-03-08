package com.example.scandal;

import android.app.Activity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Profile Activity for editing and saving a user's profile information, including their name,
 * phone number, home page URL, and profile image. The profile image is handled by converting
 * the image to a Base64 encoded string for storage in Firebase Firestore.
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

    /**
     * Returns the name of the user.
     * @return A string representing the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name The new name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the phone number of the user.
     * @return A string representing the user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     * @param phoneNumber The new phone number to be set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the home page URL of the user.
     * @return A string representing the user's home page URL.
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * Sets the home page URL of the user.
     * @param homePage The new home page URL to be set.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        /**
         *  Initialize UI components
          */
        initializeUI();

        /**
         * Setup interaction listeners
         */
        setupListeners();
    }

    /**
     * Handles the result from the image picker activity, updating the profile picture accordingly.
     * @param requestCode The request code initially supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle result from the Image Picker
        processImagePickerResult(requestCode, resultCode, data);
    }

    /**
     * Converts a Base64 encoded string back into a Bitmap image, which can then be used to set an ImageView's content.
     * @param imageString The Base64 encoded string that represents the image.
     * @return A Bitmap image if conversion is successful; otherwise, null.
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

    /**
     * Converts the image located at a Uri into a Base64 encoded string.
     * This method is useful for storing images in Firebase Firestore as strings.
     * @param imageUri The Uri of the image to convert.
     * @return A Base64 encoded string representation of the image; null if an error occurs.
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
     * Saves the user's profile data to Firebase Firestore. This includes the name, phone number,
     * home page URL, and the profile image encoded as a Base64 string.
     */
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

                saveDataToFirestore(profileData, deviceId);
            } else {
                Toast.makeText(getApplicationContext(), "Failed to convert image to string", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter name and phone number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes UI components and Firebase Firestore instance.
     */
    private void initializeUI() {
        imageView = findViewById(R.id.profilePicture);
        editButton = findViewById(R.id.editImageButton);
        deleteButton = findViewById(R.id.deleteImageButton);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextHomePage = findViewById(R.id.editHomePage);
        goBackButton = findViewById(R.id.buttonBack_EditProfilePage);
        Button buttonSave = findViewById(R.id.buttonSave);
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Sets up interaction listeners for UI components.
     */
    private void setupListeners() {
        editButton.setOnClickListener(view -> ImagePicker.with(Profile.this)
                .crop() // Crop image(Optional)
                .compress(1024) // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080
                .start());

        deleteButton.setOnClickListener(view -> imageView.setImageResource(R.drawable.img_ellipse1_124x124));

        findViewById(R.id.buttonSave).setOnClickListener(view -> saveProfileData());

        goBackButton.setOnClickListener(view -> startActivity(new Intent(Profile.this, HomeActivity.class)));
    }

    /**
     * Processes the result from the image picker activity.
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode The result code returned by the image picker activity.
     * @param data The intent data returned by the image picker activity.
     */
    private void processImagePickerResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE && data != null) {
            imageUri = data.getData();
            String imageString = convertImageUriToString(imageUri);
            if (imageString != null) {
                Bitmap bitmap = convertImageStringToBitmap(imageString);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Failed to convert image string to bitmap", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to convert image to string", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Saves or updates the user's profile data in Firebase Firestore.
     * @param profileData A map containing the user's profile data.
     * @param deviceId The device ID used to uniquely identify the user's document in Firestore.
     */
    private void saveDataToFirestore(Map<String, Object> profileData, String deviceId) {
        db.collection("profiles").whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("profiles").document(documentId)
                                .set(profileData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show());
                    } else {
                        db.collection("profiles")
                                .add(profileData)
                                .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to save profile", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
    }
}
