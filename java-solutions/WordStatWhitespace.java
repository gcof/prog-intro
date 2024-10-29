public class WordStatWhitespace implements IsWhitespaceToScanner {

    @Override
    public boolean isWhitespace(char c) {
        return !(Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION);
    }
}
