package game;

public class MNKBoard implements Board, Position {
    private final int n;
    private final int m;
    private final int k;
    protected final Cell[][] cells;
    private Cell currentCell;

    public MNKBoard(int n, int m, int k) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.cells = new Cell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = Cell.E;
            }
        }
        this.currentCell = Cell.X;
    }

    @Override
    public void clear() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = Cell.E;
            }
        }
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return currentCell;
    }

    @Override
    public boolean isValid(Move move) {
        int row = move.getRow();
        int col = move.getColumn();
        return row >= 0 && row < n && col >= 0 && col < m && cells[row][col] == Cell.E;
    }

    @Override
    public Cell getCell(int r, int c) {
        return cells[r][c];
    }

    @Override
    public Result makeMove(Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        cells[move.getRow()][move.getColumn()] = move.getValue();
        if (checkWin(move)) {
            return Result.WIN;
        }
        if (isDraw()) {
            return Result.DRAW;
        }
        currentCell = (currentCell == Cell.X) ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    private boolean checkWin(Move move) {
        int row = move.getRow();
        int col = move.getColumn();
        Cell cell = move.getValue();
        return checkDirection(row, col, cell, 1, 0) || // Horizontal
                checkDirection(row, col, cell, 0, 1) || // Vertical
                checkDirection(row, col, cell, 1, 1) || // Diagonal \
                checkDirection(row, col, cell, 1, -1);  // Diagonal /
    }

    private boolean checkDirection(int row, int col, Cell cell, int dRow, int dCol) {
        int count = 0;
        for (int i = -k + 1; i < k; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < n && c >= 0 && c < m && cells[r][c] == cell) {
                count++;
                if (count == k) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    private boolean isDraw() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (cells[i][j] == Cell.E) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getN() {
        return n;
    }

    @Override
    public String showPositionToConsole() {
        int maxDimension = Math.max(n, m);
        int numDigits = String.valueOf(maxDimension - 1).length();

        StringBuilder sb = new StringBuilder();
        for (int digit = 0; digit < numDigits; digit++) {
            sb.append(" ".repeat(numDigits + 1));
            for (int j = 0; j < m; j++) {
                String columnNumber = String.format("%" + numDigits + "d", j);
                sb.append(columnNumber.charAt(digit)).append(" ");
            }
            sb.append(System.lineSeparator());
        }

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%" + numDigits + "d ", i));
            for (int j = 0; j < m; j++) {
                if (cells[i][j] == Cell.E) {
                    sb.append(". ");
                } else if (cells[i][j] == Cell.N) {
                    sb.append("@ ");
                } else {
                    sb.append(cells[i][j]).append(" ");
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}