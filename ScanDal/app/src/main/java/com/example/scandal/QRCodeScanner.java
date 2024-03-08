package com.example.scandal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QRCodeScanner extends AppCompatActivity {
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
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning_qr);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
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
                            Intent nextIntent = new Intent(QRCodeScanner.this, EventPage.class); // Put the next activity here to go there after a QR is scanned
                            nextIntent.putExtra("QRToken", decoded); // access the token in the NextActivity using this QRToken key
                            startActivity(nextIntent);
                        } //else if (act == 2) {
//                            Intent nextIntent = new Intent(QRCodeScanner.this, OtherNextActivity.class); // Put the next activity here to go there after a QR is scanned
//                            nextIntent.putExtra("QRToken", decoded); // access the token in the NextActivity using this QRToken key
//                            nextIntent.putExtra("userID", userID);
//                            QRCodeScanner.this.startActivity(nextIntent);
//                        } else{
//                            Log.e("hpeebles", "QRCodeScanner needs a integer to be passed through an intent to function properly");
//                        }
                        //Toast.makeText(CameraTesting.this, decoded, Toast.LENGTH_SHORT).show();

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
