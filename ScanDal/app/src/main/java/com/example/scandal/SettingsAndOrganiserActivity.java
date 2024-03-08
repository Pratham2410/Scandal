package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsAndOrganiserActivity extends AppCompatActivity {

    LinearLayout buttonGotoOrganisorPage;
    FrameLayout buttonBack_SettingsAndOrganisorPage; // Corrected variable name
    FrameLayout buttonBackToHomepage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_and_organisor_page);

        buttonGotoOrganisorPage = findViewById(R.id.buttonGotoOrganisorPage);
        buttonBack_SettingsAndOrganisorPage = findViewById(R.id.buttonBack_SettingsAndOrganisorPage); // Corrected ID reference
        buttonBackToHomepage = findViewById(R.id.buttonBackToHomepage);

        // Navigate back to home page
        buttonBackToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_home = new Intent(SettingsAndOrganiserActivity.this, QRActivity.class);
                startActivity(intent_home);
            }
        });

        buttonGotoOrganisorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open OrganisorActivity
                Intent intent = new Intent(SettingsAndOrganiserActivity.this, OrganisorActivity.class);
                startActivity(intent);
            }
        });

        buttonBack_SettingsAndOrganisorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open QRActivity
                Intent intent = new Intent(SettingsAndOrganiserActivity.this, QRActivity.class);
                startActivity(intent);
            }
        });

        // Initialize other components and set their listeners if necessary
    }
}
