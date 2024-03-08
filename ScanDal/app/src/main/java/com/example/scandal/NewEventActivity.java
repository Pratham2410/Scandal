package com.example.scandal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Placeholder;

import java.util.Random;

public class NewEventActivity extends AppCompatActivity {
    /**
     * This activity requires the events name to be passed tp it by intent
     * through it's caller add line myintent.putExtra("name", eventsName)
     * before the activity is started
     */
  ImageView checkin = findViewById(R.id.checkinQRCode);
  ImageView promo = findViewById(R.id.promoQRCode);
  AppCompatButton checkinChange = findViewById(R.id.buttonSaveCheckinCode);
  AppCompatButton promoChange= findViewById(R.id.buttonSavePromoCode);
  QRCode QR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_created_page);

        String name = getIntent().getStringExtra("name");
        Random rnd = new Random();
        String random_str = rnd.toString();
        String token = name+random_str;

        if (QR.generateQR(checkin, token))
            checkin.setImageBitmap(QR.getQRPic()); // For database use getQRToken
        else {
            Log.e("hpeebles", "Checkin QR generation failed");
        }
        Random rand = new Random();

        if (QR.generateQR(promo, rand.toString())) // error checing to  see if the QR generation was successful
            promo.setImageBitmap(QR.getQRPic()); // For database to get the token use QR.getQRToken
        else {
            Log.e("hpeebles", "promo QR generation failed");
        }
//         promoChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myintent = new Intent(NewEventActivity.this, PlaceHolder.class);
//                startActivity(myintent);
//
//
//            }
//        });
//
//        checkinChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myintent = new Intent(HomeActivity.this, Placeholder.class);
//                startActivity(myintent);
//            }
//        });

    }
}
