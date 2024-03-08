package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for the admin home page
 */
public class AdminActivity  extends AppCompatActivity {

    /**
     * FrameLayout for navigating back to the admin interface.
     */
    FrameLayout goBackButton;

    /**
     * LinearLayout for managing profile, events, and images.
     */
    LinearLayout buttonManageProfile, buttonManageEvents, buttonManageImages;

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, initializing objects, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);

        initializeButtons();
        setupListeners();
    }
    /**
     * Initializes buttons by finding their views from the layout.
     */
    private void initializeButtons() {
        //buttonBrowseImages = findViewById(R.id.buttonBrowseImages);
        //buttonBrowseProfiles = findViewById(R.id.buttonBrowseProfiles);
        buttonManageEvents = findViewById(R.id.buttonManageEvents);
        buttonManageImages = findViewById(R.id.buttonManageImages);
        //buttonRemoveEvent = findViewById(R.id.buttonRemoveEvent);
        buttonManageProfile = findViewById(R.id.buttonManageProfile);

        goBackButton = findViewById(R.id.buttonBack_Admin);
    }
    /**
     * Sets up listeners for button clicks.
     */
    private void setupListeners() {
        // Listener for managing events
        buttonManageEvents.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminEventActivity.class)));

        // Listener for managing images
        // Assuming you have a corresponding activity for managing images, replace `AdminEventActivity.class` with the correct class
        buttonManageImages.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminImageActivity.class))); // Example placeholder

        // Listener for managing profiles
        buttonManageProfile.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminProfileActivity.class)));

        // Listener for going back
        goBackButton.setOnClickListener(view -> startActivity(new Intent(AdminActivity.this, HomeActivity.class)));
    }

}
