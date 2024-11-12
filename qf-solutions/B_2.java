import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        int now = -710*25000;
        n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println(now);
            now += 710;
        }
    }
}
