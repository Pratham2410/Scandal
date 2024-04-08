package com.example.scandal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.github.dhaval2404.imagepicker.ImagePicker;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Activity for creating and saving event details, including the event name,
 * description, and an associated poster image, into Firebase Firestore.
 */
public class EventActivity extends AppCompatActivity {
    /**
     * URI of the selected poster image
     */
    private Uri imageUri;
    /**
     * ImageView to display the selected poster
     */
    ImageView poster;
    /**
     * EditText for event name input
     */
    private EditText editEventName;
    /**
     * EditText for event description input
     */
    private EditText editEventDescription;
    /**
     * edit text for Event location
     */
    private EditText editlocation;
    /**
     * Edit text for attendee limit
     */
    private EditText editLimit;
    /**
     * Button to generate new event
     */
    AppCompatButton generateEventButton;
    /**
     * Button to upload a poster image
     */
    AppCompatButton uploadPosterButton;
    /**
     * Button to upload a poster image
     */
    AppCompatButton deletePosterButton;
    /**
     * Button to trigger event date saving
     */
    AppCompatButton editEventTime;
    /**
     * Calender for getting event date
     */
    Calendar calendar;
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_page);

        // Initialize Calender
        calendar = Calendar.getInstance();
        // Initialize UI components and Firestore
        initializeUIComponents();

        // Setup listeners for button actions
        setupButtonListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Process result from Image Picker
        handleImagePickerResult(requestCode, resultCode, data);
    }

    /**
     * Saves event details to Firebase Firestore. Includes the event name, description,
     * and a Base64 encoded string of the poster image.
     */
    private void saveEventData() {
        // Extract input values, defaulting to empty string if any field is empty
        String name = editEventName.getText().toString().trim();
        String description = editEventDescription.getText().toString().trim();
        String eventTime = String.valueOf(calendar.getTime());
        Log.e("etowsley", eventTime);
        String eventLocation = editlocation.getText().toString().trim();
        String attendeeLimit = editLimit.getText().toString().trim();
        // Ensure values are set to an empty string if they are empty
        name = name.isEmpty() ? "" : name;
        description = description.isEmpty() ? "" : description;
        eventTime = eventTime.isEmpty() ? "" : eventTime;
        eventLocation = eventLocation.isEmpty() ? "" : eventLocation;
        attendeeLimit = attendeeLimit.isEmpty() ? "" : attendeeLimit;

        // Convert the selected image to a Base64 string, default to empty string if no image is selected
        String imageString = (imageUri != null) ? convertImageUriToString(imageUri) : "";

        // Generate unique tokens for check-in and promotional purposes
        Random rnd = new Random();
        String randomStr = String.valueOf(rnd.nextInt(10000));
        String checkinToken = name + randomStr; // Note: This might result in a non-unique token if 'name' is empty
        String promoToken = "Promo" + name + rnd.nextInt(10000);

        // Prepare intent for the next activity, using the exact keys you've provided
        NewEventActivity.imageString = imageString;
        Intent intent = new Intent(EventActivity.this, NewEventActivity.class);
        intent.putExtra("name", name) ;
        intent.putExtra("attendeeLimit", attendeeLimit);
        intent.putExtra("description", description);
        intent.putExtra("Time", eventTime); // Changed from "eventTime" to "Time"
        intent.putExtra("Location", eventLocation); // Changed from "eventLocation" to "Location"
        intent.putExtra("CheckinToken", checkinToken); // Changed to match "CheckinToken"
        intent.putExtra("PromoToken", promoToken); // Changed to match "PromoToken"
        // Start the next activity with the prepared intent
        startActivity(intent);
    }

    private String getCalendarDate() {
    return null;
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        showTimePickerDialog();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Do something with the selected date and time
                        Toast.makeText(EventActivity.this, "Selected date and time: " + calendar.getTime(), Toast.LENGTH_LONG).show();
                        editEventTime.setText(String.valueOf(calendar.getTime()));
                    }
                }, hourOfDay, minute, false);
        timePickerDialog.show();
    }
    /**
     * Converts the image located at the provided Uri to a Base64 encoded string.
     * @param imageUri The Uri of the image to be converted.
     * @return A Base64 encoded string representation of the image, or null if conversion fails.
     */
    private String convertImageUriToString(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes UI components and configures the Firebase Firestore instance.
     */
    private void initializeUIComponents() {
        poster = findViewById(R.id.imageView_CreateEventPage);
        editEventName = findViewById(R.id.editTextEventName_CreateEventPage);
        editEventTime = findViewById(R.id.buttonEventTime_CreateEventPage);
        editlocation = findViewById(R.id.editTextEventLocation_CreateEventPage);
        editLimit = findViewById(R.id.editTextEventLimit_CreateEventPage);
        editEventDescription = findViewById(R.id.editTextEventDescription_CreateEventPage);
        generateEventButton = findViewById(R.id.buttonSave_CreateEventPage);
        uploadPosterButton = findViewById(R.id.editPosterButton_CreateEventPage);
        deletePosterButton = findViewById(R.id.deletePosterButton_CreateEventPage);
        FrameLayout backToOrganiser = findViewById(R.id.buttonBack_CreateEventPage);


        // Navigate back to OrganisorActivity
        backToOrganiser.setOnClickListener(v -> finish());
    }

    /**
     * Sets up listeners for various button actions within the activity.
     */
    private void setupButtonListeners() {
        generateEventButton.setOnClickListener(v -> saveEventData());

        editEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        uploadPosterButton.setOnClickListener(view -> ImagePicker.with(EventActivity.this)
                .crop() // Optional image cropping
                .compress(1024) // Compress image size
                .maxResultSize(1080, 1080) // Max resolution
                .start());

        deletePosterButton.setOnClickListener(view -> poster.setImageResource(R.drawable.sample_poster)); // Reset to default poster
    }

    /**
     * Handles the result returned from the image picker activity. Sets the selected
     * image as the poster's ImageView content if successful.
     * @param requestCode The request code passed in startActivityForResult().
     * @param resultCode The result code returned by the child activity.
     * @param data An Intent that carries the result data.
     */
    private void handleImagePickerResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE && data != null) {
            imageUri = data.getData();
            poster.setImageURI(imageUri); // Directly update the ImageView with the image Uri
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}