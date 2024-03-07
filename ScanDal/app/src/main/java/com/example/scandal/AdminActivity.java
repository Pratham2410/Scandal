package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    FrameLayout goBackButton;

    LinearLayout buttonBrowseImages, buttonBrowseProfiles, buttonBrowseEvents, buttonRemoveImages, buttonRemoveEvent, buttonRemoveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page); // Make sure the layout name matches your XML file name

        initializeButtons();
        setupListeners();
    }

    private void initializeButtons() {
        //buttonBrowseImages = findViewById(R.id.buttonBrowseImages);
        //buttonBrowseProfiles = findViewById(R.id.buttonBrowseProfiles);
        buttonBrowseEvents = findViewById(R.id.buttonBrowseEvents);
        buttonRemoveImages = findViewById(R.id.buttonRemoveImages);
        //buttonRemoveEvent = findViewById(R.id.buttonRemoveEvent);
        buttonRemoveProfile = findViewById(R.id.buttonRemoveProfile);

        goBackButton = findViewById(R.id.buttonBack_Admin);
    }

    private void setupListeners() {
        View.OnClickListener listener = v -> {
            Intent intent = new Intent(AdminActivity.this, HomeActivity.class);
            startActivity(intent);
        };
        goBackButton.setOnClickListener(view -> startActivity(new Intent(AdminActivity.this, HomeActivity.class)));


        buttonBrowseEvents.setOnClickListener(listener);
        buttonRemoveImages.setOnClickListener(listener);
        buttonRemoveProfile.setOnClickListener(listener);
    }
}
