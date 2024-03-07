package com.example.scandal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.Nullable;


public class User extends Service {
    private String id; // Stores Emulator id
    private String name; // Idk if this is still needed

    // Do we need constructor?
    public User() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Remove Toast when Implementation Complete
        Toast toast = Toast.makeText(this, "Starting Service", Toast.LENGTH_SHORT);
        toast.show();

        // Drhuvil Please store this on firebase
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove Toast when Implementation Complete
        Toast toast = Toast.makeText(this, "Ending Service", Toast.LENGTH_SHORT);
        toast.show();
    }
}
