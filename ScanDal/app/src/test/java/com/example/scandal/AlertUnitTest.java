package com.example.scandal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the Alert class.
 */
public class AlertUnitTest {

    /**
     * Test the constructor of the Alert class.
     */
    @Test
    public void testConstructor() {
        String testMessage = "Initial message";
        Alert alert = new Alert(testMessage);
        assertEquals(testMessage, alert.getMessage());
    }

    /**
     * Test the getMessage method of the Alert class.
     */
    @Test
    public void testGetMessage() {
        String testMessage = "Test message";
        Alert alert = new Alert(testMessage);
        assertEquals(testMessage, alert.getMessage());
    }

    /**
     * Test the setMessage method of the Alert class.
     */
    @Test
    public void testSetMessage() {
        String initialMessage = "Initial message";
        Alert alert = new Alert(initialMessage);

        String updatedMessage = "Updated message";
        alert.setMessage(updatedMessage);

        assertEquals(updatedMessage, alert.getMessage());
    }
}
