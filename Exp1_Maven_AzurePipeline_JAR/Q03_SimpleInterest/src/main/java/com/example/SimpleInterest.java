package com.example;

import java.util.Scanner;

public class SimpleInterest {

    public static double calculateSI(double principal, double rate, double time) {
        return (principal * rate * time) / 100;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter principal amount : ");
        double p = sc.nextDouble();
        System.out.print("Enter rate of interest : ");
        double r = sc.nextDouble();
        System.out.print("Enter time (years)     : ");
        double t = sc.nextDouble();

        double si = calculateSI(p, r, t);
        System.out.println();
        System.out.printf("Simple Interest = %.2f%n", si);
        System.out.printf("Total Amount    = %.2f%n", p + si);
        sc.close();
    }
}
