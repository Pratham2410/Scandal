package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class OrganisorActivity extends AppCompatActivity {
    LinearLayout buttonCreateNewEvents;
    LinearLayout buttonViewMyEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_homepage);
        buttonCreateNewEvents = findViewById(R.id.buttonCreateNewEvents);
        buttonViewMyEvents = findViewById(R.id.buttonViewMyEvents);

        FrameLayout backToOrganiser = findViewById(R.id.buttonBack_OrganisorHomepage);

        // Modified to navigate to SettingsAndOrganiserActivity
        backToOrganiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganisorActivity.this, SettingsAndOrganiserActivity.class);
                startActivity(intent);
            }
        });

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
    }
}
