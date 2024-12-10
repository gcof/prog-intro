package expression.parser;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public char peek() {
        return data.charAt(pos);
    }

    @Override
    public String peek(int depth) {
        int i = pos;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < depth; j++) {
            if (i < data.length()) {
                sb.append(data.charAt(i++));
            } else {
                sb.append('\0');
            }
        }
        return sb.toString();
    }

    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
