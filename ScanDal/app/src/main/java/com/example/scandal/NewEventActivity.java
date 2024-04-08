package com.example.scandal;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/** Activity for managing the creation of a new event */
public class NewEventActivity extends AppCompatActivity {

    /**
     * ImageView for displaying the check-in QR code.
     */
    ImageView checkinQRCode;

    /**
     * ImageView for displaying the promo QR code.
     */
    ImageView promoQRCode;

    /**
     * Button for saving the check-in code.
     */
    AppCompatButton saveCheckinCode;

    /**
     * Button for saving the promo code.
     */
    AppCompatButton savePromoCode;
    /**
     * Button for going back.
     */
    FrameLayout backButton;
    /**
     * Text congratulating user on new event creation.
     */
    TextView congratsText;
    /**
     * String containing source of activity intent
     */
    String intentSource;
    /**
     * Text informing user of new event creation
     */
    TextView newEventText;
    /**
     * Button for saving project.
     */
    AppCompatButton saveProj;
    /**
     * Firebase Firestore instance for database operations
     */
    FirebaseFirestore db;

    /**
     * QRCode object for generating and handling QR codes.
     */
    QRCode promoQR;
    QRCode checkinQR;
    /**
     * token to be encoded in the default QR code for checkins
     */
    String token;
    /**
     *  the token to be encoded for the default QR code for event promo
     */
    String token2;
    /**
     * string of the event poster to make the passed intents smaller
     */
    Button shareCheckin;
    Button sharePromo;

    String name;
    String attendeeLimit;
    String description;
    //String imageString = getIntent().getStringExtra("posterImage");
    String eventLocation;
    String eventTime;
    static String imageString;
    /**
     *
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, initializing objects, etc.
     * param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    //  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_created_page);
        db = FirebaseFirestore.getInstance();
        // Initialize your components here
        initializeUI();
        shareCheckin = findViewById(R.id.shareCheckin);
        sharePromo = findViewById(R.id.sharePromoCode);

        intentSource = getIntent().getStringExtra("source");

        if (intentSource != null) {
            Log.e("etowsley", "Successfully passed intent.");
            saveCheckinCode.setVisibility(View.INVISIBLE);
            savePromoCode.setVisibility(View.INVISIBLE);
            saveProj.setVisibility(View.INVISIBLE);
            congratsText.setVisibility(View.INVISIBLE);
            newEventText.setVisibility(View.INVISIBLE);
            token = getIntent().getStringExtra("CheckInQRCodeEventDetails");
            token2 = getIntent().getStringExtra("PromoQRCodeEventDetails");
            if (token == null) {
                Log.e("etowsley", "Token was null");
            }
            else if (token2 == null) {
                Log.e("etowsley", "Token2 was null");
            }
                Log.e("etowsley", "intentSource was not null");
            }
        else {
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            //String imageString = getIntent().getStringExtra("posterImage");
            eventLocation = getIntent().getStringExtra("Location");
            eventTime = getIntent().getStringExtra("Time");
            attendeeLimit = getIntent().getStringExtra("attendeeLimit");
            token = getIntent().getStringExtra("CheckinToken");
            token2 = getIntent().getStringExtra("PromoToken");
            Log.e("etowsley", "NewEventActivity Source Intent was null");
        }

        generateQRs();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hpeebles", "Going to event page");
                finish();
            }
        });
        saveProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> event = new HashMap<>();
                event.put("name", name);
                event.put("attendeeLimit", attendeeLimit);
                event.put("time", eventTime);
                event.put("location", eventLocation);
                event.put("description", description);
                event.put("checkinToken", token);
                event.put("promoToken", token2);
                event.put("posterImage", imageString); // Add the image string to the event map
                final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                event.put("organizer", deviceId); // Add device ID as organizer
                //event.put("attendeeCount", 0);
                // Save event to Firestore
                Log.e("hpeebles", "before storing in db");
                db.collection("events")
                        .add(event)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String eventTopic = name + "organizer";

                                eventTopic = eventTopic.replace(" ", "_"); // Replace spaces with underscores

                                if (!eventTopic.isEmpty()) {
                                    // Remove spaces and special characters if necessary
                                    // eventTopic = eventTopic.replaceAll("\\s+","_");
                                    String finalEventTopic = eventTopic;
                                    FirebaseMessaging.getInstance().subscribeToTopic(eventTopic)
                                            .addOnCompleteListener(task -> {
                                                if (!task.isSuccessful()) {
                                                    Log.w("Subscription", "Topic subscription failed for topic: " + finalEventTopic);
                                                } else {
                                                    // Optionally notify the user of successful subscription
                                                    Toast.makeText(NewEventActivity.this, "Subscribed to " + finalEventTopic + " notifications", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                Log.e("hpeebles", "Added to DB");
                                Toast.makeText(NewEventActivity.this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                                Intent homePage = new Intent(NewEventActivity.this, HomeActivity.class);
                                startActivity(homePage);
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to create event", Toast.LENGTH_SHORT).show());
//                Intent homePage = new Intent(NewEventActivity.this, HomeActivity.class);
//                startActivity(homePage);
            }
        });

        /**
         * activates the QR scanner to get the custom qr code for checkins
         */
        saveCheckinCode.setOnClickListener(v -> {
            QRCodeScanner.imageString = imageString;
            Intent scanner = new Intent(NewEventActivity.this, QRCodeScanner.class);
            scanner.putExtra("Activity", 2);
            scanner.putExtra("name", name);
            scanner.putExtra("attendeeLimit", attendeeLimit);
            scanner.putExtra("Time", eventTime);
            scanner.putExtra("Location", eventLocation);
            scanner.putExtra("description", description);
            scanner.putExtra("PromoQRCode", token2);
            startActivity(scanner);
        });
        /**
         * activates the QR scanner to get the custom qr code for event promotion
         */
        savePromoCode.setOnClickListener(v -> {
            QRCodeScanner.imageString = imageString;
            Intent scanner = new Intent(NewEventActivity.this, QRCodeScanner.class);
            scanner.putExtra("Activity", 2);
            scanner.putExtra("name", name);
            scanner.putExtra("attendeeLimit", attendeeLimit);
            scanner.putExtra("Time", eventTime);
            scanner.putExtra("Location", eventLocation);
            scanner.putExtra("description", description);
            scanner.putExtra("QRCode", token);
            startActivity(scanner);        });
        shareCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareImage(checkinQR.getQRPic(), "CheckinCode");
            }
        });
        sharePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareImage(promoQR.getQRPic(), "PromoCode");
            }
        });
    }
    private void initializeUI(){
        backButton = findViewById(R.id.buttonBack_EventsCreatedPage);
        checkinQRCode = findViewById(R.id.checkinQRCode);
        promoQRCode = findViewById(R.id.promoQRCode);
        saveCheckinCode = findViewById(R.id.buttonSaveCheckinCode);
        savePromoCode = findViewById(R.id.buttonSavePromoCode);
        saveProj = findViewById(R.id.buttonSaveProject);
        congratsText = findViewById(R.id.textCongratEventsCreated);
        newEventText = findViewById(R.id.textNewEventsCreated);
    }
    private void generateQRs(){
        checkinQR = new QRCode(); // Assuming you have a default constructor
        promoQR = new QRCode();

        if (checkinQR.generateQR(checkinQRCode, token)) {
            checkinQRCode.setImageBitmap(checkinQR.getQRPic());
        } else {
            Log.e("NewEventActivity", "Checkin QR generation failed");
        }
        if (promoQR.generateQR(promoQRCode, token2)) {
            promoQRCode.setImageBitmap(promoQR.getQRPic());
        } else {
            Log.e("NewEventActivity", "Promo QR generation failed");
        }
    }

    /**
     * Shares in image pic with other apps
     * Source in large part:
     * https://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android
     * @param pic img being shared
     * @param textAccompany text that is the title
     */
    protected void shareImage(Bitmap pic, String textAccompany){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, textAccompany);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            pic.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }


}