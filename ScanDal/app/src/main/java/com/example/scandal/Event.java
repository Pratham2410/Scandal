package com.example.scandal;
import android.provider.MediaStore;

public class Event {
    private String signInQRCode;
    private String promoQRCode;
    private AttendeeDict currentAttendees;
    private AttendeeDict registrants;
    private Alert alerts;
    private Integer maxAttendees;
    private String description;
    private MediaStore.Images poster;


    // Constructor
    public Event(EventList eventList) {
        eventList.addEvent(this);
    }

    // Methods
    public void signin(User user) {
        if(currentAttendees.getSignInCount(user)==0){
            currentAttendees.addUserFirstTime(user);
        }
        currentAttendees.incrementSignIn(user);
    }

    // Getters and Setters
    public String getSignInQRCode() {
        return signInQRCode;
    }

    public void setSignInQRCode(String signInQRCode) {
        this.signInQRCode = signInQRCode;
    }

    public String getPromoQRCode() {
        return promoQRCode;
    }

    public void setPromoQRCode(String promoQRCode) {
        this.promoQRCode = promoQRCode;
    }

    public AttendeeDict getCurrentAttendees() {
        return currentAttendees;
    }

    public void setCurrentAttendees(AttendeeDict currentAttendees) {
        this.currentAttendees = currentAttendees;
    }

    public AttendeeDict getRegistrants() {
        return registrants;
    }

    public void setRegistrants(AttendeeDict registrants) {
        this.registrants = registrants;
    }

    public Alert getAlerts() {
        return alerts;
    }

    public void setAlerts(Alert alerts) {
        this.alerts = alerts;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public void setPoster(MediaStore.Images poster) {
        this.poster = poster;
    }
    public String getDesc() {
        return description;
    }

    public MediaStore.Images getPoster() {
        return poster;
    }
    
    //Additional features
    public void displayOnMap(Usermap usermap) {
        // TODO: Implement the display on map when feature is ready
    }

    public void displayAll() {
        // TODO: Implement the displayAll when feature is ready
    }
}
