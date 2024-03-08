package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * EventPage is for displaying the event page and handling it's
 * respective actions.
 */

public class EventPage extends AppCompatActivity {

    ImageView poster;
    FrameLayout back;
    TextView eventName;
    TextView eventDescription;

    /**
     * takes care of the activities required functionality utilizing the
     * view_event_page xml
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_page);
        back = findViewById(R.id.buttonBack_ViewEventPage);
        poster = findViewById(R.id.imageView_ViewEventPage);
        eventName = findViewById(R.id.textEventName_ViewEventPage);
        eventDescription = findViewById(R.id.textEventDescription_ViewEventPage);
        String name;
        String description;
        Bitmap posterMap;
        String token = getIntent().getStringExtra("QRToken");
        Log.e("hpeebles", token);
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
