package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

public class Md2Html {
    private static class IntWrapper {
        public int value;

        IntWrapper(int value) {
            this.value = value;
        }
    }

    private static final LinkedHashMap<String, String> htmlMdCodeStartMap = new LinkedHashMap<>();
    private static final Map<String, String> htmlMdCodeEndMap = Map.of(
            "ins", ">>",
            "del", "{{"
    );
    private static final Map<String, String> htmlMdFixCodeMap = Map.of(
            "*", "em",
            "_", "em",
            "<<", "ins",
            "}}", "del"
    );
    private static final Map<Character, String> htmlMdRepCodeMap = Map.of(
            '<', "&lt;",
            '>', "&gt;",
            '&', "&amp;"
    );

    private static void makeFirstInitializations() {
        // Why not Map.of()? Cos' I need to keep the order of elements in Map.entrySet() in htmlMdCodeStartMap =)
        htmlMdCodeStartMap.put("**", "strong");
        htmlMdCodeStartMap.put("__", "strong");
        htmlMdCodeStartMap.put("--", "s");
        htmlMdCodeStartMap.put("<<", "ins");
        htmlMdCodeStartMap.put("}}", "del");
        htmlMdCodeStartMap.put("`", "code");
        htmlMdCodeStartMap.put("*", "em");
        htmlMdCodeStartMap.put("_", "em");
    }

    static {
        makeFirstInitializations();
    }

    private static int evalLevelOfHeader(String ourParagraph) {
        StringBuilder headerStart = new StringBuilder("#");
        int maxStart = 0;
        for (int i = 1; i <= 6; i++) {
            if (ourParagraph.startsWith(headerStart.toString())) {
                maxStart = i;
                headerStart.append('#');
            }
        }
        if (maxStart == 0 || !Character.isWhitespace(ourParagraph.charAt(maxStart))) {
            return -1;
        }
        return maxStart;
    }

    private static void addHeader(StringBuilder result, ArrayDeque<String> stack, int headerLevel) {
        if (headerLevel > 0) {
            stack.addLast("h" + headerLevel);
            result.append("<h").append(headerLevel).append(">");
        } else {
            stack.addLast("p");
            result.append("<p>");
        }
    }

    private static String fixHtmlSpecialInString(String toFix) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < toFix.length(); ++i) {
            if (htmlMdRepCodeMap.containsKey(toFix.charAt(i))) {
                result.append(htmlMdRepCodeMap.get(toFix.charAt(i)));
            } else {
                result.append(toFix.charAt(i));
            }
        }
        return result.toString();
    }

    private static void fixSoloTags(StringBuilder result, ArrayDeque<String> stack,
                                    Map<String, Integer> lastFixCodeInResult) {
        while (stack.size() > 1) {
            if (lastFixCodeInResult.containsKey(stack.peekLast())) {
                int start = lastFixCodeInResult.get(stack.peekLast());
                result.delete(start, start + htmlMdFixCodeMap.get(stack.peekLast()).length() + "<>".length());
                result.insert(start, fixHtmlSpecialInString(stack.peekLast()));
                stack.removeLast();
            }
        }
        result.append("</").append(stack.removeLast()).append(">");
    }

    private static boolean makeScreening(StringBuilder result, String ourParagraph, IntWrapper nowParsing) {
        if (ourParagraph.charAt(nowParsing.value) == '\\') {
            if (nowParsing.value + 1 < ourParagraph.length() &&
                    htmlMdRepCodeMap.containsKey(ourParagraph.charAt(nowParsing.value + 1))) {
                result.append(htmlMdRepCodeMap.get(ourParagraph.charAt(++nowParsing.value)));
            } else {
                result.append(ourParagraph.charAt(++nowParsing.value));
            }
            return true;
        }
        return false;
    }

    private static boolean tryAddHtmlTag(StringBuilder result, String ourParagraph, IntWrapper nowParsing,
                                         ArrayDeque<String> stack, Map<String, Integer> lastFixCodeInResult) {
        boolean somethingChanged = false;
        for (Map.Entry<String, String> entry : htmlMdCodeStartMap.entrySet()) {
            String mdCode = entry.getKey();
            String mdCodeReversed = htmlMdCodeEndMap.getOrDefault(entry.getValue(), mdCode);
            String htmlCode = entry.getValue();
            if (ourParagraph.startsWith(mdCode, nowParsing.value)
                    || ourParagraph.startsWith(mdCodeReversed, nowParsing.value)) {
                if (htmlMdFixCodeMap.containsKey(mdCode)) {
                    lastFixCodeInResult.replace(mdCode, result.length());
                }
                if (!stack.peekLast().equals(mdCode)) {
                    if (ourParagraph.startsWith(mdCodeReversed, nowParsing.value)
                            && !ourParagraph.startsWith(mdCode, nowParsing.value)) {
                        break;
                    } else {
                        stack.addLast(mdCode);
                        result.append("<").append(htmlCode).append(">");
                    }
                } else {
                    stack.removeLast();
                    result.append("</").append(htmlCode).append(">");
                }
                nowParsing.value += mdCode.length() - 1;
                somethingChanged = true;
                break;
            }
        }
        return somethingChanged;
    }

    private static boolean changeHtmlSpecialSymbols(StringBuilder result, String ourParagraph, IntWrapper nowParsing) {
        if (htmlMdRepCodeMap.containsKey(ourParagraph.charAt(nowParsing.value))) {
            result.append(htmlMdRepCodeMap.get(ourParagraph.charAt(nowParsing.value)));
            return true;
        }
        return false;
    }

    private static String parseOneParagraph(String ourParagraph) {
        StringBuilder result = new StringBuilder();
        ArrayDeque<String> stack = new ArrayDeque<>();
        Map<String, Integer> lastFixCodeInResult = new LinkedHashMap<>(htmlMdFixCodeMap.size());
        for (String key : htmlMdFixCodeMap.keySet()) {
            lastFixCodeInResult.put(key, -1);
        }
        int headerLevel = evalLevelOfHeader(ourParagraph);
        addHeader(result, stack, headerLevel);

        for (IntWrapper nowParsing = new IntWrapper(headerLevel + 1); nowParsing.value < ourParagraph.length(); nowParsing.value++) {
            boolean somethingChanged = (makeScreening(result, ourParagraph, nowParsing)
                    || tryAddHtmlTag(result, ourParagraph, nowParsing, stack, lastFixCodeInResult)
                    || changeHtmlSpecialSymbols(result, ourParagraph, nowParsing));
            if (!somethingChanged) {
                result.append(ourParagraph.charAt(nowParsing.value));
            }
        }

        fixSoloTags(result, stack, lastFixCodeInResult);

        if (result.toString().startsWith("<p></p>")) {
            return "";
        }
        if (!result.isEmpty()) {
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    private static void addOneParagraphToResult(StringBuilder result, StringBuilder paragraphToAdd) {
        if (!paragraphToAdd.isEmpty()) {
            paragraphToAdd.setLength(paragraphToAdd.length() - System.lineSeparator().length());
        }
        result.append(parseOneParagraph(paragraphToAdd.toString()));
        paragraphToAdd.setLength(0);
    }

    private static String parseMd2Html(String fileName) throws IOException {
        StringBuilder resultInHtml = new StringBuilder();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            StringBuilder buildedParagraph = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    addOneParagraphToResult(resultInHtml, buildedParagraph);
                } else {
                    buildedParagraph.append(line).append(System.lineSeparator());
                }
            }
            addOneParagraphToResult(resultInHtml, buildedParagraph);
        }
        return resultInHtml.toString();
    }

    private static void writeToFile(String html, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),
                StandardCharsets.UTF_8))) {
            writer.write(html);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Needed 2 files but found " + args.length);
            return;
        }
        try {
            String html = parseMd2Html(args[0]);
            writeToFile(html, args[1]);
        } catch (IOException e) {
            System.err.println("Error reading/writing from/to file: " + e.getMessage());
        }
    }
}
