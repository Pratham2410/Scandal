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

    private EventList eventList;
    private Event event;
    private User user;

    /**
     * Sets up the test environment before each test case.
     */
    @Before
    public void setUp() {
        eventList = new EventList();
        event = new Event(eventList);
        user = new User();
    }

    /**
     * Tests the getSignInQRCode method of the Event class.
     */
    @Test
    public void propertiesSetAndGet() {
        String promoQRCode = "PromoQR";
        event.setPromoQRCode(promoQRCode);
        assertEquals(promoQRCode, event.getPromoQRCode());

        String description = "Description of the event";
        event.setDesc(description);
        assertEquals(description, event.getDesc());
    }
}
