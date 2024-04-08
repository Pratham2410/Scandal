package com.example.scandal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
/**
 * Activity which takes care of scanning a QR code and
 * after scanning switches to the next activity passing
 * the QR's token in a intent with name QRToken (see line 39 and 40)
 * git hub source:
 * https://github.com/yuriy-budiyev/code-scanner
 * licensing for code:
 * MIT License
 *
 * Copyright (c) 2017 Yuriy Budiyev [yuriy.budiyev@yandex.ru]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class QRCodeScanner extends AppCompatActivity {
    /**
     * A code scanner object to handle QR code scanning flow
     */
    private CodeScanner mCodeScanner;
    static String imageString;
    /**
     * Displays QRCode scanner to user, and handles the scanning procedure
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning_qr);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        int requestCode = 100;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, requestCode);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String decoded = result.getText();
                        String userID = getIntent().getStringExtra("userID");
                        int act = getIntent().getIntExtra("Activity", 0);
                        if (act == 1) {
                            Log.e("hpeebles", "In activity starter");
                            Intent nextIntent = new Intent(QRCodeScanner.this, ConfirmationPage.class); // Put the next activity here to go there after a QR is scanned
                            nextIntent.putExtra("QRToken", decoded); // access the token in the NextActivity using this QRToken key

                            startActivity(nextIntent);
                            finish();
                        } else if (act == 2) {
                            String name = getIntent().getStringExtra("name");
                            String description = getIntent().getStringExtra("description");
                            String eventLocation = getIntent().getStringExtra("Location");
                            String eventTime = getIntent().getStringExtra("Time");
                            String attendeeLimit = getIntent().getStringExtra("attendeeLimit");
                            NewEventActivity.imageString =imageString;
                            Intent intent = new Intent(QRCodeScanner.this, NewEventActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("Time", eventTime);
                            intent.putExtra("attendeeLimit", attendeeLimit);
                            intent.putExtra("Location", eventLocation);
                            intent.putExtra("description", description);

                            Bundle extras = getIntent().getExtras();
                            if (extras.containsKey("PromoQRCode")){
                                String token2 = getIntent().getStringExtra("PromoQRCode");
                                intent.putExtra("PromoToken", token2);
                                intent.putExtra("CheckinToken", decoded);
                            } else if (extras.containsKey("QRCode")){
                                String token2 = getIntent().getStringExtra("QRCode");
                                intent.putExtra("CheckinToken", token2);
                                intent.putExtra("PromoToken", decoded);
                            }
                            startActivity(intent);
                            finish();
                        } //else{
//                            Log.e("hpeebles", "QRCodeScanner needs a integer to be passed through an intent to function properly");
//                        }
//                        Toast.makeText(CameraTesting.this, decoded, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}
