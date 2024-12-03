package game;

public class RhombusBoard extends MNKBoard {

    public RhombusBoard(int n, int k) {
        super(2*n-1, 2*n-1 , k);
        n = 2*n-1;
        int center = n / 2;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(i - center) + Math.abs(j - center) > center) {
                    cells[i][j] = Cell.N;
                }
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        int center = getN() / 2;
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < getN(); j++) {
                if (Math.abs(i - center) + Math.abs(j - center) > center) {
                    cells[i][j] = Cell.N;
                }
            }
        }
    }

    @Override
    public boolean isValid(Move move) {
        int row = move.getRow();
        int col = move.getColumn();
        int n = getN();

        int center = n / 2;
        if (Math.abs(row - center) + Math.abs(col - center) <= center) {
            return super.isValid(move);
        }
        return false;
    }

    @Override
    public Cell getCell(int row, int col) {
        int n = getN();
        int center = n / 2;

        if (Math.abs(row - center) + Math.abs(col - center) <= center) {
            return super.getCell(row, col);
        }
        return Cell.N;
    }
}