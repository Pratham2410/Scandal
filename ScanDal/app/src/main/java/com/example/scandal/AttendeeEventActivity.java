package com.example.scandal;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AttendeeEventActivity extends AppCompatActivity {
    FrameLayout backMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_page);

        backMain = findViewById(R.id.buttonBack_MyEventsPage);
        backMain.setOnClickListener(v -> finish());
    }
}
