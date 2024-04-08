package com.example.scandal;

import java.util.HashMap;
import java.util.Map;

/**
 * A dictionary for storing attendees of an event
 */
public class AttendeeDict {
    /**
     * Map to store profile names and their Firestore document IDs.
     */
    private Map<User, Integer> attendees = new HashMap<>();

    /**
     * Adds user to dictionary on first sign in
     *
     * @param user A user who is singing into event
     */
    public void addUserFirstTime(User user) {
        attendees.put(user, 0);
    }

    /**
     * Increments the number of times a user has signed into event
     *
     * @param user the user singing in
     */
    public void incrementSignIn(User user) {
        int count = attendees.getOrDefault(user, 0);
        attendees.put(user, count + 1);
    }

    /**
     * Gets the number of time a user has signed into an event
     *
     * @param user User object
     * @return Number of times user signed in
     */
    public int getSignInCount(User user) {
        return attendees.getOrDefault(user, 0);
    }

    /**
     * Gets the current number of attendees
     * @return
     */
    public Integer getAttendeeCount() {
        return attendees.size();
    }
}