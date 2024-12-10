package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    char peek();
    String peek(int depth);
    IllegalArgumentException error(String message);
}
