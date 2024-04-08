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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Task;


/**
 * Profile Activity for editing and saving a user's profile information, including their name,
 * phone number, home page URL, and profile image. The profile image is handled by converting
 * the image to a Base64 encoded string for storage in Firebase Firestore.
 */
public class ProfileActivity extends AppCompatActivity implements IBaseGpsListener {
    /**
     * An integer representing the GeoTracking Status(Set to 0 representing no as default)
     */
    private Integer GeoTracking;
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
    final int FINE_PERMISSION_CODE = 1;
    private static final int PERMISSION_LOCATION = 1000;
    Location currentLocation;
    CountDownTimer timer;
    Integer verifyLocation = 1;

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
                            String name = (String) profileData.get("name");
                            if (!name.equals("Unkown")) {
                                editTextName.setText(name);
                            }
                            if(Integer.parseInt(profileData.get("GeoTracking").toString()) == 1){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                                } else {
                                    showLocation();
                                }
                            }
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
                        //Toast.makeText(getApplicationContext(), "Please enter your information", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show());

        /**
         * Setup interaction listeners
         */
        setupListeners();
    }
    private void showLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }else {
            Toast.makeText(getApplicationContext(), "Please enable gps", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
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
     *
     * @param requestCode The request code initially supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle result from the Image Picker
        processImagePickerResult(requestCode, resultCode, data);
    }

    /**
     * Converts a Base64 encoded string back into a Bitmap image, which can then be used to set an ImageView's content.
     *
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
     *
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

        // Use trim() to remove leading and trailing spaces, and check if empty
        String name = editTextName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            name = "Unkown"; // Set to blaActivitnk if empty
        }

        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = ""; // Set to blank if empty
        }

        String homePage = editTextHomePage.getText().toString().trim();
        if (TextUtils.isEmpty(homePage)) {
            homePage = ""; // Set to blank if empty
        }

        String imageString = null;
        Location userLocation = new Location("gps");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            userLocation.setMslAltitudeAccuracyMeters(0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            userLocation.setMslAltitudeMeters(0);
        }

        if (imageUri != null) {
            // An image was selected by the user; convert it to a string
            imageString = convertImageUriToString(imageUri);
            customedImage = true;
        } else if (customedImage == false) {
            // No image was selected; generate a TextDrawable based on the user's name
            // Only do this if you really need a placeholder image for every profile without an image
            if (!TextUtils.isEmpty(name)) {
                // This block can be removed if you decide not to use a generated image when no image is selected
                imageString = generateProfileImageForName(name);
            }
        }

        final Map<String, Object> profileData = new HashMap<>();


        profileData.put("deviceId", deviceId);
        profileData.put("name", name);
        profileData.put("phoneNumber", phoneNumber);
        profileData.put("homePage", homePage);
        profileData.put("imageString", imageString);
        profileData.put("customedImage", customedImage);
        profileData.put("userLocation", userLocation);


        saveDataToFirestore(profileData, deviceId);
    }


    /**
     * Generates a profile image for the user based on their name and converts it to a Base64 encoded string.
     *
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
        editButton.setOnClickListener(view -> ImagePicker.with(ProfileActivity.this)
                .crop() // Crop image(Optional)
                .compress(1024) // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080
                .start());

        deleteButton.setOnClickListener(view -> {
            customedImage = false;
            saveProfileData();
        });

        findViewById(R.id.buttonSave).setOnClickListener(view -> saveProfileData());

        goBackButton.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, HomeActivity.class)));
    }

    /**
     * Processes the result from the image picker activity.
     *
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode  The result code returned by the image picker activity.
     * @param data        The intent data returned by the image picker activity.
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
     *
     * @param profileData A map containing the user's profile data.
     * @param deviceId    The device ID used to uniquely identify the user's document in Firestore.
     */
    private void saveDataToFirestore(Map<String, Object> profileData, String deviceId) {
        db.collection("profiles").whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> preData = documentSnapshot.getData();
                        if (preData.get("GeoTracking") != null && Integer.parseInt(preData.get("GeoTracking").toString()) == 1) {
                            GeoTracking = 1;
                            profileData.put("GeoTracking", GeoTracking);
                            if(preData.get("userLocation")!=null){
                                profileData.put("userLocation", preData.get("userLocation"));
                            }
                            else if(currentLocation != null){
                                profileData.put("userLocation", currentLocation);
                            }

                        } else {
                            GeoTracking = 0;
                            profileData.put("GeoTracking", GeoTracking);
                        }
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("profiles").document(documentId)
                                .set(profileData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show());
                    } else {
                        GeoTracking = 0;
                        profileData.put("GeoTracking", GeoTracking);
                        db.collection("profiles")
                                .add(profileData)
                                .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to save profile", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_LOCATION){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                showLocation();
            }
        }else{
            Toast.makeText(getApplicationContext(), "permission not allowed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void hereLocation(Location location) {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        currentLocation = location;
        //Toast.makeText(getApplicationContext(), "Long: "+location.getLongitude()+" Lat:"+location.getLatitude(), Toast.LENGTH_SHORT).show();
        db.collection("profiles").whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> preData = documentSnapshot.getData();
                        if (preData.get("GeoTracking") != null && Integer.parseInt(preData.get("GeoTracking").toString()) == 1) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                currentLocation.setMslAltitudeAccuracyMeters(0);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                currentLocation.setMslAltitudeMeters(0);
                            }
                            preData.put("userLocation", currentLocation);
                        }
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("profiles").document(documentId)
                                .set(preData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Location updated successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update location", Toast.LENGTH_SHORT).show());

                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());

    }

    @Override
    public void onLocationChanged(Location location) {
        if(verifyLocation == 1){
            verifyLocation = 0;
            final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            db.collection("profiles").whereEqualTo("deviceId", deviceId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Map<String, Object> preData = documentSnapshot.getData();
                            if (preData.get("GeoTracking") != null && Integer.parseInt(preData.get("GeoTracking").toString()) == 1) {
                                timer = new CountDownTimer(100000,100000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        verifyLocation = 1;
                                    }
                                }.start();
                                hereLocation(location);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        //n
    }

    @Override
    public void onProviderEnabled(String provider) {
        //n
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //n
    }

    @Override
    public void onGpsStatusChanged(int event) {
        //n
    }

}