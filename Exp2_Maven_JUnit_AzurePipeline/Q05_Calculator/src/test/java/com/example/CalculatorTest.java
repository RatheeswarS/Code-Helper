package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private final Calculator calc = new Calculator();

    @Test
    void testAddition() {
        assertEquals(15.0, calc.add(10, 5), 0.0001);
    }

    @Test
    void testSubtraction() {
        assertEquals(5.0, calc.subtract(10, 5), 0.0001);
    }

    @Test
    void testMultiplication() {
        assertEquals(50.0, calc.multiply(10, 5), 0.0001);
    }

    @Test
    void testDivision() {
        assertEquals(2.0, calc.divide(10, 5), 0.0001);
    }

    @Test
    void testDivisionByZero() {
        ArithmeticException ex = assertThrows(ArithmeticException.class,
                () -> calc.divide(10, 0));
        assertEquals("Division by zero is not allowed", ex.getMessage());
    }
}
