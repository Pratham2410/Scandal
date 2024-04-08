package com.example.scandal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the AttendeeDict class.
 */
public class AttendeeDictUnitTest {

    private AttendeeDict attendeeDict;
    private User user1;
    private User user2;

    /**
     * Set up the test environment before each test method.
     */
    @Before
    public void setUp() {
        attendeeDict = new AttendeeDict();
        user1 = new User();
        user2 = new User();
    }

    /**
     * Test adding a user for the first time.
     */
    @Test
    public void testAddUserFirstTime() {
        attendeeDict.addUserFirstTime(user1);
        assertEquals((Integer)1, attendeeDict.getAttendeeCount());
        assertEquals(0, attendeeDict.getSignInCount(user1));
    }

    /**
     * Test incrementing sign-in count for a user.
     */
    @Test
    public void testIncrementSignIn() {
        attendeeDict.addUserFirstTime(user1);
        attendeeDict.incrementSignIn(user1);
        assertEquals(1, attendeeDict.getSignInCount(user1));
    }

    /**
     * Test getting sign-in count for a non-existing user.
     */
    @Test
    public void testGetSignInCountForNonExistingUser() {
        int count = attendeeDict.getSignInCount(user2);
        assertEquals(0, count);
    }

    /**
     * Test multiple sign-ins for a user.
     */
    @Test
    public void testMultipleSignIns() {
        attendeeDict.addUserFirstTime(user1);
        attendeeDict.incrementSignIn(user1);
        attendeeDict.incrementSignIn(user1);
        assertEquals(2, attendeeDict.getSignInCount(user1));
    }

    /**
     * Test getting attendee count with multiple users.
     */
    @Test
    public void testGetAttendeeCountMultipleUsers() {
        attendeeDict.addUserFirstTime(user1);
        attendeeDict.addUserFirstTime(user2);
        assertEquals((Integer)2, attendeeDict.getAttendeeCount());
    }
}
