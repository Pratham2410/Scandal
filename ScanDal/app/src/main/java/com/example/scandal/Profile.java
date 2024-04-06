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

/**
 * Profile Activity for editing and saving a user's profile information, including their name,
 *
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
     * An integer representing the GeoTracking Status(Set to 0 representing no as default)
     */
    private Integer GeoTracking;
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
     * Returns the GeoTracking status of the user.
     * @return An Integer representing whether the user allows geotracking(1 for yes 0 for no).
     */
    public Integer getGeoTracking() {
        return GeoTracking;
    }
    /**
     * Sets the GeoTracking status of the user.
     * @param GeoTracking The new GeoTracking status to be set.
     */
    public void setGeoTracking(Integer GeoTracking) {
        this.GeoTracking = GeoTracking;
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
    public Profile(String name, String phoneNumber, String homePage) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.homePage = homePage;
    }
}