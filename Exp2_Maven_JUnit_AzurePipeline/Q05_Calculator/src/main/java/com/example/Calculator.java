package com.example;

import java.util.Scanner;

public class Calculator {

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return a / b;
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number : ");
        double a = sc.nextDouble();
        System.out.print("Enter second number: ");
        double b = sc.nextDouble();

        System.out.println();
        System.out.println("Addition       : " + calc.add(a, b));
        System.out.println("Subtraction    : " + calc.subtract(a, b));
        System.out.println("Multiplication : " + calc.multiply(a, b));
        try {
            System.out.println("Division       : " + calc.divide(a, b));
        } catch (ArithmeticException e) {
            System.out.println("Division       : Error - " + e.getMessage());
        }
        sc.close();
    }
}
