package com.example.scandal;
import java.util.List;
public class EventList {
    private List<Event> eventDatabase;

    // Method to add an event to the list
    public void addEvent(Event event) {
        eventDatabase.add(event);
    }

    // Method to search for an event by its QR code
    public Event searchEvent(String QRCode) {
        for (Event event : eventDatabase) {
            if (event.getSignInQRCode().equals(QRCode) || event.getPromoQRCode().equals(QRCode)) {
                return event;
            }
        }
        return null;
    }

    // Method to delete an event from the list
    public void deleteEvent(Event event) {
        eventDatabase.remove(event);
    }
}
