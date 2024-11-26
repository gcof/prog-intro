package game;

public class BoardPositionWrapper implements Position {
    private final Position position;

    public BoardPositionWrapper(Position position) {
        this.position = position;
    }

    @Override
    public boolean isValid(Move move) {
        return position.isValid(move);
    }

    @Override
    public Cell getCell(int r, int c) {
        return position.getCell(r, c);
    }

    @Override
    public String showPositionToConsole() {
        return position.showPositionToConsole();
    }
}