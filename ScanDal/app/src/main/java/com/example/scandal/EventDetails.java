package com.example.scandal;

import android.provider.MediaStore;

public class EventDetails {
    private String description;
    private MediaStore.Images poster;

    public void setDesc(String description) {
        this.description = description;
    }

    public void setPoster(MediaStore.Images poster) {
        this.poster = poster;
    }

    // Getters
    public String getDesc() {
        return description;
    }

    public MediaStore.Images getPoster() {
        return poster;
    }
}
