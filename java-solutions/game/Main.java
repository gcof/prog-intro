package game;

import java.util.List;


public class Main {
    private static void makeTicTacToeGame() {
        final Game game = new Game(false, new HumanPlayer("KGeorgiy"), new CheaterPlayer());
        int result;
        do {
            TicTacToeBoard board = new TicTacToeBoard();
            result = game.play(board);
            System.out.println("Game result: " + result);
        } while (result != 0);
    }

    private static void makeMNKGame() {
        final Game game = new Game(false, new HumanPlayer("KGeorgiy"), new HumanPlayer("BArtem"));
        int result;
        do {
            MNKBoard board = new MNKBoard(12, 12, 2);
            result = game.play(board);
            System.out.println("Game result: " + result);
        } while (result != 0);
    }

    private static void makeRhombusGame() {
        final Game game = new Game(false, new HumanPlayer("KGeorgiy"), new HumanPlayer("BArtem"));
        int result;
        do {
            RhombusBoard board = new RhombusBoard(13, 2);
            result = game.play(board);
            System.out.println("Game result: " + result);
        } while (result != 0);
    }

    private static void makeTournament() {
        List<Player> players = List.of(new HumanPlayer("KGeorgiy"), new HumanPlayer("BArtem"),
                new HumanPlayer("KGrigoriy"));
        final SwissTournament tournament = new SwissTournament(new TicTacToeBoard(), players);
        tournament.playTournament();
    }



    public static void main(String[] args) {
//        makeTicTacToeGame();
//        makeMNKGame();
        makeRhombusGame();
//        makeTournament();
    }
}
