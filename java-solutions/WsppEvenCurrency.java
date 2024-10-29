import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public class WsppEvenCurrency {

    private static class OurWhitespace implements IsWhitespaceToScanner {
        @Override
        public boolean isWhitespace(char c) {
            return !(Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION ||
                    Character.getType(c) == Character.CURRENCY_SYMBOL);
        }
    }

    static final int BUFFER_SIZE = 1024;


    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Expected two files");
            return;
        }
        Map<String, IntList> statisitics = null;
        Map<String, IntList> evenStatistics = null;
        try {
            MyScanner reader = new MyScanner((new FileInputStream(args[0])),
                    StandardCharsets.UTF_8, new OurWhitespace());
            try {
                List<Map<String, IntList>> statisticsGiven = readWord(reader);
                statisitics = statisticsGiven.get(0);
                evenStatistics = statisticsGiven.get(1);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getMessage());
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]), BUFFER_SIZE);
            try {
                for (Map.Entry<String, IntList> entry : statisitics.entrySet()) {
                    writer.write(entry.getKey() + " " + entry.getValue().size());
                    if (evenStatistics.get(entry.getKey()) != null) {
                        String enterings = evenStatistics.get(entry.getKey()).toString();
                        if (enterings != null && !enterings.isEmpty()) {
                            writer.write(" " + enterings);
                        }
                    }
                }
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error while writing file: " + e.getMessage());
            return;
        }
    }

    private static void addToMap(String word, Map<String, IntList> stats, int pos) {
        stats.putIfAbsent(word, new IntList());
        stats.get(word).add(pos);
    }

    private static List<Map<String, IntList>> readWord(MyScanner reader) throws IOException {
        int cnt = 1;
        Map<String, IntList> statistics = new LinkedHashMap<String, IntList>();
        Map<String, IntList> evenStatistics = new LinkedHashMap<String, IntList>();
        while (reader.hasNextChar()) {
            Map<String, IntList> statisticsCurrentLine = new LinkedHashMap<String, IntList>();
            for (int indInLine = 1; reader.hasNextInCurrentLine(); indInLine++) {
                String newWord = reader.next();
                addToMap(newWord.toLowerCase(), statistics, cnt);
                addToMap(newWord.toLowerCase(), statisticsCurrentLine, indInLine);
            }
            for (Map.Entry<String, IntList> entry : statisticsCurrentLine.entrySet()) {
                if (entry.getValue().size() > 1) {
                    for (int i = 1; i < entry.getValue().size(); i += 2) {
                        addToMap(entry.getKey(), evenStatistics, entry.getValue().get(i));
                    }
                }
            }
            while (reader.hasNextChar() && !reader.hasNextInCurrentLine()) {
                reader.skipEndLine();
            }
        }
        return List.of(statistics, evenStatistics);
    }
}
