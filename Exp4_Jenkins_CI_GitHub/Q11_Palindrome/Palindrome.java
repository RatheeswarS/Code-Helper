import java.util.Scanner;

public class Palindrome {

    public static boolean isPalindrome(int num) {
        if (num < 0) {
            return false;
        }
        int original = num;
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num = num / 10;
        }
        return original == reversed;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int n = sc.nextInt();
        if (isPalindrome(n)) {
            System.out.println(n + " is a PALINDROME");
        } else {
            System.out.println(n + " is NOT a palindrome");
        }
        sc.close();
    }
}
