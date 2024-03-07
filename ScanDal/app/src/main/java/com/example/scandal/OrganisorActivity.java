package com.example.scandal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class OrganisorActivity extends AppCompatActivity {
    LinearLayout buttonCreateNewEvents;
    LinearLayout buttonViewMyEvents;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_homepage);
        buttonCreateNewEvents = findViewById(R.id.buttonCreateNewEvents);
        buttonViewMyEvents = findViewById(R.id.buttonViewMyEvents);

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
