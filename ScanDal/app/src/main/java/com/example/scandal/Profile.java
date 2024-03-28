package com.example.scandal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;


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
    private boolean imagePicked = false;
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
    private boolean customedImage;

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

        // Check if the device is already registered
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
                            editTextName.setText((String) profileData.get("name"));
                            editTextPhoneNumber.setText((String) profileData.get("phoneNumber"));
                            editTextHomePage.setText((String) profileData.get("homePage"));
                            // Check if user has customized image before
                            Object customImageFlag = profileData.get("customedImage");
                            customedImage = customImageFlag instanceof Boolean && (Boolean) customImageFlag;
                            String imageString = (String) profileData.get("imageString");
                            if (imageString != null) {
                                // Convert and display the original image
                                Bitmap bitmap = convertImageStringToBitmap(imageString);
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            }
                        }
                    } else {
                        // Device is not registered, let the user enter new information
                        Toast.makeText(getApplicationContext(), "Please enter your information", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());

        /**
         * Setup interaction listeners
         */
        setupListeners();
    }
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        if(drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } else {
            // If intrinsic size is not available, define a default size or use the view's size
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888); // Example default size
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    private String convertBitmapToImageString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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

        // Fetch the current state of imagePicked from Firestore to ensure we are using the latest state
        db.collection("profiles").document(deviceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                Boolean imagePicked = document.getBoolean("imagePicked");

                String name = editTextName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String homePage = editTextHomePage.getText().toString().trim();

                final Map<String, Object> profileData = new HashMap<>();
                profileData.put("deviceId", deviceId);
                profileData.put("name", name);
                profileData.put("phoneNumber", phoneNumber);
                profileData.put("homePage", homePage);

                if (Boolean.TRUE.equals(imagePicked)) {
                    // If an image has been picked and is present, save that image
                    String imageString = convertImageUriToString(imageUri);
                    profileData.put("imageString", imageString);
                } else if (!Boolean.TRUE.equals(imagePicked) && !name.isEmpty()) {
                    // If no image has been picked or it has been deleted, generate a new one based on initials
                    String imageString = generateProfileImageForName(name);
                    profileData.put("imageString", imageString);
                }
                // Now that we have the complete profile data, including the correct image handling, proceed to save it
                saveDataToFirestore(profileData, deviceId);
            } else {
                Log.d("Profile", "Failed to fetch imagePicked state from Firestore", task.getException());
                // Consider handling the failure to fetch from Firestore, such as by using default behavior or notifying the user
            }
        });
    }


    private void updateFirestoreImagePickedState(boolean imagePicked) {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> update = new HashMap<>();
        update.put("imagePicked", imagePicked);

        // Assuming 'profiles' is your collection and 'deviceId' uniquely identifies a document
        db.collection("profiles").document(deviceId)
                .update(update)
                .addOnSuccessListener(aVoid -> Log.d("Profile", "Firestore imagePicked state updated successfully"))
                .addOnFailureListener(e -> Log.d("Profile", "Error updating Firestore imagePicked state", e));
    }


    /**
     * Generates a profile image for the user based on their name and converts it to a Base64 encoded string.
     * @param name The name of the user to generate an image for.
     * @return A Base64 encoded string representing the generated image; null if an error occurs.
     */
    private String generateProfileImageForName(String name) {
        String initials = getInitials(name);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(name);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(initials, color);

        Bitmap bitmap = drawableToBitmap(drawable);
        imageView.setImageDrawable(drawable);
        return convertBitmapToImageString(bitmap);
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

        deleteButton.setOnClickListener(view -> {
            updateFirestoreImagePickedState(false); // Update Firestore immediately when the image is deleted
            // Additional code for handling the deletion...
        });

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
            updateFirestoreImagePickedState(true);
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