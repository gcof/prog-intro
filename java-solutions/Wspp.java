import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;




public class Wspp {

    private static class OurWhitespace implements IsWhitespaceToScanner {
        @Override
        public boolean isWhitespace(char c) {
            return !(Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION);
        }
    }

    static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Expected two files");
            return;
        }
        Map<String, IntList> mp = null;
        try {
            MyScanner reader = new MyScanner((new FileInputStream(args[0])),
                    StandardCharsets.UTF_8, new OurWhitespace());
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
                Iterator<Map.Entry<String, IntList>> iterator = mp.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, IntList> entry = iterator.next();
                    writer.write(entry.getKey() + " " + entry.getValue().size());
                    if(entry.getValue().toString() != null) {
                        writer.write(" " + entry.getValue());
                    }
                    if (iterator.hasNext()) {
                        writer.newLine();
                    }
                }

            } finally {
                writer.close();
            }
        } catch (IOException e){
            System.err.println("Error while writing file: " + e.getMessage());
            return;
        }
    }

    private static void addToMap(String word, Map<String, IntList> statistics, int pos) {
        statistics.putIfAbsent(word, new IntList());
        statistics.get(word).add(pos);
    }

    private static Map<String, IntList> readWord(MyScanner reader) throws IOException {
        int cnt = 1;
        Map<String, IntList> statistics = new LinkedHashMap<String, IntList>();
        while (reader.hasNextChar()){
            while (reader.hasNextInCurrentLine()) {
                String newWord = reader.next();
                addToMap((newWord.toLowerCase()), statistics, cnt++);
            }
            while (reader.hasNextChar() && !reader.hasNextInCurrentLine()) {
                reader.skipEndLine();
            }
        }
        return statistics;
    }
}
