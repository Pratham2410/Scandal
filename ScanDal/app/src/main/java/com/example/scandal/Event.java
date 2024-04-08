package com.example.scandal;
import android.provider.MediaStore;

/**
 * Represents an event
 */
public class Event {
    /**
     * A string representing the token of the even QRCode
     */
    private String signInQRCode;
    /**
     * A string representing the QRCode for event promotion
     */
    private String promoQRCode;
    /**
     * A dictionary storing all attendees currently attending the event
     */
    private AttendeeDict currentAttendees;
    /**
     * A dictionary storing all attendees registered through the event promotion QRCode
     */
    private AttendeeDict registrants;
    /**
     * An Alert class object
     * @see Alert
     */
    private Alert alerts;
    /**
     * The max number of attendees able to attend
     */
    private Integer maxAttendees;
    /**
     * The description of the event
     */
    private String description;
    /**
     * The event poster
     */
    private MediaStore.Images poster;
    /**
     * Integer storing current number of attendees
     */
    private Integer attendeeCount;


    // Constructor

    /**
     * Add's the event to the event list
     * @param eventList instance of EventList class
     * @see EventList
     */
    public Event(EventList eventList) {
        eventList.addEvent(this);
        this.currentAttendees = new AttendeeDict();
        this.registrants = new AttendeeDict();
        attendeeCount = currentAttendees.getAttendeeCount();
    }

    // Methods

    /**
     * Signs in user to event
     * @param user
     * @see User
     */
    public void signin(User user) {
        if(currentAttendees.getSignInCount(user)==0){
            currentAttendees.addUserFirstTime(user);
        }
        currentAttendees.incrementSignIn(user);
        attendeeCount = currentAttendees.getAttendeeCount();
    }

    // Getters and Setters

    /**
     * @return the string representing QRCode used for sign in
     */
    public String getSignInQRCode() {
        return signInQRCode;
    }

    /**
     * @param signInQRCode the string representing QRCode used for sign in
     */
    public void setSignInQRCode(String signInQRCode) {
        this.signInQRCode = signInQRCode;
    }

    /**
     * @return the string representing QRCode used for promotion
     */
    public String getPromoQRCode() {
        return promoQRCode;
    }

    /**
     * @param promoQRCode the string representing QRCode used for promotion
     */
    public void setPromoQRCode(String promoQRCode) {
        this.promoQRCode = promoQRCode;
    }

    /**
     * @return current attendee dictionary
     * @see AttendeeDict
     */
    public AttendeeDict getCurrentAttendees() {
        return currentAttendees;
    }

    /**
     * @param currentAttendees current attendee dictionary
     * @see AttendeeDict
     */
    public void setCurrentAttendees(AttendeeDict currentAttendees) {
        this.currentAttendees = currentAttendees;
    }

    /**
     * @return registered attendee dictionary
     * @see AttendeeDict
     */
    public AttendeeDict getRegistrants() {
        return registrants;
    }

    /**
     * @param registrants registered attendee dictionary
     * @see AttendeeDict
     */
    public void setRegistrants(AttendeeDict registrants) {
        this.registrants = registrants;
    }

    /**
     * @return Alert object
     * @see Alert
     */
    public Alert getAlerts() {
        return alerts;
    }

    /**
     * @param alerts Alert object
     * @see Alert
     */
    public void setAlerts(Alert alerts) {
        this.alerts = alerts;
    }

    /**
     * @return max number of attendees
     */
    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    /**
     * @param maxAttendees max number of attendees
     */
    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    /**
     * @param description event description
     */
    public void setDesc(String description) {
        this.description = description;
    }

    /**
     * @param poster event poster
     * @see MediaStore.Images
     */
    public void setPoster(MediaStore.Images poster) {
        this.poster = poster;
    }

    /**
     * @return event description
     */
    public String getDesc() {
        return description;
    }

    /**
     * @return event poster
     * @see MediaStore.Images
     */
    public MediaStore.Images getPoster() {
        return poster;
    }

    public Integer getAttendeeCount() {
        return attendeeCount;
    }
    //Additional features
    public void displayOnMap(Usermap usermap) {
        // TODO: Implement the display on map when feature is ready
    }

    public void displayAll() {
        // TODO: Implement the displayAll when feature is ready
    }
}