import java.util.Scanner;

public class J {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        n = scanner.nextInt();
        int[][] matrix = new int[n][n];
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            int tmp = scanner.nextInt();
            for (int j = 0; j < n; j++) {
                matrix[i][n - 1 - j] = tmp % 10;
                tmp /= 10;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j] == 1) {
                    graph[i][j] = 1;
                    for (int k = j + 1; k < n; k++) {
                        matrix[i][k] = (matrix[i][k] - matrix[j][k] + 10) % 10;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(graph[i][j]);
            }
            System.out.println();
        }
    }
}
