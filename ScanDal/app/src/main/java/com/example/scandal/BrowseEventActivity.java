package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class BrowseEventActivity extends AppCompatActivity {
    FrameLayout buttonBack_BrowseEventsPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_events_page);

        buttonBack_BrowseEventsPage = findViewById(R.id.buttonBack_BrowseEventsPage);
        buttonBack_BrowseEventsPage.setOnClickListener(v -> finish());
    }
}
