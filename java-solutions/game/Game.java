package game;


import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private final boolean log;
    private final Player player1, player2;

    public Game(final boolean log, final Player player1, final Player player2) {
        this.log = log;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(Board board) {
        boolean drawOffered = false;
        while (true) {
            final int result1 = move(board, player1, 1, drawOffered);
            if (result1 != -1) {
                return result1;
            }
            final int result2 = move(board, player2, 2, drawOffered);
            if (result2 != -1) {
                return result2;
            }
        }
    }

    private int move(final Board board, final Player player, final int no, boolean drawOffered) {
        final Position position = board.getPosition();
        final BoardPositionWrapper positionWrapper = new BoardPositionWrapper(position);
        Move move = null;
        while(true) {
            try {
                move = player.move(positionWrapper, board.getCell(), drawOffered);
            } catch (Exception e) {
                log("Player " + no + " threw an exception: " + e.getMessage());
                System.out.println("Player " + no + " threw an exception: " + e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                return 3 - no;
            }
            if (move == null) {
                return 3 - no;
            }
            if (move == Move.OFFER_DRAW) {
                if (drawOffered) {
                    log("Player " + no + " cannot offer a draw again in the same turn");
                    return -1;
                }
                drawOffered = true;
                Player opponent = (no == 1) ? player2 : player1;
                boolean acceptDraw = askForDraw(opponent);
                if (acceptDraw) {
                    log("Both players agreed to a draw.");
                    return 0;
                } else {
                    log("Player " + (3 - no) + " rejected the draw offer.");
                    continue;
                }
            }

            if (move == Move.RESIGN) {
                log("Player " + no + " resigned");
                return 3 - no;
            }

            if (!position.isValid(move)) {
                log("Invalid move: " + move);
                return 3 - no;
            }
            
            final Result result = board.makeMove(move);
            log("Player " + no + " move: " + move);
            log("Position:\n" + positionWrapper.showPositionToConsole());

            if (result == Result.WIN) {
                log("Player " + no + " won");
                return no;
            } else if (result == Result.LOSE) {
                log("Player " + no + " lost");
                return 3 - no;
            } else if (result == Result.DRAW) {
                log("Draw");
                return 0;
            } else {
                return -1;
            }
        }
    }

    private boolean askForDraw(Player opponent) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player " + opponent.getName() + " has offered a draw. Do you accept? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equalsIgnoreCase("yes");
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
