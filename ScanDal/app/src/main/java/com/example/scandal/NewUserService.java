package com.example.scandal;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for creating default profile for new user
 */
public class NewUserService extends Service {
    private FirebaseFirestore db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("etowsley", "Service Started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = FirebaseFirestore.getInstance();
                saveProfileData();
                Log.e("etowsley", "SaveProfileData() has finished.");
            }
        }).start();
        stopSelf();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    /**
     * Saves the user's profile data to Firebase Firestore. This includes the name, phone number,
     * home page URL, and the profile image encoded as a Base64 string.
     */
    private void saveProfileData() {
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String imageString = generateProfileImageForName();

        final Map<String, Object> profileData = new HashMap<>();
        profileData.put("deviceId", deviceId);
        profileData.put("name", "Unkown");
        profileData.put("phoneNumber", "");
        profileData.put("homePage", "");
        profileData.put("imageString", imageString);
        profileData.put("customedImage", false);

        saveDataToFirestore(profileData, deviceId);
    }


    /**
     * Generates a profile image for the user based on their name and converts it to a Base64 encoded string.
     * @return A Base64 encoded string representing the generated image; null if an error occurs.
     */
    private String generateProfileImageForName() {
        String initials = "U";
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor("U");
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(initials, color);

        Bitmap bitmap = drawableToBitmap(drawable);
        return convertBitmapToImageString(bitmap);
    }

    /**
     * Saves or updates the user's profile data in Firebase Firestore.
     * @param profileData A map containing the user's profile data.
     * @param deviceId The device ID used to uniquely identify the user's document in Firestore.
     */
    private void saveDataToFirestore(Map<String, Object> profileData, String deviceId) {
        final Integer[] GeoTracking = {0};
        db.collection("profiles").whereEqualTo("deviceId", deviceId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> preData = documentSnapshot.getData();
                        if(preData.get("GeoTracking")!=null && Integer.parseInt(preData.get("GeoTracking").toString()) == 1){
                            GeoTracking[0] = 1;
                            profileData.put("GeoTracking", GeoTracking[0]);
                        }else {
                            GeoTracking[0] = 0;
                            profileData.put("GeoTracking", GeoTracking[0]);
                        }
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("profiles").document(documentId)
                                .set(profileData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show());
                    } else {
                        GeoTracking[0] = 0;
                        profileData.put("GeoTracking", GeoTracking[0]);
                        db.collection("profiles")
                                .add(profileData)
                                .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to save profile", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to check existing profile", Toast.LENGTH_SHORT).show());
    }
}
