package com.example;

import java.util.Scanner;

public class Discount {

    // 10% discount when purchase amount >= Rs.1000, otherwise no discount
    public static double finalAmount(double amount) {
        if (amount >= 1000) {
            return amount - (amount * 10 / 100);
        }
        return amount;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter purchase amount: ");
        double amount = sc.nextDouble();
        double fin = finalAmount(amount);
        System.out.println();
        System.out.printf("Purchase amount : Rs. %.2f%n", amount);
        System.out.printf("Discount        : Rs. %.2f%n", amount - fin);
        System.out.printf("Final amount    : Rs. %.2f%n", fin);
        sc.close();
    }
}
