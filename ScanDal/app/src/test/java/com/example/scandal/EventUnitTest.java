package com.example.scandal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Unit tests for the Event class.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class EventUnitTest {

    private Event event;

    /**
     * Sets up the test environment before each test case.
     */
    @Before
    void setUp() {
        //event = new Event();
        event.setSignInQRCode("signInQRCodeExample");
        event.setPromoQRCode("promoQRCodeExample");
        event.setDesc("descriptionExample");
    }

    /**
     * Tests the getSignInQRCode method of the Event class.
     */
    @Test
    void testGetSignInQRCode() {
        assertEquals("signInQRCodeExample", event.getSignInQRCode(), "The signInQRCode did not match the expected value.");
    }

    /**
     * Tests the getPromoQRCode method of the Event class.
     */
    @Test
    void testGetPromoQRCode() {
        assertEquals("promoQRCodeExample", event.getPromoQRCode(), "The promoQRCode did not match the expected value.");
    }

    /**
     * Tests the getDesc method of the Event class.
     */
    @Test
    void testGetDesc() {
        assertEquals("descriptionExample", event.getDesc(), "The description did not match the expected value.");
    }
}
