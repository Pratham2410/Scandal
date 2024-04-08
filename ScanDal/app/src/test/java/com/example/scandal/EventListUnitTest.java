package com.example.scandal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;


public class EventListUnitTest {
    private EventList eventList;
    private Event event1;
    private Event event2;

    @Before
    public void setUp() {
        eventList = new EventList();
        event1 = new Event(eventList);
        event2 = new Event(eventList);

        event1.setSignInQRCode("QRCode1");
        event2.setPromoQRCode("QRCode2");
    }

    @Test
    public void addEventAndSearchEvent() {
        eventList.addEvent(event1);
        Event foundEvent = eventList.searchEvent("QRCode1");
        assertNotNull(foundEvent);
        assertEquals(event1, foundEvent);

        // Trying to find an event that does not exist
        assertNull(eventList.searchEvent("nonExistingQRCode"));
    }

    @Test
    public void deleteEvent() {
        eventList.addEvent(event1);
        eventList.addEvent(event2);
        eventList.deleteEvent(event1);
        assertNull(eventList.searchEvent("QRCode1"));
        assertNotNull(eventList.searchEvent("QRCode2"));
    }
}
