import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;


public class ReverseSumAbsMod {
    static final int MOD = 1_000_000_007;

    static int[][] scanMatrix(MyScanner scanner) throws IOException {
        int nowRows = 0;
        int[][] matrix = new int[1][];
        while (scanner.hasNextLine()) {
            int nowCols = 0;
            int[] row = new int[1];
            while (scanner.hasNextInCurrentLine()) {
                int nextInt = scanner.nextInt();
                if (nowCols >= row.length) {
                    row = Arrays.copyOf(row, row.length * 2);
                }
                row[nowCols++] = nextInt;
            }
            scanner.skipEndLine();

            if (nowRows >= matrix.length) {
                matrix = Arrays.copyOf(matrix, matrix.length * 2);
            }
            matrix[nowRows++] = Arrays.copyOf(row, nowCols);
        }
        return Arrays.copyOf(matrix, nowRows);
    }

    // merge

    static int[][] evalColRow(int[][] matrix) {
        int rowMaxSize = 0;
        for (int[] a: matrix) {
            rowMaxSize = Math.max(rowMaxSize, a.length);
        }
        int[] cols = new int[rowMaxSize];
        int[] rows = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int toAdd = Math.abs(matrix[i][j]) % MOD;
                cols[j] = (cols[j] + toAdd) % MOD;
                rows[i] = (rows[i] + toAdd) % MOD;
            }
        }
        return new int[][]{cols, rows};
    }

    static void printMatrixReversedSumMod(int[][] matrix) {
        int[][] colRow = evalColRow(matrix);
        int[] colSum = colRow[0];
        int[] rowSum = colRow[1];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int toOut = (rowSum[i] + colSum[j] - Math.abs(matrix[i][j]) % MOD) % MOD;
                if (toOut < 0) {
                    toOut += MOD;
                }
                System.out.print(toOut + " ");
            }
            System.out.println();
        }
    }

    static void printMatrixDebug(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.err.print(matrix[i][j] + " ");
            }
            System.err.println();
        }
    }


    public static void main(String[] args) {
        MyScanner cmdScanner = new MyScanner(System.in, Charset.defaultCharset());
        int[][] matrix = null;
        try {
            matrix = scanMatrix(cmdScanner);
        } catch (IOException e) {
            System.err.println("Error while reading matrix: " + e.getMessage());
            return;
        }
        try {
            cmdScanner.close();
        } catch (IOException e) {
            System.err.println("Error while closing matrix: " + e.getMessage());
        }
        // printMatrixDebug(matrix);
        printMatrixReversedSumMod(matrix);
        // count free memory
        System.err.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }
}