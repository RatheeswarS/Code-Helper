package com.example;

import java.util.Scanner;

public class ArrayAverage {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double[] nums = new double[5];
        double sum = 0;
        System.out.println("Enter 5 numbers:");
        for (int i = 0; i < nums.length; i++) {
            nums[i] = sc.nextDouble();
            sum += nums[i];
        }
        double avg = sum / nums.length;
        System.out.printf("Sum     = %.2f%n", sum);
        System.out.printf("Average = %.2f%n", avg);
        sc.close();
    }
}
