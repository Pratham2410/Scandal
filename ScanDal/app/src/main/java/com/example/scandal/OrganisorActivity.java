package com.example.scandal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class OrganisorActivity extends AppCompatActivity {
    LinearLayout buttonCreateNewEvents;
    LinearLayout buttonViewMyEvents;
    FrameLayout buttonBackToHomepage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_homepage);
        buttonCreateNewEvents = findViewById(R.id.buttonCreateNewEvents);
        buttonViewMyEvents = findViewById(R.id.buttonViewMyEvents);
        buttonBackToHomepage = findViewById(R.id.buttonBackToHomepage);

        FrameLayout backToOrganiser = findViewById(R.id.buttonBack_OrganisorHomepage);


        // Navigate back to OrganisorActivity
        backToOrganiser.setOnClickListener(v -> finish());

        buttonCreateNewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_create = new Intent(OrganisorActivity.this, EventActivity.class);
                startActivity(intent_create);
            }
        });
        buttonViewMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_view = new Intent(OrganisorActivity.this, OrganisorEventActivity.class);
                startActivity(intent_view);
            }
        });
        // Navigate back to home page
        buttonBackToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_home = new Intent(OrganisorActivity.this, QRActivity.class);
                startActivity(intent_home);
            }
        });
    }
}
