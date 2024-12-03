package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String answer = getValidAnswer(scanner);

        if (answer.equalsIgnoreCase("no")) {
            playSingleGame(scanner);
        } else {
            playSwissTournament(scanner);
        }
    }

    private static String getValidAnswer(Scanner scanner) {
        String answer = "";
        while (true) {
            System.out.println("Do you want to play swiss tournament? (yes/no)");
            answer = scanner.nextLine().trim();
            if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        return answer;
    }

    private static void playSwissTournament(Scanner scanner) {
        int numPlayers = getNumberOfPlayers(scanner);
        List<Player> players = createPlayers(scanner, numPlayers);

        String boardType = getBoardType(scanner);
        if (boardType == null) return;

        playTournament(boardType, scanner, players);
    }

    private static void playSingleGame(Scanner scanner) {
        String boardType = getBoardType(scanner);
        playGame(boardType, scanner);
    }

    private static int getNumberOfPlayers(Scanner scanner) {
        int numPlayers = -1;
        while (numPlayers <= 1) {
            System.out.println("Enter number of players:");
            try {
                numPlayers = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return numPlayers;
    }

    private static List<Player> createPlayers(Scanner scanner, int numPlayers) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter player name:");
            String name = scanner.nextLine().trim();
            players.add(new HumanPlayer(name));
        }
        return players;
    }

    private static String getBoardType(Scanner scanner) {
        String boardType = null;
        while (boardType == null) {
            System.out.println("On which board do you want to play? (TicTacToe/MNK/Rhombus)");
            boardType = scanner.nextLine().trim();
            if (!(boardType.equalsIgnoreCase("TicTacToe") || boardType.equalsIgnoreCase("MNK")
                    || boardType.equalsIgnoreCase("Rhombus"))) {
                System.out.println("Invalid board type. Please enter either TicTacToe, MNK, or Rhombus.");
                boardType = null;
            }
        }
        return boardType;
    }

    private static void playTournament(String boardType, Scanner scanner, List<Player> players) {
        if (boardType.equalsIgnoreCase("TicTacToe")) {
            TicTacToeBoard board = new TicTacToeBoard();
            SwissTournament tournament = new SwissTournament(board, players);
            tournament.playTournament();
        } else if (boardType.equalsIgnoreCase("MNK")) {
            int n = getBoardDimension(scanner, "Enter n for MNK board:");
            int m = getBoardDimension(scanner, "Enter m for MNK board:");
            int k = getBoardDimension(scanner, "Enter k for MNK board:");
            MNKBoard board = new MNKBoard(n, m, k);
            SwissTournament tournament = new SwissTournament(board, players);
            tournament.playTournament();
        } else if (boardType.equalsIgnoreCase("Rhombus")) {
            int n = getBoardDimension(scanner, "Enter n for Rhombus board:");
            int k = getBoardDimension(scanner, "Enter k for Rhombus board:");
            RhombusBoard board = new RhombusBoard(n, k);
            SwissTournament tournament = new SwissTournament(board, players);
            tournament.playTournament();
        }
    }

    private static void playGame(String boardType, Scanner scanner) {
        int result;
        if (boardType.equalsIgnoreCase("TicTacToe")) {
            Game game = new Game(false, new HumanPlayer("playerX"), new HumanPlayer("playerO"));
            TicTacToeBoard board = new TicTacToeBoard();
            result = game.play(board);
            System.out.println("Game result: " + result);
        } else if (boardType.equalsIgnoreCase("MNK")) {
            int n = getBoardDimension(scanner, "Enter n for MNK board:");
            int m = getBoardDimension(scanner, "Enter m for MNK board:");
            int k = getBoardDimension(scanner, "Enter k for MNK board:");
            Game game = new Game(false, new HumanPlayer("playerX"), new HumanPlayer("playerO"));
            MNKBoard board = new MNKBoard(n, m, k);
            result = game.play(board);
            System.out.println("Game result: " + result);
        } else if (boardType.equalsIgnoreCase("Rhombus")) {
            int n = getBoardDimension(scanner, "Enter n for Rhombus board:");
            int k = getBoardDimension(scanner, "Enter k for Rhombus board:");
            Game game = new Game(false, new HumanPlayer("playerX"), new HumanPlayer("playerO"));
            RhombusBoard board = new RhombusBoard(n, k);
            result = game.play(board);
            System.out.println("Game result: " + result);
        }
    }

    private static int getBoardDimension(Scanner scanner, String prompt) {
        int dimension = -1;
        while (dimension <= 0) {
            System.out.println(prompt);
            try {
                dimension = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return dimension;
    }
}
