package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Move {
    private final int row;
    private final int column;
    private final Cell value;
    public static final Move OFFER_DRAW = new Move(0, 0, null);
    public static final Move RESIGN = new Move(0, 0, null);

    public Move(final int row, final int column, final Cell value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Cell getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "row=" + row + ", column=" + column + ", value=" + value;
    }
}
