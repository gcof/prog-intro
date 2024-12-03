import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class WordStatInput {
    static final int BUFFER_SIZE = 2048;
    static final int SUFFIX_SIZE = 3;
    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Expected two files");
            return;
        }
        Map<String, Integer> mp = null;
        WordStatWhitespace whitespace = new WordStatWhitespace();
        try {
            MyScanner reader = new MyScanner((new FileInputStream(args[0])), StandardCharsets.UTF_8, whitespace);
            try {
                mp = readWord(reader);
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
                for (Map.Entry<String, Integer> entry : mp.entrySet()) {
                    writer.write(entry.getKey() + " " + entry.getValue() + System.lineSeparator());
                }
            } finally {
                writer.close();
            }
        } catch (IOException e){
            System.err.println("Error while writing file: " + e.getMessage());
            return;
        }
    }

    private static boolean isOurLetter(Character c) {
        return Character.isLetter(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'';
    }

    private static void addToMap(String word, Map<String, Integer> statistics) {
        statistics.put(word, statistics.getOrDefault(word, 0) + 1);
    }

    private static String wordToSort(String word) {
        return word;
    }

    private static Map<String, Integer> readWord(MyScanner reader) throws IOException {
        int x;
        StringBuilder word = new StringBuilder();
        Map<String, Integer> statistics = new LinkedHashMap<String, Integer>();
        while((x = reader.readChar()) != -1) {
            if (isOurLetter(Character.valueOf((char)x))) {
                word.append((char)x);
            } else if (!word.isEmpty()) {
                addToMap(wordToSort((word.toString()).toLowerCase()), statistics);
                word.setLength(0);
            }
        }
        if (!word.isEmpty()) {
            addToMap(wordToSort((word.toString()).toLowerCase()), statistics);
        }
        return statistics;
    }
}
