echo "===== Java version ====="
java -version

echo "===== Creating NumberCheck.java ====="
cat > NumberCheck.java <<'EOF'
import java.util.Scanner;

public class NumberCheck {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int n = sc.nextInt();
        if (n > 0) {
            System.out.println(n + " is POSITIVE");
        } else if (n < 0) {
            System.out.println(n + " is NEGATIVE");
        } else {
            System.out.println("The number is ZERO");
        }
        sc.close();
    }
}
EOF

echo "===== Compiling ====="
javac NumberCheck.java

echo "===== Executing ====="
echo "-15" | java NumberCheck
