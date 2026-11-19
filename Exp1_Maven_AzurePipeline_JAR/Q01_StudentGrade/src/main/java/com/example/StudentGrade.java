package com.example;

import java.util.Scanner;

public class StudentGrade {

    public static double average(double m1, double m2, double m3) {
        return (m1 + m2 + m3) / 3.0;
    }

    public static char grade(double avg) {
        if (avg >= 90) {
            return 'A';
        } else if (avg >= 75) {
            return 'B';
        } else if (avg >= 50) {
            return 'C';
        } else {
            return 'F';
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter marks of subject 1: ");
        double m1 = sc.nextDouble();
        System.out.print("Enter marks of subject 2: ");
        double m2 = sc.nextDouble();
        System.out.print("Enter marks of subject 3: ");
        double m3 = sc.nextDouble();

        double avg = average(m1, m2, m3);
        System.out.println();
        System.out.printf("Average = %.2f%n", avg);
        System.out.println("Grade   = " + grade(avg));
        sc.close();
    }
}
