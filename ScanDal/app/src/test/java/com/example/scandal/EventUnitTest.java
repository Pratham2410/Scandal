package com.example.scandal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class EventUnitTest {

    private Event event;

    @Before
    void setUp() {
        //event = new Event();
        event.setSignInQRCode("signInQRCodeExample");
        event.setPromoQRCode("promoQRCodeExample");
        event.setDesc("descriptionExample");
    }

    @Test
    void testGetSignInQRCode() {
        assertEquals("signInQRCodeExample", event.getSignInQRCode(), "The signInQRCode did not match the expected value.");
    }

    @Test
    void testGetPromoQRCode() {
        assertEquals("promoQRCodeExample", event.getPromoQRCode(), "The promoQRCode did not match the expected value.");
    }

    @Test
    void testGetDesc() {
        assertEquals("descriptionExample", event.getDesc(), "The description did not match the expected value.");
    }
}

