import java.util.Scanner;

public class H {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        int sumA = 0;
        int maxA = 0;

        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            sumA += a[i];
            if (a[i] > maxA) {
                maxA = a[i];
            }
        }

        int q = scanner.nextInt();
        int[] tValues = new int[q];
        for (int i = 0; i < q; i++) {
            tValues[i] = scanner.nextInt();
        }

        int[] f = new int[sumA + 1];
        int[] starts = new int[n];
        int[] answers = new int[sumA + 1];
        int next = 0;

        for (int i = 0; i < n; i++) {
            starts[i] = next + 1;
            for (int j = next + 1; j <= next + a[i]; j++) {
                f[j] = i;
            }
            next += a[i];
        }

        for (int i = 0; i < q; i++) {
            if (answers[tValues[i]] != 0) {
                if (answers[tValues[i]] == -1) {
                    System.out.println("Impossible");
                } else {
                    System.out.println(answers[tValues[i]]);
                }
                continue;
            }
            if (tValues[i] < maxA) {
                answers[tValues[i]] = -1;
                System.out.println("Impossible");
                continue;
            }

            int ans = 1;
            int cur = 1;

            while (cur + tValues[i] <= sumA) {
                cur = starts[f[Math.min(sumA, cur + tValues[i])]];
                ans++;
            }

            answers[tValues[i]] = ans;
            System.out.println(ans);
        }

        scanner.close();
    }
}
