package com.example.scandal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import com.example.scandal.Profile;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Unit Test for the Profile class.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class ProfileUnitTest {

    /**
     * Helper method to create a mock profile with default values.
     * @return A Profile object with default values.
     */
    private Profile mockProfile() {
        return new Profile("John Doe","123-456-7890","http://www.example.com");
    }

    /**
     * Tests the setName and getName methods.
     */
    @Test
    public void testSetNameAndGetName() {
        Profile profile = mockProfile();
        String testName = "John Doe";
        profile.setName(testName);
        assertEquals(testName, profile.getName());
    }

    /**
     * Tests the setGeoTracking and getGeoTracking methods.
     */
    @Test
    public void testSetGeoTrackingAndGetGeoTracking() {
        Profile profile = mockProfile();
        Integer testGeoTracking = 1;
        profile.setGeoTracking(testGeoTracking);
        assertEquals(testGeoTracking, profile.getGeoTracking());
    }

    /**
     * Tests the setPhoneNumber and getPhoneNumber methods.
     */
    @Test
    public void testSetPhoneNumberAndGetPhoneNumber() {
        Profile profile = mockProfile();
        String testPhoneNumber = "123-456-7890";
        profile.setPhoneNumber(testPhoneNumber);
        assertEquals(testPhoneNumber, profile.getPhoneNumber());
    }

    /**
     * Tests the setHomePage and getHomePage methods.
     */
    @Test
    public void testSetHomePageAndGetHomePage() {
        Profile profile = mockProfile();
        String testHomePage = "http://www.example.com";
        profile.setHomePage(testHomePage);
        assertEquals(testHomePage, profile.getHomePage());
    }
}
