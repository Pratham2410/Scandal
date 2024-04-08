package com.example.scandal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the EventList class.
 */
public class EventListUnitTest {
    private EventList eventList;
    private Event event1;
    private Event event2;

    /**
     * Set up the test environment before each test method.
     */
    @Before
    public void setUp() {
        eventList = new EventList();
        event1 = new Event(eventList);
        event2 = new Event(eventList);

        event1.setSignInQRCode("QRCode1");
        event2.setPromoQRCode("QRCode2");
    }

    /**
     * Test adding an event to the event list and searching for it.
     */
    @Test
    public void addEventAndSearchEvent() {
        eventList.addEvent(event1);
        Event foundEvent = eventList.searchEvent("QRCode1");
        assertNotNull(foundEvent);
        assertEquals(event1, foundEvent);

        // Trying to find an event that does not exist
        assertNull(eventList.searchEvent("nonExistingQRCode"));
    }

    /**
     * Test deleting an event from the event list.
     */
    @Test
    public void deleteEvent() {
        eventList.addEvent(event1);
        eventList.addEvent(event2);
        eventList.deleteEvent(event1);
        assertNull(eventList.searchEvent("QRCode1"));
        assertNotNull(eventList.searchEvent("QRCode2"));
    }
}
