package game;

import java.io.PrintStream;
import java.util.Scanner;


public class HumanPlayer implements Player {
    private final PrintStream out;
    private Scanner in;
    private String name;

    public HumanPlayer(final PrintStream out, final Scanner in, final String name) {
        this.out = out;
        this.in = in;
        this.name = name;
    }

    public HumanPlayer(String name) {
        this(System.out, new Scanner(System.in), name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Move move(final Position position, final Cell cell, boolean drawOffered) {
        in = new Scanner(System.in);
        while (true) {
            out.println("Position");
            out.println(position.showPositionToConsole());
            out.println(cell + "'s move");
            out.println("Enter row and column, or type \"resign\" to surrender, or \"offer draw\" to propose a draw");

            String line = in.nextLine().trim();

            if (line.equals("resign")) {
                return Move.RESIGN;
            }

            if (line.equals("offer draw")) {
                if (drawOffered) {
                    out.println("You have already offered a draw this turn.");
                    continue;
                }
                return Move.OFFER_DRAW;
            }

            Scanner lineScanner = new Scanner(line);
            if (lineScanner.hasNextInt()) {
                int row = lineScanner.nextInt();
                int col;
                if (lineScanner.hasNextInt()) {
                    col = lineScanner.nextInt();
                } else {
                    out.println("Invalid input. Please enter row and column.");
                    continue;
                }
                lineScanner.close();
                Move move = new Move(row, col, cell);
                if (position.isValid(move)) {
                    return move;
                } else {
                    out.println("Move (" + row + ", " + col + ") is invalid.");
                }
            } else {
                out.println("Invalid input. Please enter row and column.");
            }
        }
    }
}
