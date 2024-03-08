package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventPage extends AppCompatActivity {
    ImageView poster;
    FrameLayout back;
    TextView eventName;
    TextView eventDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = findViewById(R.id.buttonBack_ViewEventPage);
        poster = findViewById(R.id.imageView_ViewEventPage);
        eventName = findViewById(R.id.textEventName_ViewEventPage);
        eventDescription = findViewById(R.id.textEventDescription_ViewEventPage);
        String name;
        String description;
        Bitmap posterMap;
        String token = getIntent().getStringExtra("QRToken");
        // added DB stuff here

//        eventDescription.setText(description);
//        eventName.setText(name);
//        poster.setImageBitmap(posterMap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(EventPage.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });

    }
}
