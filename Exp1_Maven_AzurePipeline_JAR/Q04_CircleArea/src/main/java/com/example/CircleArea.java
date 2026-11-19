package com.example;

import java.util.Scanner;

public class CircleArea {

    public static double area(double radius) {
        return Math.PI * radius * radius;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter radius of the circle: ");
        double r = sc.nextDouble();
        if (r < 0) {
            System.out.println("Radius cannot be negative");
        } else {
            System.out.printf("Area of the circle = %.2f%n", area(r));
        }
        sc.close();
    }
}
