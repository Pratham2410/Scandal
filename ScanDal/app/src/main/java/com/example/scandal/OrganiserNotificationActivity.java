package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class OrganiserNotificationActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private Button buttonSend;
    private FrameLayout buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification); // Use your layout file name

        // Initialize views
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBack = findViewById(R.id.buttonBack_Send_Notification);

        // Set the onClickListener for the back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve title and message from EditTexts
                String title = editTitle.getText().toString().trim();
                String message = editDescription.getText().toString().trim();

                // Check if the title or message is empty
                if (title.isEmpty() || message.isEmpty()) {
                    // You can show a Toast message here to inform the user to fill in the fields
                    return;
                }

                // Initialize your FcmNotificationsSender with the title and message
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        "/topics/all",
                        title,
                        message,
                        getApplicationContext(),
                        OrganiserNotificationActivity.this
                );

                Log.d("NotificationButton", "Send Notification button clicked");

                // Send the notification
                notificationsSender.SendNotifications();

                // Log after sending notification
                Log.d("NotificationButton", "Notification sent, no further action");
            }
        });
    }
}
