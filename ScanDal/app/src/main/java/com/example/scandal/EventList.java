package com.example.scandal;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of all events managed by ScanDal
 */
public class EventList {
    /** A list of events to populate activity */
    private List<Event> eventDatabase = new ArrayList<>();
    /**
     *  Method to add an event to the list
     * @param event An event object
     * @see Event
     */
    public void addEvent(Event event) {
        eventDatabase.add(event);
    }

    /**
     *  Method to search for an event by its QR code
     * @param QRCode QRCode belonging to Event begin searched for
     * @return Event object associated with given QRCode
     * @see QRCode
     * @see Event
     */
    public Event searchEvent(String QRCode) {
        for (Event event : eventDatabase) {
            if (event.getSignInQRCode().equals(QRCode) || event.getPromoQRCode().equals(QRCode)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Method to delete an event from the list
     * @param event The event to be deleted
     * @see Event
     */
    public void deleteEvent(Event event) {
        eventDatabase.remove(event);
    }
}
