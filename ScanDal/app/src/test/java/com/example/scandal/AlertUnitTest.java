package com.example.scandal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AlertUnitTest {
    @Test
    public void testConstructor() {
        String testMessage = "Initial message";
        Alert alert = new Alert(testMessage);
        assertEquals(testMessage, alert.getMessage());
    }

    @Test
    public void testGetMessage() {
        String testMessage = "Test message";
        Alert alert = new Alert(testMessage);
        assertEquals(testMessage, alert.getMessage());
    }

    @Test
    public void testSetMessage() {
        String initialMessage = "Initial message";
        Alert alert = new Alert(initialMessage);

        String updatedMessage = "Updated message";
        alert.setMessage(updatedMessage);

        assertEquals(updatedMessage, alert.getMessage());
    }
}
