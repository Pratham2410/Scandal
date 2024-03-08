package com.example.scandal;

/**
 * Sends alerts to organizers and attendees
 */
public class Alert {
    private String message;

    public Alert(String message) {
        this.message = message;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
