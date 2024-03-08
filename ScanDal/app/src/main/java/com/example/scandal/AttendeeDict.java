package com.example.scandal;

import java.util.HashMap;
import java.util.Map;

/**
 * A dictionary for storing attendees of an event
 */
public class AttendeeDict {
    private Map<User, Integer> attendees = new HashMap<>();

    public void addUserFirstTime(User user) {
        attendees.put(user, 0);
    }

    public void incrementSignIn(User user) {
        int count = attendees.getOrDefault(user, 0);
        attendees.put(user, count + 1);
    }

    public int getSignInCount(User user) {
        return attendees.getOrDefault(user, 0);
    }
}
