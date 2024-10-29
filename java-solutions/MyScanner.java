import java.io.*;
import java.nio.charset.Charset;

    public class MyScanner {
        private final static int EOF_INT = -1;
        private final static int BUFFER_SIZE = 2048;
        private final String EOL = System.lineSeparator();

        private int lastUnscannedIndex = 0;
        private int lastBufferLength = 0;
        private String lastCachedToken;
        private boolean isClosed = false;
        private final Reader reader;
        private final char[] buffer = new char[BUFFER_SIZE];
        private IsWhitespaceToScanner OurWhitespace = null;

        public MyScanner(String input) {
            this.reader = new StringReader(input);
        }

        public MyScanner(InputStream source, Charset charset) {
            this.reader = new InputStreamReader(source, charset);
        }

        public MyScanner(InputStream source, Charset charset, IsWhitespaceToScanner WhitespaceClass) {
            this.reader = new InputStreamReader(source, charset);
            this.OurWhitespace = WhitespaceClass;
        }

        public boolean hasNext() throws IOException {
            return hasNext(true);
        }

        public boolean hasNextInCurrentLine() throws IOException {
            return hasNext(false);
        }

        public boolean hasNextLine() throws IOException {
            return hasNextChar();
        }

        public boolean hasNextChar() throws IOException{
            if (lastUnscannedIndex < lastBufferLength) {
                return true;
            }
            fillBuffer();
            return lastBufferLength > 0;
        }

        public int readChar() throws IOException {
            if (!hasNextChar()) {
                return EOF_INT;
            }
            return buffer[lastUnscannedIndex++];
        }

        public boolean hasNextInt() throws IOException {
            String token = peekToken(true);
            return !token.isEmpty() && (Character.isDigit(token.charAt(0)) ||
                    (token.charAt(0) == '-' && token.length() > 1 && Character.isDigit(token.charAt(1))));
        }

        public int nextInt() throws IOException {
            if (!hasNextInt()) {
                throw new IOException("No int found");
            }
            String number = readToken(true).toLowerCase();
            if (number.charAt(number.length() - 1) == 'o') {
                return Integer.parseUnsignedInt(number.substring(0, number.length() - 1), 8);
            }
            return Integer.parseInt(number);
        }

        public String next() throws IOException {
            if (!hasNext()) {
                throw new IOException("Nothing found in InputStream");
            }
            return readToken(true);
        }

        public void close() throws IOException {
            if (!isClosed) {
                reader.close();
                isClosed = true;
            }
        }

        private void fillBuffer() throws IOException {
            if (isClosed) {
                throw new IOException("Stream closed");
            }
            lastUnscannedIndex = 0;
            lastBufferLength = reader.read(buffer);
            if (lastBufferLength == EOF_INT) {
                lastBufferLength = 0;
            }
        }

        private boolean isEndOfLineChar(char c) {
            return EOL.indexOf(c) != -1;
        }

        private boolean isWhitespace(char c) {
            return OurWhitespace == null ? Character.isWhitespace(c): OurWhitespace.isWhitespace(c);
        }

        private boolean isWhitespace(char c, boolean skipEndLine) {
            if (skipEndLine) {
                return isWhitespace(c);
            }
            return isWhitespace(c) && !isEndOfLineChar(c);
        }

        private void skipWhitespaces(boolean skipEndLine) throws IOException {
            while (hasNextChar() && isWhitespace((char) peekChar(), skipEndLine)) {
                readChar();
            }
        }

        private void readOneEol() throws IOException{
            for(int i = 0; i < EOL.length(); ++i){
                readChar();
            }
        }

        private int peekChar() throws IOException {
            if (!hasNextChar()) {
                return EOF_INT;
            }
            return buffer[lastUnscannedIndex];
        }

        private boolean hasNext(boolean skipEndLine) throws IOException {
            if (!hasNextChar()) {
                return false;
            }
            if (peekToken(skipEndLine).isEmpty()) {
                lastCachedToken = null;
                return false;
            }
            return true;
        }

        private String peekToken(boolean skipEndLine) throws IOException {
            if (lastCachedToken == null) {
                lastCachedToken = readToken(skipEndLine);
            }
            return lastCachedToken;
        }

        private String readToken(boolean skipEndLine) throws IOException {
            if (lastCachedToken != null) {
                String token = lastCachedToken;
                lastCachedToken = null;
                return token;
            }
            skipWhitespaces(skipEndLine);
            StringBuilder sb = new StringBuilder();
            while (hasNextChar() && !isWhitespace((char) peekChar(), true)) {
                sb.append((char) readChar());
            }
            return sb.toString();
        }

        public void skipEndLine() throws IOException {
            skipWhitespaces(false);
            if (hasNextChar() && isEndOfLineChar((char) peekChar())) {
                readOneEol();
            }
        }
    }
