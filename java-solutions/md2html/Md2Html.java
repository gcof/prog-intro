package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Stack;

public class Md2Html {
    private static final String htmlcode[] = { "strong", "strong", "code", "em", "em", "s", "ins", "del" };
    private static final String[] mdcode = { "**", "__", "`", "*", "_", "--", "<<", "}}" };
    private static final String[] mdcodeReversed = { "**", "__", "`", "*", "_", "--", ">>", "{{" };
    private static final String fixcode[] = { "*", "_" };
    private static final String htmlFixcode[] = { "em", "em" };
    private static final char repcode[] = { '<', '>', '&' };
    private static final String tocode[] = { "&lt;", "&gt;", "&amp;" };

    private static String parseOneParagraph(String ourParagraph) {
        String[] ourHeaders = { "# ", "## ", "### ", "#### ", "##### ", "###### " };
        StringBuilder result = new StringBuilder();
        Stack<String> stack = new Stack<>();
        int[] lastFixCode = new int[fixcode.length];
        Arrays.fill(lastFixCode, -1);
        boolean isHeader = false;
        int nowParsing = 0;
        for (int i = 0; i < ourHeaders.length; i++) {
            if (ourParagraph.startsWith(ourHeaders[i])) {
                stack.push("h" + (i + 1));
                result.append("<h").append(i + 1).append(">");
                isHeader = true;
                nowParsing = ourHeaders[i].length();
                break;
            }
        }
        if (!isHeader) {
            stack.push("p");
            result.append("<p>");
        }
        boolean smthDid = false;
        for (; nowParsing < ourParagraph.length(); nowParsing++) {
            smthDid = false;
            if (ourParagraph.charAt(nowParsing) == '\\') {
                for (int i = 0; i < repcode.length; i++) {
                    if (nowParsing + 1 < ourParagraph.length() && ourParagraph.charAt(nowParsing + 1) == repcode[i]) {
                        result.append(tocode[i]);
                        nowParsing++;
                        smthDid = true;
                        break;
                    }
                }
                if (!smthDid) {
                    result.append(ourParagraph.charAt(++nowParsing));
                }
                smthDid = true;
                continue;
            }
            for (int i = 0; i < mdcode.length; ++i) {
                if (ourParagraph.startsWith(mdcode[i], nowParsing)
                        || ourParagraph.startsWith(mdcodeReversed[i], nowParsing)) {
                    for (int j = 0; j < fixcode.length; j++) {
                        if (fixcode[j].equals(mdcode[i])) {
                            lastFixCode[j] = result.length();
                        }
                    }
                    if (!stack.peek().equals(mdcode[i])) {
                        if (ourParagraph.startsWith(mdcodeReversed[i], nowParsing)
                                && !ourParagraph.startsWith(mdcode[i], nowParsing)) {
                            break;
                        }
                        stack.push(mdcode[i]);
                        result.append("<").append(htmlcode[i]).append(">");
                    } else {
                        stack.pop();
                        result.append("</").append(htmlcode[i]).append(">");
                    }
                    nowParsing += mdcode[i].length() - 1;
                    smthDid = true;
                    break;
                }
            }
            for (int i = 0; i < repcode.length; i++) {
                if (!smthDid && ourParagraph.charAt(nowParsing) == repcode[i]) {
                    result.append(tocode[i]);
                    smthDid = true;
                    break;
                }
            }
            if (!smthDid) {
                result.append(ourParagraph.charAt(nowParsing));
            }
        }
        while (!stack.empty()) {
            boolean isFix = false;
            for (int i = 0; i < fixcode.length; i++) {
                if (stack.peek().equals(fixcode[i])) {
                    isFix = true;
                    result = new StringBuilder(result.substring(0, lastFixCode[i]) + fixcode[i]
                            + result.substring(lastFixCode[i] + htmlFixcode[i].length() + 2));
                    stack.pop();
                }
            }
            if (!isFix) {
                result.append("</").append(stack.pop()).append(">");
            }
        }
        if (result.toString().startsWith("<p></p>")) {
            return "";
        }
        if (!result.isEmpty()) {
            result.append('\n');
        }
        return result.toString();
    }

    private static String parseMd2Html(String fileName) {
        BufferedReader reader = null;
        StringBuilder ourResult = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            StringBuilder ourParagraph = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                if (line.isEmpty()) {
                    if (!ourParagraph.isEmpty()) {
                        ourParagraph = new StringBuilder(
                                ourParagraph.substring(0, ourParagraph.length() - System.lineSeparator().length()));
                    }
                    ourResult.append(parseOneParagraph(ourParagraph.toString()));
                    ourParagraph = new StringBuilder();
                } else {
                    ourParagraph.append(line);
                    ourParagraph.append(System.lineSeparator());
                }
                line = reader.readLine();
            }
            if (!ourParagraph.isEmpty()) {
                ourParagraph = new StringBuilder(
                        ourParagraph.substring(0, ourParagraph.length() - System.lineSeparator().length()));
            }
            ourResult.append(parseOneParagraph(ourParagraph.toString()));
            ourParagraph = new StringBuilder();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            return "";
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            return "";
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Error closing file: " + fileName);
            }
        }
        return ourResult.toString();
    }

    private static void writeToFile(String html, String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            writer.write(html);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + fileName);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Error closing file: " + fileName);
            }
        }

    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Needed 2 files but found " + args.length);
            return;
        }
        String html = parseMd2Html(args[0]);
        writeToFile(html, args[1]);
    }
}
