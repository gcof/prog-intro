package game;

public class CheaterPlayer implements Player{

    @Override
    public String getName() {
        return "CheaterBot";
    }

    @Override
    public Move move(final Position position, final Cell cell, boolean drawOffered) {
        Board board = (Board) position;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    board.makeMove(move);
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }
}
