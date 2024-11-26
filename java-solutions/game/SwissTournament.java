package game;

import java.util.*;

public class SwissTournament {
    private final Board board;
    private final List<Player> players;
    private final Map<Player, Integer> scores;
    private final int rounds;
    private final Random random = new Random();

    public SwissTournament(Board board, List<Player> players) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.scores = new HashMap<>();
        for (Player player : players) {
            scores.put(player, 0);
        }
        this.rounds = (int) Math.ceil(Math.log(players.size()) / Math.log(2));
    }

    public void playTournament() {
        for (int round = 1; round <= rounds; round++) {
            System.out.println("Round " + round);
            playRound(round);
        }
        printResults();
    }

    private void playRound(int round) {
        if (round == 1) {
            Collections.shuffle(players);
        } else {
            players.sort((player1, player2) -> Integer.compare(scores.get(player2), scores.get(player1)));
        }
        for (int i = 0; i < players.size(); i += 2) {
            if (i + 1 < players.size()) {
                Player player1 = players.get(i);
                Player player2 = players.get(i + 1);
                if (random.nextInt() % 2 == 0) {
                    playMatch(player1, player2);
                } else {
                    playMatch(player2, player1);
                }
                board.clear();
            } else {
                scores.put(players.get(i), scores.get(players.get(i)) + 1);
            }
        }
    }

    private void playMatch(Player player1, Player player2) {
        System.out.println(player1.getName() + " vs " + player2.getName());
        Game game = new Game(false, player1, player2);
        int result = game.play(board);
        if (result == 1) {
            scores.put(player1, scores.get(player1) + 1);
        } else if (result == 2) {
            scores.put(player2, scores.get(player2) + 1);
        } else {
            scores.put(player1, scores.get(player1) + 1);
            scores.put(player2, scores.get(player2) + 1);
        }
    }

    private void printResults() {
        List<Map.Entry<Player, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("Final Results:");
        for (Map.Entry<Player, Integer> entry : sortedScores) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue() + " points");
        }
    }
}