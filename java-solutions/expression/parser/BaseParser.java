package expression.parser;

import static java.lang.Character.isWhitespace;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean takeWhitespace() {
        if (isWhitespace(ch)) {
            take();
            return true;
        }
        return false;
    }


    protected String peek(int depth) {
        return ch + source.peek(depth - 1);
    }

    protected boolean testEqual(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (testEqual(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean take(final String expected) {
        String s = peek(expected.length());
        if (s.equals(expected)) {
            for (int i = 0; i < expected.length(); i++) {
                take();
            }
            return true;
        }
        return false;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
