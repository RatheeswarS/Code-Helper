import java.util.Scanner;

public class GrossSalary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Basic Salary: ");
        double basic = sc.nextDouble();
        System.out.print("Enter HRA: ");
        double hra = sc.nextDouble();
        System.out.print("Enter DA: ");
        double da = sc.nextDouble();

        double gross = basic + hra + da;
        System.out.println();
        System.out.printf("Basic Salary : %.2f%n", basic);
        System.out.printf("HRA          : %.2f%n", hra);
        System.out.printf("DA           : %.2f%n", da);
        System.out.printf("Gross Salary : %.2f%n", gross);
        sc.close();
    }
}
