import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class M {

    private static void solve(Scanner scanner) {
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        long ans = 0;
        TreeMap<Integer, Integer> mp = new TreeMap<>();
        for (int j = n - 1; j >= 0; j--) {
            for(int i = 0; i < j; i++) {
                ans += mp.getOrDefault(a[j] * 2 - a[i], 0);
            }
            mp.put(a[j], mp.getOrDefault(a[j], 0) + 1);
        }
        System.out.println(ans);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t;
        t = scanner.nextInt();
        for (int i = 0; i < t; i++) {
            solve(scanner);
        }
    }
}
