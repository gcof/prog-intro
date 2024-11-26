package game;

public interface Player {
    Move move(Position position, Cell cell, boolean drawOffered);
    String getName();
}
