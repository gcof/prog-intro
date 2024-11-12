import java.util.Scanner;

public class I {

    final static int INF = 1_000_000_000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        n = scanner.nextInt();
        int xl = INF, xr = -INF, yl = INF, yr = -INF;
        int xi, yi, hi;
        for (int i = 0; i < n; i++) {
            xi = scanner.nextInt();
            yi = scanner.nextInt();
            hi = scanner.nextInt();
            xl = Math.min(xl, xi - hi);
            xr = Math.max(xr, xi + hi);
            yl = Math.min(yl, yi - hi);
            yr = Math.max(yr, yi + hi);
        }
        int h = (Math.max(xr - xl, yr - yl) + 1) / 2;
        int x = (xl + xr) / 2;
        int y = (yl + yr) / 2;
        System.out.println(x + " " + y + " " + h);
        scanner.close();
    }
}
