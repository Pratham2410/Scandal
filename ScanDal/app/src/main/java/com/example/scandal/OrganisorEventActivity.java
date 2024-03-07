package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class OrganisorEventActivity extends AppCompatActivity {
    FrameLayout backToOrganiser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organisor_events_page);
        backToOrganiser = findViewById(R.id.buttonBack_OrganisorEventsPage);

        backToOrganiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent(OrganisorEventActivity.this, OrganisorActivity.class);
                startActivity(intent_back);
            }
        });
    }
}
