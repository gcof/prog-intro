import java.util.Scanner;

public class H {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, q;
        n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        q = scanner.nextInt();
        int[] b = new int[q];
        for (int i = 0; i < q; i++) {
            b[i] = scanner.nextInt();
        }
        int cnt, s;
        boolean fl;
        for (int i = 0; i < q; i++) {
            cnt = 0; s = 0;
            fl = true;
            for (int j = 0; j < n; j++) {
                if (a[j] > b[i]) {
                    fl = false;
                    break;
                }
                if (s + a[j] > b[i]) {
                    cnt++;
                    s = a[j];
                } else {
                    s += a[j];
                }
            }

            if (fl) {
                cnt++;
                System.out.println(cnt);
            } else {
                System.out.println("Impossible");
            }
        }
    }
}
