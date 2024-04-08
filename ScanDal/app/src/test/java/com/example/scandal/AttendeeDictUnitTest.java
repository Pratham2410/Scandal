package com.example.scandal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AttendeeDictUnitTest {
    private AttendeeDict attendeeDict;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        attendeeDict = new AttendeeDict();
        user1 = new User();
        user2 = new User();
    }

    @Test
    public void testAddUserFirstTime() {
        attendeeDict.addUserFirstTime(user1);
        assertEquals((Integer)1, attendeeDict.getAttendeeCount());
        assertEquals(0, attendeeDict.getSignInCount(user1));
    }

    @Test
    public void testIncrementSignIn() {
        attendeeDict.addUserFirstTime(user1);
        attendeeDict.incrementSignIn(user1);
        assertEquals(1, attendeeDict.getSignInCount(user1));
    }

    @Test
    public void testGetSignInCountForNonExistingUser() {
        int count = attendeeDict.getSignInCount(user2);
        assertEquals(0, count);
    }

    @Test
    public void testMultipleSignIns() {
        attendeeDict.addUserFirstTime(user1);
        attendeeDict.incrementSignIn(user1);
        attendeeDict.incrementSignIn(user1);
        assertEquals(2, attendeeDict.getSignInCount(user1));
    }

    @Test
    public void testGetAttendeeCountMultipleUsers() {
        attendeeDict.addUserFirstTime(user1);
        attendeeDict.addUserFirstTime(user2);
        assertEquals((Integer)2, attendeeDict.getAttendeeCount());
    }
}
