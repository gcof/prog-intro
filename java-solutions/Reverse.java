import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;


public class Reverse {

    static ArrayList<ArrayList<Integer>> scanMatrix(MyScanner scanner) throws IOException {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        while (scanner.hasNextLine()) {
            ArrayList<Integer> row = new ArrayList<>();
            while (scanner.hasNextInCurrentLine()) {
                int nextInt = scanner.nextInt();
                row.add(nextInt);
            }
            scanner.skipEndLine();
            matrix.add(row);
        }
        return matrix;
    }

    static void printMatrixReversed(ArrayList<ArrayList<Integer>> matrix) {
        for (int i = matrix.size() - 1; i >= 0; i--) {
            for (int j = matrix.get(i).size() - 1; j >= 0; j--) {
                System.out.print(matrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        MyScanner cmdScanner = new MyScanner(System.in, Charset.defaultCharset());
        ArrayList<ArrayList<Integer>> matrix = null;
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
        printMatrixReversed(matrix);
    }
}