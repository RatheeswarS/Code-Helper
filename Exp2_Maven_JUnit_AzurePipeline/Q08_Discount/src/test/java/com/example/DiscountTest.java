package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiscountTest {

    @Test
    void belowThousandNoDiscount() {
        assertEquals(999.0, Discount.finalAmount(999), 0.001);
    }

    @Test
    void exactlyThousandGetsDiscount() {
        assertEquals(900.0, Discount.finalAmount(1000), 0.001);
    }

    @Test
    void aboveThousandGetsDiscount() {
        assertEquals(1350.0, Discount.finalAmount(1500), 0.001);
    }
}
