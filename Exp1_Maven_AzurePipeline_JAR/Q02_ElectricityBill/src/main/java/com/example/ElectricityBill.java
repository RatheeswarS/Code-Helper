package com.example;

import java.util.Scanner;

public class ElectricityBill {

    // Rs.1.50 for first 100 units, Rs.2.50 for next 100, Rs.4.00 above 200
    public static double calculateBill(int units) {
        double bill;
        if (units <= 100) {
            bill = units * 1.50;
        } else if (units <= 200) {
            bill = 100 * 1.50 + (units - 100) * 2.50;
        } else {
            bill = 100 * 1.50 + 100 * 2.50 + (units - 200) * 4.00;
        }
        return bill;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter units consumed: ");
        int units = sc.nextInt();
        if (units < 0) {
            System.out.println("Units cannot be negative");
        } else {
            System.out.println();
            System.out.println("Units consumed : " + units);
            System.out.printf("Total bill     : Rs. %.2f%n", calculateBill(units));
        }
        sc.close();
    }
}
