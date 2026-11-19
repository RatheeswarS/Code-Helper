package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LeapYearTest {

    // ---- leap years (divisible by 4, not by 100)
    @Test
    void year2024IsLeap() {
        assertTrue(LeapYear.isLeapYear(2024));
    }

    @Test
    void year2016IsLeap() {
        assertTrue(LeapYear.isLeapYear(2016));
    }

    // ---- non-leap years
    @Test
    void year2023IsNotLeap() {
        assertFalse(LeapYear.isLeapYear(2023));
    }

    @Test
    void year2019IsNotLeap() {
        assertFalse(LeapYear.isLeapYear(2019));
    }

    // ---- century years
    @Test
    void century2000IsLeap() {          // divisible by 400
        assertTrue(LeapYear.isLeapYear(2000));
    }

    @Test
    void century1900IsNotLeap() {       // divisible by 100, not by 400
        assertFalse(LeapYear.isLeapYear(1900));
    }

    @Test
    void century2100IsNotLeap() {
        assertFalse(LeapYear.isLeapYear(2100));
    }
}
